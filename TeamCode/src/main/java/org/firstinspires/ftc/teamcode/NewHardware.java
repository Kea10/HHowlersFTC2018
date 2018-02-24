package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a K9 robot.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Servo channel:  Servo to raise/lower arm: "arm"
 * Servo channel:  Servo to open/close claw: "claw"
 *
 * Note: the configuration of the servos is such that:
 *   As the arm servo approaches 0, the arm position moves up (away from the floor).
 *   As the claw servo approaches 0, the claw opens up (drops the game element).
 */
public class NewHardware
{
    /* Public OpMode members. */
    public DcMotor  fl   = null;
    public DcMotor  bl   = null;
    public DcMotor  fr   = null;
    public DcMotor  br   = null;
    public DcMotor al = null;
    public DcMotor ar = null;
    public Servo cl = null;
    public Servo cr = null;
    public Servo tcl = null;
    public Servo tcr = null;
    public Servo j = null;
    public ColorSensor sensor = null;
    public DcMotor relic = null;
    public Servo grabber = null;
    public DcMotor pulley = null;



    /* Local OpMode members. */
    HardwareMap hwMap  = null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public NewHardware() {
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // save reference to HW Map
        hwMap = ahwMap;

        // Define and Initialize Motors
        fl  = hwMap.get(DcMotor.class, "fl");
        bl  = hwMap.get(DcMotor.class, "bl");
        fr  = hwMap.get(DcMotor.class, "fr");
        br  = hwMap.get(DcMotor.class, "br");
        al = hwMap.get(DcMotor.class, "al");
        ar = hwMap.get(DcMotor.class, "ar");
        cl = hwMap.get(Servo.class, "cl");
        cr = hwMap.get(Servo.class, "cr");
        tcl = hwMap.get(Servo.class, "tcl");
        tcr = hwMap.get(Servo.class, "tcr");
        j = hwMap.get(Servo.class, "jewel");
        relic = hwMap.get(DcMotor.class, "relic");
        grabber = hwMap.get(Servo.class, "grab");
        sensor = hwMap.get(ColorSensor.class, "sensor");
        pulley = hwMap.get(DcMotor.class, "pulley");
        fl.setPower(0);
        bl.setPower(0);
        fr.setPower(0);
        br.setPower(0);
        pulley.setPower(0);
        ar.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setDirection(DcMotorSimple.Direction.REVERSE);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        bl.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fr.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        br.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        relic.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pulley.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        cl.scaleRange(0,.3);
        cr.scaleRange(.5,.8);
        tcr.scaleRange(0,.3);
        tcl.scaleRange(.5,.8);
        j.scaleRange(.05,.7);

    }
}