package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.FTC2015_2016;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankBase;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankMotor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Zack on 1/16/2016.
 */
public class AutoDriveStop extends LinearOpMode{
    private TankBase Tank;
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private DcMotor lDrive;
    private DcMotor rDrive;
    private final int encoderDistance = -8100;
    private final double motorSpeed = 0.25;

    public void runOpMode()throws InterruptedException{
        leftDrive = hardwareMap.dcMotor.get("ml2");
        lDrive = hardwareMap.dcMotor.get("ml1");
        rightDrive = hardwareMap.dcMotor.get("mr2");
        rDrive = hardwareMap.dcMotor.get("mr1");
        lDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        rDrive.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        lDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        rDrive.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        //lDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //rDrive.setChannelMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //Tank.setSideDirection(TankMotor.Side.Left, TankMotor.Direction.Reverse);
       // lDrive.setTargetPosition(-encoderDistance);
        //rDrive.setTargetPosition(encoderDistance);
        while (true) {
            if(lDrive.getCurrentPosition() < -encoderDistance){
                lDrive.setPower(motorSpeed);
                leftDrive.setPower(motorSpeed);
            }else{
                lDrive.setPower(0);
                leftDrive.setPower(0);
            }
            if(rDrive.getCurrentPosition() > encoderDistance){
                rDrive.setPower(-motorSpeed);
                rightDrive.setPower(-motorSpeed);
            }else{
                rDrive.setPower(0);
                rightDrive.setPower(0);
            }
            telemetry.addData("Left", lDrive.getCurrentPosition());
            telemetry.addData("Right", rDrive.getCurrentPosition());
            waitOneFullHardwareCycle();
        }
    }
}
