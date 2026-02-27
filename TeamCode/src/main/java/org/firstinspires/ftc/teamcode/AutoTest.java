package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Direction;
import org.firstinspires.ftc.teamcode.BotConfig;
import org.firstinspires.ftc.teamcode.Auto;


@Autonomous(name = "Pedro Blue Close", /*preselectTeleOp="Your Drive Code Here",*/ group="pedroblue")
//@Disabled
public class AutoTest extends Auto {
    private final Pose startPose = new Pose(72+42, 72+52, Math.toRadians(0));
    private final Pose launchPose = new Pose(82, 86, Math.toRadians(135));

    public Action[] getActions() {

        Action[] launchSequence = {
                // Get ready for launching
                new OpenStopper(this),
                new SpinLauncher(this),

                // Move to shooting position
                new FollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(follower::getPose, launchPose))
                        .setLinearHeadingInterpolation(follower.getHeading(), launchPose.getHeading())
                        .build()
                ),

                // Launch!
                new WaitForLauncher(this),
                new Wait(this, 500),

                new SpinPusher(this),
                new SpinIntake(this),
                new Wait(this, 800),
                new SpinIntake(this, -.3),
                new Wait(this, 350),
                new SpinIntake(this),
                new SpinPusher(this, 1.5),

                new Wait(this, 1500),

                // Reset
                new StopLauncher(this),
                new StopIntake(this),
                new StopPusher(this)
        };

        Action[] actions = {
                // ======================= AUTO START ======================= //

                // Init
                new SetStartingPose(this, startPose),

                // Launch!
                new ActionSequence(this, launchSequence),

                // Get ready to intake
                new CloseStopper(this),
                new SpinIntake(this),
                new SpinPusher(this),

                // Grab line and hit gate
                new FollowPath(this, follower.pathBuilder()
                        // Grab line
                        .addPath(new BezierCurve(
                                launchPose,
                                new Pose(60, 90),
                                new Pose(60, 134)
                        ))
                        .setTangentHeadingInterpolation()
                        // Hit gate
                        .addPath(new BezierCurve(
                                follower::getPose,
                                new Pose(55, 100),
                                new Pose(70, 125)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(80))
                        .setTValueConstraint(0.95)
                        // Get out smoothly
                        .addPath(new BezierLine(
                                follower::getPose,
                                new Pose(65, 100)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(135))
                        .setNoDeceleration()
                        .build()
                ),

                new StopPusher(this),
                new StopIntake(this),

                // Launch!
                new ActionSequence(this, launchSequence),

                // Cycling time
                new FollowPath(this, follower.pathBuilder()
                        .addPath(new BezierCurve(
                                launchPose,
                                new Pose(60, 90),
                                new Pose(60, 130)
                        ))
                        .setLinearHeadingInterpolation(follower.getHeading(), Math.toRadians(50))
                        .build()),

                // ======================== AUTO END ======================== //
        };

        return actions;
    }
}
