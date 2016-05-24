package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase;

import java.util.ArrayList;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class TankBase {
    private ArrayList<TankMotor> Motors;
    private HardwareMap hm;
    private Controls controls;
    private static final double s1 = Math.PI / 4;
    private static final double s2 = 3 * Math.PI / 4;

    private double powerFunction = 1;
    private double minPower = 0;
    private double maxPower = 1;
    private double coef;
    private double deadzone = 0.2;
    private double maxX = 1;

    private double leftDebugPower = 0;
    private double rightDebugPower = 0;

    private double motorPower = 1;
    private DriveMode driveMode;

    public TankBase(HardwareMap hm){
        Motors = new ArrayList<TankMotor>();
        this.hm = hm;
        setDeadzone(0.2);
        setPowerFunction(2);
        setMaxPower(1);
        setMaxX(1);
        setMotorPower(1);
        setControls(Controls.LeftStickDrive);
        setDriveMode(DriveMode.FreeRun);
    }

    public TankMotor addMotor(DcMotor motor, TankMotor.Side side){
        TankMotor t = new TankMotor(motor, side);
        t.setRunMode(driveMode.RunMode);
        Motors.add(t);
        return t;
    }

    public TankMotor addMotor(String motor, TankMotor.Side side){
        //DcMotor m = hm.dcMotor.get(motor);
        return addMotor(hm.dcMotor.get(motor), side);
    }

    public void DriveMotors(Gamepad controller){//useLeftStick = false then it uses right stick.
        if(controls == Controls.LeftStickDrive){
            DriveMotors(controller.left_stick_x, controller.left_stick_y, controller.left_stick_x, controller.left_stick_y);
        }else if(controls == Controls.RightStickDrive){
            DriveMotors(controller.right_stick_x, controller.right_stick_y, controller.right_stick_x, controller.right_stick_y);
        }else if(controls == Controls.ShooterDrive){
            DriveMotors(controller.left_stick_x, controller.left_stick_y, controller.right_stick_x, controller.right_stick_y);
        }else if(controls == Controls.TankDrive){
            DriveMotors(controller.left_stick_y, controller.right_stick_y);
        }else{
            System.out.println("YOU SUCK");
        }
    }

    public void DriveMotors(float leftDriveAxis, float rightDriveAxis){
        double powerLeft = 0;
        double powerRight = 0;
        if(Math.abs(leftDriveAxis) >= deadzone){
            powerLeft = getSpeed(leftDriveAxis);
            if(leftDriveAxis < 0){
                powerLeft *= -1;
            }
        }
        if(Math.abs(rightDriveAxis) >= deadzone){
            powerRight = getSpeed(rightDriveAxis);
            if(rightDriveAxis < 0){
                powerRight *= -1;
            }
        }
        setMotors(powerLeft, powerRight);
    }

    public void DriveMotors(float driveStickX, float driveStickY, float turnStickX, float turnStickY) {
        double distDrive = Math.sqrt(Math.pow(driveStickX, 2) + Math.pow(driveStickY, 2));
        double distTurn = Math.sqrt(Math.pow(turnStickX, 2) + Math.pow(turnStickY, 2));
        boolean setDrive = false;
        if(distDrive >= deadzone){
            double angle = Math.atan2(driveStickY, driveStickX);
            double speed = getSpeed(distDrive);
            if ((angle >= s1) && (angle < s2)){
                setDrive = true;
                DriveMotors(Direction.Forward, speed);
            }else if((angle >= -s2) && (angle < -s1)){
                setDrive = true;
                DriveMotors(Direction.Backward, speed);
            }
        }
        if((distTurn >= deadzone) && (!setDrive)){
            double angle = Math.atan2(turnStickY, turnStickX);
            double speed = getSpeed(distTurn);
            if((angle >= -s1) && (angle <= s1)){
                DriveMotors(Direction.TurnRight, speed);
            }else if(Math.abs(angle) >= s2){
                DriveMotors(Direction.TurnLeft, speed);
            }else{
                DriveMotors(Direction.Stop, 0);
            }
        }else if(!setDrive){
            DriveMotors(Direction.Stop, 0);
        }
    }

    public double getSpeed(double x){
        return Math.max(0, Math.min(1, Math.pow((coef * x) - deadzone, powerFunction) + minPower));
    }

    public void DriveMotors(Direction direction, double speed){
        if (direction == Direction.Forward){
            setMotors(-speed, speed);
        }else if(direction == Direction.Backward){
            setMotors(speed, -speed);
        }else if(direction == Direction.TurnRight){
            setMotors(speed);
        }else if(direction == Direction.TurnLeft){
            setMotors(-speed);
        }else if(direction == Direction.Stop){
            setMotors(0);
        }
    }

    private void setMotors(double power){
        power *= motorPower;
        rightDebugPower = power;
        leftDebugPower = power;
        for(TankMotor m: Motors){
            m.setPower(power * motorPower);
        }
    }

    private void setMotors(double leftPower, double rightPower){
        leftPower *= motorPower;
        rightPower *= motorPower;
        for(TankMotor m: Motors){
            if(m.getSide() == TankMotor.Side.Left){
                leftDebugPower = leftPower;
                m.setPower(leftPower);
            }else if(m.getSide() == TankMotor.Side.Right){
                rightDebugPower = rightPower;
                m.setPower(rightPower);
            }
        }
    }

    private void setMotors(TankMotor.Side side, double power){
        power *= motorPower;
        if(TankMotor.Side.Left == side){
            leftDebugPower = power;
        }else{
            rightDebugPower = power;
        }
        for(TankMotor m: Motors){
            if(m.getSide() == side){
                m.setPower(power);
            }
        }
    }

    public void setControls(Controls controls){
        this.controls = controls;
    }

    private void checkException(double v){
        if(v < -1 || v > 1){
            throw new IndexOutOfBoundsException("Value must be between inclusive -1 to 1.");
        }
    }

    public void setDeadzone(double zone){
        checkException(zone);
        deadzone = zone;
        setCoef();
    }

    public void setCoef(){
        if(maxX == 0){
            throw new IndexOutOfBoundsException("Value can not be 0");
        }else {
            coef = (deadzone + Math.pow(maxPower - minPower, 1 / powerFunction)) / maxX;
            //coef = (Math.pow(y, 1 / p) + d) / m;
            //m = max X value
            //y = max Y value
            //d = min X
            //p = power of the function (i.e. x^2 versus x^10)
        }
    }

    public void setSideDirection(TankMotor.Side side, TankMotor.Direction direction){
        for(TankMotor m: Motors){
            if(m.getSide() == side){
                m.setDirection(direction);
            }
        }
    }

    public void setMotorPower(double power){
        checkException(power);
        motorPower = power;
    }

    public void setDriveMode(DriveMode mode){
        this.driveMode = mode;
        for(TankMotor motor : Motors){
            motor.setRunMode(mode.RunMode);
        }
    }

    public void setPowerFunction(double func){
        this.powerFunction = func;
        setCoef();
    }

    public void setMaxPower(double power){
        checkException(power);
        this.maxPower = power;
        setCoef();
    }

    public void setMinPower(double power){
        checkException(power);
        this.minPower = power;
        setCoef();
    }

    public void setMaxX(double val){
        checkException(val);
        this.maxX = val;
        setCoef();
    }

    public double getLeftPower(){
        return leftDebugPower;
    }

    public double getRightPower(){
        return rightDebugPower;
    }

    public enum Direction{
        Forward,
        Backward,
        TurnLeft,
        TurnRight,
        Stop
    }

    public enum Controls{
        LeftStickDrive,
        RightStickDrive,
        ShooterDrive,
        TankDrive
    }

    public enum DriveMode{
        FreeRun(TankMotor.RunMode.RunWithoutEncoders),
        SyncWithEncoders(TankMotor.RunMode.RunUsingEncoders);

        TankMotor.RunMode RunMode;

        DriveMode(TankMotor.RunMode mode){
            this.RunMode = mode;
        }
    }
}

