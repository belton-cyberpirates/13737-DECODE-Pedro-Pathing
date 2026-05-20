package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


@Autonomous(name = "launch test", /*preselectTeleOp="Your Drive Code Here",*/ group="zz-test")
//@Disabled
public class ASAutoLaunchTest extends ASAuto {

    public AS_Action[] getActions() {

        return new AS_Action[] {
                // ======================= AUTO START ======================= //
                // Intake preloads
                new ASSpinLauncherFast(this),
                new ASOpenStopper(this),
                new ASSpinIntake(this),
                new ASSpinPusher(this),

                new ASWait(this, 100000)
                // ======================== AUTO END ======================== //
        };
    }
}
