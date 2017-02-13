package org.usfirst.frc.team5892.robot;

import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
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


	
	NetworkTable table;
	
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
/*
	 *     - PWM 3 - Connected to front left drive motor
	 *     - PWM 7 - Connected to rear left drive motor
	 *     - PWM 8 - Connected to front right drive motor
	 *     - PWM 2 - Connected to rear right drive motor
	 */
	
	RobotDrive m_robotDrive = new RobotDrive(3, 7, 8, 2);
	Victor intake = new Victor(9);
	Victor agetator = new Victor(0);
	Victor shooter = new Victor(1);
	Joystick m_driveStick = new Joystick(1);
	
	public AnalogInput pidA = new AnalogInput(1);
    public AnalogInput pidB = new AnalogInput(2);
    //public int averageA = pidA.getAverageValue();
    //public int averageB = pidB.getAverageValue();
	public int rawA = pidA.getValue();
	public int rawB = pidB.getValue();
	
	@Override
	public void robotInit(){
		
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(320, 240);
        
        CameraServer.getInstance().getVideo();
        CameraServer.getInstance().putVideo("Blur", 320, 240);
		table = NetworkTable.getTable("datatable");
		
		
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
		//network table
		double x = 0;
		double y = 0;
		Timer.delay(0.25);
		table.putNumber("X", x);
		table.putNumber("Y", y);
		x += 0.05;
		y += 1.0;
		
		//PID motor
		 SmartDashboard.putNumber("PID A", rawA);
         SmartDashboard.putNumber("PID B", rawB);
         
        //intake 
         if(m_driveStick.getRawButton(1)){
        	 intake.set(-1);
         }else{
        	 intake.set(0);
         }
        
         /*
         if(Math.abs(m_driveStick.getRawAxis(2)) >= .2){
        	 double Ltrigger = m_driveStick.getRawAxis(2);
        	 shooter.set(Ltrigger);
         }else{
        	 shooter.set(0);
         }
         
         if(Math.abs(m_driveStick.getRawAxis(3)) >= .2){
        	 shooter.set(1);
         }else{
        	 shooter.set(0);
         }
  */     
      
         
		if(m_driveStick.getRawButton(2)){
				shooter.set(.55);	
			}else{
				shooter.set(0);
			}
		

		//agetator
		double rng = Math.ceil(Math.random()*10);
		if(rng>=6){
			agetator.set(.5);
			Timer.delay(5);
		}else{
			agetator.set(-5);
			Timer.delay(5);
		}
    	   
		//drive
		double xAxis;
		double yAxis;
		double twist = 0;
		double m = 0.8;
		
		if (m_driveStick.getRawButton(5)){
			m = 1;
		}
			if(Math.abs(m_driveStick.getX()) >= 0.18){
				xAxis = m_driveStick.getX() * m;
			}else{
				xAxis = 0;			
			}
			if(Math.abs(m_driveStick.getRawAxis(4)) >= 0.18){
				yAxis = m_driveStick.getRawAxis(4) * m;
			}else{
				yAxis = 0;			
			}
			if(Math.abs(m_driveStick.getRawAxis(1)) >= 0.18){
				if (m_driveStick.getRawAxis(1) >= 0.18){
					//twist = Math.pow(m_driveStick.getRawAxis(1), 2) * m;
					twist = Math.sin(3 * Math.PI *m_driveStick.getRawAxis(1)+ 2); 
				}else if (m_driveStick.getRawAxis(1) <= -0.18){
					//twist = -(Math.pow(m_driveStick.getRawAxis(1), 2)) * m;
					twist = -Math.sin(3 * Math.PI *m_driveStick.getRawAxis(1)+ 2);
					if (m_driveStick.getRawAxis(1) == -1){
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

	/*
	@Override
	public void testPeriodic{
	}
	*/
	}
}

