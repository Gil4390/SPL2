package bgu.spl.mics;

import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.services.ConferenceService;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.application.services.TimeService;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private HashMap<Class<? extends Message>, Queue<MicroService>> eventsBroadcast_subscribe; // string = type event

	private HashMap<String, Queue<Event>> microService_queues; // string = microservice name

	private Stack<StudentService> Students;

	private Stack<List<String>> conferenceEvent;

	private TimeService timeService;

	private ConferenceService conference;

	private MessageBusImpl() {
		//TODO singelton
	}

	private static class MessageBusImplHolder{
		private static MessageBusImpl messageBusImplInstance = new MessageBusImpl();
	}

	public static MessageBusImpl getInstance() {
		return MessageBusImpl.MessageBusImplHolder.messageBusImplInstance;
	}

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
	 * @post microService_queues[m].size() = @pre microService_queues[m].size()-1
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
	public boolean isSubscribed(MicroService s, Message m){
		return false;
	}

	/**
	 * the function check if s the microService is registered
	 * <p>
	 * @return true if already registered or false
	 * @param s the MicroService
	 */
	public boolean isRegistered(MicroService s){
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

	public HashMap<Class<? extends Message>, Queue<MicroService>> getEventsBroadcast_subscribe() {
		return eventsBroadcast_subscribe;
	}

	public HashMap<String, Queue<Event>> getMicroService_queues() {
		return microService_queues;
	}
}
