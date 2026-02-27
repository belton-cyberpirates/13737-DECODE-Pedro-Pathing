package org.firstinspires.ftc.teamcode;

import java.util.Arrays;


public class ActionSequence extends Action {
    Auto auto;
    Action[] actions;
    
    Action currentAction;

    public ActionSequence(Auto auto, Action[] actions) {
        this.auto = auto;
        this.actions = actions;
    }

    public void onStart() {}

    public void process() {
        if ( actions.length > 0 ) { // <----------------------------------------------------------------
            if (currentAction == null) {
                currentAction = actions[0];
                currentAction.onStart();
            }
            else {
                currentAction.process();
            }
            
            if ( actions[0].isDone() ) {
                currentAction = null;
                actions = Arrays.copyOfRange(actions, 1, actions.length);
            }
        }
    }

    public boolean isDone() {
        return actions.length == 0;
    }
}
