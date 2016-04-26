
import lejos.nxt.*;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.nxt.TouchSensor;

/* 
Assingment 2 - Mobile Robotics
Pedro Foltran - D14128455
Yuri Anfrisio Reis - D15124347
 */
 
//Behavior that handles the touch sensor
//This behavior makes the robot stop if the bumper is pressed
 
public class BumperBehavior implements Behavior {
	
	private boolean suppressed = false;
	public DifferentialPilot pilot;

	TouchSensor touch = new TouchSensor(SensorPort.S4);
	
	//Constructor receives the pilot
	public BumperBehavior(DifferentialPilot pilot){
		this.pilot = pilot;
	}

	@Override
	public boolean takeControl() {		//Wants to take control only if the touch sensor is pressed
		if(touch.isPressed()){
			return true;
		}
		return false;
	}

	@Override
	public void action() {
		LCD.drawString("Stop - Bump", 0, 0);
		pilot.stop();					//Stopping is the only action on this behavior
	    while( !suppressed ){			//The program is going to stop here and end
	    	Thread.yield();
	    }
		LCD.clear();
	}

	@Override
	public void suppress() {
		//suppressed = true;
	}

}