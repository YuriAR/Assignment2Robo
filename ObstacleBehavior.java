import lejos.nxt.LightSensor;
import lejos.nxt.*;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Pose;
import lejos.geom.Point;
import lejos.util.Delay;

/* 
Assingment 2 - Mobile Robotics
Pedro Foltran - D14128455
Yuri Anfrisio Reis - D15124347
 */

//Behavior that handles avoiding the obstacles on the robot's path
//The behavior will make the robot avoid an obstacle by turning 90 degrees and going backwards in a curved path. 
//At the end of the curved path, it will turn 90 degrees again to return to the straigh path it was going before

public class ObstacleBehavior implements Behavior {

	public int wallDistance;			//distance to the wall
	public DifferentialPilot pilot;
	public OdometryPoseProvider pp;		//Pose provider responsible to give us the current position of the robot

	public UltrasonicSensor sonic;
	
	//Constructor (the wall distance is measured in the recognition lap)
	public ObstacleBehavior(DifferentialPilot pilot, OdometryPoseProvider pp, int wallDistance, UltrasonicSensor sonic){
		this.pp = pp;
		this.pilot = pilot;
		this.sonic = sonic;
		this.wallDistance = wallDistance;
	}
	
	@Override
	public boolean takeControl() {		
		Pose pose = pp.getPose();
		Point start = MoveForwardBehavior.start;		//Starting point of the current movement, present in the MoveForwardBehavior class
		if(start == null){								//If the starting point is null, MoveForwardBehavior still did not take control
			return false;
		}
		//Calculates the distance to the starting point of the current movement, adds 31 (sonar distance +1 for erros) and checks if its smaller than the wallDistance
		if(sonic.getDistance() < 30 && pose.distanceTo(start)+31 < wallDistance){
			return true;
		}
		else{								  
			return false;
		}
	}

	@Override
	public void action() {
		LCD.drawString("Avoiding obstacle", 0, 0);

		pilot.rotate(90);			//Rotates 90 degrees
		pilot.arc(-7, 180, false);	//Moves backwards in an arc measuring 180 units, with 14 units of diameter
		pilot.rotate(90);			//Rotates 90 degrees again to return to the straght path

		//Updates how much is still left to move (diameter plus 6 for erros). Value obtained through testing
		MoveForwardBehavior.toMoveForward = MoveForwardBehavior.toMoveForward - 20;

		LCD.clear();
	}

	@Override
	public void suppress() {	
		pilot.stop();
	}

}