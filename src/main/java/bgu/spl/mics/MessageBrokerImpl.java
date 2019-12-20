package bgu.spl.mics;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 * @inv MessageBrokerImpl is a singleton - only one instance of this class can be initialized
 */
public class MessageBrokerImpl implements MessageBroker {
	private static MessageBrokerImpl _instance = new MessageBrokerImpl();
	// All datasets are concurrent or blocking (thread-safe) either way.
	// Which removes the need to synchronize altogether
	private ConcurrentHashMap<Class<? extends Event>, ConcurrentLinkedQueue<Subscriber>> _eventSubscribers;
	private ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<Subscriber>> _broadcastSubscribers;
	private ConcurrentHashMap<Event, Future> _eventFutures;
	private ConcurrentHashMap<Subscriber, BlockingQueue<Message>> _subscriberQueues;

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return _instance;
	}

	private MessageBrokerImpl() {
		_eventSubscribers = new ConcurrentHashMap<>();
		_broadcastSubscribers = new ConcurrentHashMap<>();
		_eventFutures = new ConcurrentHashMap<>();
		_subscriberQueues = new ConcurrentHashMap<>();
	}

	@Override
	/**
	 @pre: none
	 @post: m's queue will receive Event<T> objects.
	 */
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		if(_eventSubscribers.get(type) == null) _eventSubscribers.put(type, new ConcurrentLinkedQueue<>());
		_eventSubscribers.get(type).add(m);
	}

	@Override
	/**
	 * @pre: none
	 * @post: m's queue will receive Broadcast<T> objects.
	 */
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		if(_broadcastSubscribers.get(type) == null) _broadcastSubscribers.put(type, new ConcurrentLinkedQueue<>());
		_broadcastSubscribers.get(type).add(m);
	}

	@Override
	/**
	 * @pre: e's associated future object isDone() is false
	 * @post: e's associated future object isDone() is true
	 */
	public <T> void complete(Event<T> e, T result) {
		_eventFutures.get(e).resolve(result);
	}

	@Override
	/**
	 * @pre none
	 * @post all subscribers subscribed to b.getClass() queue's last element will be b.
	 */
	public void sendBroadcast(Broadcast b) {
		// ConcurrentLinkedQueue also implements a thread-safe iterator
		for(Subscriber s : _broadcastSubscribers.get(b.getClass())) {
			_subscriberQueues.get(s).add(b);
		}
	}


	@Override
	/**
	 * @pre none
	 * @post all subscribers subscribed to e.getClass() queue's last element will be e.
	 */
	public <T> Future<T> sendEvent(Event<T> e) {
		Subscriber currentSub = _eventSubscribers.get(e.getClass()).poll();
		if(currentSub == null) return null; // Queue returns null if the queue is empty
		_subscriberQueues.get(currentSub).add(e);
		_eventSubscribers.get(e.getClass()).add(currentSub);
		Future<T> f = new Future<>();
		_eventFutures.put(e, f);
		return f;
	}

	@Override
	/**
	 * @pre: no queue associated with m
	 * @post: exists queue associated with m
	 */
	public void register(Subscriber m) {
		_subscriberQueues.put(m, new LinkedBlockingQueue<>());
	}

	@Override
	/**
	 * @post: no queue associated with m
	 */
	public void unregister(Subscriber m) {
		if(_subscriberQueues.get(m) == null) return;
		// Resolve all event's futures assigned to m with null, to avoid infinite wait for these futures
		for(Message message : _subscriberQueues.get(m)) {
			if(message instanceof Event) {
				_eventFutures.get(message).resolve(null);
			}
		}
		// All we need is to remove the reference to the queue and the garbage collector will do the rest
		_subscriberQueues.remove(m);
	}

	@Override
	/**
	 * @post: queue's @pre(Head) removed.
	 */
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		return _subscriberQueues.get(m).poll();
	}



}
