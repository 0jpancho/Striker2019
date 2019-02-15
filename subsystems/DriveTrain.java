package frc.robot.subsystems;

import frc.robot.util.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.FollowerType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.*;

public class DriveTrain extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public WPI_TalonSRX leftMaster, rightMaster, leftSlave, rightSlave;
	Constants constants;

	double kP = 1;
	double kI = 0;
	double kD = 0;
	double kF = 0;

	public final double wheelDiameter = 6;
	public final double wheelCircumference = wheelDiameter * Math.PI;

	public final double ppr = 4096;

	public final double gearRatio = 1;

	public final double inchesPerRev = (wheelCircumference * gearRatio) / ppr;

	double targetDistance; 

	public DriveTrain (){
				// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());

		/*
		SmartDashboard.putNumber("Drive kP", Constants.driveCoefficients.getP());
		SmartDashboard.putNumber("Drive kI", Constants.driveCoefficients.getI());
		SmartDashboard.putNumber("Drive kD", Constants.driveCoefficients.getD());
		SmartDashboard.putNumber("Drive kF", Constants.driveCoefficients.getF());
		*/

		SmartDashboard.putNumber("Drive kP", kP);
        SmartDashboard.putNumber("Drive kI", kI);
        SmartDashboard.putNumber("Drive kD", kP);
        SmartDashboard.putNumber("Drive kF", kF);
		
        kP = SmartDashboard.getNumber("Drive kP", kP);
        kI = SmartDashboard.getNumber("Drive kP", kI);
        kD = SmartDashboard.getNumber("Drive kP", kD);
		kF = SmartDashboard.getNumber("Drive kP", kF);

		leftMaster = new WPI_TalonSRX(2);

		rightMaster = new WPI_TalonSRX(3);
		
		leftSlave = new WPI_TalonSRX(1);

		rightSlave = new WPI_TalonSRX(4);

		leftSlave.follow(leftMaster, FollowerType.PercentOutput);
		rightSlave.follow(rightMaster, FollowerType.PercentOutput);

		leftMaster.setInverted(true);
		leftSlave.setInverted(true);
		 
		leftMaster.setNeutralMode(NeutralMode.Coast);
		leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx,
				Constants.kTimeoutMs);

		leftMaster.configNominalOutputForward(0, Constants.kTimeoutMs);
		leftMaster.configNominalOutputReverse(0, Constants.kTimeoutMs);
		leftMaster.configPeakOutputForward(1, Constants.kTimeoutMs);
		leftMaster.configPeakOutputReverse(-1, Constants.kTimeoutMs);

		leftMaster.config_kP(Constants.kPIDLoopIdx, kP, Constants.kTimeoutMs);
		leftMaster.config_kI(Constants.kPIDLoopIdx, kI, Constants.kTimeoutMs);
		leftMaster.config_kD(Constants.kPIDLoopIdx, kD, Constants.kTimeoutMs);
		leftMaster.config_kF(Constants.kPIDLoopIdx, kF, Constants.kTimeoutMs);

		leftMaster.configOpenloopRamp(0.5, 20);

		rightMaster.setNeutralMode(NeutralMode.Coast);
		rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx,
				Constants.kTimeoutMs);

		rightMaster.configNominalOutputForward(0, Constants.kTimeoutMs);
		rightMaster.configNominalOutputReverse(0, Constants.kTimeoutMs);
		rightMaster.configPeakOutputForward(1, Constants.kTimeoutMs);
		rightMaster.configPeakOutputReverse(-1, Constants.kTimeoutMs);

		rightMaster.config_kP(Constants.kPIDLoopIdx, kP, Constants.kTimeoutMs);
		rightMaster.config_kI(Constants.kPIDLoopIdx, kI, Constants.kTimeoutMs);
		rightMaster.config_kD(Constants.kPIDLoopIdx, kD, Constants.kTimeoutMs);
		rightMaster.config_kF(Constants.kPIDLoopIdx, kF, Constants.kTimeoutMs);

		rightMaster.configOpenloopRamp(0.5, 20);
		
		resetDriveMotors();
	}
	@Override
	public void initDefaultCommand() {
	
	}
	
	public void tankDrive(double leftInput, double rightInput, double multiplier, boolean toggleFull) {
		if (toggleFull){
			leftMaster.set(ControlMode.PercentOutput, -leftInput);
			rightMaster.set(ControlMode.PercentOutput, -rightInput);
		}

		else {
			leftMaster.set(ControlMode.PercentOutput, -leftInput * multiplier);
			rightMaster.set(ControlMode.PercentOutput, -rightInput * multiplier);
		}
	}

	
	public void arcadeDrive(double inputY, double inputX, double multiplier, boolean toggleFull) {
		if (toggleFull){
			leftMaster.set(ControlMode.PercentOutput, inputY - inputX);
			rightMaster.set(ControlMode.PercentOutput, inputY + inputX);
		}

		else{
			leftMaster.set(ControlMode.PercentOutput, (inputY - inputX) * multiplier);
			rightMaster.set(ControlMode.PercentOutput, (inputY + inputX) * multiplier);
		}
	}
	

	public void moveByInches(double distance) {

		targetDistance = distance * inchesPerRev;

		leftMaster.set(ControlMode.Position, leftMaster.getSelectedSensorPosition() + targetDistance);
		rightMaster.set(ControlMode.Position, rightMaster.getSelectedSensorPosition() + targetDistance);

		
	}
	
	public void resetDriveMotors() {

		leftMaster.set(ControlMode.PercentOutput, 0);
		rightMaster.set(ControlMode.PercentOutput, 0);
		
		leftMaster.setSelectedSensorPosition(0, 0, 20);
		rightMaster.setSelectedSensorPosition(0, 0, 20);

		leftMaster.getSensorCollection().setQuadraturePosition(0, 20);
		rightMaster.getSensorCollection().setQuadraturePosition(0, 20);
	}
}