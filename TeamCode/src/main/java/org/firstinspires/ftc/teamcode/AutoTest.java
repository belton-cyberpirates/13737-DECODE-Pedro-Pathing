package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Direction;
import org.firstinspires.ftc.teamcode.BotConfig;
import org.firstinspires.ftc.teamcode.Auto;


@Autonomous(name = "Pedro Blue Close"/*, preselectTeleOp="Your Drive Code Here"*/, group="blue")
//@Disabled
public class AutoTest extends Auto {
    private final Pose startPose = new Pose(72+52, 72+42, Math.toRadians(0));
    private final Pose launchPose = new Pose(96, 96, Math.toRadians(-135));

    public Action[] getActions() {

        Action[] launchSequence = {
                // Get ready for launching
                new OpenStopper(this),
                new SpinLauncherFast(this),

                // Move to shooting position
                new FollowPath(this, follower.pathBuilder()
                        .addPath(new BezierLine(startPose, launchPose))
                        .setLinearHeadingInterpolation(startPose.getHeading(), launchPose.getHeading())
                        .build()
                ),

                // Launch!
                new WaitForLauncher(this),
                new Wait(this, 500),

                new SpinPusher(this),
                new SpinIntake(this),
                new Wait(this, 750),
                new SpinIntake(this, -.3),
                new Wait(this, 350),
                new SpinIntake(this),
                new SpinPusher(this, 3),

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

                // Move to first line
//                new Move(this, 680, 300, -90),
//
//                // Intake artifacts
//                new CloseStopper(this),
//                new SpinIntake(this),
//                new SpinPusher(this),
//                new Move(this, 680, 1100, -90),
//                new Wait(this, 750),
//                new StopPusher(this),
//                new StopIntake(this),
//
//                new ActionSequence(this, launchSequence),
//
//                // If we have more time
//
//                // Move to second line
//                new Move(this, 1220, 300, -90),
//
//                // Intake artifacts
//                new CloseStopper(this),
//                new SpinIntake(this),
//                new SpinPusher(this),
//                new Move(this, 1190, 1100, -90),
//                new Wait(this, 750),
//                new StopPusher(this),
//                new StopIntake(this),
//
//                new ActionSequence(this, launchSequence),
//
//                // I don't think we have any chance of shooting these artifacts, if we even have time to grab them
//
//                // End sequence
//
//                // Move out of triangle
//                new Move(this, 50, 500, -90),

                // ======================== AUTO END ======================== //
        };

        return actions;
    }
}
