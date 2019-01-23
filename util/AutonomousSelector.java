package frc.robot.util;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousSelector {
	
	public static final String kDefault = "Drive Forward";
	public static final String kLeft = "Left";
	public static final String kMiddle = "Middle";
	public static final String kRight = "Right";
		
	private SendableChooser<String> m_AutonChooser = new SendableChooser<>();
	
	public AutonomousSelector() {
		
		m_AutonChooser.setDefaultOption("Drive Forward", kDefault);
		m_AutonChooser.addOption("Left", kLeft);
		m_AutonChooser.addOption("Middle", kMiddle);
		m_AutonChooser.addOption("Right", kRight);
		
		SmartDashboard.putData("Auto Selector", m_AutonChooser);
	}
}
