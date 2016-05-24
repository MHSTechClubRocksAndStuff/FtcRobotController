/*
 * Copyright (c) 2015, Zachary D. Carey
 * Made for use in the MHS Technology Club
 * 777 E. Union St., Medina, OH 44256
 */

package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub;

import com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub.common.OmniBaseDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


public class OmniBase extends OpMode{
  private OmniBaseDrive BaseDrive;

    @Override
    public void init() {
      BaseDrive = new OmniBaseDrive(hardwareMap, "mfl", "mfr", "mbl", "mbr");
    }

    @Override
    public void loop() {
      BaseDrive.DriveMotors(gamepad1);

    //telemetry.addData("xAxis", direction);
    //telemetry.addData("yAxis", throttle);
  }

}
