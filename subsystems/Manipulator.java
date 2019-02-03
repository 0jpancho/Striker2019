package frc.robot.subsystems;

import frc.robot.util.Constants;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator extends Subsystem
{
    public DoubleSolenoid hatchExtender, hatchRelease;
    
    public boolean extenderToggle = false;
    public boolean hatchReleaseToggle = false;

    public Compressor compressor;

    Constants constants;

    public Manipulator(){
        hatchExtender = new DoubleSolenoid(2, 3);

        hatchRelease = new DoubleSolenoid(0, 1);
        
        compressor = new Compressor();
        compressor.start();
    }

    @Override
    public void initDefaultCommand()
    {
        
    }

    public void toggleExtender(boolean joyToggle){

        if (joyToggle && !extenderToggle){

            if (hatchExtender.get() == Value.kReverse || 
                    hatchExtender.get() == Value.kOff){
                
                        hatchExtender.set(Value.kForward);
            }

            else if (hatchExtender.get() == Value.kForward){
                hatchExtender.set(Value.kReverse);
            }

            extenderToggle = true;
        }
        else if (!joyToggle){
            extenderToggle = false;
        }
    }

    public void toggleHatchRelease(boolean joyToggle){
        
        if (joyToggle && !hatchReleaseToggle){
            
            if (hatchRelease.get() == Value.kReverse ||
                    hatchRelease.get() == Value.kOff){
                    
                        hatchRelease.set(Value.kForward);
            }

            else if (hatchRelease.get() == Value.kForward){
                hatchRelease.set(Value.kReverse);
            }
            hatchReleaseToggle = true;
        }
        
        else if (!joyToggle){
            hatchReleaseToggle = false;
        }
    }

    public void useCompressor(boolean toggle){
        if (toggle){
            compressor.start();
        }
    }

    public void telemetry(){
        SmartDashboard.putBoolean("Hatch Extender Toggle", extenderToggle);
        SmartDashboard.putBoolean("Hatch Release Toggle", hatchReleaseToggle);
    }
}