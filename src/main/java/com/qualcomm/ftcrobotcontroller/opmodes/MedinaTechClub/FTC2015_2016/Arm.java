package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.FTC2015_2016;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Zack on 1/15/2016.
 */
public class Arm {
    private Servo extendMotor;
    private Servo hookerMotor;

    private double armContractedPos = .569;
    private double armExtendedPos = .808;

    private double armExtensionPosition = 0;
    private double deltaExtend = 0.05;

    private boolean hooked = false;
    private boolean hookToggle = true;

    private double HookerUnhooked = 0.12;
    private double HookerHooked = 1;
    private double hookDebug = 0;

    private Winch winch;

    public Arm(Winch sup, HardwareMap hm, String armExtendName, String hookerName){
        winch = sup;
        extendMotor = hm.servo.get(armExtendName);
        extendMotor.scaleRange(armContractedPos, armExtendedPos);
        hookerMotor = hm.servo.get(hookerName);
    }

    public void initArm(){
        armExtend(false, false);
        setHookState(false);
    }

    public void moveArm(boolean extendArm, boolean contractArm, boolean toggleHook){
        armExtend(extendArm, contractArm);
        hookerToggle(toggleHook);
    }

    private void armExtend(boolean extendArm, boolean contractArm){
        if(extendArm){
            armExtensionPosition += deltaExtend;
        }
        if(contractArm){
            armExtensionPosition -= deltaExtend;
        }
        armExtensionPosition = Math.min(1, Math.max(0, armExtensionPosition));
        extendMotor.setPosition(armExtensionPosition);
        if(armExtensionPosition > 0.5){
            winch.setExtensionSpeed();
        }else{
            winch.setContractedSpeed();
        }
    }

    private void setHookState(boolean hook){
        hooked = hook;
        if(hook){
            hookerMotor.setPosition(HookerHooked);
            hookDebug = HookerHooked;
        }else{
            hookerMotor.setPosition(HookerUnhooked);
            hookDebug = HookerUnhooked;
        }
    }

    private void hookerToggle(boolean toggleHook){
        if(hookToggle && toggleHook){
            hookToggle = false;
            setHookState(!hooked);
        }else if(!toggleHook){
            hookToggle = true;
        }
    }

    public double getExtension(){
        return armExtensionPosition;
    }

    public double getHookDebug(){
        return hookDebug;
    }
}
