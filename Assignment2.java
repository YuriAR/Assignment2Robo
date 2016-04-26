import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.localization.*;
import lejos.geom.Point;
import lejos.robotics.navigation.*;
import lejos.robotics.localization.OdometryPoseProvider;
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
//This class is going to control the recognition lap, and start the arbitrator with 3 behaviors

public class Assignment2 {
	
	public static void main(String[] args) {
		LCD.drawString("Assignment 2", 0, 0);
		Button.waitForAnyPress();							
		LCD.clear();
		
		//Array that saves the size of each wall
		float sides[] = new float[4];

		//Starting point of each wall
		Point start;

		//Allows us to get the current location (point) of the robot
		Pose pose;
		
		//Sensors to be used here and inside the behaviors
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
		DifferentialPilot pilot = new DifferentialPilot(2.25f, 4.4f, Motor.A, Motor.B);
		OdometryPoseProvider pp = new OdometryPoseProvider(pilot);
		LightSensor light = new LightSensor(SensorPort.S2);

		//Setting slower speed to try to minimize erros
		pilot.setRotateSpeed(50);
		pilot.setTravelSpeed(8);

		//Repeating this for each wall
		for(int i=0; i<4; i++){

			//Gets the starting point
			pose = pp.getPose();
			start = pose.getLocation();

			//Starts the movement
			pilot.forward();

			//Waits until the sonar detects a wall
			while(sonic.getDistance() > 22){
				//do nothing
			}
			//Stop the movement
			pilot.stop();

			//Gets the final pose and calculates the distance to the starting point
			pose = pp.getPose();
			sides[i] = pose.distanceTo(start);

			//Rotates 90 degrees
			pilot.rotate(90);
		}

		//We consider the distance it will have to move in paralel lines to be the first wall
		int distanceForward = Math.round(sides[0]);

		//The second wall will be the lenght of the room horizontally
		int distanceSide = Math.round(sides[1]);

		//Instanciating behaviors with custom constructors
		Behavior b0 = new MoveForwardBehavior(pilot, distanceForward, distanceSide, pp, light.getNormalizedLightValue());
		Behavior b1 = new ObstacleBehavior(pilot,pp,distanceForward, sonic);
		Behavior b2 = new BumperBehavior(pilot);

		Behavior [] bArray = {b0,b1,b2};				//Setting behavior priority

	    Arbitrator arby = new Arbitrator(bArray);		//Starting the arbitrator
	    arby.start();
	}
}
