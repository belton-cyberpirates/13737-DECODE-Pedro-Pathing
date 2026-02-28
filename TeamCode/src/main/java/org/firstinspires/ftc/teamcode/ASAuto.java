package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Arrays;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


public abstract class ASAuto extends LinearOpMode {
    abstract AS_Action[] getActions();

    public Follower follower;
    public ASIntake intake;
    public ASLauncher launcher;

    
    /**
     * Initialize classes used by autos
     */
    protected void Initialize() {
        // Create robot component classes
        intake = new ASIntake(this);
        launcher = new ASLauncher(this);

        // Assign needed variables
        follower = Constants.createFollower(hardwareMap);

        // Run class initialization funcs

        // Let the user know when initialization is done
        telemetry.addLine("Fully initialized! Press start to begin auto.");
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        Initialize();

        waitForStart();

        AS_Action[] actions = getActions();
        AS_Action currentAction = null;

        while (opModeIsActive() && ( actions.length > 0 )) { // <----------------------------------------------------------------
            if (currentAction == null) {
                currentAction = actions[0];
                currentAction.onStart();
            }
            else {
                currentAction.process();
            }

            if ( actions[0].isDone() ) {
                currentAction = null;
                actions = Arrays.copyOfRange(actions, 1, actions.length);
            }

            // Process classes
            // Add, remove, modify depending on how your robot works
            launcher.process();
            follower.update();

            // Loop telemetry; updates constantly

            telemetry.update();
        }
    }
}
