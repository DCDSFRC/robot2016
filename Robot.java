package org.usfirst.frc.team835.robot;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;
import java.util.Arrays;

import edu.wpi.cscore.AxisCamera;

/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	RobotDrive myRobot;
	Joystick white;
	int autoLoopCounter;
	double[] values;
	TalonSRX leftM, rightM;
	NetworkTable table;
	
	private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 480;
	private VisionThread visionThread;
	private double centerX = 0.0;
	private final Object imgLock = new Object();
	private AxisCamera camera;
	

	private double x;
	double[] xvalues, areas;
	
	
	public Robot() {
		// NetworkTable.setTeam(835);
		table = NetworkTable.getTable("GRIP/targets");
	}

	public void robotInit() {

		camera = new AxisCamera("Axis Camera", "10.8.35.4");
		
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		
		visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
	        if (!pipeline.filterContoursOutput().isEmpty()) {
	        	System.out.println("+"+pipeline.filterContoursOutput().get(0));
	        	System.out.println(pipeline.filterContoursOutput().get(1));
	            Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
	            synchronized (imgLock) {
	                centerX = r.x + (r.width / 2);
	            }
	        }
	        else {
	        	System.out.print("fail\n");
	        }
	    });
		visionThread.start();
		
		
//		CameraServer.getInstance().addAxisCamera("Axis Camera", "10.8.35.4");
		CameraServer.getInstance().addCamera(camera);
		
		// CameraServer.getInstance().getVideo();
		// CameraServer.getInstance().putVideo("Stream", 640, 480);

		leftM = new TalonSRX(0);
		rightM = new TalonSRX(1);

		white = new Joystick(0);

		myRobot = new RobotDrive(leftM, rightM);

		// NetworkTable.setTeam(835);
		// table = NetworkTable.getTable("GRIP/targets");

//		setTables(table);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousInit() {
		autoLoopCounter = 0;
	}


	public void autonomousPeriodic() {
		double centerX;
		System.out.println("asdf");
		SmartDashboard.putNumber("auto", autoLoopCounter);
		autoLoopCounter++;
		synchronized (imgLock) {
			centerX = this.centerX;
		}
		double turn = centerX - (IMG_WIDTH / 2);
		myRobot.arcadeDrive(0, 0);
		System.out.printf("turn: %s\n", turn);
		System.out.printf("centerX: %s\n", centerX);

//		setTables(table);
//		areas = table.getNumberArray("area", new double[0]);
//		if (areas.length == 0) {
//			return;
//		}
//		xvalues = table.getNumberArray("centerX", new double[0]);
//		if (xvalues.length == 0) {
//			x = IMG_WIDTH / 2;
//		} else {
//			x = xvalues[0];
//		}
//		SmartDashboard.putNumber("actualX", x);
//		double curve = curveToCenter(x);
//		SmartDashboard.putNumber("PreCurve", curve);
//		curve *= -1.55;
//		SmartDashboard.putNumber("PostCurve", curve);
//		
//		if (autoLoopCounter < 500 || true) {
//			myRobot.arcadeDrive(0.0, curve);
//			autoLoopCounter++;
//		}
	}

	double curveToCenter(double pos) {
		
		if (Math.abs(pos - IMG_WIDTH / 2) > 20) {
			return (pos - IMG_WIDTH / 2) / IMG_WIDTH;
		} else {
			return 0.0;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopInit() {// The teleopInit method is called once each time
								// the robot enters teleop mode

	}

	public void teleopPeriodic() { // The teleopPeriodic method is entered each
									// time the robot receives a packet
									// instructing it to be in teleoperated
									// enabled mode
		setTables(table);
		myRobot.arcadeDrive(-white.getY(), -white.getZ());

	}

	public void testPeriodic() {

		
	}

	void setTables(NetworkTable... tables) {
		for (NetworkTable t : tables) {
			// System.out.println(t.getKeys());
			for (String k : t.getKeys()) {
				values = t.getNumberArray(k, new double[0]);
				SmartDashboard.putString(k, Arrays.toString(values).substring(1, Arrays.toString(values).length() - 1));
			}
		}
	}
}
