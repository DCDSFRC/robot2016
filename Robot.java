package org.usfirst.frc.team835.robot;

import edu.wpi.cscore.*;

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
import java.util.Arrays;
import java.util.Collections;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
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
    Joystick whiteR, whiteL, black;
    int autoLoopCounter;
	double winchSpeed, scissorsSpeed, leftCo, rightCo;
    TalonSRX leftM, rightM, winch, scissors;
    NetworkTable table;
    Thread cam;
    public void robotInit() {
    	
//    	CameraServer.getInstance().addAxisCamera("10.8.35.4");
    	cam = new Thread(() -> {
    		AxisCamera camera = CameraServer.getInstance().addAxisCamera("10.8.35.4");
    		//camera.setResolution(320, 240);
    		CvSink cvSink = CameraServer.getInstance().getVideo();
    		CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 320, 240);
    		
    		Mat source = new Mat();
    		Mat output = new Mat();
    		System.out.println("asdfasdf");
    		while(!Thread.interrupted()) {
    			cvSink.grabFrame(source);
    			Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
    			outputStream.putFrame(output);
    			System.out.println("in thread");
    		}
    	});
    	
    	System.out.println("done");
    	table = NetworkTable.getTable("GRIP/myContoursReport");
    	
//      leftM = new TalonSRX(0);
//      rightM = new TalonSRX(1);
//      leftM.setInverted(true);
//      rightM.setInverted(true);
//      whiteR = new Joystick(0);
//      myRobot = new RobotDrive(leftM, rightM);
        
//    	SmartDashboard.putNumber("myNum", autoLoopCounter);
//      winch = new TalonSRX(2);
//      scissors = new TalonSRX(3);
//      black = new Joystick(1);
//      whiteL = new Joystick(2);     
    }
    

    public void showNetworkTables(){
    	double[] defaultValue = new double[0];
		double[] areas = table.getNumberArray("area", defaultValue);
		for( double area : areas){
			System.out.print(area + " ");
		}
		System.out.println();
    }
 
    public void autonomousInit() {
//        autoLoopCounter = 0;
    	cam.start();
    }
    
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
//    	showNetworkTables();
//        if (autoLoopCounter < 100) {
//            myRobot.drive(0.30, 0.0);
//            autoLoopCounter++;
//        } else {
//            myRobot.drive(0.0, 0.0);
//        }
//        Timer.delay(0.05);
    }

    /**
     * This function is called periodically during operator control
     */
    public void disabledInit() {
    	System.out.print("asdf");
    }
    public void teleopInit() {
    	//The teleopInit method is called once each time the robot enters teleop mode
    	
//    	SmartDashboard.putNumber("whiteR.getY()", whiteR.getY());
//    	SmartDashboard.putNumber("whiteR.getZ()", whiteR.getZ());
//    	SmartDashboard.putNumber("leftM.getPosition()", leftM.getPosition());
//    	SmartDashboard.putNumber("rightM.getPosition()", rightM.getPosition());
    }

    public void teleopPeriodic() { //The teleopPeriodic method is entered each time the robot receives a packet instructing it to be in teleoperated enabled mode
//    	showNetworkTables();
//    	myRobot.arcadeDrive(whiteR);
//    	myRobot.tankDrive(black, whiteL);
    	System.out.println("periodic");
    }

    public void testPeriodic() {
        LiveWindow.run();
    }
}
