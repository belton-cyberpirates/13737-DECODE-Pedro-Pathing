package org.firstinspires.ftc.teamcode;


public class PIDFController {
    double Kf;
    double Kp;
    double Ki;
    double Kd;

    double integralSum = 0;
    double lastError = 100;
    
    double lastOutput = 10000;

    public PIDFController(double Kf, double Kp, double Ki, double Kd) {
        this.Kf = Kf;
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
    }

    public double PIDFControlRadians(double reference, double state, double delta) {
        double error = angleWrap(reference - state);
        if (Double.isNaN(error)) {
            throw new RuntimeException("Error is NaN: " + error + ", reference=" + reference + ", state=" + state + ", delta=" + delta);
        }
        
        double derivative = (error - lastError) / delta;

        this.lastError = error;
        this.integralSum += error * delta;

        double output = (error * this.Kp) + (derivative * this.Kd) + (this.integralSum * this.Ki);
        
        lastOutput = output;
        return output + (Kf * reference);
    }



    public double PIDFControl(double reference, double state, double delta) {
        double error = reference - state;
        
        double derivative = (error - lastError) / delta;

        this.lastError = error;
        this.integralSum += error * delta;

        double output = (this.Kp * error) + 
                        (this.Ki * this.integralSum) + 
                        (this.Kd * derivative);
        
        lastOutput = output;
        return output + (Kf * reference);
    }
    
    

    public double angleWrap(double radians) {
        while (radians > Math.PI) {
            radians -= 2 * Math.PI;
        }
        
        while (radians < -Math.PI) {
            radians += 2 * Math.PI;
        }

        return radians;
    }
}
