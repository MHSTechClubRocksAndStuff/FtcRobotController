package com.qualcomm.ftcrobotcontroller.opmodes.MedinaTechClub;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Zack on 11/11/2015.
 */
public class MoveServo extends OpMode {
    Servo servoRack;
    Servo servoArm;
    Servo servoLeftScoop;
    Servo servoRightScoop;

    double minPos = 0;
    double maxPos = 1;
    double deltaMove = 0.001;
    double posRack = 0.5;

    boolean armExtended = false; //true for hook deployed, false for hook stored
    double servoArmMax = 1;
    double servoArmMin = 0;

    public void init() {
        servoRack = hardwareMap.servo.get("Hook Arm");
        servoRack.scaleRange(0, 1);
        //servoArm = hardwareMap.servo.get("Arm Extender");

        //servoLeftScoop = hardwareMap.servo.get("leftScoop");
        //servoRightScoop = hardwareMap.servo.get("rightScoop");
    }


    public void loop() {
        //original code for buttons x and b
       /* if(gamepad1.x){
            pos -= deltaMove;
        }
        if(gamepad1.b){
            pos += deltaMove;
        }*/
        //new code for bumpers to move rack
        if (gamepad1.left_bumper) {
            posRack -= deltaMove;
            posRack = Math.max(minPos, Math.min(maxPos, posRack));
        }
        if (gamepad1.right_bumper) {
            posRack += deltaMove;
            posRack = Math.max(minPos, Math.min(maxPos, posRack));
        }


        servoRack.setPosition(posRack);

        //new code to control arm angle
       /* if (gamepad1.y) {
            servoArm.setPosition(servoArmMax);
            armExtended = true;
        }
        if (gamepad1.x) {
            servoArm.setPosition(servoArmMin);
            armExtended = false;
        }
*/

        telemetry.addData("Rack Servo", posRack);

    }
}




