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
		
		float sides[];
		Point start;
		//Point finish;
		float xStart;
		float xFinish;
		float yStart;
		float yFinish;
		float xWall;
		float yWall;

		Pose pose;
		
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
		DifferentialPilot pilot = new DifferentialPilot(2.25f, 5.5f, Motor.A, Motor.B);
		PoseProvider pp = new OdometryPoseProvider(pilot);
		//start = pose.getLocation();
		//LCD.drawString(location1.toString(), -10, 0);
		//pilot.travel(30);
		for(int i=0; i<4; i++){

			pose = pp.getPose();
			start = pose.getLocation();

			//xStart = pose.getX();
			//yStart = pose.getY();

			pilot.forward();

			while(sonic.getDistance() > 20){
				
			}
			pilot.stop();

			pose = pp.getPose();
			sides[i] = pose.distanceTo(start);

			//pose = pp.getPose();
			//xFinish = pose.getX();
			//yFinish = pose.getY();

			//xWall = xFinish - xStart;
			//yWall = yFinish - yStart;

			//if(xWall > yWall){			//Moviment in only one coordinate
			//	sides[i] = xWall;
			//}
			//else{
			//	sides[i] = yWall;
			//}



			pilot.rotate(75);
			
			//finish = pose.getLocation();
			//subtract finish - start for each side and store it
			//sides[i] = result
		}

		//Calculate wallDistance depending on the direction of the movement (horizontal or vertical in relation to the longest wall)
		
		Behavior b0 = new MoveForwardBehavior(pilot);
		Behavior b1 = new ObstacleBahavior(pilot,pp,wallDistance);
		Behavior b2 = new BumperBehavior(pilot);
		Behavior [] bArray = {b0,b1,b2};				//Setting behavior priority.
	    Arbitrator arby = new Arbitrator(bArray);
	    arby.start();
		
		
		//pilot.(75);
		//Pose pose2 = pp.getPose();
		//Point location2 = pose2.getLocation();
		//LCD.clear();
		//.drawString(location2.toString(), -15, 0);
		//Button.waitForAnyPress();
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
