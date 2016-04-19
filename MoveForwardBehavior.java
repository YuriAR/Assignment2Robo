import lejos.geom.Point;
import lejos.nxt.*;
import lejos.nxt.Sound;
import lejos.nxt.LightSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Pose;
import lejos.nxt.Button;

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

	public static int distanceTotal;
	public static int leftToMoveSideways;
	public static int direction = 0;
	public static int toMoveForward;

	LightSensor light = new LightSensor(SensorPort.S2);

	public MoveForwardBehavior(DifferentialPilot pilot, int distanceTotal, int distanceSide, OdometryPoseProvider pp){
		this.pilot = pilot;
		this.distance = distanceTotal;
		this.pp = pp;
		this.toMoveForward = distanceTotal;
		this.leftToMoveSideways = distanceSide;
	}
	
	@Override
	public boolean takeControl() {				//This behavior will always want control 
		return true;
	}

	@Override
	public void action() {
		if(leftToMoveSideways > 10){
			suppressed = false;
			pose = pp.getPose();
			start = pose.getLocation();		
			LCD.drawString("Moving", 0, 0);


			pilot.forward();
			while(!supressed && toMoveForward > 0){
				if(light.getNormalizedLightValue() < 600){ //needs testing (carpet thing... should beep and print carpet)
					LCD.clear();
					Sound.twoBeeps();
					LCD.drawString("Carpet", 0, 0);
				}
				else{
					LCD.clear();
					LCD.drawString("Moving", 0, 0);
				} 
				toMoveForward = toMoveForward - pilot.getMovementIncrement();
				Thread.yield();
			}
			pilot.stop();

			if(toMoveForward <= 0){
				toMoveForward = distanceTotal;
			}

			if(direction%2 == 0 && !suppressed){
				pilot.rotate(90);
				pilot.travel(7,false);
				pilot.rotate(90);
				direction++;
				leftToMoveSideways = leftToMoveSideways - 7;	
			}
			else if (direction%2 != 0 && !suppressed){
				pilot.rotate(-90);
				pilot.travel(7,false);
				pilot.rotate(-90);
				direction++;
				leftToMoveSideways = leftToMoveSideways - 7;	
			}
			LCD.clear();
		}
		else{
			LCD.drawString("No room left - END", 0, 0);
			Button.waitForAnyPress();
			while(true){
				//Thread.yield();
			}
		}
	}

			// pilot.travel(toMoveForward,true);			//alternative
			// //pilot.forward();
			// while( !suppressed && pilot.isMoving()){
			// 	if(light.getNormalizedLightValue() < 600){ //needs testing (carpet thing... should beep and print carpet)
			// 		LCD.clear();
			// 		Sound.twoBeeps();
			// 		LCD.drawString("Carpet", 0, 0);
			// 	}
			// 	else{
			// 		LCD.clear();
			// 		LCD.drawString("Moving", 0, 0);
			// 	} 
			// 	Thread.yield();
			// 	if(direction%2 == 0 && !pilot.isMoving() && !suppressed){
			// 		pilot.rotate(90);
			// 		pilot.travel(7,false);
			// 		pilot.rotate(90);
			// 		direction++;
			// 		leftToMove = leftToMove - 7;	
			// 	}
			// 	else if (direction%2 != 0 && !pilot.isMoving() && !suppressed){
			// 		pilot.rotate(-90);
			// 		pilot.travel(7,false);
			// 		pilot.rotate(-90);
			// 		direction++;
			// 		leftToMove = leftToMove - 7;	
			// 	}
			// }
/* 			if(direction%2 == 0 && !suppressed){
				pilot.rotate(90);
				pilot.travel(7,false);
				pilot.rotate(90);
				direction++;
				leftToMove = leftToMove - 7;	
			}
			else if (direction%2 != 0 && !suppressed){
				pilot.rotate(-90);
				pilot.travel(7,false);
				pilot.rotate(-90);
				direction++;
				leftToMove = leftToMove - 7;	
			} */						
			
		//}
	//}

	@Override
	public void suppress() {
		//update distance variable if interupted
		//static variable
		//get the pose and see how much it moved, and calculate whats left when the behavior takes control again
		//Button.waitForAnyPress();
		pilot.stop();
		pose = pp.getPose();
		//int distanceDone = Math.round(pose.distanceTo(start));
		int distanceDone = Math.round(pilot.getMovementIncrement());
		toMoveForward = toMoveForward - distanceDone;
		
		if(toMoveForward == 0){
			toMoveForward = distanceTotal;
		}
		suppressed = true;
	}

}