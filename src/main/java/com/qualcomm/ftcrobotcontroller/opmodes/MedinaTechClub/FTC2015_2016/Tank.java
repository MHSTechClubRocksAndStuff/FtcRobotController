package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.FTC2015_2016;

import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.ColorSensor;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankBase;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;


public class Tank extends OpMode{
    private TankBase Tank;
    private Scoop scoop;
    private Arm arm;
    private Servo servo1;
    //private ColorButtonPusher colorPusher;
    private ColorSensor colorSensor;
    private Winch winch;
    private ServoController theDisabler;
    private boolean isDisabledToggle = true;
    private boolean isEnabled = true;

    private Servo tail;
    private long time;
    //private double tailMiddle = 0.465;
    //private boolean tailDebounce = true;

    private boolean debounce = true;
    private boolean isMovingTail = false;

    @Override
    public void init(){
        Tank = new TankBase(hardwareMap);
        Tank.setControls(TankBase.Controls.TankDrive);
        Tank.setMotorPower(1);
        Tank.setDeadzone(0.2);
        Tank.setPowerFunction(10);
        Tank.setMinPower(0.15);
        Tank.setDriveMode(TankBase.DriveMode.FreeRun);
        Tank.addMotor("ml1", TankMotor.Side.Left);
        Tank.addMotor("ml2", TankMotor.Side.Left);
        Tank.addMotor("mr1", TankMotor.Side.Right);
        Tank.addMotor("mr2", TankMotor.Side.Right);
        Tank.setSideDirection(TankMotor.Side.Left, TankMotor.Direction.Reverse);

        theDisabler = hardwareMap.servoController.get("ServoController");

        winch = new Winch(this, hardwareMap, "Arm Shoulder", "Winch");
        arm = new Arm(winch, hardwareMap, "Arm Extender", "Hook Arm");
        arm.initArm();
        //winch.initArm();
        scoop = new Scoop(hardwareMap, "Left Scoop", "Right Scoop", "Left Dump", "Right Dump");
        scoop.initScoop();

        tail = hardwareMap.servo.get("Left Dump");
        tail.setPosition(0.5);
        //colorSensor = new ColorSensor(hardwareMap, "color");
        //colorSensor.addLED("ColorLED");
       // colorSensor.setLED(true);
    }

    @Override
    public void loop(){
        //System.out.println(gamepad1.right_stick_x);
        Tank.DriveMotors(gamepad1);

        //
        //                                      armLeft,                                    armRight                                        armUp                                 armDown                   winchUp          winchDown        toggleLift
        winch.moveWinchAndArm((gamepad1.dpad_left || gamepad2.dpad_left), (gamepad1.dpad_right || gamepad2.dpad_right), (gamepad1.dpad_up || gamepad2.dpad_up), (gamepad1.dpad_down || gamepad2.dpad_down), false, (gamepad1.y && gamepad2.y), gamepad1.x);

         if(isEnabled) {
          arm.moveArm(gamepad1.right_bumper, gamepad1.left_bumper, (gamepad1.b && gamepad2.b));
         }
        if(isEnabled) {
            float scoopAxis = gamepad2.right_stick_y;
            float dumpAxis = gamepad2.left_stick_y;
            scoop.moveScoop(scoopAxis, dumpAxis);
        }
        if(gamepad2.a && isDisabledToggle){
           // isEnabled = !isEnabled;
           // isDisabledToggle = false;
            if(!isEnabled){
            //    theDisabler.pwmDisable();
            }
        }else if(!gamepad2.a){
           // isDisabledToggle = true;
        }
       // SetTail();

        //winch.checkWinch();
        //if(gamepad2.a && debounce){
        //    debounce = false;
        //    colorSensor.setLED(!colorSensor.getLEDState());
        //}else if(!gamepad2.a){
        //    debounce = true;
        //}
        //colorSensor.updateBackgroundColor();
       // scoop.moveScoop(gamepad1.a, gamepad1.b, gamepad1.y, gamepad1.x);
        //scoop.ScoopLoop();

        //winch.moveWinchAndArm(gamepad1.dpad_up, gamepad1.dpad_down, gamepad1.left_bumper, gamepad1.right_bumper);

       // telemetry.addData("Encoder", m1.);
        telemetry.addData("Scoop Pos", scoop.getScoopPosition());
        telemetry.addData("Dump Pos", scoop.getDumpPosition());
        //telemetry.addData("Scoop Target", scoop.getScoopTarget());
  //      telemetry.addData("Arm Servo", winch.getServoPosition());
    }

    public double getExtension(){
        return arm.getExtension();
    }

    private void SetTail(){
        if(isMovingTail){
            if(System.currentTimeMillis() - time >= 1000){
                isMovingTail = false;
                tail.setPosition(0.5);
            }
        }else{
            if(gamepad2.left_bumper){
                isMovingTail = true;
                tail.setPosition(0);
                time = System.currentTimeMillis();
            }else if(gamepad2.right_bumper){
                isMovingTail = true;
                tail.setPosition(1);
                time = System.currentTimeMillis();
            }
        }

        if(gamepad2.left_bumper){
            tail.setPosition(0);
        }else if(gamepad2.right_bumper){
            tail.setPosition(1);
        }else{
            tail.setPosition(0.5);
        }
    }

}
