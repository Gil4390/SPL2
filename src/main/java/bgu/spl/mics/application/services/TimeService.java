package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	private int TickCount;
	private int speed;
	private int duration;
	private TickBroadcast tickBroadcast;

	public TimeService(int speed, int duration) {
		super("TimeService");
		this.speed = speed;
		this.duration = duration;
	}

	@Override
	protected void initialize() {
		this.TickCount = 1;
		tickBroadcast = new TickBroadcast();
		act();
		
	}

	private void act(){
		while(TickCount < duration){


			TickCount++;
		}
	}
}
