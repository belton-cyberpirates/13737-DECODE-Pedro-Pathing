package org.firstinspires.ftc.teamcode;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "Intake", /*preselectTeleOp="Your Drive Code Here",*/ group="z-setup")
//@Disabled
public class ASAutoSetup extends ASAuto {

    public AS_Action[] getActions() {

        return new AS_Action[] {
                // ======================= AUTO START ======================= //
                // Intake preloads
                new ASCloseStopper(this),
                new ASSpinIntake(this),
                new ASSpinPusher(this),

                new ASWait(this, 2000)
                // ======================== AUTO END ======================== //
        };
    }
}
