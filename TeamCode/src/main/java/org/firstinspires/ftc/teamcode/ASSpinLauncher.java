package org.firstinspires.ftc.teamcode;


public class ASSpinLauncher extends AS_Action {
    ASAuto auto;

    public ASSpinLauncher(ASAuto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.launcher.Spin();
    }

    public boolean isDone() {
        return true;
    }
}
