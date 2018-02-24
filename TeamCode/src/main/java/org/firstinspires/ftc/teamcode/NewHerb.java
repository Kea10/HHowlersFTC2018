package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by student on 11/15/17.
 */

@TeleOp(name="NewHerb", group="HYSA")
public class NewHerb extends LinearOpMode {

    /* Declare OpMode members. */
    NewHardware robot           = new NewHardware();
    boolean mode = true; // true == sideways
    boolean press = false;

    @Override
    public void runOpMode() {


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.init(hardwareMap);
        robot.j.setPosition(1);
        robot.fl.setPower(0);
        robot.bl.setPower(0);
        robot.fr.setPower(0);
        robot.br.setPower(0);
        robot.cr.setPosition(.5);
        robot.cl.setPosition(.5);
        mode = true;
        press = false;
        while (opModeIsActive()) {
            if (gamepad1.a)
            {
                mode = false;
            }
            else
            {
                mode = true;
            }
            if (mode == false)
            {
                float x = gamepad1.left_stick_x;
                float y = -gamepad1.left_stick_y;
                double right = y + x;
                double left = y - x;
                left = left * (Math.sqrt(2) / 3);
                right = right * (Math.sqrt(2) / 3);
                robot.bl.setPower(left);
                robot.br.setPower(right);
                robot.fr.setPower(left);
                robot.fl.setPower(right);
            }
            if (mode == true)
            {
                float x = gamepad1.left_stick_x;
                float y = -gamepad1.left_stick_y;
                double left = y + x;
                double right = y - x;
                left = left * (Math.sqrt(2) / 2);
                right = right * (Math.sqrt(2) / 2);
                robot.bl.setPower(left);
                robot.br.setPower(right);
                robot.fr.setPower(right);
                robot.fl.setPower(left);
            }
            float arm = gamepad1.right_stick_y;
            float claw = 0;
            if (gamepad1.left_bumper) {
                claw = 1;
            }
            if (gamepad1.right_bumper) {
                claw = -1;
            }
            robot.cl.setPosition(robot.cl.getPosition()+(claw));
            robot.cr.setPosition(robot.cr.getPosition()-(claw));
            robot.tcl.setPosition(robot.tcl.getPosition()-(claw));
            robot.tcr.setPosition(robot.tcr.getPosition()+(claw));
            float grabby = 0;
            if (gamepad2.b)
            {
                grabby = 1;
            }
            if (gamepad2.y)
            {
                grabby = -1;
            }
            robot.grabber.setPosition(robot.grabber.getPosition() + grabby);
            float oofvar = 0;
            if (gamepad2.right_trigger > 0) {
                oofvar = -1;
            }
            robot.relic.setPower(oofvar);

            robot.pulley.setPower(gamepad2.right_stick_y / 3);
            robot.al.setPower(arm);
            robot.ar.setPower(arm);
            telemetry.addData("clp", robot.cl.getPosition());
            telemetry.addData("crp", robot.cr.getPosition());
            telemetry.addData("mode", mode);
            telemetry.addData("red", robot.sensor.red());
            telemetry.addData("blue", robot.sensor.blue());
            telemetry.addData("green", robot.sensor.green());
            telemetry.addData("alpha", robot.sensor.alpha());
            telemetry.addData("hue", robot.sensor.argb());
            telemetry.update();

            // Pause for 40 mS each cycle = update 25 times a second.
            sleep(40);
        }
    }

    }


