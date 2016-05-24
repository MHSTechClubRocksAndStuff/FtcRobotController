package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankBase;
import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.TankBase.TankMotor;

/**
 * Created by Zack on 9/15/2015.
 */
public class TankOpExample extends OpMode{
    private TankBase Tank;

    @Override
    public void init(){
        Tank = new TankBase(hardwareMap);
        Tank.setControls(TankBase.Controls.RightStickDrive);
        Tank.setMotorPower(0.8);
        Tank.setDeadzone(0.2);
        Tank.setDriveMode(TankBase.DriveMode.SyncWithEncoders);
        Tank.addMotor("ml1", TankMotor.Side.Left);
        Tank.addMotor("ml2", TankMotor.Side.Left);
        Tank.addMotor("mr1", TankMotor.Side.Right);
        Tank.addMotor("mr2", TankMotor.Side.Right);
        Tank.setSideDirection(TankMotor.Side.Left, TankMotor.Direction.Reverse);
    }

    @Override
    public void loop(){
        Tank.DriveMotors(gamepad1);
        //telemetry.addData("Encoder", m1.);
    }
}
