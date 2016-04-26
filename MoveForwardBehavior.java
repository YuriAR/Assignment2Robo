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
	LightSensor light = new LightSensor(SensorPort.S2);

	//Allows us to get the current location (point) of the robot
	Pose pose;

	//Keeps the current move stating point
	public static Point start;

	public static int distanceTotal;   //distanceTotal will be kept unchanged and used to reset the movement
	public static int leftToMoveSideways;	//distance left horizontally
	public static int direction = 0;	//Variable to guide the robot to turn right or to turn left
	public static int toMoveForward;   //toMoveForward will be changed to keep track of how much was moved
	public static int firstLight;		//Number representing the color of the floor where the robot started

	//Constructor
	//Distances are calculated in the recognition lap
	//the light is a variable that represents the number of the first reading of the floor color
	public MoveForwardBehavior(DifferentialPilot pilot, int distanceTotal, int distanceSide, OdometryPoseProvider pp, int light){
		this.pilot = pilot;
		this.distanceTotal = distanceTotal;	
		this.toMoveForward = distanceTotal;	
		this.leftToMoveSideways = distanceSide;
		this.pp = pp;
		this.firstLight = light;
	}
	
	@Override
	public boolean takeControl() {				//This behavior will always want control 
		return true;
	}

	@Override
	public void action() {
		//The robot only moves if there is still room sideways (7 units), with an error of 3 units
		if(leftToMoveSideways > 10){
			suppressed = false;

			//Gets initial position
			pose = pp.getPose();
			start = pose.getLocation();	

			LCD.drawString("Moving", 0, 0);

			//Starts the movement			
			pilot.forward();

			//The robot will move until the distance is 0 or the behavior is supressed
			while(!suppressed && toMoveForward > 0){
				if(light.getNormalizedLightValue() < firstLight){ //If the floor is darker than the first one, it will be a carpet
					LCD.clear();
					Sound.twoBeeps();
					LCD.drawString("Carpet", 0, 0);
				}
				else{
					LCD.clear();
					LCD.drawString("Moving", 0, 0);
				} 
				//Gets the pose to update how much it still has to move
				pose = pp.getPose();
				toMoveForward = distanceTotal - Math.round(pose.distanceTo(start));

				Thread.yield();
			}
			//If the behavior is suppressed or the movement finishes, it will stop the robot 
			pilot.stop();

			//Resets the control variable if the movement was completed
			if(toMoveForward <= 0){
				toMoveForward = distanceTotal;
			}

			//The robot will turn right twice if the behavior is not supressed (meaning it completed the whole move) and the counter is even
			if(direction%2 == 0 && !suppressed){
				pilot.rotate(90);
				pilot.travel(7,false);
				pilot.rotate(90);
				direction++;

				//Updates the position
				pose = pp.getPose();
				start = pose.getLocation();

				//Updates how much it still has to move sideways
				leftToMoveSideways = leftToMoveSideways - 7;	
			}
			//The robot will turn left twice if the behavior is not supressed (meaning it completed the whole move) and the counter is odd
			else if (direction%2 != 0 && !suppressed){
				pilot.rotate(-90);
				pilot.travel(7,false);
				pilot.rotate(-90);
				direction++;

				//Updates the position
				pose = pp.getPose();
				start = pose.getLocation();

				//Updates how much it still has to move sideways
				leftToMoveSideways = leftToMoveSideways - 7;
			}
			LCD.clear();
		}
		//If there is no more room sideways, the program stops
		else{
			LCD.drawString("No room left - END", 0, 0);
			Button.waitForAnyPress();
			while(true){
				//Thread.yield();
			}
		}
	}

	@Override
	public void suppress() {
		suppressed = true;

		//Stops the movement and updates it's position to make sure it only move the needed space when the behavior takes control again
		pilot.stop();
		pose = pp.getPose();
		int distanceDone = Math.round(pose.distanceTo(start));
		toMoveForward = distanceTotal - distanceDone;
		
		//Resets the move if it was finished when the behavior lost control
		if(toMoveForward <= 0){
			toMoveForward = distanceTotal;
		}
	}
}