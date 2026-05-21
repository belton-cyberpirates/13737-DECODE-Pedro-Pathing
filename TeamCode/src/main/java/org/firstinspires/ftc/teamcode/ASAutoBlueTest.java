package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Blue Close Test", preselectTeleOp="Field Centric (Pedro)", group="test")
//@Disabled
public class ASAutoBlueTest extends ASAuto {
    private final Pose startPose = new Pose(72-54, 72+40, Math.toRadians(180));

    public AS_Action[] getActions() {
        return new AS_Action[] {
                // ======================= AUTO START ======================= //

                // Init
                new ASSetStartingPose(this, startPose),
                new ASSetColor(this, Color.BLUE),

                // ======================== AUTO END ======================== //
        };
    }
}
