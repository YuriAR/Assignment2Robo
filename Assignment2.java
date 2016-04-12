import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.localization.*;
import lejos.geom.Point;
import lejos.robotics.navigation.*;
import lejos.nxt.LCD;
import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.*;

/* 
Assingment 2 - Mobile Robotics
Pedro Foltran - D14128455
Yuri Anfrisio Reis - D15124347
 */

//Main control class

public class Assignment2 {
	
	public static void main(String[] args) {
		LCD.drawString("Assignment 2", 0, 0);
		Button.waitForAnyPress();							
		LCD.clear();
		
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
		DifferentialPilot pilot = new DifferentialPilot(2.25f, 5.5f, Motor.A, Motor.B);
		PoseProvider pp = new OdometryPoseProvider(pilot);
		//Pose pose1 = pp.getPose();
		//Point location1 = pose1.getLocation();
		//LCD.drawString(location1.toString(), -10, 0);
		//pilot.travel(30);
		for(int i=0; i<4; i++){
			pilot.forward();
			while(sonic.getDistance() < 20){
				
			}
			
		}
		
		pilot.(75);
		Pose pose2 = pp.getPose();
		Point location2 = pose2.getLocation();
		LCD.clear();
		LCD.drawString(location2.toString(), -15, 0);
		Button.waitForAnyPress();
		//Behavior b1 = new ClapBehavior();
		//Behavior b2 = new Turn90LightBehavior();
		//Behavior b3 = new UltrasonicSensorBehavior();
		//Behavior b4 = new BumperBehavior();
		//Behavior b5 = new MoveForwardBehavior();
	    //Behavior [] bArray = {b1,b5,b2,b3,b4};				//Setting behavior priority.
	    //Arbitrator arby = new Arbitrator(bArray);
	    //arby.start();
	}
	
	//Returns the value of the control variable
	//public static boolean getClap(){
	//	return clapped;
	//}
	
	//Sets the control variable to true
	//public static void setClap(){
	//	clapped = true;
	//}
}