package org.usfirst.frc.team5892.robot;
//test git
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
/*
	 *     - PWM 1 - Connected to front left drive motor
	 *     - PWM 2 - Connected to rear left drive motor
	 *     - PWM 3 - Connected to front right drive motor
	 *     - PWM 4 - Connected to rear right drive motor
	 */
	
	RobotDrive m_robotDrive = new RobotDrive(1, 2, 3, 4);
	Joystick m_driveStick = new Joystick(1);
	@Override
	public void robotInit() {
		
	}
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}
	@Override
	public void teleopPeriodic() {
		double xAxis;
		double yAxis;
		double twist;
		if(Math.abs(m_driveStick.getX()) >= 0.18){
			xAxis = m_driveStick.getX();
		}else{
			xAxis = 0;			
		}
		if(Math.abs(m_driveStick.getY()) >= 0.18){
			yAxis = m_driveStick.getY();
		}else{
			yAxis = 0;			
		}
		if(Math.abs(m_driveStick.getRawAxis(4)) >= 0.18){
			twist = m_driveStick.getRawAxis(4);
		}else{
			twist = 0;			
		}
		m_robotDrive.mecanumDrive_Cartesian(xAxis, yAxis, twist,0);
	}
	@Override
	public void testPeriodic() {
	}
}

