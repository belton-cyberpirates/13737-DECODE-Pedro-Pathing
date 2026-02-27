package org.firstinspires.ftc.teamcode;


public class StopIntake extends Action {
    Auto auto;

    public StopIntake(Auto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.intake.SetIntakeVelocity(0);
    }

    public boolean isDone() {
        return true;
    }
}
