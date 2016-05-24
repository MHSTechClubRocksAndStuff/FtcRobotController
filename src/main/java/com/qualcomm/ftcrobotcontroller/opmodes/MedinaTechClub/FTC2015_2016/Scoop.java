package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.FTC2015_2016;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Scoop {
    double scoopSpeed = 0.1;
    double dumpSpeed = 0.1;

    Servo leftScoop;
    Servo rightScoop;
    Servo leftDump;
    Servo rightDump;

    double scoopTarget = 1;
    double scoopPosition = scoopTarget;
    double scoopInterval = 0.005;

    double dumpTarget = 0;
    double dumpPosition = dumpTarget;
    double dumpInterval = 0.01;

    public Scoop(HardwareMap hm, String scoopLeft, String scoopRight, String dumpLeft, String dumpRight){
        leftScoop = hm.servo.get(scoopLeft);
        rightScoop = hm.servo.get(scoopRight);
        leftDump = hm.servo.get(dumpLeft);
        rightDump = hm.servo.get(dumpRight);
        setScoopLimits(0, 0.46);
    }

    public void initScoop(){
        moveScoopToPos(scoopTarget);
        moveDumpToPos(dumpTarget);
    }

    public void moveScoop(float scoopAxis, float dumpAxis){
        if(scoopAxis >= 0.3){
            changeScoopTarget(scoopSpeed * 0.05);
        }else if(scoopAxis <= -0.3){
            changeScoopTarget(scoopSpeed * -0.05);
        }

        if(dumpAxis >= 0.3){
            changeDumpTarget(dumpSpeed * 0.05);
        }else if(dumpAxis <= -0.3){
            changeDumpTarget(dumpSpeed * -0.05);
        }
    }

    private void changeScoopTarget(double inc){
        scoopTarget = Math.max(0, Math.min(1, scoopTarget + inc));
        moveScoopToPos(scoopTarget);
    }

    private void changeDumpTarget(double inc){
        dumpTarget = Math.max(0, Math.min(1, dumpTarget + inc));
        moveDumpToPos(dumpTarget);
    }

    private void moveScoopToPos(double pos){
        leftScoop.setPosition(1 - pos);
        rightScoop.setPosition(pos);
    }

    private void moveDumpToPos(double pos){
        rightDump.setPosition(1 - pos);
        leftDump.setPosition(pos);
    }

    private void setScoopLimits(double min, double max){
        leftScoop.scaleRange(1 - max, 1 - min);
        rightScoop.scaleRange(min, max);
    }

    public double getScoopTarget(){
        return scoopTarget;
    }

    public double getScoopPosition(){
        return scoopPosition;
    }

    public double getDumpTarget(){
        return dumpTarget;
    }

    public double getDumpPosition(){
        return dumpPosition;
    }
}

