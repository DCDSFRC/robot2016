package org.usfirst.frc.team835.robot;

import java.util.Arrays;

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
	int autoLoopCounter, Xres, Yres;
	double[] values;
	TalonSRX leftM, rightM;
	NetworkTable table;

	public Robot() {
		// NetworkTable.setTeam(835);
		table = NetworkTable.getTable("GRIP/targets");
	}

	public void robotInit() {
		CameraServer.getInstance().addAxisCamera("Axis Camera", "10.8.35.4");
		Xres = 640;
		Yres = 480;
		// CameraServer.getInstance().getVideo();
		// CameraServer.getInstance().putVideo("Stream", 640, 480);

		leftM = new TalonSRX(0);
		rightM = new TalonSRX(1);

		white = new Joystick(0);

		myRobot = new RobotDrive(leftM, rightM);

		 NetworkTable.setTeam(835);
		 table = NetworkTable.getTable("GRIP/targets");

		setTables(table);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousInit() {
		autoLoopCounter = 0;
	}

	private double x;
	double[] xvalues, areas, heights, widths;

	public void autonomousPeriodic() {
		setTables(table);
		areas = table.getNumberArray("area", new double[0]);
		if (areas.length == 0) {
			return;
		}
		heights = table.getNumberArray("height", new double[1]);
		xvalues = table.getNumberArray("centerX", new double[0]);
		if (xvalues.length == 0) {
			x = Xres / 2;
		} else {
			x = xvalues[0];
		}
		SmartDashboard.putNumber("actualX", x);
		double curve = curveToCenter(x);
		SmartDashboard.putNumber("PreCurve", curve);
		curve *= -1.55;
		SmartDashboard.putNumber("PostCurve", curve);
		double distance = 4.15/12 * Yres / (2 * heights[0] * Math.tan(54*Math.PI/180));
		SmartDashboard.putNumber("distance", distance);

		if (autoLoopCounter < 500 || true) {
			myRobot.arcadeDrive(0.0, curve);
			autoLoopCounter++;
		}
	}

	double curveToCenter(double pos) {
		
		if (Math.abs(pos - Xres / 2) > 20) {
			return (pos - Xres / 2) / Xres;
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
		LiveWindow.run();
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
