package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

public class ASLauncher {

    private LinearOpMode auto;

    public DcMotorEx launcher;
    
    ASPIDFController launcherPIDFController = new ASPIDFController(0.0004, 0.025, 0/*.00001*/, 0/*.0001*/);

    int launcherTargetVelocity = 0;

    ElapsedTime deltaTimer = new ElapsedTime();


    public ASLauncher(LinearOpMode auto) {
        this.auto = auto;
        
        this.launcher = auto.hardwareMap.get(DcMotorEx.class, ASBotConfig.LAUNCHER_NAME);
        this.launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }
    
    
    public double process() {
        double deltaTime = deltaTimer.seconds();
        
        double launcherVelocity = launcher.getVelocity();

        if (this.launcherTargetVelocity != 0) {
            launcher.setPower(
                launcherPIDFController.PIDFControl(
                    this.launcherTargetVelocity,
                    launcherVelocity,
                    deltaTime
                )
            );
        }
        else {
            launcher.setPower(0);
        }
        
        deltaTimer.reset();
        
        // Telemetry
        auto.telemetry.addData("Launcher Velocity", getVelocity());
        auto.telemetry.addData("Launcher Target Velocity", launcherTargetVelocity);
        auto.telemetry.addData("Launcher Power", launcherPIDFController.lastOutput);
        
        return deltaTime;
    }
  
  
    public void SetVelocity(int velocity) {
        this.launcherTargetVelocity = velocity;
    }


    public void Spin() {
        this.SetVelocity(ASBotConfig.AUTO_LAUNCHER_VELOCITY);
    }


    public boolean isAtVelocity() {
        return launcherPIDFController.lastError <= 50;
    }
    

    public int getVelocity() {
        return (int)launcher.getVelocity();
    }
}