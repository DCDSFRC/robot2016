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
enum robotState{
	LOST(0), FOUND(1);
	private int val;
	private robotState(int val){
		this.val = val;
	}
}

public class Robot extends IterativeRobot {

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	RobotDrive myRobot;
	Joystick white;
	int autoLoopCounter, xRes, yRes;
	double[] values;
	TalonSRX leftM, rightM;
	NetworkTable table;
	String tablepath = "GRIP/targets";

	public Robot() {
		// NetworkTable.setTeam(835);
		table = NetworkTable.getTable(tablepath);
	}

	public void robotInit() {
		CameraServer.getInstance().addAxisCamera("Axis Camera", "10.8.35.4");
		xRes = 640;
		yRes = 480;
		// CameraServer.getInstance().getVideo();
		// CameraServer.getInstance().putVideo("Stream", 640, 480);

		leftM = new TalonSRX(0);
		rightM = new TalonSRX(1);

		white = new Joystick(0);

		myRobot = new RobotDrive(leftM, rightM);

		NetworkTable.setTeam(835);
		table = NetworkTable.getTable(tablepath);

//		setTables(table);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousInit() {
		autoLoopCounter = 0;
	}

	double x, distance, power, curve = 0;
	double[] xvalues, areas, heights, widths;

	public void autonomousPeriodic() {
//		setTables(table);
		areas = table.getNumberArray("area", new double[0]);
		if (areas.length == 0) {
			myRobot.arcadeDrive(0, 0.7);
			return;
		}
		heights = table.getNumberArray("height", new double[0]);
		xvalues = table.getNumberArray("centerX", new double[0]);
		if (xvalues.length == 0) {
			x = xRes / 2;
		} else {
			x = xvalues[0];
		}
		curve = curveToCenter(x);
		SmartDashboard.putNumber("X", x);
		SmartDashboard.putNumber("PreCurve", curve);
		curve *= -Math.sqrt(2.0);
		if (curve > 0.5) {
			curve = 0.5;
		}
		if (curve < -0.5) {
			curve = -0.5;
		}
		if (heights.length != 0) {
			distance = 5 / 12 * yRes / (2 * heights[0] * Math.tan(54 * Math.PI / 180));
		} else {
			distance = 0;
		}

		if (distance > 0.5) {
			power = 5.0 / 9;
		} else {
			power = 0;
		}
		SmartDashboard.putNumber("PostCurve", curve);
		SmartDashboard.putNumber("distance", distance);
		SmartDashboard.putNumber("power", power);
		// power = 0;
		// curve = 0;
		myRobot.arcadeDrive(power, curve);
	}

	double curveToCenter(double pos) {
		if (Math.abs(pos - xRes / 2) > 20) {
			return (pos - xRes / 2) / xRes;
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
//		setTables(table);
		myRobot.arcadeDrive(-white.getY(), -white.getZ());

	}

	public void testPeriodic() {
		LiveWindow.run();
	}

	void sendTables(NetworkTable... tables) {
		for (NetworkTable t : tables) {
			// System.out.println(t.getKeys());
			for (String k : t.getKeys()) {
				values = t.getNumberArray(k, new double[0]);
				SmartDashboard.putString(k, Arrays.toString(values).substring(1, Arrays.toString(values).length() - 1));
			}
		}
	}

	void routine1() {

	}

	/**
	 * double tape. uses blobs
	 */
	void routine2() {

	}
}
