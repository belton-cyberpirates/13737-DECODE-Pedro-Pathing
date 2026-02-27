package org.firstinspires.ftc.teamcode;


public class SpinPusher extends Action {
    Auto auto;
    double mult = 1;

    public SpinPusher(Auto auto) {
        this.auto = auto;
    }

    public SpinPusher(Auto auto, double mult) {
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
