package org.usfirst.frc.team5892.robot;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {


	
	NetworkTable table;
	NetworkTable visionTestTable1;
	ITable vTT2;
	
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
		
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(320, 240);
        
        CameraServer.getInstance().getVideo();
        CameraServer.getInstance().putVideo("Blur", 320, 240);
        
        
        
		
		table = NetworkTable.getTable("datatable");
		visionTestTable1 = NetworkTable.getTable("PixyVision");
		vTT2 = visionTestTable1.getSubTable("get_blocks");
		
		
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
		
		double x = 0;
		double y = 0;
		Timer.delay(0.25);
		table.putNumber("X", x);
		table.putNumber("Y", y);
		x += 0.05;
		y += 1.0;
		
		double x1 = vTT2.getNumber("x1", -2);  //-1 indicates that no data was received from the Pixy
		double y1 = vTT2.getNumber("y1", -2);  //-2 indicates that no data was received from the Pi
		double x2 = vTT2.getNumber("x2", -2);
		double y2 = vTT2.getNumber("y2", -2);
		
		System.out.print("Network data recieved: (" + x1 + ", " + y1 + ") and (" + x2 + ", " + y2 + ")\r");
		
		double xAxis;
		double yAxis;
		double twist = 0;
		double m =.8;
		
		if (m_driveStick.getRawButton(5)){
			m = 1;
		}
			if(Math.abs(m_driveStick.getX()) >= 0.18){
				xAxis = m_driveStick.getX() * m;
			}else{
				xAxis = 0;			
			}
			if(Math.abs(m_driveStick.getY()) >= 0.18){
				yAxis = m_driveStick.getY() * m;
			}else{
				yAxis = 0;			
			}
			if(Math.abs(m_driveStick.getRawAxis(4)) >= 0.18){
				if (m_driveStick.getRawAxis(4) >= 0.18){
					twist = Math.pow(m_driveStick.getRawAxis(4), 2) * m;
				}else if (m_driveStick.getRawAxis(4) <= -0.18){
					twist = -(Math.pow(m_driveStick.getRawAxis(4), 2)) * m;
					if (m_driveStick.getRawAxis(4) == -1){
						twist = -1 * m;
					}
				}
			}else{
				twist = 0;			
			}
		
		if(m_driveStick.getRawButton(6)){
			xAxis = xAxis/2;
			yAxis = yAxis/2;
			twist = twist/2;
		}
		m_robotDrive.mecanumDrive_Cartesian(xAxis, yAxis, twist,0);
	}
	@Override
	public void testPeriodic() {
	}
}

