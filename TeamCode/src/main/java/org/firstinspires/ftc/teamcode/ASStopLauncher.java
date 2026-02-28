package org.firstinspires.ftc.teamcode;


public class ASStopLauncher extends AS_Action {
    ASAuto auto;

    public ASStopLauncher(ASAuto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.launcher.SetVelocity(0);
    }

    public boolean isDone() {
        return true;
    }
}
