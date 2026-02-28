package org.firstinspires.ftc.teamcode;


import com.pedropathing.paths.PathChain;

public class ASFollowPath extends AS_Action {
    ASAuto auto;
    PathChain path;

    public ASFollowPath(ASAuto auto, PathChain path) {
        this.auto = auto;
        this.path = path;
    }

    public void onStart() {
        auto.follower.followPath(path);
    }

    public boolean isDone() {
        return auto.follower.atParametricEnd();
    }
}
