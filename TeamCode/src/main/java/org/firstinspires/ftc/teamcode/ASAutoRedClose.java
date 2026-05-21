package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Red Close | 15 | PL->L2->Cycle(x2)->L1", preselectTeleOp="Field Centric (Pedro)", group="!pedrored")
//@Disabled
public class ASAutoRedClose extends ASAuto {
    private final Pose startPose = new Pose(72+53.5, 72+41, Math.toRadians(180));
    private final Pose launchPose = new Pose(72+12, 72+15, Math.toRadians(137));
    private final Pose cyclePose = new Pose(72+58.5, 72-11, Math.toRadians(30));
    private final Pose endLaunchPose = new Pose(72+12, 72+27, Math.toRadians(134));


    private void launchSetup() {
        intake.OpenStopper();
        intake.SetPusherVelocity(0);
        intake.SetIntakeVelocity(0);
    }

    public AS_Action[] getActions() {

        AS_Action[] launchSequence = {
                // Launch!
                new ASWaitForLauncher(this),
                //new ASWait(this, 500),

                new ASSpinPusher(this),
                new ASWait(this, 100),
                new ASSpinIntake(this),

                new ASWait(this, 900),

                // Reset
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

                new ASWait(this, 1000),

                // Move to shooting position (dodge first line)
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierCurve(
                                cyclePose,
                                new Pose(72+25, 72-15),
                                launchPose
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(-10), launchPose.getHeading())
                        .addParametricCallback(.4, this::launchSetup)
                        .build()
                ),
        };

        return new AS_Action[] {
                // ======================= AUTO START ======================= //

                // Init
                new ASSetStartingPose(this, startPose),
                new ASSetColor(this, Color.RED),
                new ASSpinLauncher(this),

                // Launch!
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(
                                startPose,
                                launchPose
                        ))
                        .setLinearHeadingInterpolation(startPose.getHeading(), launchPose.getHeading())
                        .addParametricCallback(.5, this::launchSetup)
                        .build()
                ),
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
                                new Pose(72+52, 72-12)
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
                        // Get out smoothly
                        .addPath(new BezierCurve(
                                follower::getPose,
                                new Pose(72+25, 72-15),
                                launchPose
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(-10), launchPose.getHeading())
                        .addParametricCallback(.5, this::launchSetup)
                        .build()
                ),

                // Launch!
                new ASActionSequence(this, launchSequence),

                // Cycle 'n launch
                new ASActionSequence(this, CycleSequence),
                new ASActionSequence(this, launchSequence),

                // Another one
                new ASActionSequence(this, CycleSequence),
                new ASActionSequence(this, launchSequence),

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
                        .addPath(new BezierCurve(
                                follower::getPose,
                                new Pose(72+20, 72+3),
                                endLaunchPose
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(0), endLaunchPose.getHeading())
                        .addParametricCallback(.3, this::launchSetup)
                        .setBrakingStart(10)
                        .build()
                ),

                // Launch Last set
                new ASActionSequence(this, launchSequence),

                // Leave
//                new ASFollowPath(this, follower.pathBuilder()
//                        .addPath(new BezierLine(
//                                launchPose,
//                                leavePose
//                        ))
//                        .setConstantHeadingInterpolation(launchPose.getHeading())
//                        .setNoDeceleration()
//                        .setTValueConstraint(.2)
//                        .build()
//                ),

                // ======================== AUTO END ======================== //
        };
    }
}
