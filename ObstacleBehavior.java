import lejos.nxt.LightSensor;
import lejos.nxt.*;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.DifferentialPilot;

/* 
Assingment 2 - Mobile Robotics
Pedro Foltran - D14128455
Yuri Anfrisio Reis - D15124347
 */

//Behavior that handles avoiding the obstacles on the robot's path

public class ObstacleBehavior implements Behavior {

	public int wallDistance;
	public DifferentialPilot pilot;
	public PoseProvider pp;

	UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
	
	public ObstacleBehavior(DifferentialPilot pilot, PoseProvider pp, int wallDistance){
		this.pp = pp;
		this.pilot = pilot;
	}
	
	@Override
	public boolean takeControl() {		
		Pose pose = pp.getPose();
		Point current = pose.getLocation();			  
		if(sonic.getDistance() < 20 && current+10 < wallDistance){
			return true;
		}
		else{								  
			return false;
		}
	}

	@Override
	public void action() {
		//Decide if its an object blocking the way or if is the wall
		LCD.drawString("Avoiding obstacle", 0, 0);
		pilot.arc(45, 180);

		LCD.clear();
	}

	@Override
	public void suppress() {	
		pilot.stop();
	}

}