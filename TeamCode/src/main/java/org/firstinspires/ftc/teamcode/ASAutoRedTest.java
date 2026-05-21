package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Red Close Test", preselectTeleOp="Field Centric (Pedro)", group="test")
//@Disabled
public class ASAutoRedTest extends ASAuto {
    private final Pose startPose = new Pose(72+53.5, 72+41, Math.toRadians(180));

    public AS_Action[] getActions() {
        return new AS_Action[] {
                // ======================= AUTO START ======================= //

                // Init
                new ASSetStartingPose(this, startPose),
                new ASSetColor(this, Color.RED),

                // ======================== AUTO END ======================== //
        };
    }
}
