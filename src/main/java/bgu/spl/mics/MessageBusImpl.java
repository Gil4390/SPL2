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
	HashMap<MicroService, Queue<Event>> GPU_TrainModel;
	int GPU_TrainModel_Index;

	HashMap<MicroService, Queue<Event>> GPU_TestModel;
	int GPU_TestModel_Index;

	Stack<StudentService> Students;

	Stack<List<String>> conferenceEvent;

	TimeService timeService;

	ConferenceService conference;


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
	 * @pre //TODO
	 * @post
	 */
	@Override
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	/**
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
	 * @pre  this.isRegistered(m) == true;
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

	private boolean isSubscribed(MicroService s, Message m){
		return false;
	}

	private boolean isRegistered(MicroService s){
		return false;
	}

	private MicroService getNextMicroService(Message m){
		return null;
	}
}
