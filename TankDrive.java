/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Tank Drive", group="Linear Opmode")
//@Disabled
public class TankDrive extends LinearOpMode {

    HardwarePushbot robot   = new HardwarePushbot();   // Use a Pushbot's hardware

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftDrive0 = null;
    public DcMotor leftDrive1 = null;
    public DcMotor rightDrive2 = null;
    public DcMotor rightDrive3 = null;
    private DcMotor rightArm = null;
    private DcMotor leftArm = null;
    private DcMotor Zuck = null;
    public Servo servoTest0;
    public Servo servoTest1;

    public Servo servoArm0;
    public Servo servoArm1;

    private DcMotor Arm = null;

    double suckpeed = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.init(hardwareMap);


        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step on busty blondes(using the FTC Robot Controller app on the phone).
        leftDrive0  = hardwareMap.get(DcMotor.class, "left_drive0");
        leftDrive1 = hardwareMap.get(DcMotor.class, "left_drive1");
        rightDrive2 = hardwareMap.get(DcMotor.class, "right_drive2");
        rightDrive3 = hardwareMap.get(DcMotor.class, "right_drive3");
        rightArm = hardwareMap.get(DcMotor.class, "right_Arm");
        leftArm = hardwareMap.get(DcMotor.class, "left_Arm");
        Zuck = hardwareMap.get(DcMotor.class, "Zuck");
        servoTest0 = hardwareMap.get(Servo.class, "servo1");
        servoTest1 = hardwareMap.get(Servo.class, "servo1");

        servoArm0 = hardwareMap.get(Servo.class, "servoArm0");
        servoArm1 = hardwareMap.get(Servo.class, "servoArm1");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive0.setDirection(DcMotor.Direction.FORWARD);
        leftDrive1.setDirection(DcMotor.Direction.FORWARD);
        rightDrive2.setDirection(DcMotor.Direction.REVERSE);
        rightDrive3.setDirection(DcMotor.Direction.REVERSE);
        Zuck.setDirection(DcMotor.Direction.REVERSE);
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Setup a variable for each drive wheel to save power level for telemetry
            //double drivel;
            //double driverj;

            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            double drivel = -gamepad1.left_stick_y;
            double driver =  -gamepad1.right_stick_y;
            double ArmPower = gamepad2.left_stick_y;
            double tgtPower = 0;
            //double suckspeed = gamepad2.right_trigger * 2;
            double zuckspeed = gamepad2.left_trigger;

            int armStartPosition = 0;
            int armEndPosition = 120;

            //leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            //rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
            rightArm.setPower(ArmPower);
            leftArm.setPower(ArmPower);

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            // Send calculated power to wheels
            /*while (gamepad2.left_bumper) {
                if (rightArm.getTargetPosition() - 20 >= rightArm.getCurrentPosition() && (rightArm.getTargetPosition() + 20 <= rightArm.getCurrentPosition())) {
                    rightArm.setPower(0);
                } else if (rightArm.getCurrentPosition() < rightArm.getTargetPosition()) {
                    ArmPower = ((rightArm.getCurrentPosition() - rightArm.getTargetPosition()) * .01);
                    rightArm.setPower(ArmPower);
                } else if (rightArm.getCurrentPosition() > rightArm.getTargetPosition()) {
                    ArmPower = ((rightArm.getCurrentPosition() - rightArm.getTargetPosition()) * .01);
                    rightArm.setPower(ArmPower);
                }
                if (leftArm.getTargetPosition() - 35 >= leftArm.getCurrentPosition() && (leftArm.getTargetPosition() + 35 <= leftArm.getCurrentPosition())){
                    leftArm.setPower(0);
                    rightArm.setPower(0);
                }
                else if (leftArm.getCurrentPosition() < leftArm.getTargetPosition() + 35) {
                    ArmPower = ((leftArm.getCurrentPosition() - leftArm.getTargetPosition()) * .01);
                    leftArm.setPower(ArmPower);
                    rightArm.setPower(ArmPower);
                }
                else if (leftArm.getCurrentPosition() > leftArm.getTargetPosition() - 35) {
                    ArmPower = ((leftArm.getCurrentPosition() - leftArm.getTargetPosition()) * .01);
                    leftArm.setPower(ArmPower);
                    rightArm.setPower(ArmPower);
                }
            }
            if (gamepad2.right_bumper) {
                rightArm.setPower(0);
                leftArm.setPower(0);
            }*/
            if (gamepad1.left_bumper) {
                leftDrive0.setPower(drivel);
                leftDrive1.setPower(drivel);
                rightDrive2.setPower(driver);
                rightDrive3.setPower(-driver);
            }
            else {
                leftDrive0.setPower(drivel*.6);
                leftDrive1.setPower(drivel*.6);
                rightDrive2.setPower(driver*.7);
                rightDrive3.setPower(-driver*.7);
            }

            //Zuck is programed like any other motor we dont care about fine tune control so we just go as simple as possible.
            Zuck.setPower(zuckspeed);

            double armPosition0 = servoArm0.getPosition();
            //int armPosition1 = servoArm1.getCurrentPosition();

            if (gamepad2.dpad_down) {
               servoArm0.setPosition(0);
               servoArm1.setPosition(1);
            }
            if (gamepad2.dpad_down & gamepad2.b){
                servoArm0.setPosition(1);
                servoArm1.setPosition(0);
            }

            /*while (gamepad2.left_bumper) {
                if (Arm.getTargetPosition() - 20 >= Arm.getCurrentPosition() && (Arm.getTargetPosition() + 20 <= Arm.getCurrentPosition())) {
                    Arm.setPower(0);
                } else if (Arm.getCurrentPosition() < Arm.getTargetPosition()) {
                    ArmPower = ((Arm.getCurrentPosition() - Arm.getTargetPosition()) * .01);
                    Arm.setPower(ArmPower);
                } else if (Arm.getCurrentPosition() > Arm.getTargetPosition()) {
                    ArmPower = ((Arm.getCurrentPosition() - Arm.getTargetPosition()) * .01);
                    Arm.setPower(ArmPower);
                }


            }
            if (gamepad1.right_bumper) {
                Arm.setPower(0);
            }*/
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", drivel, driver);
            telemetry.addData("Arm_PositionR", rightArm.getCurrentPosition());
            telemetry.addData("Target Power", tgtPower);
            telemetry.addData("Arm_PositionL", leftArm.getCurrentPosition());
            telemetry.update();

            /*if (gamepad2.right_trigger >= .05){
                Zuck.setPower(zuckspeed);
            }
            else if (gamepad2.left_trigger >= .05);
            Zuck.setPower(-zuckspeed);*/
        }
    }
}
