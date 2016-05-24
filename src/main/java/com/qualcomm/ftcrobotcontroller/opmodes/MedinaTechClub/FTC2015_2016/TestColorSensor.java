package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.FTC2015_2016;

/**
 * Created by Zack on 1/15/2016. Zack The Jac
 * */

import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankBase;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.ColorSensor;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankBase;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankMotor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

public class TestColorSensor extends OpMode{

    private TankBase Tank;
    private boolean drive = true;
    private ColorSensor color;
    private DcMotor testL;
    private DcMotor testR;

    public void init(){
        Tank = new TankBase(hardwareMap);
        Tank.setControls(TankBase.Controls.TankDrive);
        Tank.setMotorPower(1);
        Tank.setDeadzone(0.2);
        Tank.setPowerFunction(10);
        Tank.setMinPower(0.15);
        Tank.setDriveMode(TankBase.DriveMode.FreeRun);
        testL = Tank.addMotor("ml1", TankMotor.Side.Left).getMotor();
        testR = Tank.addMotor("mr1", TankMotor.Side.Right).getMotor();
        testL.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        testR.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        testL.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        testR.setChannelMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        Tank.setSideDirection(TankMotor.Side.Left, TankMotor.Direction.Reverse);

        //color = new ColorSensor(hardwareMap, "ColorSensor");
    }
    //-8694
    //-8287
    public void loop(){
       // if(drive) {
            Tank.DriveMotors(gamepad1);
       // }else{
       //     Tank.DriveMotors(TankBase.Direction.Stop, 0);
       // }

       // double red = color.getRed();
       // double green = color.getGreen();
       // double blue = color.getBlue();

       // if(red < 70 && green < 70 && blue > 170){
        //    drive = false;
       // }

        //telemetry.addData("Red", red);
       // telemetry.addData("Green", green);
       // telemetry.addData("Blue", blue);
        telemetry.addData("EncoderL", testL.getCurrentPosition());
        telemetry.addData("EncoderR", testR.getCurrentPosition());
    }
}
