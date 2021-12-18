package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Cluster;
import bgu.spl.mics.application.services.ConferenceService;
import bgu.spl.mics.application.services.StudentService;
import bgu.spl.mics.application.services.TimeService;

import javax.swing.text.html.HTMLDocument;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	private HashMap<Class<? extends Message>, Queue<MicroService>> event_subscribe; // string = type event
	private ReadWriteLock eventLock;
	private HashMap<Class<? extends Message>, LinkedList<MicroService>> Broadcast_subscribe;
	private ReadWriteLock broadcastLock;
	private HashMap<String, Queue<Message>> microService_queues; // string = microservice name


	private MessageBusImpl() {
		event_subscribe = new HashMap<>();
		Broadcast_subscribe= new HashMap<>();
		microService_queues = new HashMap<>();
		eventLock = new ReentrantReadWriteLock();
		broadcastLock = new ReentrantReadWriteLock();
		Initialize();
	}

	private synchronized void Initialize(){
		this.Broadcast_subscribe.put(TickBroadcast.class, new LinkedList<>());
		this.Broadcast_subscribe.put(TerminateBroadcast.class, new LinkedList<>());
		this.Broadcast_subscribe.put(PublishConferenceBroadcast.class, new LinkedList<>());
		this.Broadcast_subscribe.put(TrainedBroadcast.class, new LinkedList<>());
		this.Broadcast_subscribe.put(TestedBroadcast.class, new LinkedList<>());

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
	public   <T> void  subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		eventLock.writeLock().lock();
		event_subscribe.get(type).add(m);
		eventLock.writeLock().unlock();
	}

	/**
	 * @pre  this.isSubscribed(m,type) == false;
	 * @post this.isSubscribed(m,type) == true;
	 */
	@Override
	public  void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		broadcastLock.writeLock().lock();
		Broadcast_subscribe.get(type).add(m);
		broadcastLock.writeLock().unlock();
	}

	/**
	 * @pre e.result != null ;
	 * @post e.future.isDone = true;
	 * @post result = e.future.get();
	 */
	@Override
	public  <T> void complete(Event<T> e, T result) {
		e.getFuture().resolve(result);//todo need to understand what to to here
	}

	/**
	 * @pre none
	 * @post none
	 */
	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		broadcastLock.readLock().lock();
		LinkedList<MicroService> list = Broadcast_subscribe.get(b.getClass());
		broadcastLock.readLock().unlock();
		for (MicroService m:list) {
			synchronized (microService_queues.get(m.getName())) {
				microService_queues.get(m.getName()).add(b);
			}
		}
		notifyAll();
	}

	/**
	 * @pre none
	 * @post none
	 */
	@Override
	public  <T> Future<T> sendEvent(Event<T> e) {
		eventLock.readLock().lock();
		Queue<MicroService> queue = event_subscribe.get(e.getClass());
		eventLock.readLock().unlock();
		if(!queue.isEmpty()) {
			synchronized (queue) {
				MicroService m = queue.poll();
				synchronized (microService_queues.get(m.getName())) {
					microService_queues.get(m.getName()).add(e);
				}
				queue.add(m);
			}
			this.notifyAll();
		}
		return e.getFuture();
	}

	/**
	 * @pre  this.isRegistered(m) == false;
	 * @post this.isRegistered(m) == true;
	 */
	@Override
	public  void register(MicroService m) {// todo check if we need here sync
		microService_queues.put(m.getName(),new LinkedList<>());
	}

	/**
	 * @post this.isRegistered(m) == false;
	 */
	@Override
	public  void unregister(MicroService m) {
		eventLock.writeLock().lock();
		for (Queue<MicroService> queue:event_subscribe.values()) {
			for(int i=0; i<queue.size();i++){
				MicroService temp = queue.poll();
				if(temp.getName()!=m.getName())
					queue.add(temp);
			}
		}
		eventLock.writeLock().unlock();
		broadcastLock.writeLock().lock();
		for (LinkedList<MicroService> list:Broadcast_subscribe.values()) {
			list.removeIf(temp -> temp.getName() == m.getName());
		}
		broadcastLock.writeLock().unlock();
		synchronized (microService_queues) {
			microService_queues.remove(m.getName());
		}
	}

	/**
	 * @pre  this.isRegistered(m) == true;
	 * @post microService_queues[m].size() = @pre microService_queues[m].size()-1
	 */
	@Override
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException {
		synchronized (microService_queues) {
			if (!isRegistered(m))
				throw new IllegalArgumentException();
		}
		while(microService_queues.get(m.getName()).isEmpty())//todo better wait
			this.wait();
		synchronized (microService_queues.get(m.getName())) {
		return microService_queues.get(m.getName()).poll();
		}
	}

	/**
	 * the function check if this microService is subscribed for receiving a message, from message type
	 * <p>
	 * @return true if already subscribed or false
	 * @param s the MicroService
	 * @param m the Message
	 */
	public boolean isSubscribed(MicroService s, Message m){
		broadcastLock.readLock().lock();
		boolean result= Broadcast_subscribe.get(m).contains((s.getName()));
		broadcastLock.readLock().unlock();
		return result;
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

}
