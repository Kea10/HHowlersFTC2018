package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@Autonomous(name="HerbAuto", group="Pushbot")
public class HerbAuto extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareBot        robot   = new HardwareBot();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;
    static final double     DIAMETER_BIG    = 4.0 ;
    static final double     DIAMETER_SMALL    = 1.5;
    static final double     COUNTS_PER_WHEEL_REV    = 370;
    static final double     WHEEL_DIAMETER_INCHES   = 4.0;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_WHEEL_REV) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.1;
    static final double     WHEEL_HORIZONTAL             = 12.5;
    public double team = 2; // 1 is blue, 2 is red

    public static final String TAG = "Vuforia VuMark Sample";

    OpenGLMatrix lastLocation = null;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    VuforiaLocalizer vuforia;

    @Override
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AaVb4Jf/////AAAAGWllO0NU2knplmA51xoqUyJFF2S8c1YQ2q+vqNuYRDoDcs2K67j3q7doW6OmQoMuxe2e5t3xi+bIiFH/YrcdkCiFTTT77ilcPLAOLihM0MhjfhJcII2YwayY0akGSBNE+82RhGPcfl9SlQgX37TUSUnyPUCSDWq9rxbzNiJpMTm6ju+mEtsd16N/jZqgsO1AtcR0FgWwRxWg6++NoEMg55K79Dudj7D1UC+0piGJEUI8V+jlvUk3+ijv71ZYDtZm08Q38Z+m87R1vAxDgwaFqrGk5WzW7LHxPGlg9bNBF75y6kR+yj7N7WZXbRMNTiph3RQnO2K17GWZb4cEVqC0T1ZqkJgRPAKr5S4eX46X0avv";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robot.claw.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.claw.setPower(.2);
        robot.claw.setTargetPosition(0);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                          robot.leftMotor.getCurrentPosition(),
                          robot.rightMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.clampleft.setPosition(0);
        robot.clampright.setPosition(1);
        relicTrackables.activate();

        robot.claw.setTargetPosition(400);
        ElapsedTime etime = new ElapsedTime();
        etime.reset();
        RelicRecoveryVuMark mark = null;
        boolean loop = true;
        while (loop && etime.seconds() <= 5 && opModeIsActive()) {
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                /* Found an instance of the template. In the actual game, you will probably
                 * loop until this condition occurs,  then move on to act accordingly depending
                 * on which VuMark was visible. */
                telemetry.addData("VuMark", "%s visible", vuMark);
                telemetry.update();

                /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                 * it is perhaps unlikely that you will actually need to act on this pose information, but
                 * we illustrate it nevertheless, for completeness. */
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
                mark = vuMark;
                loop = false;

                /* We further illustrate how to decompose the pose into useful rotational and
                 * translational components */
                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }
            }
            else {
                telemetry.addData("VuMark", "not visible");
                telemetry.update();
            }

        }
        DriveForward(-23);
        team = 1;
        if (team == 1) // blue team
        {
            BulkMove("Turn -63");
            robot.claw.setTargetPosition(100);
            if (mark == RelicRecoveryVuMark.LEFT) {
                BulkMove("Move 4");
            }
            if (mark == RelicRecoveryVuMark.CENTER || mark == RelicRecoveryVuMark.UNKNOWN || mark == null) {
                BulkMove("Move 11");
            }
            if (mark == RelicRecoveryVuMark.RIGHT) {
                BulkMove("Move 21");
            }
            BulkMove("Turn -65");
            BulkMove("Move 12");
        }
        else if (team == 2) { // red team
            BulkMove("Turn 64");
            robot.claw.setTargetPosition(100);
            if (mark == RelicRecoveryVuMark.RIGHT) {
                BulkMove("Move 4");
            }
            if (mark == RelicRecoveryVuMark.CENTER || mark == RelicRecoveryVuMark.UNKNOWN || mark == null) {
                BulkMove("Move 13.5");
            }
            if (mark == RelicRecoveryVuMark.LEFT) {
                BulkMove("Move 21");
            }
            BulkMove("Turn 65");
            BulkMove("Move 12");
        }
        robot.clampleft.setPosition(1);
        robot.clampright.setPosition(0);
        BulkMove("Move -12");
        telemetry.addData("Path", "Complete");
        telemetry.update();

    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */
    public void encoderDrive(double leftInches, double rightInches) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftMotor.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightMotor.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftMotor.setPower(Math.abs(DRIVE_SPEED));
            robot.rightMotor.setPower(Math.abs(DRIVE_SPEED));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (robot.leftMotor.isBusy()  && robot.rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
                        robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }
    public void DriveForward(double Inches)
    {
        encoderDrive(Inches, Inches);
    }
    public void TurnClockwise(double Degrees)
    {
        double w = WHEEL_HORIZONTAL;
        double r = (Degrees * 3.1415) / 180;
        double c = Math.sqrt(2) / 2;
        double d = w * r * c;
        encoderDrive(-d,d);
    }
    public void BulkMove(String list)
    {
        String[] commands = list.split(", ");
        for (String command : commands)
        {
            String[] words = command.split(" ");
            if (words[0].equals("Move"))
            {
                DriveForward(Double.parseDouble(words[1]));
            }
            if (words[0].equals("Turn"))
            {
                TurnClockwise(Double.parseDouble(words[1]));
            }
        }
    }
}