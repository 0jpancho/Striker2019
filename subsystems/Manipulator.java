package frc.robot.subsystems;

import frc.robot.util.Constants;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Manipulator extends Subsystem
{
    private DoubleSolenoid hatchExtender, hatchRelease, cargoArms, cargoPuncher;

    private Relay pivotOne, pivotTwo;
    
    private boolean extenderToggle = false;
    private boolean hatchReleaseToggle = false;

    private boolean cargoArmsToggle = false;
    private boolean cargoPuncherToggle = false; 

    private Compressor compressor;

    Constants constants;

    public Manipulator(){
        
        hatchExtender = new DoubleSolenoid(2, 3);
        hatchRelease = new DoubleSolenoid(0, 1);

        cargoPuncher = new DoubleSolenoid(6, 7);
        cargoArms = new DoubleSolenoid(4, 5);
        
        pivotOne = new Relay(0);
        pivotTwo = new Relay(1);

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

    public void toggleCargoArms(boolean joyToggle){

        if (joyToggle && !cargoArmsToggle){

            if (cargoArms.get() == Value.kReverse ||
                cargoArms.get() == Value.kOff){
                    
                    cargoArms.set(Value.kForward);
            }

            else if (cargoArms.get() == Value.kForward){
                cargoArms.set(Value.kReverse);
            }
            cargoArmsToggle = true;
        }

        else if (!joyToggle){
            cargoArmsToggle = false;
        }
    }

    public void toggleCargoPuncher (boolean joyToggle){
        if (joyToggle && !cargoPuncherToggle){
            if(cargoPuncher.get() == Value.kReverse || cargoPuncher.get() == Value.kOff){
                cargoPuncher.set(Value.kForward);
            }
            else if(cargoPuncher.get() == Value.kForward){
                cargoPuncher.set(Value.kReverse);
            }
            cargoPuncherToggle = true;
        }
        else if (!joyToggle){
            cargoPuncherToggle = false;
        }
    }

    public void pivotWrist(double inputY){

        if (inputY > 0.25){
            pivotOne.set(edu.wpi.first.wpilibj.Relay.Value.kForward);
            pivotTwo.set(edu.wpi.first.wpilibj.Relay.Value.kForward);
        }

        else if (inputY < -0.25){
            pivotOne.set(edu.wpi.first.wpilibj.Relay.Value.kReverse);
            pivotTwo.set(edu.wpi.first.wpilibj.Relay.Value.kReverse);
        }

        else {
            pivotOne.set(edu.wpi.first.wpilibj.Relay.Value.kOff);
            pivotTwo.set(edu.wpi.first.wpilibj.Relay.Value.kOff);
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
        SmartDashboard.putBoolean("Cargo Release Toggle", cargoArmsToggle);
    }
}