package org.firstinspires.ftc.teamcode;


public class ASSpinLauncherFast extends AS_Action {
    ASAuto auto;

    public ASSpinLauncherFast(ASAuto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.launcher.SetVelocity(ASBotConfig.LAUNCHER_FAR_VELOCITY);
    }

    public boolean isDone() {
        return true;
    }
}
