package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;
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

	private MessageBusImpl() {
		event_subscribe = new HashMap<>();
		Broadcast_subscribe= new HashMap<>();
		microService_queues = new HashMap<>();
		Initialize();
	}

	private void Initialize(){
		this.Broadcast_subscribe.put(TickBroadcast.class, new LinkedList<>());
		this.Broadcast_subscribe.put(TerminateBroadcast.class, new LinkedList<>());
		this.Broadcast_subscribe.put(PublishConferenceBroadcast.class, new LinkedList<>());

		this.event_subscribe.put(TrainModelEvent.class, new LinkedList<>());
		this.event_subscribe.put(TestModelEvent.class, new LinkedList<>());
		this.event_subscribe.put(PublishResultsEvent.class, new LinkedList<>());

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
	public synchronized <T> void  subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		event_subscribe.get(type).add(m);
	}

	/**
	 * @pre  this.isSubscribed(m,type) == false;
	 * @post this.isSubscribed(m,type) == true;
	 */
	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		Broadcast_subscribe.get(type).add(m);
	}

	/**
	 * @pre e.result != null ;
	 * @post e.future.isDone = true;
	 * @post result = e.future.get();
	 */
	@Override
	public synchronized <T> void complete(Event<T> e, T result) {
		e.getFuture().resolve(result);
	}

	/**
	 * @pre none
	 * @post none
	 */
	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		LinkedList<MicroService> list = Broadcast_subscribe.get(b);
		for (MicroService m:list) {
			microService_queues.get(m.getName()).add(b);
			notifyAll();
		}
	}

	/**
	 * @pre none
	 * @post none
	 */
	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		Queue<MicroService> queue = event_subscribe.get(e);
		MicroService m = queue.poll();
		microService_queues.get(m.getName()).add(e);
		microService_queues.notifyAll();
		queue.add(m);
		return e.getFuture();
	}

	/**
	 * @pre  this.isRegistered(m) == false;
	 * @post this.isRegistered(m) == true;
	 */
	@Override
	public synchronized void register(MicroService m) {
		microService_queues.put(m.getName(),new PriorityQueue<Message>());
	}

	/**
	 * @post this.isRegistered(m) == false;
	 */
	@Override
	public synchronized void unregister(MicroService m) {
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
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException {
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
	public synchronized boolean isSubscribed(MicroService s, Message m){
		return Broadcast_subscribe.get(m).contains((s.getName()));
	}

	/**
	 * the function check if s the microService is registered
	 * <p>
	 * @return true if already registered or false
	 * @param s the MicroService
	 */
	public synchronized boolean isRegistered(MicroService s){
		return microService_queues.containsKey((s.getName()));
	}

//	public HashMap<Class<? extends Message>, Queue<MicroService>> getEvent_subscribe() {
//		return event_subscribe;
//	}
//
//	public HashMap<Class<? extends Message>, LinkedList<MicroService>> getBroadcast_subscribe() {
//		return Broadcast_subscribe;
//	}

	public synchronized HashMap<String, Queue<Message>> getMicroService_queues() {
		return microService_queues;
	}
}
