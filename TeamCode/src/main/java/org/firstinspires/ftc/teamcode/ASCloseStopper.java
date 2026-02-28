package org.firstinspires.ftc.teamcode;


public class ASCloseStopper extends AS_Action {
    ASAuto auto;

    public ASCloseStopper(ASAuto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.intake.CloseStopper();
    }

    public boolean isDone() {
        return true;
    }
}
