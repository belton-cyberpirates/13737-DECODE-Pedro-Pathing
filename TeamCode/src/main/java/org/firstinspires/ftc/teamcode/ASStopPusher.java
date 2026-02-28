package org.firstinspires.ftc.teamcode;


public class ASStopPusher extends AS_Action {
    ASAuto auto;

    public ASStopPusher(ASAuto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.intake.SetPusherVelocity(0);
    }

    public boolean isDone() {
        return true;
    }
}
