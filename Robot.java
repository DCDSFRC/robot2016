package org.usfirst.frc.team835.robot;

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
    Joystick whiteR, whiteL, black;
    int autoLoopCounter;
	double winchSpeed, scissorsSpeed, leftCo, rightCo;
    TalonSRX leftM, rightM, winch, scissors;
    double[] contours, defaultValue;
    NetworkTable table;
    public void robotInit() {
    	CameraServer.getInstance().startAutomaticCapture();
    	CameraServer.getInstance().getVideo();
    	CameraServer.getInstance().putVideo("Stream", 640, 480);
    	
    	SmartDashboard.putNumber("myNum", autoLoopCounter);

        leftM = new TalonSRX(0);
        rightM = new TalonSRX(1);
//        winch = new TalonSRX(2);
//        scissors = new TalonSRX(3);
        leftM.setInverted(true);
        rightM.setInverted(true);
        whiteR = new Joystick(0);
//        black = new Joystick(1);
//        whiteL = new Joystick(2);
        myRobot = new RobotDrive(leftM, rightM);
    	
        table = NetworkTable.getTable("GRIP/myContoursReport");
    	defaultValue = new double[0];
//    	while(true){
    		double[] areas = table.getNumberArray("area", defaultValue);
    		System.out.println("Areas: ");
    		for( double area : areas){
    			System.out.print(area + " ");
    		}
    		System.out.println();
    		Timer.delay(1.0);
//    	}
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit() {
        autoLoopCounter = 0;
    }

    public void autonomousPeriodic() {
        if (autoLoopCounter < 100) {
            myRobot.drive(0.30, 0.0);
            autoLoopCounter++;
        } else {
            myRobot.drive(0.0, 0.0);
        }
        Timer.delay(0.05);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit() {//The teleopInit method is called once each time the robot enters teleop mode
    	
//    	SmartDashboard.putNumber("whiteR.getY()", whiteR.getY());
//    	SmartDashboard.putNumber("whiteR.getZ()", whiteR.getZ());
//    	SmartDashboard.putNumber("leftM.getPosition()", leftM.getPosition());
//    	SmartDashboard.putNumber("rightM.getPosition()", rightM.getPosition());
    }

    public void teleopPeriodic() { //The teleopPeriodic method is entered each time the robot receives a packet instructing it to be in teleoperated enabled mode
    	myRobot.arcadeDrive(whiteR);
    	
////    	contours = table.getNumberArray("area",defaultValue);
////        for(int i=0;i<contours.length;i++)
////        {
////        	SmartDashboard.putNumber("Contour "+ (i+1) +": ", contours[i]);
////        }
////    	table = NetworkTable.getTable("GRIP/myContoursReport");
////    	SmartDashboard.putData((NamedSendable) table);
//    	
//    	myRobot.tankDrive(black, whiteL);
    	
    	
//    	if(whiteR.getRawButton(11)) winchSpeed = .5;
//    	else if(whiteR.getRawButton(9)) winchSpeed = .75;
//    	else if(whiteR.getRawButton(7)) winchSpeed = 1;
//        else winchSpeed = 0;														
//        winch.set(-winchSpeed);
//        
//        
//        if(whiteR.getRawButton(5)) scissorsSpeed = 1;                                             
//    	else if(whiteR.getRawButton(3)) scissorsSpeed = -1;
//        else scissorsSpeed = 0;
//        scissors.set(scissorsSpeed);
    }
    public void trigger()
    {
        myRobot.drive((black.getTrigger()?-0.10:-.15), 0.0);
    }
    public void testPeriodic() {
        LiveWindow.run();
    }
}
