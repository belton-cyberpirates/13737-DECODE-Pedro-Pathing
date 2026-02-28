package org.firstinspires.ftc.teamcode;


public class ASOpenStopper extends AS_Action {
    ASAuto auto;

    public ASOpenStopper(ASAuto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.intake.OpenStopper();
    }

    public boolean isDone() {
        return true;
    }
}
