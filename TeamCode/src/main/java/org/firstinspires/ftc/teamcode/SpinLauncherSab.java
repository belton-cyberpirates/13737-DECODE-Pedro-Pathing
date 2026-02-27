package org.firstinspires.ftc.teamcode;


public class SpinLauncherSab extends Action {
    Auto auto;

    public SpinLauncherSab(Auto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.launcher.SetVelocity(BotConfig.LAUNCHER_SAB_VELOCITY);
    }

    public boolean isDone() {
        return true;
    }
}
