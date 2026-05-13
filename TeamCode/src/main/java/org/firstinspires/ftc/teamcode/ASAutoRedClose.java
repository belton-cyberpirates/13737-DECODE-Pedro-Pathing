package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Pedro Red Close", /*preselectTeleOp="Your Drive Code Here",*/ group="pedrored")
//@Disabled
public class ASAutoRedClose extends ASAuto {
    private final Pose startPose = new Pose(72+53.5, 72+41, Math.toRadians(90));
    private final Pose launchPose = new Pose(72+12, 72+15, Math.toRadians(136));
    private final Pose cyclePose = new Pose(72+58.5, 72-11, Math.toRadians(30));

    public AS_Action[] getActions() {

        AS_Action[] launchSequence = {
                // Get ready for launching
                new ASOpenStopper(this),
                new ASSpinLauncher(this),

                // Move to shooting position
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(follower::getPose, launchPose))
                        .setConstantHeadingInterpolation(launchPose.getHeading())
                        .setTimeoutConstraint(50)
                        .build()
                ),

                // Launch!
                new ASWaitForLauncher(this),
                //new ASWait(this, 500),

                new ASSpinPusher(this),
                new ASSpinIntake(this),
                //new ASWait(this, 600),
                //new ASSpinIntake(this, -.3),
                //new ASWait(this, 300),
                //new ASSpinIntake(this),
                //new ASSpinPusher(this, 2.5),

                new ASWait(this, 1200),

                // Reset
                //new ASStopLauncher(this),
                new ASStopIntake(this),
                new ASStopPusher(this)
        };

        AS_Action[] CycleSequence = {
                // Get ready to intake
                new ASCloseStopper(this),
                new ASSpinIntake(this),
                new ASSpinPusher(this),

                // Cycling time
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierCurve(
                                launchPose,
                                new Pose(72+18, 72-8),
                                cyclePose
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(135), cyclePose.getHeading())
                        .setTimeoutConstraint(50)
                        .build()
                ),

                new ASWait(this, 1200),

                // Dodge first line
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(
                                cyclePose,
                                new Pose(72+35, 72-7)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(-10), Math.toRadians(70))
                        .setNoDeceleration()
                        .build()
                ),

                // Stop intaking
                new ASStopPusher(this),
                new ASStopIntake(this),

                // Launch
                new ASActionSequence(this, launchSequence),
        };

        AS_Action[] actions = {
                // ======================= AUTO START ======================= //

                // Init
                new ASSetStartingPose(this, startPose),
                new ASSpinLauncher(this),
                new ASWait(this, 300),

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
                                new Pose(72+26, 72-12),
                                new Pose(72+50, 72-12)
                        ))
                        .setTangentHeadingInterpolation()
                        .setNoDeceleration()
                        // Hit gate
                        .addPath(new BezierCurve(
                                follower::getPose,
                                new Pose(72+38, 72-13),
                                new Pose(72+53, 72-2)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(10))
                        .setNoDeceleration()
                        //.setTValueConstraint(0.95)
                        // Get out smoothly
                        .addPath(new BezierLine(
                                follower::getPose,
                                new Pose(72+35, 72-7)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(-10), Math.toRadians(70))
                        .setNoDeceleration()
                        .build()
                ),

                // Stop intaking
                new ASStopPusher(this),
                new ASStopIntake(this),

                // Launch!
                new ASActionSequence(this, launchSequence),

                new ASActionSequence(this, CycleSequence),

                new ASActionSequence(this, CycleSequence),

                // Get ready to intake
                new ASCloseStopper(this),
                new ASSpinIntake(this),
                new ASSpinPusher(this),

                // Grab close line
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierCurve(
                                follower::getPose,
                                new Pose(72+13, 72+13),
                                new Pose(72+44, 72+13)
                        ))
                        .setTangentHeadingInterpolation()
                        .setNoDeceleration()
                        .addPath(new BezierLine(
                                follower::getPose,
                                new Pose(72+30, 72+13)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(140))
                        .setNoDeceleration()
                        .setTValueConstraint(.9)
                        .build()
                ),

                // Stop intaking
                new ASStopPusher(this),
                new ASStopIntake(this),

                // Launch Last set
                new ASActionSequence(this, launchSequence),

                // Leave
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(
                                follower::getPose,
                                new Pose(72+13+15, 72+14-15)
                        ))
                        .setConstantHeadingInterpolation(Math.toRadians(140))
                        .setNoDeceleration()
                        .setTValueConstraint(.2)
                        .build()
                ),

                // ======================== AUTO END ======================== //
        };

        return actions;
    }
}
