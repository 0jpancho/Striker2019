package frc.robot.util;

public class Constants{
    // Gamepad axis
	public static final int kGamepadAxisLeftStickX = 1;
	public static final int kGamepadAxisLeftStickY = 1;
	public static final int kGamepadAxisShoulder = 3;
	public static final int kGamepadAxisRightStickX = 4;
	public static final int kGamepadAxisRightStickY = 5;
	public static final int kGamepadAxisDpad = 6;

	// Gamepad buttons
	public static final int kGamepadButtonA = 1; // Bottom Button
	public static final int kGamepadButtonB = 2; // Right Button
	public static final int kGamepadButtonX = 3; // Left Button
	public static final int kGamepadButtonY = 4; // Top Button
	public static final int kGamepadButtonShoulderL = 5;
	public static final int kGamepadButtonShoulderR = 6;
	public static final int kGamepadButtonBack = 7;
	public static final int kGamepadButtonStart = 8;
	public static final int kGamepadButtonLeftStick = 9;
	public static final int kGamepadButtonRightStick = 10;
	public static final int kGamepadButtonMode = -1;
	public static final int kGamepadButtonLogitech = -1;

	/**
	 * Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
	 * configuration.
	 */
	public static final int kSlotIdx = 0;

	/**
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
	 * now we just want the primary one.
	 */
	public static final int kPIDLoopIdx = 0;

	/**
	 * Set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
	public static final int kTimeoutMs = 20;
	
	/* Choose so that Talon does not report sensor out of phase */
	public static boolean kSensorPhase = true;

	/**
	 * Choose based on what direction you want to be positive,
	 * this does not affect motor invert. 
	 */
	public static boolean kMotorInvert = false;

	/**
	 * Gains used in Positon Closed Loop, to be adjusted accordingly
     * Gains(kp, ki, kd, kf, izone, peak output);
     */
	

	public final double wheelDiameter = 6;
	public final double wheelCircumference = wheelDiameter * Math.PI;

	public final double ppr = 4096;

	public final double gearRatio = 1;

	public final double inchesPerRev = (wheelCircumference * gearRatio) / ppr;

	/*
	public static PIDCoefficients driveCoefficients = new PIDCoefficients(0, 0, 0, 0);
	public static PIDCoefficients turnCoefficients = new PIDCoefficients(0, 0, 0, 0);
	public static PIDCoefficients elevatorCoefficients = new PIDCoefficients(0, 0, 0, 0);
	*/
	
	//Drive Train Coefficients

	//Turn Controller Coefficients

	

	//Elevator Coefficients
}

