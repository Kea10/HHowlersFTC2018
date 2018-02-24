package org.firstinspires.ftc.teamcode;

/**
 * Created by student on 1/17/18.
 */

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by student on 11/15/17.
 */

@TeleOp(name="Oofbot", group="HYSA")
public class OOFBOT extends LinearOpMode {

    /* Declare OpMode members. */
    OOF robot           = new OOF();
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
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (MoveMode) {
                robot.arm.setPower(0);
                float x = gamepad1.left_stick_x;
                float y = -gamepad1.left_stick_y;
                left = y + x;
                right = y - x;
                left = left * (Math.sqrt(2) / 2);
                right = right * (Math.sqrt(2) / 2);
                if (gamepad1.left_bumper) {
                    left = left * 2;
                    right = right * 2;
                }
                if (gamepad1.right_bumper) {
                    left = left / 2;
                    right = right / 2;
                }
                if (gamepad1.right_stick_y > 0)
                {
                    robot.leftclaw.setPosition(robot.leftclaw.getPosition() + (1));
                    robot.rightclaw.setPosition(robot.rightclaw.getPosition() - (1));
                }
                if (gamepad1.right_stick_y < 0)
                {
                    robot.leftclaw.setPosition(robot.leftclaw.getPosition() - (1));
                    robot.rightclaw.setPosition(robot.rightclaw.getPosition() + (1));
                }
                if (gamepad1.a)
                {
                    robot.arm.setPower(1);
                }
                if (gamepad1.b)
                {
                    robot.arm.setPower(-1);
                }
            }
            robot.leftMotor.setPower(left);
            robot.rightMotor.setPower(right);
            telemetry.addData("leftclampposition", robot.leftclaw.getPosition());
            telemetry.addData("rightclampposition", robot.rightclaw.getPosition());
            telemetry.update();
            // Pause for 40 mS each cycle = update 25 times a second.
            sleep(40);
        }
    }
}
