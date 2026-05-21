package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Arrays;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;


public abstract class ASAuto extends LinearOpMode {
    public enum Color {
        BLUE,
        RED,
    }
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
        ASDriveCodeBlue.follower = follower;

        // Run class initialization funcs

        // Let the user know when initialization is done
        telemetry.addLine("Fully initialized! Press start to begin auto.");
        telemetry.update();
        telemetry.setMsTransmissionInterval(50);
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
            telemetry.addData("Robot stuck", follower.isRobotStuck());

            telemetry.update();
        }
    }

    public void SetColor(Color color) {
        switch (color) {
            case BLUE:
                ASDriveCodeBlue.targetPose = ASBotConfig.BLUE_TARGET_POSE;
                ASDriveCodeBlue.headingOffset = 0;
                break;
            case RED:
                ASDriveCodeBlue.targetPose = ASBotConfig.RED_TARGET_POSE;
                ASDriveCodeBlue.headingOffset = Math.PI;
                break;

        }
    }
}
