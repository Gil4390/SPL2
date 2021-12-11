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

	private HashMap<Class<? extends Message>, Queue<MicroService>> event_subscribe; // string = type event
	private HashMap<Class<? extends Message>, LinkedList<MicroService>> Broadcast_subscribe;

	private HashMap<String, Queue<Message>> microService_queues; // string = microservice name

	//private Stack<StudentService> Students;

	//private Stack<List<String>> conferenceEvent;

	private TimeService timeService;

	private ConferenceService conference;

	private MessageBusImpl() {//todo create all event and broadcast in eventsBroadcast_subscribe
		//todo make this function a thread save
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
		event_subscribe.get(type).add(m);

	}

	/**
	 * @pre  this.isSubscribed(m,type) == false;
	 * @post this.isSubscribed(m,type) == true;
	 */
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		Broadcast_subscribe.get(type).add(m);
	}

	/**
	 * @pre e.result != null ;
	 * @post e.future.isDone = true;
	 * @post result = e.future.get();
	 */
	@Override
	public <T> void complete(Event<T> e, T result) {
		e.getFuture().resolve(result);
	}

	/**
	 * @pre none
	 * @post none
	 */
	@Override
	public void sendBroadcast(Broadcast b) {//todo notify the microservice that waits
		LinkedList<MicroService> list = Broadcast_subscribe.get(b);
		for (MicroService m:list) {
			microService_queues.get(m.getName()).add(b);
		}
	}

	/**
	 * @pre none
	 * @post none
	 */
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {//todo notify the microservice that waits
		Queue<MicroService> queue = event_subscribe.get(e);
		MicroService m = queue.poll();
		microService_queues.get(m.getName()).add(e);
		queue.add(m);
		return e.getFuture();
	}

	/**
	 * @pre  this.isRegistered(m) == false;
	 * @post this.isRegistered(m) == true;
	 */
	@Override
	public void register(MicroService m) {
		microService_queues.put(m.getName(),new PriorityQueue<Message>());
	}

	/**
	 * @post this.isRegistered(m) == false;
	 */
	@Override
	public void unregister(MicroService m) {
		for (Queue<MicroService> queue:event_subscribe.values()) {
			for(int i=0; i<queue.size();i++){
				MicroService temp = queue.poll();
				if(temp.getName()!=m.getName())
					queue.add(m);
			}
		}
		for (LinkedList<MicroService> list:Broadcast_subscribe.values()) {
			for (MicroService temp:list) {
				if(temp.getName()==m.getName())
					list.remove(temp);
			}
		}
		microService_queues.remove(m.getName());
	}

	/**
	 * @pre  this.isRegistered(m) == true;
	 * @post microService_queues[m].size() = @pre microService_queues[m].size()-1
	 */
	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		if(!isRegistered(m))
			throw new IllegalArgumentException();
		if(microService_queues.get(m.getName()).isEmpty())
			wait();

		return microService_queues.get(m.getName()).poll();
	}

	/**
	 * the function check if this microService is subscribed for receiving a message, from message type
	 * <p>
	 * @return true if already subscribed or false
	 * @param s the MicroService
	 * @param m the Message
	 */
	public boolean isSubscribed(MicroService s, Message m){
		return Broadcast_subscribe.get(m).contains((s.getName()));
	}

	/**
	 * the function check if s the microService is registered
	 * <p>
	 * @return true if already registered or false
	 * @param s the MicroService
	 */
	public boolean isRegistered(MicroService s){
		return microService_queues.containsKey((s.getName()));
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

	public HashMap<Class<? extends Message>, Queue<MicroService>> getEvent_subscribe() {
		return event_subscribe;
	}

	public HashMap<Class<? extends Message>, LinkedList<MicroService>> getBroadcast_subscribe() {
		return Broadcast_subscribe;
	}

	public HashMap<String, Queue<Message>> getMicroService_queues() {
		return microService_queues;
	}
}
