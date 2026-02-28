package org.firstinspires.ftc.teamcode;


import com.pedropathing.geometry.Pose;


public class ASSetStartingPose extends AS_Action {
    ASAuto auto;
    Pose pose;

    public ASSetStartingPose(ASAuto auto, Pose pose) {
        this.auto = auto;
        this.pose = pose;
    }

    public void onStart() {
        auto.follower.setStartingPose(pose);
    }

    public boolean isDone() {
        return true;
    }
}
