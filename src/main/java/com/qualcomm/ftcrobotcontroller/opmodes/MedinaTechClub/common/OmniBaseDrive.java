/*
 * Copyright (c) 2015, Zachary D. Carey
 * Made for use in the MHS Technology Club
 * 777 E. Union St., Medina, OH 44256
 *
 * HOW TO USE (Example)
 * ---------------------
public class Example {
    private OmniBaseDrive BaseDrive;

    public void init(){
        BaseDrive = new OmniBaseDrive(FrontLeft motorName, FrontRight motorName, BackLeft motorName, BackRight motorName);
    }

    public void loop(){
        BaseDrive.SetMotors(gamepad1); //Or gamepad2, if thats what you need.
        BaseDrive.SetMotors(driveStickX, driveStickY, turnStickX, turnStickY); //Optional. Can dive it values of a joystick.
        BaseDrive.SetMotors(OmniBaseDrive.Direction, speed); //Optional. Designed for easy autonomous. Please note that when using Direction.Stop, speed can be any value.
        BaseDrive.setDeadzone(number 0 to 1); //Optional. Sets the deadzone for the sticks on the controller
        BaseDrive.setMotorPower(number -1 to 1); //Optional. Sets how fast the motors go. Negative numbers make all motors go in reverse.
        BaseDrive.setControls(boolean); //Optional. If true, left stick is used for driving and right is used for turning. If false, vice versa.
        BaseDrive.setBackLeftMotor(DcMotor or string); //Optional. DcMotor can be used or motor name. Will change what motor that uses. works for all motors.
        BaseDrive.setMotorDirection(DcMotor, boolean direction); //Optional. DcMotor is what motor and if true, motor is set forwards. false for reverse.
        BaseDrive.getBackLeftMotor(); //Optional. returns that DcMotor being used. Works for all corners.
        BaseDrive.getMotorPower(); //Optional. returns current motor power being used.
    }
}

 */

package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robocol.Telemetry;


public class OmniBaseDrive{
  private Telemetry telemetry = new Telemetry();
  private HardwareMap hardwareMap;

  private static final double s1 = Math.PI / 4;
  private static final double s2 = 3 * Math.PI / 4;
  private double deadzone;
  private double coef;
  private double MOTOR_POWER;
  private Controls StandardControls;

  private DcMotor mFL;
  private DcMotor mFR;
  private DcMotor mBL;
  private DcMotor mBR;

  public OmniBaseDrive(HardwareMap hm){
    hardwareMap = hm;
    setDeadzone(0.2);
    setMotorPower(1);
    setControls(Controls.Standard);
  }

  public OmniBaseDrive(HardwareMap hm, String motorFrontLeft, String motorFrontRight, String motorBackLeft, String motorBackRight){
    hardwareMap = hm;
    mFL = hardwareMap.dcMotor.get(motorFrontLeft);
    mFR = hardwareMap.dcMotor.get(motorFrontRight);
    mBL = hardwareMap.dcMotor.get(motorBackLeft);
    mBR = hardwareMap.dcMotor.get(motorBackRight);
    setDeadzone(0.2);
    setMotorPower(1);
    setControls(Controls.Standard);
    setMotorDirection(mFL, true);
    setMotorDirection(mFR, true);
    setMotorDirection(mBL, true);
    setMotorDirection(mBR, true);
  }

  public void DriveMotors(Gamepad controller){//useLeftStick = false then it uses right stick.
    if(StandardControls == Controls.Standard){
      DriveMotors(controller.left_stick_x, controller.left_stick_y, controller.right_stick_x, controller.right_stick_y);
    }else{
      DriveMotors(controller.right_stick_x, controller.right_stick_y, controller.left_stick_x, controller.left_stick_y);
    }
  }

  public void DriveMotors(float driveStickX, float driveStickY, float turnStickX, float turnStickY) {
    double distDrive = Math.sqrt(Math.pow(driveStickX, 2) + Math.pow(driveStickY, 2));
    double distTurn = Math.sqrt(Math.pow(turnStickX, 2) + Math.pow(turnStickY, 2));

    if(distDrive >= deadzone){
      double angle = Math.atan2(driveStickY, driveStickX);
      double speed = Math.min(Math.pow(coef * (distDrive - deadzone), 2), 1);
      if (angle >= s1 && angle < s2){
        DriveMotors(Direction.Forward, speed);
      }else if(angle >= -s1 && angle < s1){
        DriveMotors(Direction.Right, speed);
      }else if(angle >= -s2 && angle < -s1){
        DriveMotors(Direction.Backward, speed);
      }else{
        DriveMotors(Direction.Left, speed);
      }
    }else if(distTurn >= deadzone){
      //telemetry.addData("♪You spin me right round, baby Right round like a record, baby Right round round round♪", 0);
      double angle = Math.atan2(turnStickY, turnStickX);
      double speed = Math.min(Math.pow(coef * (distTurn - deadzone), 2), 1);
      if(angle >= -s1 && angle <= s1){
        DriveMotors(Direction.TurnRight, speed);
      }else if(Math.abs(angle) >= s2){
        DriveMotors(Direction.TurnLeft, speed);
      }else{
        DriveMotors(Direction.Stop, 0);
      }
    }else{
      DriveMotors(Direction.Stop, 0);
    }
  }

  public void DriveMotors(Direction direction, double speed){
    if (direction == Direction.Forward){
      setMotorSpeed(mFL, -speed);
      setMotorSpeed(mFR, speed);
      setMotorSpeed(mBL, -speed);
      setMotorSpeed(mBR, speed);
    }else if(direction == Direction.Right){
      setMotorSpeed(mFL, speed);
      setMotorSpeed(mFR, speed);
      setMotorSpeed(mBL, -speed);
      setMotorSpeed(mBR, -speed);
    }else if(direction == Direction.Backward){
      setMotorSpeed(mFL, speed);
      setMotorSpeed(mFR, -speed);
      setMotorSpeed(mBL, speed);
      setMotorSpeed(mBR, -speed);
    }else if(direction == Direction.Left){
      setMotorSpeed(mFL, -speed);
      setMotorSpeed(mFR, -speed);
      setMotorSpeed(mBL, speed);
      setMotorSpeed(mBR, speed);
    }else if(direction == Direction.TurnRight){
      setMotorSpeed(mFL, -speed);
      setMotorSpeed(mFR, -speed);
      setMotorSpeed(mBL, -speed);
      setMotorSpeed(mBR, -speed);
    }else if(direction == Direction.TurnLeft){
      setMotorSpeed(mFL, speed);
      setMotorSpeed(mFR, speed);
      setMotorSpeed(mBL, speed);
      setMotorSpeed(mBR, speed);
    }else if(direction == Direction.Stop){
      setMotorSpeed(mFL, 0);
      setMotorSpeed(mFR, 0);
      setMotorSpeed(mBL, 0);
      setMotorSpeed(mBR, 0);
    }
  }

  private void setMotorSpeed(DcMotor m, double s){
    m.setPower(constrain(s *= MOTOR_POWER, -1, 1));
  }

  private double constrain(double value, double min, double max){
    return Math.min(Math.max(value, min), max);
  }

  public void setControls(Controls control){
    StandardControls = control;
  }

  public void setDeadzone(double zone){
    zone = constrain(zone, 0, 1);
    deadzone = zone;
    if(zone == 0){
      coef = 1;
    }else {
      coef = 1 / Math.pow(1 - zone, 2);
    }
  }

  public void setMotorDirection(DcMotor motor, boolean direction){
    if(direction) {
      motor.setDirection(DcMotor.Direction.FORWARD);
    }else{
      motor.setDirection(DcMotor.Direction.REVERSE);
    }
  }

  public boolean getMotorDirection(DcMotor motor){
    if(motor.getDirection() == DcMotor.Direction.FORWARD){
      return true;
    }
    return false;
  }

  public void setMotorPower(double power){
    MOTOR_POWER = constrain(power, -1, 1);
  }

  public double getMotorPower(){
    return MOTOR_POWER;
  }

  public DcMotor getFrontLeftMotor(){
    return mFL;
  }

  public DcMotor getFrontRightMotor(){
    return mFR;
  }

  public DcMotor getBackLeftMotor(){
    return mBL;
  }

  public DcMotor getBackRightMotor(){
    return mBR;
  }

  public void setFrontLeftMotor(DcMotor motor){
    mFL = motor;
  }

  public void setFrontRightMotor(DcMotor motor){
    mFR = motor;
  }

  public void setBackLeftMotor(DcMotor motor){
    mBL = motor;
  }

  public void setBackRightMotor(DcMotor motor){
    mBR = motor;
  }

  public void setFrontLeftMotor(String motor){
    mFL = hardwareMap.dcMotor.get(motor);
  }

  public void setFrontRightMotor(String motor){
    mFR = hardwareMap.dcMotor.get(motor);
  }

  public void setBackLeftMotor(String motor){
    mBL = hardwareMap.dcMotor.get(motor);
  }

  public void setBackRightMotor(String motor){
    mBR = hardwareMap.dcMotor.get(motor);
  }

  public void setMotors(String motorFrontLeft, String motorFrontRight, String motorBackLeft, String motorBackRight){
    mFL = hardwareMap.dcMotor.get(motorFrontLeft);
    mFR = hardwareMap.dcMotor.get(motorFrontRight);
    mBL = hardwareMap.dcMotor.get(motorBackLeft);
    mBR = hardwareMap.dcMotor.get(motorBackRight);
    setMotorDirection(mFL, true);
    setMotorDirection(mFR, true);
    setMotorDirection(mBL, true);
    setMotorDirection(mBR, true);
  }

  public void setMotors(DcMotor motorFrontLeft, DcMotor motorFrontRight, DcMotor motorBackLeft, DcMotor motorBackRight){
    mFL = motorFrontLeft;
    mFR = motorFrontRight;
    mBL = motorBackLeft;
    mBR = motorBackRight;
  }

  public static enum Direction{
    Forward,
    Backward,
    Left,
    Right,
    TurnLeft,
    TurnRight,
    Stop;
  }

  public static enum Controls{
    Standard,
    Reversed;
  }

}
