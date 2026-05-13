package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.ASBotConfig;
import java.util.List;

public class ASLauncher {

    private LinearOpMode auto;

    public DcMotorEx launcherLeft;
    public DcMotorEx launcherRight;
    Servo light;

    ASPIDFController launcherPIDFController = new ASPIDFController(0.0004, .0175, 0/*.00001*/, 0.0001);

    int launcherTargetVelocity = 0;
    boolean safe = false;

    ElapsedTime deltaTimer = new ElapsedTime();


    public ASLauncher(LinearOpMode auto) {
        this.auto = auto;

        this.launcherLeft = auto.hardwareMap.get(DcMotorEx.class, ASBotConfig.LAUNCHER_LEFT_NAME);
        this.launcherRight = auto.hardwareMap.get(DcMotorEx.class, ASBotConfig.LAUNCHER_RIGHT_NAME);

        this.launcherLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.launcherRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        this.light = auto.hardwareMap.get(Servo.class, ASBotConfig.LIGHT_NAME);
    }


    public double process() {
        double deltaTime = deltaTimer.seconds();

        double launcherVelocity = launcherLeft.getVelocity();

        if (this.launcherTargetVelocity != 0) {
            double launcherPower = launcherPIDFController.PIDFControl(
                    this.launcherTargetVelocity,
                    launcherVelocity,
                    deltaTime
            );

            launcherLeft.setPower(launcherPower);
            launcherRight.setPower(-launcherPower);
        }
        else {
            launcherLeft.setPower(0);
            launcherRight.setPower(0);
        }

        deltaTimer.reset();

        // Light
        if (this.safe) {
            light.setPosition(ASBotConfig.LIGHT_BLUE);
        }
        else if (this.isAtVelocity()) {
            light.setPosition(ASBotConfig.LIGHT_GREEN);
        }
        else {
            light.setPosition(ASBotConfig.LIGHT_RED);
        }

        // Telemetry
        auto.telemetry.addData("Launcher Velocity", getVelocity());
        auto.telemetry.addData("Launcher Target Velocity", launcherTargetVelocity);
        auto.telemetry.addData("Launcher Is At Velocity", isAtVelocity());
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
        return Math.abs(launcherPIDFController.lastError) <= ASBotConfig.LAUNCHER_VELOCITY_MARGIN;
    }


    public int getVelocity() {
        return (int)launcherLeft.getVelocity();
    }
}
