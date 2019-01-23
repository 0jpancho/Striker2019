package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Pneumatics extends Subsystem
{
    Compressor compressor;
    public Solenoid hPusher;
    public Solenoid aLifter;



    public void initDefaultCommand()
    {
        compressor = new Compressor();
        compressor.start();

    
        hPusher = new Solenoid(0);
        aLifter = new Solenoid(1);
    }
}