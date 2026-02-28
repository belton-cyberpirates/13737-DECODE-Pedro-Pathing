package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Pedro Blue Close", /*preselectTeleOp="Your Drive Code Here",*/ group="pedroblue")
//@Disabled
public class ASAutoTest extends ASAuto {
    private final Pose startPose = new Pose(72+42, 72+52, Math.toRadians(0));
    private final Pose launchPose = new Pose(82, 86, Math.toRadians(135));

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
                new ASWait(this, 500),

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

                new ASStopPusher(this),
                new ASStopIntake(this),

                // Launch!
                new ASActionSequence(this, launchSequence),

                // Cycling time
                new ASFollowPath(this, follower.pathBuilder()
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
