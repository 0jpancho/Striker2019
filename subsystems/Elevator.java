package frc.robot.subsystems;

import frc.robot.util.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Subsystem{

    public TalonSRX  liftMaster, liftSlave;

    double kP = 0;
	double kI = 0;
	double kD = 0;
	double kF = 0;

    Constants constants;

    @Override
    public void initDefaultCommand(){

        SmartDashboard.putNumber("Elevator kP", kP);
        SmartDashboard.putNumber("Elevator kI", kI);
        SmartDashboard.putNumber("Elevator kD", kP);
        SmartDashboard.putNumber("Elevator kF", kF);

        kP = SmartDashboard.getNumber("Elevator kP", kP);
        kI = SmartDashboard.getNumber("Elevator kP", kI);
        kD = SmartDashboard.getNumber("Elevator kP", kD);
        kF = SmartDashboard.getNumber("Elevator kP", kF);

        liftMaster = new TalonSRX(5);
        liftMaster.setNeutralMode(NeutralMode.Brake);
        
        liftSlave = new TalonSRX(6);
        liftSlave.setNeutralMode(NeutralMode.Brake);
        liftSlave.follow(liftMaster);

		liftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx,
				Constants.kTimeoutMs);

        liftMaster.configNominalOutputForward(0, Constants.kTimeoutMs);
        liftMaster.configNominalOutputReverse(0, Constants.kTimeoutMs);
        liftMaster.configPeakOutputForward(1, Constants.kTimeoutMs);
        liftMaster.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        liftMaster.config_kP(Constants.kPIDLoopIdx, kP, Constants.kTimeoutMs);
        liftMaster.config_kI(Constants.kPIDLoopIdx, kI, Constants.kTimeoutMs);
        liftMaster.config_kD(Constants.kPIDLoopIdx, kD, Constants.kTimeoutMs);
        liftMaster.config_kF(Constants.kPIDLoopIdx, kF, Constants.kTimeoutMs);
    }

    public void moveByInput(double inputY){
        if(inputY == 0){
            liftMaster.set(ControlMode.Position, liftMaster.getSelectedSensorPosition(Constants.kPIDLoopIdx));
        }

        else {
            liftMaster.set(ControlMode.PercentOutput, inputY);
        }
    }

    /*
    public void elevatorPIDSendables(){
		Constants.elevatorCoefficients.setP(SmartDashboard.getNumber("Elevator kP", Constants.elevatorCoefficients.getP()));
		Constants.elevatorCoefficients.setI(SmartDashboard.getNumber("Elevator kI", Constants.elevatorCoefficients.getI()));
		Constants.elevatorCoefficients.setD(SmartDashboard.getNumber("Elevator kD", Constants.elevatorCoefficients.getD()));
        Constants.elevatorCoefficients.setF(SmartDashboard.getNumber("Elevator kF", Constants.elevatorCoefficients.getF()));
    }
    */
}