package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub;

import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankBase;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.robocol.Telemetry;

/**
 * Created by Zack on 10/19/2015.
 */
public class TestStuff extends OpMode{
    //private Servo s;
    private DcMotor motor;
    private Servo s;
    private int target = 0;
    private double pos = 0;

    public void init(){
        motor = hardwareMap.dcMotor.get("Motor1");
        s = hardwareMap.servo.get("Servo1");
        s.setPosition(pos);
        motor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        motor.setPower(1);
    }

    public void loop(){
        if(gamepad1.a){
            target -= 5;
            pos = 0;
        }
        if(gamepad1.y){
            target += 5;
            pos = 1;
        }
        if(gamepad1.b){
            pos -= 0.005;
        }
        if(gamepad1.x){
            pos += 0.005;
        }
        if(gamepad1.start){
            target = 0;
            motor.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
            motor.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        }

        pos = Math.min(1, Math.max(0, pos));

        motor.setTargetPosition(target);
        s.setPosition(pos);
        telemetry.addData("Encoder", target);
        telemetry.addData("Servo", pos);
    }
}
