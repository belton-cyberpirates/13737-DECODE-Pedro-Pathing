package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.drawOnlyCurrent;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.AnalogInput;
import java.util.Arrays;
import com.qualcomm.robotcore.hardware.IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.firstinspires.ftc.teamcode.BotConfig;
import org.firstinspires.ftc.teamcode.Launcher;


public abstract class Auto extends LinearOpMode {
    abstract Action[] getActions();

    public Follower follower;
    public Intake intake;
    public Launcher launcher;
    
    /**
     * Initialize classes used by autos
     */
    protected void Initialize() {
        // Create robot component classes
        // Add, remove, modify depending on how your robot works
        intake = new Intake(this);
        launcher = new Launcher(this);

        // Run class initialization funcs


        // Let the user know when initialization is done
        telemetry.update();
    }

    @Override
    public void runOpMode() {
        Initialize();

        waitForStart();

        Action[] actions = getActions();
        Action currentAction = null;

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
            drawOnlyCurrent();

            // Loop telemetry; updates constantly

            telemetry.update();
        }
    }
}
