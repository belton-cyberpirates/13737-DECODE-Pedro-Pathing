package org.firstinspires.ftc.teamcode;


public class SpinIntake extends Action {
    Auto auto;
    double mult = 1;

    public SpinIntake(Auto auto) {
        this.auto = auto;
    }

    public SpinIntake(Auto auto, double mult) {
        this.auto = auto;
        this.mult = mult;
    }

    public void onStart() {
        auto.intake.SpinIntake(mult);
    }

    public boolean isDone() {
        return true;
    }
}
