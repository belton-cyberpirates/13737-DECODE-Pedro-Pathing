package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Red Far | 9 | PL->L1->L2", /*preselectTeleOp="Your Drive Code Here",*/ group="!pedrored")
//@Disabled
public class ASAutoRedFar_9Basic extends ASAuto {
    private final Pose startPose = new Pose(72+14.5, 72-62, Math.toRadians(180));
    private final Pose launchPose = new Pose(72+16, 72-53, Math.toRadians(162));
    private final Pose leavePose = new Pose(72+16, 72-50);

    private void launchSetup() {
        intake.OpenStopper();
        intake.SetPusherVelocity(0);
        intake.SetIntakeVelocity(0);
    }

    public AS_Action[] getActions() {

        AS_Action[] launchSequence = {
                // Launch!
                new ASWaitForLauncher(this),

                new ASSpinPusherSlow(this),
                new ASWait(this, 100),
                new ASSpinIntake(this),

                new ASWait(this, 2200),

                // Reset
                new ASStopIntake(this),
                new ASStopPusher(this)
        };

        return new AS_Action[] {
                // ======================= AUTO START ======================= //

                // Init
                new ASSetStartingPose(this, startPose),
                new ASSpinLauncherFast(this),

                // Launch!
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(
                                startPose,
                                launchPose
                        ))
                        .setLinearHeadingInterpolation(startPose.getHeading(), launchPose.getHeading())
                        .addParametricCallback(.1, this::launchSetup)
                        .build()
                ),
                new ASActionSequence(this, launchSequence),

                // Get ready to intake
                new ASCloseStopper(this),
                new ASSpinIntake(this),
                new ASSpinPusher(this),

                // Grab line and launch
                new ASFollowPath(this, follower.pathBuilder()
                        // Grab line
                        .addPath(new BezierCurve(
                                launchPose,
                                new Pose(72+2, 72-26),
                                new Pose(72+52, 72-36)
                        ))
                        .setConstantHeadingInterpolation(Math.toRadians(0))
                        .setNoDeceleration()
                        // Back to launch zone
                        .addPath(new BezierLine(
                                follower::getPose,
                                launchPose
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(0), launchPose.getHeading())
                        .addParametricCallback(.5, this::launchSetup)
                        .build()
                ),

                // Launch!
                new ASActionSequence(this, launchSequence),

                // Get ready to intake
                new ASCloseStopper(this),
                new ASSpinIntake(this),
                new ASSpinPusher(this),

                // Grab line and launch
                new ASFollowPath(this, follower.pathBuilder()
                        // Grab line
                        .addPath(new BezierCurve(
                                launchPose,
                                new Pose(72+2, 72-2),
                                new Pose(72+52, 72-12)
                        ))
                        .setConstantHeadingInterpolation(Math.toRadians(0))
                        .setNoDeceleration()
                        // Back to launch zone
                        .addPath(new BezierLine(
                                follower::getPose,
                                launchPose
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(0), launchPose.getHeading())
                        .addParametricCallback(.5, this::launchSetup)
                        .build()
                ),

                // Launch!
                new ASActionSequence(this, launchSequence),

                // Leave
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(
                                launchPose,
                                leavePose
                        ))
                        .setConstantHeadingInterpolation(launchPose.getHeading())
                        .setNoDeceleration()
                        .build()
                ),

                // ======================== AUTO END ======================== //
        };
    }
}
