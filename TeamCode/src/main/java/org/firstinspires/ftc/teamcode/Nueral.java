package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by student on 11/15/17.
 */

@TeleOp(name="NewHerb", group="HYSA")
public class Nueral extends LinearOpMode {

    /* Declare OpMode members. */
    NewHardware robot           = new NewHardware();

    @Override
    public void runOpMode() {


        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        robot.init(hardwareMap);
        
            telemetry.update();

            // Pause for 40 mS each cycle = update 25 times a second.
            sleep(40);
        }
    }

    }


