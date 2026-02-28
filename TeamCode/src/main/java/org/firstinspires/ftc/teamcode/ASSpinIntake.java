package org.firstinspires.ftc.teamcode;


public class ASSpinIntake extends AS_Action {
    ASAuto auto;
    double mult = 1;

    public ASSpinIntake(ASAuto auto) {
        this.auto = auto;
    }

    public ASSpinIntake(ASAuto auto, double mult) {
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
