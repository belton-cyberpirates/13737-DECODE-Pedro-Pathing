package org.firstinspires.ftc.teamcode;


import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.function.Function;
import java.util.function.Supplier;

public class ASFollowPath extends AS_Action {
    ASAuto auto;
    PathChain path;
    Function<Double, Boolean> endCondition;

    ElapsedTime runTimer = new ElapsedTime();

    public ASFollowPath(ASAuto auto, PathChain path) {
        this.auto = auto;
        this.path = path;
    }

    public ASFollowPath(ASAuto auto, PathChain path, Function<Double, Boolean> endCondition) {
        this.auto = auto;
        this.path = path;
        this.endCondition = endCondition;
    }

    public void onStart() {
        runTimer.reset();
        auto.follower.followPath(path);
    }

    public boolean isDone() {
        return !auto.follower.isBusy() || (endCondition != null ? endCondition.apply(runTimer.milliseconds()) : false);
    }
}
