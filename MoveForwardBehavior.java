import lejos.geom.Point;
import lejos.nxt.*;
import lejos.nxt.Sound;
import lejos.nxt.LightSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Pose;

/* 
Assingment 2 - Mobile Robotics
Pedro Foltran - D14128455
Yuri Anfrisio Reis - D15124347
 */
 
//Behavior that handles the moving forward task
//This behavior moves the robot forward until it gets supressed (always wants control)
//Also, it beeps if its moving on carpet

public class MoveForwardBehavior implements Behavior {
	
	private boolean suppressed = false;
	public DifferentialPilot pilot;
	public OdometryPoseProvider pp;
	Pose pose;
	Point start;

	public static int distance;
	public static int leftToMove;
	public static int direction = 0;
	public static int toMove;

	LightSensor light = new LightSensor(SensorPort.S2);

	public MoveForwardBehavior(DifferentialPilot pilot, int distance, OdometryPoseProvider pp){
		this.pilot = pilot;
		this.distance = distance;
		this.pp = pp;
		this.toMove = distance;
		this.leftToMove = distance;
	}
	
	@Override
	public boolean takeControl() {				//This behavior will always want control 
		return true;
	}

	@Override
	public void action() {
		if(leftToMove >10){
			suppressed = false;
			pose = pp.getPose();
			start = pose.getLocation();		
			LCD.drawString("Moving", 0, 0);
			pilot.travel(toMove,true);			//alternative
			//pilot.forward();
			while( !suppressed && pilot.isMoving()){
				if(light.getNormalizedLightValue() > 600){ //needs testing (carpet thing... should beep and print carpet)
					LCD.clear();
					Sound.twoBeeps();
					LCD.drawString("Carpet", 0, 0);
				}
				else{
					LCD.clear();
					LCD.drawString("Moving", 0, 0);
				} 
				Thread.yield();
			}
			if(direction%2 == 0){
				pilot.rotate(90);
				pilot.travel(7,false);
				pilot.rotate(90);
				direction++;
			}
			else{
				pilot.rotate(-90);
				pilot.travel(7,false);
				pilot.rotate(-90);
				direction++;
			}
			leftToMove = distance - 7;								
			LCD.clear();
		}
		else{
			LCD.drawString("No room to move", 0, 0);
			while( !suppressed){
				Thread.yield();
			}
		}
	}

	@Override
	public void suppress() {
		//update distance variable if interupted
		//static variable
		//get the pose and see how much it moved, and calculate whats left when the behavior takes control again
		pose = pp.getPose();
		int distanceDone = Math.round(pose.distanceTo(start));
		toMove = toMove - distanceDone;
		
		if(toMove == 0){
			toMove = distance;
		}
		suppressed = true;
	}

}