package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub;

/**
 * Created by RFTEST on 11/30/2015.
 */
import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;


public class AdafruitSensor extends OpMode{

    ColorSensor rgb;

    private float hsvValues[] = {0F,0F,0F};
    private final float values[] = hsvValues;
    private View relativeLayout;

    public void init(){
        rgb = hardwareMap.colorSensor.get("color");
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);
    }

    public void loop(){
        Color.RGBToHSV((rgb.red() * 255) / 800, (rgb.green() * 255) / 800, (rgb.blue() * 255) / 800, hsvValues);

        String whatColor = "";
        if(rgb.red() < 50 && rgb.blue() < 50){
            whatColor = "Is it even on?";
        }else if(rgb.red() > rgb.blue()){
            whatColor = "It's red!";
        }else if(rgb.red() < rgb.blue()){
            whatColor = "It's blue!";
        }else{
            whatColor = "They're... the same?";
        }

        telemetry.addData("Clear", rgb.alpha());
        telemetry.addData("Red  ", rgb.red());
        telemetry.addData("Green", rgb.green());
        telemetry.addData("Blue ", rgb.blue());
        telemetry.addData("Hue", hsvValues[0]);
        telemetry.addData("Color?", whatColor);

        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
        });
    }
}
