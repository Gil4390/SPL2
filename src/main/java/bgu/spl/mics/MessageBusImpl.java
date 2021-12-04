package bgu.spl.mics;

import bgu.spl.mics.application.services.ConferenceService;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.application.services.TimeService;

import java.util.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private HashMap<MicroService, Queue<Event>> GPU_TrainModel;
	private int GPU_TrainModel_Index;

	private HashMap<MicroService, Queue<Event>> GPU_TestModel;
	private int GPU_TestModel_Index;

	private Stack<StudentService> Students;

	private Stack<List<String>> conferenceEvent;

	private TimeService timeService;

	private ConferenceService conference;


	/**
	 * @pre  this.isSubscribed(m,type) == false;
	 * @post this.isSubscribed(m,type) == true;
	 */
	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre  this.isSubscribed(m,type) == false;
	 * @post this.isSubscribed(m,type) == true;
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre e.result != null ;
	 * @post e.future.isDone = true;
	 * @post result = e.future.get();
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre none
	 * @post none
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre none
	 * @post none
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @pre  this.isRegistered(m) == false;
	 * @post this.isRegistered(m) == true;
	 */
	@Override
	public void register(MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * @post this.isRegistered(m) == false;
	 */
	@Override
	public void unregister(MicroService m) {
		// TODO Auto-generated method stub

	}

	/**
	 * @pre  this.isRegistered(m) == true;
	 * @post this.queue.size() = @pre this.queue.size()-1
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * the function check if this microService is subscribed for receiving a message, from message type
	 * <p>
	 * @return true if already subscribed or false
	 * @param s the MicroService
	 * @param m the Message
	 */
	private boolean isSubscribed(MicroService s, Message m){
		return false;
	}

	/**
	 * the function check if s the microService is registered
	 * <p>
	 * @return true if already registered or false
	 * @param s the MicroService
	 */
	private boolean isRegistered(MicroService s){
		return false;
	}

	/**
	 * the function return the next MicroService that will handle the event
	 * <p>
	 * @return the next MicroService that will handle the event
	 * @param m the MicroService
	 */
	private MicroService getNextMicroService(Message m){
		return null;
	}
}
