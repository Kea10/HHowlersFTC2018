package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by student on 11/15/17.
 */

@TeleOp(name="HerbBot", group="HYSA")
public class HerbBot extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareBot robot           = new HardwareBot();
    boolean MoveMode = true; // false is tank drive

    @Override
    public void runOpMode() {
        double left = 0;
        double right = 0;
///top = -890 bottom = -15
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();
        robot.claw.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.claw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.claw.setPower(.2);
        robot.claw.setTargetPosition(0);
        robot.clampright.setPosition(1);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        int currentframe = robot.claw.getCurrentPosition();
        int previousframe = currentframe;
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            previousframe = currentframe;
            currentframe = robot.claw.getCurrentPosition();
            int movement = previousframe - currentframe;
            if (MoveMode) {
                float x = gamepad1.left_stick_x;
                float y = -gamepad1.left_stick_y;
                left = y + x;
                right = y - x;
                left = left * (Math.sqrt(2) / 6);
                right = right * (Math.sqrt(2) / 6);
                if (gamepad1.left_bumper) {
                    left = left * 2;
                    right = right * 2;
                }
                if (gamepad1.right_bumper) {
                    left = left / 2;
                    right = right / 2;
                }
            }
            if (gamepad1.x)
            {
                robot.claw.setTargetPosition(0);
            }
            if (gamepad1.y)
            {
                robot.claw.setTargetPosition(440);
            }
            if (gamepad1.a)
            {
                robot.claw.setTargetPosition(200);
            }
            if (gamepad1.b)
            {
                robot.claw.setTargetPosition(940);
            }
            int finetune = 10 * Math.round(gamepad1.right_trigger - gamepad1.left_trigger);
            robot.claw.setTargetPosition(robot.claw.getTargetPosition() + finetune);

            robot.leftMotor.setPower(right);
            robot.rightMotor.setPower(left);

            robot.clampleft.setPosition(robot.clampleft.getPosition()+(gamepad1.right_stick_y/32));
            robot.clampright.setPosition(robot.clampright.getPosition()-(gamepad1.right_stick_y)/32);


            telemetry.clearAll();
            telemetry.addData("clawposition", robot.claw.getCurrentPosition());
            telemetry.addData("clawtarget", robot.claw.getTargetPosition());
            telemetry.addData("clawpower", robot.claw.getPower());
            telemetry.addData("leftclampposition", robot.clampleft.getPosition());
            telemetry.addData("rightclampposition", robot.clampright.getPosition());
            telemetry.addData("rightposition", robot.leftMotor.getCurrentPosition());
            telemetry.addData("leftposition", robot.rightMotor.getCurrentPosition());
            telemetry.update();
            // Pause for 40 mS each cycle = update 25 times a second.
            sleep(40);
        }
    }
}
