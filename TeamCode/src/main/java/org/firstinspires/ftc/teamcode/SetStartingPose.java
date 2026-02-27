package org.firstinspires.ftc.teamcode;


import com.pedropathing.geometry.Pose;


public class SetStartingPose extends Action {
    Auto auto;
    Pose pose;

    public SetStartingPose(Auto auto, Pose pose) {
        this.auto = auto;
        this.pose = pose;
    }

    public void onStart() {
        auto.follower.setStartingPose(pose);
    }

    public boolean isDone() {
        return auto.follower.getCurrentPath().isAtParametricEnd();
    }
}
