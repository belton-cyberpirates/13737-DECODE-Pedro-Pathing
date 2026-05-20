package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp(name="Field Centric (Blue)", group="!CompDriveCodes")
public class ASDriveCodeBlue extends LinearOpMode {

    // Constants
    double LEAD_ANGLE_MULT = .0003;

    // Hardware Helper Classes
    ASIntake intake;
    ASLauncher launcher;
    Follower follower;

    // Vars
    public static Pose startingPose;

    double headingOffset = 0;

    Pose aPose = new Pose();

    Pose xPose = new Pose();
    Pose yPosX = new Pose();

    Pose targetPose;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.setMsTransmissionInterval(200);

        intake = new ASIntake(this);
        launcher = new ASLauncher(this);
        follower = Constants.createFollower(hardwareMap);

        // Wait for the start button to be pressed
        waitForStart();

        while (opModeIsActive()) {
            // Allow resetting yaw in case of misalignment
            if (gamepad1.dpad_right) {
                headingOffset = follower.getHeading();
            }

            // Process classes
            launcher.process();

            // P1 variables

            // Get the speed the bot should go with the joystick pushed all the way
            double maxSpeed = calcMaxSpeed(gamepad1.right_trigger - gamepad1.left_trigger, ASBotConfig.BASE_SPEED, ASBotConfig.MAX_BOOST);

            double turnPower = -gamepad1.right_stick_x;

            // Virtually rotate the joystick by the angle of the robot
            double heading = driveMotors.heading - headingOffset;

            double rotatedX =
                    gamepad1.left_stick_x * Math.cos(heading) -
                    gamepad1.left_stick_y * Math.sin(heading);
            double rotatedY =
                    gamepad1.left_stick_x * Math.sin(heading) +
                    gamepad1.left_stick_y * Math.cos(heading);

            // strafing is slower than rolling, bump horizontal speed
            rotatedX *= ASBotConfig.STRAFE_MULT;

            // set pin locations when corresponding dpad buttons are pressed
            if (gamepad1.dpadDownWasPressed()) {
                aPose = follower.getPose();
            }
            else if (gamepad1.dpadLeftWasPressed()) {
                xPose = follower.getPose();
            }

            // track pin locations when respective buttons are being pressed
            if (gamepad1.aWasPressed()) {
                follower.followPath(follower.pathBuilder()
                    .addPath(new BezierLine(
                            follower::getPose,
                            aPose
                    ))
                    .build()
                );
            }
            if (gamepad1.xWasPressed()) {
                follower.followPath(follower.pathBuilder()
                        .addPath(new BezierLine(
                                follower::getPose,
                                xPose
                        ))
                        .build()
                );
            }
            else if (gamepad1.y) {
                if (!prevY) {
                    yPosX = driveMotors.GetX();
                    yPosY = driveMotors.GetY();
                    yPosHeading = -Math.toDegrees(driveMotors.GetHeading());
                }
                driveMotors.Move(
                        yPosX,
                        yPosY,
                        yPosHeading
                );
            }

            // aim at angle for far goal when respective button pressed
            else if (gamepad1.b) {
                double refX = driveMotors.GetX() - targetX;
                double refY = driveMotors.GetY() - targetY;

                double tanVelocity = ( refX * driveMotors.getVelY() - refY * driveMotors.getVelX() ) /
                        Math.sqrt( Math.pow(refX, 2) + Math.pow(refY, 2) );
                double angleOffset = tanVelocity * LEAD_ANGLE_MULT + Math.PI/2;

                driveMotors.DriveAndAim(
                        rotatedY,
                        rotatedX,
                        targetX,
                        targetY,
                        maxSpeed,
                        angleOffset
                );
            }

            // drive normally when keybinds aren't in use
            else {
                // Set the power of the wheels based off the new joystick coordinates
                // y+x+stick <- [-1,1]
                driveMotors.DriveWithPower(
                        ( rotatedY + rotatedX + ( turnPower )) * maxSpeed, // Back left
                        ( rotatedY - rotatedX + ( turnPower )) * maxSpeed, // Front left
                        (-rotatedY - rotatedX + ( turnPower )) * maxSpeed, // Front right
                        (-rotatedY + rotatedX + ( turnPower )) * maxSpeed  // Back right
                );
            }

            prevY = gamepad1.y;

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
            if (gamepad2.dpad_down) {
                launcher.SetVelocity(ASBotConfig.LAUNCHER_DROP_VELOCITY);
            }
            else if (rightStickYGP2 < -.5) {
                launcher.SetVelocity((gamepad2.left_trigger > 0) ? ASBotConfig.LAUNCHER_FAR_VELOCITY : ASBotConfig.LAUNCHER_VELOCITY);
            }
            else {
                launcher.SetVelocity(ASBotConfig.LAUNCHER_PASSIVE_VELOCITY);
            }

            launcher.safe = safe;

            // Telemetry
            telemetry.addData("headingOffset", headingOffset);

            telemetry.addData("x", aPosX);
            telemetry.addData("y", aPosY);
            telemetry.addData("h", aPosHeading);

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