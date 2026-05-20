package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Blue Far | 9+1c | PL->Dump+L2->L1->Cycle", /*preselectTeleOp="Your Drive Code Here",*/ group="!pedroblue")
//@Disabled
public class ASAutoBlueFar_9DumpCycle extends ASAuto {
    private final Pose startPose = new Pose(72-14.5, 72-62, Math.toRadians(180));
    private final Pose launchPose = new Pose(72-17, 72-53, Math.toRadians(203));
    private final Pose leavePose = new Pose(72-17, 72-50);
    private final Pose cycleStartPose = new Pose(72-55, 72-62);
    private final Pose cycleEndPose = new Pose(72-60, 72-40, Math.toRadians(180-25));

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

        AS_Action[] cycleSequence = {
                // Get ready to intake
                new ASCloseStopper(this),
                new ASSpinIntake(this),
                new ASSpinPusher(this),

                // Grab from human player zone
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(
                                launchPose,
                                cycleStartPose
                        ))
                        .setConstantHeadingInterpolation(Math.toRadians(180))
                        .addPath(new BezierLine(
                                cycleStartPose,
                                cycleEndPose
                        ))
                        .setConstantHeadingInterpolation(cycleEndPose.getHeading())
                        .setNoDeceleration()
                        .build(),
                        (Double runTime) -> (Math.abs(intake.getIntakeVelocity()) < 20 && runTime >= 1500) || runTime >= 3000
                ),
                // Back to launch zone
                new ASFollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(
                                follower::getPose,
                                launchPose
                        ))
                        .setLinearHeadingInterpolation(cycleEndPose.getHeading(), launchPose.getHeading())
                        .addParametricCallback(.75, this::launchSetup)
                        .build()
                ),
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

                // Grab and dump
                new ASFollowPath(this, follower.pathBuilder()
                        // Grab line
                        .addPath(new BezierCurve(
                                launchPose,
                                new Pose(72-2, 72-2),
                                new Pose(72-54, 72-12)
                        ))
                        .setConstantHeadingInterpolation(Math.toRadians(180))
                        .setNoDeceleration()
                        // Hit gate
                        .addPath(new BezierCurve(
                                follower::getPose,
                                new Pose(72-41, 72-13),
                                new Pose(72-54, 72-2)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180-10))
                        .setTimeoutConstraint(500)
                        .setNoDeceleration()
                        // Get out smoothly
                        .addPath(new BezierCurve(
                                follower::getPose,
                                new Pose(72-2, 72-2),
                                launchPose
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(180+10), launchPose.getHeading())
                        .addParametricCallback(.5, this::launchSetup)
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
                                new Pose(72-2, 72-26),
                                new Pose(72-52, 72-36)
                        ))
                        .setConstantHeadingInterpolation(Math.toRadians(180))
                        .setNoDeceleration()
                        // Back to launch zone
                        .addPath(new BezierLine(
                                follower::getPose,
                                launchPose
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(180), launchPose.getHeading())
                        .addParametricCallback(.5, this::launchSetup)
                        .build()
                ),

                // Launch!
                new ASActionSequence(this, launchSequence),

                // Cycle human and tunnel
                new ASActionSequence(this, cycleSequence),
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
