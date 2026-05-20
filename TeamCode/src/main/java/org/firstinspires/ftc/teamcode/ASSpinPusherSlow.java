package org.firstinspires.ftc.teamcode;


public class ASSpinPusherSlow extends AS_Action {
    ASAuto auto;
    double mult = 1;

    public ASSpinPusherSlow(ASAuto auto) {
        this.auto = auto;
    }

    public void onStart() {
        auto.intake.SetPusherVelocity(ASBotConfig.AUTO_PUSHER_FAR_VELOCITY);
    }

    public boolean isDone() {
        return true;
    }
}
