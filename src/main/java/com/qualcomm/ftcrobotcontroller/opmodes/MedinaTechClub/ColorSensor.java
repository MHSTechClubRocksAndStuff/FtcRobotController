package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub;

/**
 * Created by RFTEST on 12/4/2015.
 */
import android.app.Activity;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ColorSensor {
    private HardwareMap hm;
    private com.qualcomm.robotcore.hardware.ColorSensor rgb;
    private View background;
    private static final double pushDistance = 0.01;
    private DigitalChannel LED;
    private boolean haveLED = false;

    public ColorSensor(HardwareMap hm, String colorSensor){
        this.hm = hm;
        rgb = hm.colorSensor.get(colorSensor);
        background = ((Activity) hm.appContext).findViewById(R.id.RelativeLayout);
    }

    public String getDebug(){
        return "RGB: (" + rgb.red() + ", " + rgb.green() + ", " + rgb.blue() + ")";
    }

    public int getRed(){
        return rgb.red();
    }

    public int getGreen(){
        return rgb.green();
    }

    public int getBlue(){
        return rgb.blue();
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

    public boolean getLEDState(){
        return LED.getState();
    }
}
