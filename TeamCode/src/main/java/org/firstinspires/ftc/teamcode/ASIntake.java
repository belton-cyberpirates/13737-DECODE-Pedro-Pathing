package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class ASIntake {

    private LinearOpMode auto;

    public DcMotorEx intake;
    public DcMotorEx pusher;
    public Servo stopper;


    public ASIntake(LinearOpMode auto) {
        this.auto = auto;
        
        this.intake = auto.hardwareMap.get(DcMotorEx.class, ASBotConfig.INTAKE_NAME);
        this.pusher = auto.hardwareMap.get(DcMotorEx.class, ASBotConfig.PUSHER_NAME);
        this.stopper = auto.hardwareMap.get(Servo.class, ASBotConfig.STOPPER_NAME);
        
        this.intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    public void SetPower(double power) {
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setPower(power);
    }


    public void SetPusherPower(double power) {
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        pusher.setPower(power);
    }
  
  
    public void SetIntakeVelocity(int velocity) {
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setVelocity(velocity);
    }


    public void SpinIntake() {
        this.SetIntakeVelocity(ASBotConfig.AUTO_INTAKE_VELOCITY);
    }


    public void SpinIntake(double mult) {
        this.SetIntakeVelocity((int)(ASBotConfig.AUTO_INTAKE_VELOCITY * mult));
    }
  
  
    public void SetPusherVelocity(int velocity) {
        pusher.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        pusher.setVelocity(velocity);
    }


    public void SpinPusher() {
        this.SetPusherVelocity(ASBotConfig.AUTO_PUSHER_VELOCITY);
    }


    public void SpinPusher(double mult) {
        this.SetPusherVelocity((int)(ASBotConfig.AUTO_PUSHER_VELOCITY * mult));
    }


    public void OpenStopper() {
        stopper.setPosition(ASBotConfig.STOPPER_OPEN_POS);
    }


    public void CloseStopper() {
        stopper.setPosition(ASBotConfig.STOPPER_CLOSE_POS);
    }


    public void SetStopper(boolean open) {
        stopper.setPosition(open ? ASBotConfig.STOPPER_OPEN_POS : ASBotConfig.STOPPER_CLOSE_POS);
    }


    public boolean isStopperOpen() {
        return stopper.getPosition() == ASBotConfig.STOPPER_OPEN_POS;
    }
    
    public int getIntakeVelocity() {
        return (int)intake.getVelocity();
    }
}