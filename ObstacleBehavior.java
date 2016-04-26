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

public class ObstacleBehavior implements Behavior {

	public int wallDistance;
	public DifferentialPilot pilot;
	public OdometryPoseProvider pp;

	public UltrasonicSensor sonic;
	
	public ObstacleBehavior(DifferentialPilot pilot, OdometryPoseProvider pp, int wallDistance, UltrasonicSensor sonic){
		this.pp = pp;
		this.pilot = pilot;
		this.sonic = sonic;
		this.wallDistance = wallDistance;
	}
	
	@Override
	public boolean takeControl() {		
		Pose pose = pp.getPose();
		//Point current = pose.getLocation();	
		Point start = MoveForwardBehavior.start;
		if(start == null){
			return false;
		}
		if(sonic.getDistance() < 30 && pose.distanceTo(start)+31 < wallDistance){	//+5 for errors
			return true;
		}
		else{								  
			return false;
		}
	}

	@Override
	public void action() {
		LCD.drawString("Avoiding obstacle", 0, 0);
		Delay.msDelay(1000);
		//Pose pose = pp.getPose();
		//Point current = pose.getLocation(); //print this and the finish position to see how much it moved in the line, to hardcode/update the move behavior
		pilot.rotate(90);
		pilot.arc(-7, 180, false);	//to test
		pilot.rotate(90);
		MoveForwardBehavior.toMoveForward = MoveForwardBehavior.toMoveForward - 16;


		LCD.clear();
	}

	@Override
	public void suppress() {	
		pilot.stop();
	}

}