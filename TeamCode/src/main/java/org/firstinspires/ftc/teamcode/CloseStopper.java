package org.firstinspires.ftc.teamcode;


public class CloseStopper extends Action {
    Auto auto;

    public CloseStopper(Auto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.intake.CloseStopper();
    }

    public boolean isDone() {
        return true;
    }
}
