package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import java.util.Collections;
import java.util.Arrays;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import com.qualcomm.robotcore.hardware.AnalogInput;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import org.firstinspires.ftc.teamcode.ASDirection;
import org.firstinspires.ftc.teamcode.ASBotConfig;
import org.firstinspires.ftc.teamcode.ASPIDController;


public class ASDriveMotors {

    enum states {
        ODOMETRY,
        DISTANCE,
        POWER,
        IDLE
    }

    public static ASPIDController distanceSensorPidController = new ASPIDController(0, 0, 0);
    public static ASPIDController forwardPidController = new ASPIDController(0.00255, 0.00000033, 0.0000025);
    public static ASPIDController strafePidController = new ASPIDController(0.00265, 0.0000004, 0.000004);
    public static ASPIDController imuPidController = new ASPIDController(1.4, 0, 0.0001);

    static Orientation angles;

    public DcMotorEx frontLeft;
    public DcMotorEx frontRight;
    public DcMotorEx backLeft;
    public DcMotorEx backRight;

    public GoBildaPinpointDriver odometry;

    private LinearOpMode auto;

    private DistanceSensor distSensor;

    ElapsedTime deltaTimer = new ElapsedTime();

    public states state = states.IDLE;

    public double targetX;
    public double targetY;
    public double targetHeading;

    public int targetDistance;

    ElapsedTime odometryTimer = new ElapsedTime();

    public double heading = 0;
    double deltaTime = 0;


    public ASDriveMotors(LinearOpMode auto) {
        this.auto = auto;

        this.frontRight = auto.hardwareMap.get(DcMotorEx.class, ASBotConfig.FRONT_RIGHT_WHEEL_NAME);
        this.frontLeft = auto.hardwareMap.get(DcMotorEx.class, ASBotConfig.FRONT_LEFT_WHEEL_NAME);
        this.backLeft = auto.hardwareMap.get(DcMotorEx.class, ASBotConfig.BACK_LEFT_WHEEL_NAME);
        this.backRight = auto.hardwareMap.get(DcMotorEx.class, ASBotConfig.BACK_RIGHT_WHEEL_NAME);

        this.backLeft.setTargetPosition(0);
        this.frontLeft.setTargetPosition(0);
        this.frontRight.setTargetPosition(0);
        this.backRight.setTargetPosition(0);

        //this.distSensor = auto.hardwareMap.get(DistanceSensor.class, BotConfig.DISTANCE_SENSOR_NAME);

        this.odometry = auto.hardwareMap.get(GoBildaPinpointDriver.class, "odo");

        ResetEncoders();
        SetZeroBehaviour();
    }


    public void InitializeOdometry() {
        /*
        Set the odometry pod positions relative to the point that the odometry computer tracks around.
        The X pod offset refers to how far sideways from the tracking point the
        X (forward) odometry pod is. Left of the center is a positive number,
        right of center is a negative number. the Y pod offset refers to how far forwards from
        the tracking point the Y (strafe) odometry pod is. forward of center is a positive number,
        backwards is a negative number.
         */
        this.odometry.setOffsets(-100, -25, DistanceUnit.MM); 

        /*
        Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
        the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
        If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
        number of ticks per mm of your odometry pod.
         */
        this.odometry.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        //odo.setEncoderResolution(13.26291192);


        /*
        Set the direction that each of the two odometry pods count. The X (forward) pod should
        increase when you move the robot forward. And the Y (strafe) pod should increase when
        you move the robot to the left.
         */
        this.odometry.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);


        /*
        Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
        The IMU will automatically calibrate when first powered on, but recalibrating before running
        the robot is a good idea to ensure that the calibration is "good".
        resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
        This is recommended before you run your autonomous, as a bad initial calibration can cause
        an incorrect starting value for x, y, and heading.
         */
        //odo.recalibrateIMU();
        this.odometry.resetPosAndIMU();
        this.odometry.setPosition( new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.DEGREES, 0) );
    }


    public double process() {
        deltaTime = deltaTimer.seconds();

        // Get the heading of the bot (the angle it is facing) in radians
        double newHeading = odometry.getHeading(AngleUnit.RADIANS);
        if (!Double.isNaN(newHeading)) {
            this.heading = newHeading;
        }

        switch (this.state) {
            case ODOMETRY:
                SetToRunWithVelocity();
                driveWithOdometry(deltaTime);
                break;

            case DISTANCE:
                SetToRunWithPower();
                driveWithDistanceSensor(deltaTime);
                break;

            case POWER:
                SetToRunWithPower();
                break;
        }

        auto.telemetry.addData("X pos", this.odometry.getPosX(DistanceUnit.MM));
        auto.telemetry.addData("Y pos", this.odometry.getPosY(DistanceUnit.MM));
        auto.telemetry.addData("drivemotors heading", this.heading);

        this.odometry.update();
        deltaTimer.reset();

        return deltaTime;
    }


    public void DriveWithPower(double backLeftPower, double frontLeftPower, double frontRightPower, double backRightPower) {
        this.state = states.POWER;

        backLeft.setPower(backLeftPower);
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }


    public void DriveAndAim(double forward, double strafe, double targetAngle, double maxSpeed, double angleOffset) {
        this.state = states.POWER;
        double anglePower = imuPidController.PIDControlRadians(targetAngle + angleOffset, heading, deltaTime);

        DriveWithPower(
                ( forward + strafe) * maxSpeed + ( anglePower ), // Back left
                ( forward - strafe) * maxSpeed + ( anglePower ), // Front left
                (-forward - strafe) * maxSpeed + ( anglePower ), // Front right
                (-forward + strafe) * maxSpeed + ( anglePower )  // Back right
        );

        auto.telemetry.addData("Angle Error", imuPidController.lastError);
    }


    public void DriveAndAim(double forward, double strafe, double targetX, double targetY, double maxSpeed, double angleOffset) {
        double xPos = odometry.getPosX(DistanceUnit.MM);
        double yPos = odometry.getPosY(DistanceUnit.MM);

        double deltaX = targetX - xPos;
        double deltaY = targetY - yPos;

        double targetAngle = Math.atan2(deltaY, deltaX);

        DriveAndAim(forward, strafe, targetAngle, maxSpeed, angleOffset);
    }


    private void driveWithOdometry(double delta) {

        // Get robot movement errors
        double xError = targetX - odometry.getPosX(DistanceUnit.MM);
        double yError = targetY - odometry.getPosY(DistanceUnit.MM);

        // Rotate the movement vector to convert field relative direction to robot relative direction
        double forwardError =
                xError * Math.cos(-heading) -
                        yError * Math.sin(-heading);
        double horizontalError =
                xError * Math.sin(-heading) +
                        yError * Math.cos(-heading);

        // Get powers from PID
        double forwardPower = forwardPidController.PIDControl(forwardError, delta);
        double horizontalPower = strafePidController.PIDControl(horizontalError, delta);
        double anglePower = imuPidController.PIDControlRadians(targetHeading, heading, delta);

        // Set the power of the wheels based off the new movement vector
        double backLeftPower   = (-forwardPower - horizontalPower + anglePower);
        double frontLeftPower  = (-forwardPower + horizontalPower + anglePower);
        double frontRightPower = ( forwardPower + horizontalPower + anglePower);
        double backRightPower  = ( forwardPower - horizontalPower + anglePower);

        // Find highest motor power value
        double highestPower = Collections.max(Arrays.asList( Math.abs(backLeftPower), Math.abs(frontLeftPower), Math.abs(frontRightPower), Math.abs(backRightPower) ));
        auto.telemetry.addData("", highestPower);
        // Scale power values if trying to run motors faster than possible
        // if (highestPower > 1) {
        //     backLeftPower /= highestPower;
        //     frontLeftPower /= highestPower;
        //     frontRightPower /= highestPower;
        //     backRightPower /= highestPower;
        // }

        backLeft.setPower(backLeftPower);
        frontLeft.setPower(frontLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);

        // Telemetry
        auto.telemetry.addData("drivemotors xError", xError);
        auto.telemetry.addData("drivemotors yError", yError);
        auto.telemetry.addData("drivemotors angleError", targetHeading - heading);

        // auto.telemetry.addData("drivemotors forwardPower", forwardPower);
        // auto.telemetry.addData("drivemotors horizontalPower", horizontalPower);
        // auto.telemetry.addData("drivemotors anglePower", anglePower);

        // auto.telemetry.addData("device status", odometry.getDeviceStatus());
    }


    public void driveWithDistanceSensor(double delta) {
        double dist = distSensor.getDistance(DistanceUnit.MM);
        double error = targetDistance - dist;

        double power = distanceSensorPidController.PIDControl(error, delta);

        frontLeft.setPower(-power);
        frontRight.setPower(power);
        backLeft.setPower(-power);
        backRight.setPower(power);

        auto.telemetry.addData("dist", dist);
        auto.telemetry.addData("error", error);
        auto.telemetry.addData("power", power);
    }


    public void Move(double xPos, double yPos, double heading) {
        this.targetX = xPos;
        this.targetY = yPos;
        this.targetHeading = -( heading * ( Math.PI / 180 ) );

        this.odometryTimer.reset();

        this.state = states.ODOMETRY;
    }


    public void Move(double xPos, double yPos, double heading, double mult) {
        // mult things :|
        Move(xPos, yPos, heading);
    }


    public void MoveToDistance(int distance) {
        this.targetDistance = distance;

        this.state = states.DISTANCE;
    }


    public void Stop() {
        this.state = states.IDLE;
    }


    public boolean isDone() {
        switch (this.state) {
            case ODOMETRY:
                return odometryTimer.milliseconds() > 750 &&
                        (Math.abs(forwardPidController.lastError) < 20) && // max vertical error - MM
                        (Math.abs(strafePidController.lastError) < 20) && // max horizontal error - MM
                        (Math.abs(imuPidController.lastError) < .03); // max angle error - radians

            case DISTANCE:
                return (Math.abs(distanceSensorPidController.lastError) < 5);
        }

        return true;
    }


    private void SetToRunWithPower() {
        this.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    private void SetToRunWithVelocity() {
        this.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    private void SetZeroBehaviour() {
        this.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }


    private void ResetEncoders() {
        this.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }


    public double GetX() {
        return odometry.getPosX(DistanceUnit.MM);
    }


    public double GetY() {
        return odometry.getPosY(DistanceUnit.MM);
    }


    public double GetHeading() {
        return heading;
    }


    public double getVelX() {
        return odometry.getVelX(DistanceUnit.MM);
    }


    public double getVelY() {
        return odometry.getVelY(DistanceUnit.MM);
    }
}

