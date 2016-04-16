
import lejos.nxt.*;
import lejos.nxt.Sound;
import lejos.nxt.LightSensor;
import lejos.robotics.subsumption.Behavior;
import lejos.robotics.navigation.DifferentialPilot;

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

	//public static float distance;

	LightSensor light = new LightSensor(SensorPort.S2);

	public MoveForwardBehavior(DifferentialPilot pilot){
		this.pilot = pilot;
	}
	
	@Override
	public boolean takeControl() {				//This behavior will always want control 
		return true;
	}

	@Override
	public void action() {
		suppressed = false;								//Moves until supressed
		LCD.drawString("Moving", 0, 0);
		//pilot.travel(x,true);			//alternative
		pilot.forward();
	    while( !suppressed )
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
		pilot.stop();									//Stops moving before changing to other behaviors
		LCD.clear();
	}

	@Override
	public void suppress() {
		//update distance variable if interupted
		//static variable
		//get the pose and see how much it moved, and calculate whats left when the behavior takes control again
		suppressed = true;
	}

}