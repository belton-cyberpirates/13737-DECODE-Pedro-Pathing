package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.ASBotConfig;
import java.util.List;

public class ASLauncher {

    private LinearOpMode auto;

    public DcMotorEx launcherLeft;
    public DcMotorEx launcherRight;
    Servo light;

    ASPIDFController launcherPIDFController = new ASPIDFController(0.0004, 0.01, 0/*.00001*/, 0.000);

    final double[] DIST_VALS  = {57.5, 63.8, 70.0, 78.7, 86.4, 93.4, 100.5, 107.6, 122.7, 130.6, 137.0, 141.0, 147.4, 153.3};
    final int[] VELOCITY_VALS = {1260, 1260, 1300, 1320, 1340, 1350, 1380,  1410,  1470,  1490,  1510,  1580,  1600,  1610};

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


    public double getVelocity(AngleUnit angleUnit) {
        return launcherLeft.getVelocity(angleUnit);
    }


    public int CalcSpeed(double currentDistance) {
        int lowerDistIndex = 0;
        int higherDistIndex = 0;
        for (int i = DIST_VALS.length-1; i >= 0; i--) {
            double distance = DIST_VALS[i];

            if (distance < currentDistance) {
                lowerDistIndex = i;
                higherDistIndex = i + 1;
                break;
            }
        }

        double lowerDistVal = DIST_VALS[lowerDistIndex];
        double higherDistVal = DIST_VALS[higherDistIndex];

        double sliceLength = higherDistVal - lowerDistVal;
        double sliceLengthCovered = currentDistance - lowerDistVal;
        double interpMult = sliceLengthCovered / sliceLength;

        int lowerVelocityVal = VELOCITY_VALS[lowerDistIndex];
        int higherVelocityVal = VELOCITY_VALS[higherDistIndex];

        int velocitySliceLength = higherVelocityVal - lowerVelocityVal;
        int velocitySliceLengthCovered = (int)(velocitySliceLength * interpMult);

        return lowerVelocityVal + velocitySliceLengthCovered;
    }
}
