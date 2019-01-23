package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.Constants;

public class PIDDriveTrain extends PIDSubsystem{
    
    Constants constants;
    
    public PIDDriveTrain(String name, double kP, double kI, double kD, double kF ){
        super(name, kP, kI, kD, kF);


    }
    
    public void initDefaultCommand(){

    }

    public double returnPIDInput(){
        return returnPIDInput();
    }

    @Override
    public void usePIDOutput(double output){    
    }

    public void drivePIDSendables(){
		SmartDashboard.putData(this.getPIDController());
    }
}