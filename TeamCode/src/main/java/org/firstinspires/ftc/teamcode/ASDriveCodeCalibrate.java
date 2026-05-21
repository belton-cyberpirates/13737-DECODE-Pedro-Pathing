package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.math.Vector;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name="Field Centric - Calibrate Launcher", group="test")
public class ASDriveCodeCalibrate extends LinearOpMode {

    public static Follower follower;
    public static Pose targetPose;
    public static double headingOffset = 0;

    ElapsedTime deltaTimer = new ElapsedTime();

    // Constants
    double LEAD_ANGLE_MULT = .0003;

    // Hardware Helper Classes
    ASIntake intake;
    ASLauncher launcher;
    ASPIDController imuPidController = new ASPIDController(1.4, 0.00005, 0.0001);

    // Vars

    // Poses
    Pose aPose = new Pose(72, 72);
    Pose xPose = new Pose(72, 72);
    Pose yPose = new Pose(72, 72);

    @Override
    public void runOpMode() throws InterruptedException {
        int targetVel = 1000;
        telemetry.setMsTransmissionInterval(200);

        intake = new ASIntake(this);
        launcher = new ASLauncher(this);
        if (follower == null) {
            follower = Constants.createFollower(hardwareMap);
            follower.setStartingPose(new Pose());
        }

        // Wait for the start button to be pressed
        waitForStart();
        follower.startTeleOpDrive(true);

        while (opModeIsActive()) {
            // Telemetry
            telemetry.addData("pos x", follower.getPose().getX());
            telemetry.addData("pos y", follower.getPose().getY());
            telemetry.addData("heading", follower.getHeading());
            telemetry.addData("velocity", follower.getVelocity().getMagnitude());
            telemetry.addData("angular velocity", follower.getAngularVelocity());
            telemetry.addData("velocity x", follower.getVelocity().getXComponent());
            telemetry.addData("velocity y", follower.getVelocity().getYComponent());

            // Allow resetting yaw in case of misalignment
            if (gamepad1.dpad_right) {
                headingOffset = follower.getHeading();
            }

            // Process classes
            launcher.process();
            follower.update();

            double deltaTime = deltaTimer.seconds();
            deltaTimer.reset();

            // Get the speed the bot should go with the joystick pushed all the way
            double maxSpeed = calcMaxSpeed(gamepad1.right_trigger - gamepad1.left_trigger, ASBotConfig.BASE_SPEED, ASBotConfig.MAX_BOOST);


            // set pin locations when corresponding dpad buttons are pressed
            if (gamepad1.dpadDownWasPressed()) {
                targetVel -= 10;
            }
            else if (gamepad1.dpadLeftWasPressed()) {
                xPose = follower.getPose();
            }
            else if (gamepad1.dpadUpWasPressed()) {
                targetVel += 10;
            }

            // track pin locations when respective buttons are being pressed
            if (gamepad1.aWasPressed()) {
                follower.followPath(follower.pathBuilder()
                        .addPath(new BezierLine(
                                follower::getPose,
                                aPose
                        ))
                        .setConstantHeadingInterpolation(follower.getHeading())
                        .build()
                );
            }
            if (gamepad1.xWasPressed()) {
                follower.followPath(follower.pathBuilder()
                        .addPath(new BezierLine(
                                follower::getPose,
                                xPose
                        ))
                        .setConstantHeadingInterpolation(follower.getHeading())
                        .build()
                );
            }
            if (gamepad1.yWasPressed()) {
                follower.followPath(follower.pathBuilder()
                        .addPath(new BezierLine(
                                follower::getPose,
                                yPose
                        ))
                        .setConstantHeadingInterpolation(follower.getHeading())
                        .build()
                );
            }

            // aim at angle for far goal when respective button pressed
            else if (gamepad1.b) {
                Pose refPose = new Pose(follower.getPose().getX() - targetPose.getX(), follower.getPose().getY() - targetPose.getY());
                Vector velocity = follower.getVelocity();

                double tanVelocity = ( refPose.getX() * velocity.getYComponent() - refPose.getY() * velocity.getXComponent() ) /
                        Math.sqrt( Math.pow(refPose.getX(), 2) + Math.pow(refPose.getY(), 2) );
                double launchVelocity = launcher.launcherTargetVelocity * ASBotConfig.LAUNCHER_SPEED_CONVERSION_RATIO;
                double angleOffset =
                        -Math.PI/2 + // account for offset launcher
                        Math.atan(tanVelocity / launchVelocity) + // ideal angle (math)
                        0; // account for PID drift

                double targetAngle = refPose.getAsVector().getTheta() + angleOffset;

                follower.setTeleOpDrive(
                        -gamepad1.left_stick_y * maxSpeed,
                        -gamepad1.left_stick_x * maxSpeed,
                        imuPidController.PIDControlRadians(targetAngle, follower.getHeading(), deltaTime),
                        false,
                        headingOffset
                );
                telemetry.addData("tangential velocity", tanVelocity);
                telemetry.addData("target angle", targetAngle);
            }
            else {
                follower.setTeleOpDrive(
                        -gamepad1.left_stick_y * maxSpeed,
                        -gamepad1.left_stick_x * maxSpeed,
                        -gamepad1.right_stick_x * maxSpeed,
                        false,
                        headingOffset
                );
            }

            // P2 variables
            double leftStickYGP2 = gamepad2.left_stick_y;
            double rightStickYGP2 = gamepad2.right_stick_y;

            // Intake
            intake.SetPower(leftStickYGP2 < 0 ? leftStickYGP2 : leftStickYGP2 / 3);

            // Pusher
            boolean safe = gamepad2.left_trigger > 0 || gamepad2.left_bumper;
            if (launcher.isAtVelocity() || launcher.launcherTargetVelocity < 1100 || gamepad2.right_trigger == 0 || !safe) {
                intake.SetPusherPower(gamepad2.dpad_down ? .6 : -leftStickYGP2);
            }
            else {
                intake.SetPusherPower(0);
            }

            // Stopper
            intake.SetStopper(gamepad2.dpad_down || gamepad2.right_trigger > 0.5);

            // Flywheel
            launcher.SetVelocity(targetVel);

            launcher.safe = safe;

            // Telemetry
            telemetry.addData("headingOffset", headingOffset);

            telemetry.update();
        }
    }


    /**
     * if boost trigger unpressed, return base_speed,
     * else return base_speed + boost amount
     */
    double calcMaxSpeed(double triggerVal, double baseSpeed, double boostMult) {
        double boostRatio = triggerVal * boostMult;
        double boostSpeed = boostRatio * baseSpeed;

        return baseSpeed + boostSpeed;
    }
}