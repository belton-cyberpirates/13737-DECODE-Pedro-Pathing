package org.firstinspires.ftc.teamcode;


import com.pedropathing.geometry.Pose;


public class ASSetColor extends AS_Action {
    ASAuto auto;
    ASAuto.Color color;

    public ASSetColor(ASAuto auto, ASAuto.Color color) {
        this.auto = auto;
        this.color = color;
    }

    public void onStart() {
        auto.SetColor(color);
    }

    public boolean isDone() {
        return true;
    }
}
