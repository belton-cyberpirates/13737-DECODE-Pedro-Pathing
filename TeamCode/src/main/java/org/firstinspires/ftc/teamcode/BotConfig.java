package org.firstinspires.ftc.teamcode;


public class BotConfig {
  /*****************************************************************************
  ** HARDWARE CONSTANTS
  *****************************************************************************/
  public static final String FRONT_RIGHT_WHEEL_NAME = "front_right";
  public static final String FRONT_LEFT_WHEEL_NAME = "front_left";
  public static final String BACK_LEFT_WHEEL_NAME = "back_left";
  public static final String BACK_RIGHT_WHEEL_NAME = "back_right";
  
  public static final String INTAKE_NAME = "intake";
  public static final String PUSHER_NAME = "pusher";
  public static final String STOPPER_NAME = "stopper";

  public static final String LAUNCHER_NAME = "flywheel";

  public static final String LIGHT_NAME = "light";
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** DISTANCE CALIBRATION CONSTANTS
  *****************************************************************************/
  public static final int TILE_LENGTH = 875; // TICKS
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** TELEOP CALIBRATION CONSTANTS
  *****************************************************************************/
  public static final double BASE_SPEED = .6;
  
  public static final double MAX_BOOST = 0.66; // boost maxes out at an additional 66% of the base speed
  public static final double STRAFE_MULT = 1.41;

  public static final int LAUNCHER_DROP_VELOCITY = 300;
  public static final int LAUNCHER_PASSIVE_VELOCITY = 1000;
  public static final int LAUNCHER_VELOCITY = 1325;
  public static final int LAUNCHER_FAR_VELOCITY = 1600;
  
  public static final double LIGHT_GREEN = 0.472;
  public static final double LIGHT_RED = 0.277;
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** AUTO CALIBRATION CONSTANTS
  *****************************************************************************/
  public static final int AUTO_DRIVE_VELOCITY = 1000;
  public static final int AUTO_PUSHER_VELOCITY = 600;
  public static final int AUTO_INTAKE_VELOCITY = -2500;
  public static final int AUTO_LAUNCHER_VELOCITY = 1300;
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** BASE CONSTANTS
  *****************************************************************************/
  //public static final int PICKUP_X = 650;
  //public static final int PICKUP_Y = -850;
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** INTAKE CONSTANTS
  *****************************************************************************/
  public static final double STOPPER_OPEN_POS = 0.075;
  public static final double STOPPER_CLOSE_POS = 0.25;
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** LAUNCHER CONSTANTS
  *****************************************************************************/
  public static final int LAUNCHER_VELOCITY_MARGIN = 40;
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** ODOMETRY CALIBRATION CONSTANTS
  *****************************************************************************/
  public static final double FORWARD_OFFSET = 16.25;
  public static final double TRACK_WIDTH = 367;
  public static final double WHEEL_DIAMETER = 38;
  public static final double TICKS_PER_REVOLUTION = 2048;
  public static final double TICKS_PER_MM = (int)( TICKS_PER_REVOLUTION / ( Math.PI * WHEEL_DIAMETER ) );
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** CAMERA CONSTANTS
  *****************************************************************************/
  public static final String CAMERA_NAME = "Webcam 1";
    public static final int CAMERA_RESO_X = 640;
    public static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/teampiece.tflite";
    public static final String[] LABELS = { // Define the labels used in our model (must be in training order!)
        "BLUE",
        "RED",
    };
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** CAMERA CALIBRATION CONSTANTS
  * Lens intrinsics
  * UNITS ARE PIXELS
  * NOTE: this calibration is for the C920 webcam at 800x448.
  * You will need to do your own calibration for other configurations!

  Resolution: 1280x720
  Pixel Size: 2.8um
  Sensor Size: 3.58x2.02mm
  Stock lens focal length: 4.2mm
  *****************************************************************************/
  public static final double FX = 1430;
  public static final double FY = 1430;
  public static final double CX = 480;
  public static final double CY = 620;

  public static final double TAGSIZE = 0.166;
  // ---------------------------------------------------------------------------


  /*****************************************************************************
  ** DETECTION CONSTANTS
  *****************************************************************************/
  public static final float DECIMATION_HIGH = 3;
  public static final float DECIMATION_LOW = 2;
  public static final float THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f;
  public static final int NUM_FRAMES_BEFORE_LOW_DECIMATION = 4;
  public static final int MAX_NUM_FRAMES_NO_DETECTION = 100; // How many attempts to detect before giving up
  // ---------------------------------------------------------------------------
}