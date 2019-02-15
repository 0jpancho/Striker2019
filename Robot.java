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
import frc.robot.subsystems.Manipulator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
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
	public static Manipulator m_Manipulator;

	public Joystick joyLeft, joyRight, operator;

	public PIDController turnController;
	public static double kToleranceDegrees = 2.0f;

	public AHRS navX;

	CameraServer camera;

	double rotateToAngleRate = 0;

	double kP = 1;
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

		/*
		kP = SmartDashboard.getNumber("Turn kP", 1);
		kI = SmartDashboard.getNumber("Turn kI", 0);
		kD = SmartDashboard.getNumber("Turn kD", 0);
		kF = SmartDashboard.getNumber("Turn kF", 0);
		*/

		turnController = new PIDController(kP, kI, kD, kF, navX, this);
		turnController.setInputRange(-180.0f, 180.0f);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(kToleranceDegrees);
		turnController.setContinuous(true);
		
		autonSelector = new AutonomousSelector();

		m_DriveTrain = new DriveTrain();
		m_Elevator = new Elevator();
		m_Manipulator = new Manipulator();

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

		zeroSensors();
		
		System.out.println("Autonomous Initalized");

		Timer timer = new Timer();
		timer.start();

		while (timer.get() < 2 && isEnabled()){
			m_DriveTrain.leftMaster.set(ControlMode.PercentOutput, 0.5);
			m_DriveTrain.rightMaster.set(ControlMode.PercentOutput, 0.5);
		}

		m_DriveTrain.leftMaster.set(ControlMode.PercentOutput, 0);
		m_DriveTrain.rightMaster.set(ControlMode.PercentOutput, 0);
	
		timer.stop();
		timer.reset();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		
		m_DriveTrain.arcadeDrive(-joyLeft.getY(), joyLeft.getX(), 0.5, joyLeft.getTrigger());
		
		//String autoSelected = SmartDashboard.getString("Auto Selector", autonSelector.kForward);
		
		/*
		while (isAutonomous() && isEnabled()){
			
			if (autoSelected.equals(AutonomousSelector.kForward)) {
				System.out.println("Driving Forward");
				m_DriveTrain.moveByInches(24);
			}
	
			else if (autoSelected.equals(AutonomousSelector.kLeft)) {
				System.out.println("Going to the Left");
				//rotateByGyro(-90f);
				simpleTurnByGyro(-90, 0.5, 1);
			}
	
			else if (autoSelected.equals(AutonomousSelector.kMiddle)) {
	
			}
	
			else if (autoSelected.equals(AutonomousSelector.kRight)) {
				rotateByGyro(90f);
			}
			break;
		}
		*/
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		
		//m_DriveTrain.tankDrive(joyLeft.getY(), joyRight.getY(), 0.5, joyRight.getTrigger());
		m_DriveTrain.arcadeDrive(-joyLeft.getY(), joyLeft.getX(), 0.5, joyLeft.getTrigger());
		m_Elevator.testMove(operator.getRawAxis(Constants.kGamepadAxisRightStickY));
		
		m_Manipulator.toggleExtender(operator.getRawButton(Constants.kGamepadButtonShoulderL));
		m_Manipulator.toggleHatchRelease(operator.getRawButton(Constants.kGamepadButtonShoulderR));

		m_Manipulator.pivotWrist(operator.getRawAxis(Constants.kGamepadAxisLeftStickY));

		m_Manipulator.toggleCargoArms(operator.getRawButton(Constants.kGamepadButtonX));
		m_Manipulator.toggleCargoPuncher(operator.getRawButton(Constants.kGamepadButtonA));
	}

	@Override
	public void disabledInit(){
		zeroSensors();
	}

	public void robotPeriodic() {
		robotTelemetry();

		m_Manipulator.telemetry();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public void rotateByGyro(double angle) {

		turnController.enable();

		turnController.setSetpoint(angle);
		while (turnController.onTarget()){
			pidWrite(turnController.get());
		}

		turnController.disable();
		turnController.reset();
	}
	
	@Override
	public void pidWrite(double output) {
		m_DriveTrain.leftMaster.pidWrite(output);
		m_DriveTrain.rightMaster.pidWrite(output);
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