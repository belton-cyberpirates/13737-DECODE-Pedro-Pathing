package org.firstinspires.ftc.teamcode;


public class SpinLauncherFast extends Action {
    Auto auto;

    public SpinLauncherFast(Auto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.launcher.SetVelocity(BotConfig.LAUNCHER_FAR_VELOCITY);
    }

    public boolean isDone() {
        return true;
    }
}
