package org.firstinspires.ftc.teamcode;
// AUTONOMOUS BY TIME WITH COLOR, DISTANCE and TOUCH CODE

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;//package org.firstinspires.ftc.robotcontroller.external.samples;

@Autonomous(name = "AutoBlue_Back", group = "Sensor")
//@Disabled                            // Comment this out to add to the opmode list

public class AutoBlueBack extends LinearOpMode {

    DcMotor backleftmotor;
    DcMotor backrightmotor;
    DcMotor liftmotor;
    Servo servo0;  //Grabber
    Servo servo1;  //Grabber
    Servo servo2;  //Sensor Arm
    DigitalChannel digitaltouch;  // Hardware Device Object
    ColorSensor sensorcolor;
    DistanceSensor sensordistance;
    Boolean Isblue;
    int ColorRed = 0;
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() throws InterruptedException {


        backleftmotor = hardwareMap.dcMotor.get("backwards_left_drive");
        backrightmotor = hardwareMap.dcMotor.get("backwards_right_drive");

        // get a reference to our digitalTouch object.
        digitaltouch = hardwareMap.get(DigitalChannel.class, "sensordigital");
        // set the digital channel to input.
        //digitaltouch.setMode(DigitalChannel.Mode.INPUT);

        servo0 = hardwareMap.servo.get("servo0");
        servo1 = hardwareMap.servo.get("servo1");
        servo2 = hardwareMap.servo.get("servo2");

        // get a reference to the color sensor.
        sensorcolor = hardwareMap.get(ColorSensor.class, "sensorcolordistance");
        // get a reference to the distance sensor that shares the same name.
        sensordistance = hardwareMap.get(DistanceSensor.class, "sensorcolordistance");

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        // wait for the start button to be pressed
        waitForStart();


        // LOOP LOOP LOOP
        // loop and read the RGB and distance data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.

        runtime.reset();

        while (opModeIsActive()) {
            // convert the RGB values to HSV values.
            // multiply by the SCALE_FACTOR.
            // then cast it back to int (SCALE_FACTOR is a double)
            Color.RGBToHSV((int) (sensorcolor.red() * SCALE_FACTOR),
                    (int) (sensorcolor.green() * SCALE_FACTOR),
                    (int) (sensorcolor.blue() * SCALE_FACTOR),
                    hsvValues);

            // Send the info back to driver station using telemetry function.


            telemetry.addData("Motor Output ", "Back Left" + backleftmotor.getPower());
            telemetry.addData("Motor Output ", "Back Right" + backrightmotor.getPower());

            telemetry.addData("Distance (cm)",
                    String.format(Locale.US, "%.02f", sensordistance.getDistance(DistanceUnit.CM)));
            telemetry.addData("Hue", hsvValues[0]);

            //RESET RUNTIME
            if (runtime.seconds() >= 0 && runtime.seconds() <= 1) {
                telemetry.addData("Runtime Reset ", "Running: " + runtime.toString());
                //servo0.setPosition(0);
                //servo1.setPosition(1); //CLAMP GLYPH

                backleftmotor.setPower(0);
                backrightmotor.setPower(0);
            }

            //DROP SERVO
            if (runtime.seconds() >= 1.1 && runtime.seconds() <= 3) {
                telemetry.addData("In Drop Servo2 ", "Running: " + runtime.toString());
                servo2.setPosition(1);
            }

            //READ COLOR
            if (runtime.seconds() >= 3.1 && runtime.seconds() < 5) {
                telemetry.addData("In Read Color ", "Running: " + runtime.toString());

                // ASSUME WE ARE RED TEAM-+
                // ASSUME SENSOR FACING BACKWARDS ON ROBOT ON RIGHT SIDE
                // CHECK IF STATEMENT FOR BLUE ONLY
                if (hsvValues[0] >= 150 && hsvValues[0] <= 250) {
                    //add variable
                    ColorRed = 0;
                    telemetry.addData("In RED If TRUE ", sensorcolor.blue());
                    telemetry.addData("Hue", hsvValues[0]);

                } else {
                    telemetry.addData("In BLUE Else FALSE ", sensorcolor.red());
                    telemetry.addData("Hue", hsvValues[0]);
                    ColorRed = 1;

                }

            }


            // DO NOTHING
            /*if(runtime.seconds() >= 5.1 && runtime.seconds() <= 10) {
                backleftmotor.setPower(0);
                backrightmotor.setPower(0);
            }
*/

            // MOVE TO KNOCK OFF BALL
            if (runtime.seconds() >= 5.1 && runtime.seconds() <= 5.2) {

                telemetry.addData("ColorRed ", ColorRed);

                if (ColorRed == 1) {
                    telemetry.addData("In 2 Second Move Forwards TRUE ", "Running: " + runtime.toString());
                    backleftmotor.setPower(-.3);   //Move FORWARDS
                    backrightmotor.setPower(-.3);
                    //liftmotor.setPower(1);
                } else {
                    telemetry.addData("In 2 Second Move forward FALSE ", "Running: " + runtime.toString());
                    backleftmotor.setPower(.45);   //Move BACK
                    backrightmotor.setPower(.45);
                    //servo0.setPosition(0);
                    //servo1.setPosition(1);

                    //liftmotor.setPower(1);
                    // finish red
                }
            }

            //STOP
            if (runtime.seconds() >= 5.6 && runtime.seconds() <= 6.0) {
                telemetry.addData("STOP After Knock ", "Running: " + runtime.toString());

                backleftmotor.setPower(0);     //Stop
                backrightmotor.setPower(0);     //Stop
            }

            //RAISE SERVO
            if (runtime.seconds() >= 7.0 && runtime.seconds() <= 9) {
                telemetry.addData("In Raise Servo2 ", "Running: " + runtime.toString());

                servo2.setPosition(0);
                //liftmotor.setPower(0);
            }


            //MOVE FORWARD OFF RAMP
//            if(runtime.seconds() >= 10.1 && runtime.seconds() <= 11.0) {
//                if (ColorRed == 1 ) {
//                    telemetry.addData("In Move Backwards BLUE ", "Running: " + runtime.toString());
//                    backleftmotor.setPower(.5);     //Faster BACKWARD
//                    backrightmotor.setPower(-.55);     //Faster BACKWARD
//                    //servo0.setPosition(1);
//                    //servo1.setPosition(0);
//                }
//                else {
//                    telemetry.addData("In Move Backward RED ", "Running: " + runtime.toString());
//                    backleftmotor.setPower(-.5);     //Faster BACKWARD
//                    backrightmotor.setPower(.55);     //Faster BACKWARD
//                    //servo0.setPosition(1);
//                    //servo1.setPosition(0);
//
//                }
//            }
//
//            //STOP
//            if(runtime.seconds() >= 11.1 && runtime.seconds() <= 13.0) {
//                telemetry.addData("STOP After Ramp ", "Running: " + runtime.toString());
//                    backleftmotor.setPower(0);     //Stop
//                    backrightmotor.setPower(0);     //Stop
//
//            }
//
//            //SPIN SLOW, TURN RIGHT
//            if(runtime.seconds() >= 13.1 && runtime.seconds() <= 13.9) {
//                telemetry.addData("In Spin ", "Running: " + runtime.toString());
//                backleftmotor.setPower(.5);   // Slow Turn Left
//                backrightmotor.setPower(.5);  // Slow Turn Left
//            }
//
//            //STOP
//            if(runtime.seconds() >= 14.2 && runtime.seconds() <= 15.0) {
//                telemetry.addData("STOP After Spin ", "Running: " + runtime.toString());
//                backleftmotor.setPower(0);     //Stop
//                backrightmotor.setPower(0);     //Stop
//
//            }
//
//            //MOVE FORWARD TO WALL
//            if(runtime.seconds() >= 15.1 && runtime.seconds() <= 15.5) {
//                    telemetry.addData("In Move Forward ", "Running: " + runtime.toString());
//                    backleftmotor.setPower(-.3);     //Faster FORWARD
//                    backrightmotor.setPower(.3);     //Faster FORWARD
//            }
//
//            //STOP DRIVE MOTORS
//            if(runtime.seconds() >= 15.6 && runtime.seconds() <= 20) {
//                telemetry.addData("STOP END ", "Running: " + runtime.toString());
//                backleftmotor.setPower(0);
//                backrightmotor.setPower(0);
//
//                //servo0.setPosition(1);
//                //servo1.setPosition(0); //CLAMP GLYPH
//
//            }
//
//            if (runtime.seconds() >= 20.1 && runtime.seconds() <= 30) {
//                telemetry.addData("STOP END ", "Running: " + runtime.toString());
//                backleftmotor.setPower(0);
//                backrightmotor.setPower(0);
//            }

            // change the background color to match the color detected by the RGB sensor.
            // pass a reference to the hue, saturation, and value array as an argument
            // to the HSVToColor method.

            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });

            telemetry.update();
        }

        // Set the panel back to the default color
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });
    }
}


