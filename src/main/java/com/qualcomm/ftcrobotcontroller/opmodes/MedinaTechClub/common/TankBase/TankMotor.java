package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class TankMotor {
    private DcMotor motor;
    private Side side;
    private Direction direction;
    private double power;

    public TankMotor(DcMotor motor, Side side){
        this.motor = motor;
        this.side = side;
        direction = Direction.Forward;
        power = 0;
    }

    public void setPower(double speed){
        if(power < -1 || power > 1){
            throw new IndexOutOfBoundsException("Power must be between inclusive -1 and 1.");
        }
        power = speed;
        try {
            motor.setPower(speed);
        }finally{

        }
    }

    public void setDirection(Direction direc){
        direction = direc;
        motor.setDirection(direc.Direction);
    }

    public int getCurrentPosition(){
        return motor.getCurrentPosition();
    }

    public double getPower(){
        return power;
    }

    public Direction getDirection(){
        return direction;
    }

    public Side getSide(){
        return side;
    }

    public static enum Side{
        Right,
        Left;
    }

    public static enum Direction{
        Forward(DcMotor.Direction.FORWARD),
        Reverse(DcMotor.Direction.REVERSE);

        DcMotor.Direction Direction;

        Direction(DcMotor.Direction direction){
            this.Direction = direction;
        }
    }

    public static enum RunMode{
        RunToPosition(DcMotorController.RunMode.RUN_TO_POSITION),
        RunUsingEncoders(DcMotorController.RunMode.RUN_USING_ENCODERS),
        RunWithoutEncoders(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        DcMotorController.RunMode RunMode;

        RunMode(DcMotorController.RunMode runMode){
            this.RunMode = runMode;
        }
    }

    public DcMotor getMotor(){
        return motor;
    }

    public void setRunMode(RunMode runMode){
        motor.setChannelMode(runMode.RunMode);
    }
}
