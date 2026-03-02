package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Pedro Blue Close", /*preselectTeleOp="Your Drive Code Here",*/ group="pedroblue")
//@Disabled
public class ASAutoBlueClose extends ASAuto {
    private final Pose startPose = new Pose(72-52, 72+42, Math.toRadians(90));
    private final Pose launchPose = new Pose(72-13, 72+14, Math.toRadians(-130));

    public AS_Action[] getActions() {

        AS_Action[] launchSequence = {
                // Get ready for launching
                new ASOpenStopper(this),
                new ASSpinLauncher(this),

                // Move to shooting position
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(follower::getPose, launchPose))
                        .setLinearHeadingInterpolation(follower.getHeading(), launchPose.getHeading())
                        .build()
                ),

                // Launch!
                new ASWaitForLauncher(this),
                new ASWait(this, 650),

                new ASSpinPusher(this),
                new ASSpinIntake(this),
                new ASWait(this, 800),
                new ASSpinIntake(this, -.3),
                new ASWait(this, 350),
                new ASSpinIntake(this),
                new ASSpinPusher(this, 1.5),

                new ASWait(this, 1500),

                // Reset
                new ASStopLauncher(this),
                new ASStopIntake(this),
                new ASStopPusher(this)
        };

        AS_Action[] actions = {
                // ======================= AUTO START ======================= //

                // Init
                new ASSetStartingPose(this, startPose),

                // Launch!
                new ASActionSequence(this, launchSequence),

                // Get ready to intake
                new ASCloseStopper(this),
                new ASSpinIntake(this),
                new ASSpinPusher(this),

                // Grab line and hit gate
                new ASFollowPath(this, follower.pathBuilder()
                        // Grab line
                        .addPath(new BezierCurve(
                                launchPose,
                                new Pose(72-18, 72-12),
                                new Pose(72-62, 72-12)
                        ))
                        .setTangentHeadingInterpolation()
                        // Hit gate
                        .addPath(new BezierCurve(
                                follower::getPose,
                                new Pose(72-28, 72-17),
                                new Pose(72-53, 72-2)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(170))
                        .setTValueConstraint(0.95)
                        // Get out smoothly
                        .addPath(new BezierLine(
                                follower::getPose,
                                new Pose(72-35, 72-7)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(190), Math.toRadians(-135))
                        .setNoDeceleration()
                        .build()
                ),

                // Stop intaking
                new ASStopPusher(this),
                new ASStopIntake(this),

                // Launch!
                new ASActionSequence(this, launchSequence),

                // Get ready to intake
                new ASCloseStopper(this),
                new ASSpinIntake(this),
                new ASSpinPusher(this),

                // Cycling time
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierCurve(
                                launchPose,
                                new Pose(72-18, 72-8),
                                new Pose(72-58.5, 72-11)
                        ))
                        .setLinearHeadingInterpolation(follower.getHeading(), Math.toRadians(150))
                        .build()),
                new ASWait(this, 2500),
                // Dodge first line
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(
                                follower::getPose,
                                new Pose(72-35, 72-7)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(190), Math.toRadians(-135))
                        .setNoDeceleration()
                        .build()
                ),

                // Stop intaking
                new ASStopPusher(this),
                new ASStopIntake(this),

                new ASActionSequence(this, launchSequence),

                // ======================== AUTO END ======================== //
        };

        return actions;
    }
}
