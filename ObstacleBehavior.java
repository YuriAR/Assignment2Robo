import lejos.nxt.LightSensor;
import lejos.nxt.*;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Pose;

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
		//Pose pose = pp.getPose();
		//Point current = pose.getLocation();			  
		if(sonic.getDistance() < 22 && pilot.getMovementIncrement()+5 < wallDistance){	//+5 for errors
			return true;
		}
		else{								  
			return false;
		}
	}

	@Override
	public void action() {
		LCD.drawString("Avoiding obstacle", 0, 0);

		//Pose pose = pp.getPose();
		//Point current = pose.getLocation(); //print this and the finish position to see how much it moved in the line, to hardcode/update the move behavior

		pilot.arc(45, 180);	//to test


		LCD.clear();
	}

	@Override
	public void suppress() {	
		pilot.stop();
	}

}