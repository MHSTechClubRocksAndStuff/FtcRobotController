package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.FTC2015_2016;

/**
 * Created by RFTEST on 12/4/2015.
 */
import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import android.graphics.Color;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ColorButtonPusher {
    private HardwareMap hm;
    private ColorSensor rgb;
    private Servo pusher;
    private View background;
    private static final double pushDistance = 0.01;
    private DigitalChannel LED;
    private boolean haveLED = false;

    public ColorButtonPusher(HardwareMap hm, String servoName){
        this.hm = hm;
        rgb = hm.colorSensor.get("color");
        pusher = hm.servo.get(servoName);
        background = ((Activity) hm.appContext).findViewById(R.id.RelativeLayout);
        pusher.setPosition(0.5);
    }

    public void pushButton(Color teamColor){
        boolean pushRight = false;
        Color color = getColor();
        if(teamColor == Color.Red) {
            if(color == Color.Red){
                pushRight = true;
            }
        }

        if(pushRight){
            pusher.setPosition(0.5 + pushDistance);
        }else{
            pusher.setPosition(0.5 - pushDistance);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pusher.setPosition(0.5);
    }

    private Color getColor(){
        int r = rgb.red();
        int b = rgb.blue();

        if(r < 50 && b < 50){
            return Color.Unknown;
        }else if(r > b){
            return Color.Red;
        }else if(r < b){
            return Color.Blue;
        }

        return Color.Unknown;
    }

    public void updateBackgroundColor(){
        final float hsvValues[] = {0F, 0F, 0F};
        android.graphics.Color.RGBToHSV((rgb.red() * 255) / 800, (rgb.green() * 255) / 800, (rgb.blue() * 255) / 800, hsvValues);
        background.post(new Runnable() {
            public void run() {
                background.setBackgroundColor(android.graphics.Color.HSVToColor(0xff, hsvValues));
            }
        });
    }

    public enum Color{
        Red, Blue, Unknown;
    }

    public void addLED(String name){
        LED = hm.digitalChannel.get(name);
        haveLED = true;
        setLED(true);
    }

    public boolean setLED(boolean state){
        if(!haveLED) return false;
        LED.setState(state);
        return true;
    }
}
