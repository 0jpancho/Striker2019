package frc.robot.subsystems;

import frc.robot.util.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public WPI_TalonSRX leftMaster, rightMaster, leftSlave, rightSlave;
	Constants constants;

	double kP = 0;
	double kI = 0;
	double kD = 0;
	double kF = 0;

	@Override
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());

		//SmartDashboard.putNumber("Drive kP", Constants.driveCoefficients.getP());
		//SmartDashboard.putNumber("Drive kI", Constants.driveCoefficients.getI());
		/*SmartDashboard.putNumber("Drive kD", Constants.driveCoefficients.getD());
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

		rightMaster = new WPI_TalonSRX(3);

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

		rightMaster.setInverted(true);

		leftSlave = new WPI_TalonSRX(1);

		leftSlave.follow(leftMaster);

		rightSlave = new WPI_TalonSRX(3);

		rightSlave.follow(rightMaster);

		rightSlave.setInverted(true);

		resetDriveMotors();
	}

	public void tankDrive(double leftInput, double rightInput) {
		leftMaster.set(ControlMode.PercentOutput, leftInput);
		rightMaster.set(ControlMode.PercentOutput, rightInput);
	}

	public void arcadeDrive(Joystick mainDrive) {
		leftMaster.set(ControlMode.PercentOutput, mainDrive.getY() - mainDrive.getX());
		rightMaster.set(ControlMode.PercentOutput, mainDrive.getY() - mainDrive.getX());
	}

	public void moveByInches(double distance) {

		double targetDistance = distance * constants.inchesPerRev;

		leftMaster.set(ControlMode.Position, leftMaster.getSelectedSensorPosition(0) + targetDistance);
		rightMaster.set(ControlMode.Position, rightMaster.getSelectedSensorPosition(0) + targetDistance);

		/*
		while (leftMaster.isAlive() || rightMaster.isAlive()) {
			SmartDashboard.putNumber("Left Enc Counts", leftMaster.getSelectedSensorPosition(0));
			SmartDashboard.putNumber("Right Enc Counts", rightMaster.getSelectedSensorPosition(0));
		}
		*/
	}

	public void resetDriveMotors() {

		leftMaster.set(ControlMode.PercentOutput, 0);
		rightMaster.set(ControlMode.PercentOutput, 0);
		
		leftMaster.setSelectedSensorPosition(0, 0, 20);
		rightMaster.setSelectedSensorPosition(0, 0, 20);

		leftMaster.getSensorCollection().setQuadraturePosition(0, 20);
		rightMaster.getSensorCollection().setQuadraturePosition(0, 20);
	}

	/*
	public void drivePIDSendables(){
		Constants.driveCoefficients.setP(SmartDashboard.getNumber("Drive kP", Constants.driveCoefficients.getP()));
		Constants.driveCoefficients.setI(SmartDashboard.getNumber("Drive kI", Constants.driveCoefficients.getI()));
		Constants.driveCoefficients.setD(SmartDashboard.getNumber("Drive kD", Constants.driveCoefficients.getD()));
		Constants.driveCoefficients.setF(SmartDashboard.getNumber("Drive kF", Constants.driveCoefficients.getF()));	
	}
	*/
}