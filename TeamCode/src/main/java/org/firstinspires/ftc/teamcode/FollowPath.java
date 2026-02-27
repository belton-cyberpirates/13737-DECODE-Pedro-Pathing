package org.firstinspires.ftc.teamcode;


import com.pedropathing.paths.PathChain;

public class FollowPath extends Action {
    Auto auto;
    PathChain path;

    public FollowPath(Auto auto, PathChain path) {
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
