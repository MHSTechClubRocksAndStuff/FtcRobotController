package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.FTC2015_2016;

/**
 * Created by Zack on 10/12/2015.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankBase.Controls;

public class RobotArm {

    private final DcMotor base;
    private final DcMotor shoulder;
    private final DcMotor extender;
    private double deadzone;
    private double maxSpeed;
    private double minSpeed;
    private Controls userControls;

    public RobotArm(DcMotor base, DcMotor shoulder, DcMotor extender){
        this.base = base;
        this.shoulder = shoulder;
        this.extender = extender;
        setDeadzone(0.2);
        setMinSpeed(0);
        setMaxSpeed(1);
        setControls(Controls.LeftStickDrive);
    }

    public void moveArm(Gamepad gamepad){
        if(userControls == Controls.LeftStickDrive){
            moveArm(gamepad.left_stick_x, gamepad.left_stick_y, gamepad.a, gamepad.y);
        }else if(userControls == Controls.RightStickDrive){
            moveArm(gamepad.right_stick_x, gamepad.right_stick_y, gamepad.a, gamepad.y);
        }
        if(userControls == Controls.ShooterDrive){
            float a = 0, b = 0;

            if(gamepad.dpad_left){
                a = -1;
            }else if(gamepad.dpad_right){
                a = 1;
            }

            if(gamepad.dpad_up){
                b = 1;
            }else if(gamepad.dpad_down){
                b = -1;
            }

            moveArm(a, b, gamepad.a, gamepad.y);
        }
    }

    public void moveArm(float baseMove, float shoulderMove, boolean extenderIn, boolean extenderOut){
        if(Math.abs(baseMove) > deadzone){
            base.setPower(getAdjustedPower(baseMove));
        }else{
            base.setPower(0);
        }
        if(Math.abs(shoulderMove) > deadzone){
            shoulder.setPower(getAdjustedPower(shoulderMove));
        }else{
            shoulder.setPower(0);
        }
        if(extenderIn){
            extender.setPower(-maxSpeed);
        }else if(extenderOut){
            extender.setPower(maxSpeed);
        }else{
            extender.setPower(0);
        }
    }

    public void setDeadzone(double zone){
        deadzone = zone;
    }

    private double getAdjustedPower(float val){
        if(val == 0)
            return 0;
        return ((val - deadzone) / (1 - deadzone)) * (maxSpeed - minSpeed) + (minSpeed) * (val / Math.abs(val));
    }

    public void setMaxSpeed(double speed){
        maxSpeed = speed;
    }

    public void setMinSpeed(double speed){
        minSpeed = speed;
    }

    public void setControls(Controls controls){
        userControls = controls;
    }
}
