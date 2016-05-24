package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.FTC2015_2016;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Winch {
    private DcMotor winchMotor;
    private DcMotor shoulderMotor;

    private double baseTurnSpeed = 0.5;
    private double armLiftContracted = 0.3;
    private double armLiftExtended = 0.5;
    private double armLiftSpeed = armLiftContracted;
    private double armDropSpeed = 0.1;
    private int armPos = 0;
    private double armInterval = 1;

    private double winchLiftSpeed = 1;
    private double winchDropSpeed = -0.5;

    private boolean moving = false;

    private int WinchStop = 0;
    private boolean CheckWinchStop = false;
    private boolean winching = false;
    private boolean winchingStop = false;

    private boolean slowLift = false;
    private boolean db = true;

    private Tank tank;


    public Winch(Tank t, HardwareMap hm, String armShoulderName, String winchName){
        tank = t;
        winchMotor = hm.dcMotor.get(winchName);
        shoulderMotor = hm.dcMotor.get(armShoulderName);
        shoulderMotor.setDirection(DcMotor.Direction.REVERSE);
        //shoulderMotor.setPower();

    }

    public void moveWinchAndArm(boolean armLeft, boolean armRight, boolean armUp, boolean armDown, boolean winchUp, boolean winchDown, boolean toggleLift){
        turnArm(armLeft, armRight);
        liftArm(armUp, armDown, toggleLift);
        winch(winchUp, winchDown);
    }

    private void winch(boolean winchUp, boolean winchDown){
        winchUp = false;
        if(winchUp){
            winching = true;
            winchingStop = false;
            CheckWinchStop = true;
            winchMotor.setPower(winchLiftSpeed);
        }else if(winchDown){
            winching = true;
            winchingStop = false;
            winchMotor.setPower(winchLiftSpeed);
        }else {
            if (winching){
                WinchStop = winchMotor.getCurrentPosition();
            }
            winching = false;
            winchMotor.setPower(0);
        }
    }

    public void checkWinch(){
        if(CheckWinchStop){
            if(winchingStop){
                if(winchMotor.getCurrentPosition() >= WinchStop + 210){
                    winchMotor.setPower(0);
                }
            }else if(winchMotor.getCurrentPosition() <= WinchStop - 210){
                winchingStop = true;
                winchMotor.setPower(1);
            }
        }
    }

    private void liftArm(boolean armUp, boolean armDown, boolean toggleLift){
      /*  if(toggleLift && db){
            db = false;
            slowLift = !slowLift;
        }else if(!toggleLift){
            db = true;
        }*/
        if(armUp){
            shoulderMotor.setPower(armLiftSpeed);
        }else if(armDown){
            shoulderMotor.setPower(-armDropSpeed);
        }else {
            /*if (slowLift && tank.getExtension() >= 0.67) {
                shoulderMotor.setPower(0.15);
            }else{*/
                shoulderMotor.setPower(0);
            //}

        }
    }

    private void turnArm(boolean turnLeft, boolean turnRight){
        if(turnLeft){
         //   baseMotor.setPower(-baseTurnSpeed);
        }else if(turnRight){
          //  baseMotor.setPower(baseTurnSpeed);
        }else{
         //   baseMotor.setPower(0);
        }
    }

    public void setExtensionSpeed(){
        armLiftSpeed = armLiftExtended;
    }

    public void setContractedSpeed(){
        armLiftSpeed = armLiftContracted;
    }

}
