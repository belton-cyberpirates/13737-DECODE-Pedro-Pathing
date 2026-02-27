package org.firstinspires.ftc.teamcode;


public class StopPusher extends Action {
    Auto auto;

    public StopPusher(Auto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.intake.SetPusherVelocity(0);
    }

    public boolean isDone() {
        return true;
    }
}
