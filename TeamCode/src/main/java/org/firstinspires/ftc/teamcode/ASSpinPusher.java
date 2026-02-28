package org.firstinspires.ftc.teamcode;


public class ASSpinPusher extends AS_Action {
    ASAuto auto;
    double mult = 1;

    public ASSpinPusher(ASAuto auto) {
        this.auto = auto;
    }

    public ASSpinPusher(ASAuto auto, double mult) {
        this.auto = auto;
        this.mult = mult;
    }

    public void onStart() {
        auto.intake.SpinPusher(mult);
    }

    public boolean isDone() {
        return true;
    }
}
