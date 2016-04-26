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

public class Assignment2 {
	
	public static void main(String[] args) {
		LCD.drawString("Assignment 2", 0, 0);
		Button.waitForAnyPress();							
		LCD.clear();
		
		float sides[] = new float[4];
		Point start;

		Pose pose;
		
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
		DifferentialPilot pilot = new DifferentialPilot(2.25f, 4.4f, Motor.A, Motor.B);
		OdometryPoseProvider pp = new OdometryPoseProvider(pilot);
		LightSensor light = new LightSensor(SensorPort.S2);

		pilot.setRotateSpeed(50);
		pilot.setTravelSpeed(8);		//test

		for(int i=0; i<4; i++){

			pose = pp.getPose();
			start = pose.getLocation();


			pilot.forward();

			while(sonic.getDistance() > 22){
				//do nothing
			}
			pilot.stop();

			pose = pp.getPose();
			sides[i] = pose.distanceTo(start);

			pilot.rotate(90);
		}

		
		int distanceForward = Math.round(sides[0]);
		int distanceSide = Math.round(sides[1]);

		Behavior b0 = new MoveForwardBehavior(pilot, distanceForward, distanceSide, pp, light.getNormalizedLightValue());
		Behavior b1 = new ObstacleBehavior(pilot,pp,distanceForward, sonic);
		Behavior b2 = new BumperBehavior(pilot);
		Behavior [] bArray = {b0,b1,b2};				//Setting behavior priority.
	    Arbitrator arby = new Arbitrator(bArray);
	    arby.start();
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
