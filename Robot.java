/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.util.AutonomousSelector;
import frc.robot.util.Constants;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Pneumatics;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot implements PIDOutput {

	public AutonomousSelector autonSelector;

	public static DriveTrain m_DriveTrain;
	public static Elevator m_Elevator;
	public static Pneumatics m_Pneumatics;

	public Joystick joyLeft, joyRight, operator;

	public PIDController turnController;
	public static double kToleranceDegrees = 2.0f;

	public AHRS navX;
	double[] tempCoefficients = new double[4];

	CameraServer camera;

	double rotateToAngleRate = 0;

	double kP = 0;
	double kI = 0;
	double kD = 0;
	double kF = 0;

	boolean isRotatingLeft = false; 
	boolean isRotatingRight = false;

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit() {

		navX = new AHRS(SPI.Port.kMXP);
		navX.setPIDSourceType(PIDSourceType.kRate);

		SmartDashboard.putNumber("Turn kP", kP);
		SmartDashboard.putNumber("Turn kI", kI);
		SmartDashboard.putNumber("Turn kD", kD);
		SmartDashboard.putNumber("Turn kF", kF);

		kP = SmartDashboard.getNumber("Turn kP", 0);
		kI = SmartDashboard.getNumber("Turn kI", 0);
		kD = SmartDashboard.getNumber("Turn kD", 0);
		kF = SmartDashboard.getNumber("Turn kF", 0);

		turnController = new PIDController(kP, kI, kD, kF, navX, this);
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(kToleranceDegrees);
		turnController.setContinuous(true);

		/*
		tempCoefficients[0] = Constants.driveCoefficients.getP();
		tempCoefficients[1] = Constants.driveCoefficients.getI();
		tempCoefficients[2] = Constants.driveCoefficients.getD();
		tempCoefficients[3] = Constants.driveCoefficients.getF();
		*/

		//SmartDashboard.putNumberArray("Turn Coefficients", tempCoefficients);

		autonSelector = new AutonomousSelector();

		m_DriveTrain = new DriveTrain();
		m_DriveTrain.initDefaultCommand();

		m_Elevator = new Elevator();
		m_Elevator.initDefaultCommand();

		//m_Pneumatics = new Pneumatics();
		//m_Pneumatics.initDefaultCommand();

		//camera = CameraServer.getInstance();
		//camera.startAutomaticCapture();

		joyLeft = new Joystick(0);
		joyRight = new Joystick(1);
		operator = new Joystick(2);

		System.out.println("Robot Initialized");
		zeroSensors();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString line to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the SendableChooser
	 * make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {

		System.out.println("Autonomous Initalized");

		String autoSelected = SmartDashboard.getString("Auto Selector", AutonomousSelector.kDefault);

		if (autoSelected.equals(AutonomousSelector.kDefault)) {
			m_DriveTrain.moveByInches(24);
		}

		else if (autoSelected.equals(AutonomousSelector.kLeft)) {
			rotateByGyro(-90f);
		}

		else if (autoSelected.equals(AutonomousSelector.kMiddle)) {

		}

		else if (autoSelected.equals(AutonomousSelector.kRight)) {
			rotateByGyro(90f);
		}
		
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		m_DriveTrain.tankDrive(joyLeft.getY(), joyRight.getY());
		m_Elevator.moveByInput(operator.getRawAxis(Constants.kGamepadAxisRightStickY));
		
		if(operator.getTrigger())
		{
			m_Pneumatics.hPusher.set(true);
		}

		else
		{
			m_Pneumatics.hPusher.set(false);
		}

		if(operator.getRawButton(2))
		{
			m_Pneumatics.aLifter.set(true);		
		}

		else
		{
			m_Pneumatics.aLifter.set(false);
		}
		

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public void robotPeriodic() {
		robotTelemetry();

		//turnPIDSendables();
		//m_DriveTrain.drivePIDSendables();
		//m_Elevator.elevatorPIDSendables();
	}

	public void rotateByGyro(double angle) {

		boolean rotateToAngle = false;

		while (isAutonomous() && isEnabled()) {
			
			turnController.setSetpoint((float) angle);
			rotateToAngle = false;

			double currentRotationRate = 0;

			if (rotateToAngle) {
				turnController.enable();
				currentRotationRate = rotateToAngleRate;
				
				m_DriveTrain.leftMaster.set(ControlMode.PercentOutput, turnController.get());
				m_DriveTrain.rightMaster.set(ControlMode.PercentOutput, turnController.get());
			}

			else {
				turnController.disable();
			}
		}
		turnController.reset();
	}
	
	public void robotTelemetry() {
		SmartDashboard.putNumber("Left Enc Counts", m_DriveTrain.leftMaster.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Enc Counts", m_DriveTrain.rightMaster.getSelectedSensorPosition(0));

		SmartDashboard.putNumber("Heading", navX.getYaw());
		SmartDashboard.putBoolean("NavX Calibrating?", navX.isCalibrating());
		SmartDashboard.putBoolean("NavX Connected?", navX.isConnected());

		SmartDashboard.putBoolean("Rotating Left?", isRotatingLeft);
		SmartDashboard.putBoolean("Rotating Right?", isRotatingRight);
	}
	
	@Override
	public void pidWrite(double output) {
		rotateToAngleRate = output;
	}

	void zeroYaw() {
		navX.reset();
	}
	
	void zeroSensors() {
		m_DriveTrain.resetDriveMotors();
		zeroYaw();

		System.out.println("Sensors Zeroed");
	}

	public void simpleTurnByGyro (double angle, double power, double direction){

		zeroYaw();

		

		//Left
		if (direction == 0){
			while (angle < navX.getYaw()){
				
				isRotatingLeft = true;

				m_DriveTrain.leftMaster.set(ControlMode.PercentOutput, -power);
				m_DriveTrain.rightMaster.set(ControlMode.PercentOutput, power);
			}
			m_DriveTrain.resetDriveMotors();
		}

		//Right
		else if (direction == 1){
			while (angle > navX.getYaw()){

				isRotatingRight = true;
				
				m_DriveTrain.leftMaster.set(ControlMode.PercentOutput, power);
				m_DriveTrain.rightMaster.set(ControlMode.PercentOutput, -power);
			}
			m_DriveTrain.resetDriveMotors();
		}
	}
	
	/*
	public void turnPIDSendables(){

		turnController.setPID(SmartDashboard.getNumber("Turn kP", Constants.turnCoefficients.getP()), 
								SmartDashboard.getNumber("Turn kI", Constants.turnCoefficients.getI()), 
								SmartDashboard.getNumber("Turn kD", Constants.turnCoefficients.getD()), 
								SmartDashboard.getNumber("Turn kF", Constants.turnCoefficients.getF()));
	}
	*/
}