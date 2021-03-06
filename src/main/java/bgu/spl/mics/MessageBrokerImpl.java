package bgu.spl.mics;

import bgu.spl.mics.application.messages.MissionReceivedEvent;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface. Write your implementation
 * here! Only private fields and methods can be added to this class.
 *
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
        synchronized (type) { //Synchronization is needed to avoid replacing an existing queue with a new one needlessly
            if (_eventSubscribers.get(type) == null) _eventSubscribers.put(type, new ConcurrentLinkedQueue<>());
        }
        _eventSubscribers.get(type).add(m);
    }

    @Override
    /**
     * @pre: none
     * @post: m's queue will receive Broadcast<T> objects.
     */
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        synchronized (type) { //Synchronization is needed to avoid replacing an existing queue with a new one needlessly
            if (_broadcastSubscribers.get(type) == null) _broadcastSubscribers.put(type, new ConcurrentLinkedQueue<>());
        }
        _broadcastSubscribers.get(type).add(m);
    }

    @Override
    /**
     * @pre: e's associated future object isDone() is false
     * @post: e's associated future object isDone() is true
     */
    public <T> void complete(Event<T> e, T result) {
        _eventFutures.remove(e).resolve(result);
    }

    @Override
    /**
     * @pre none
     * @post all subscribers subscribed to b.getClass() queue's last element will be b.
     */
    public void sendBroadcast(Broadcast b) {
        // ConcurrentLinkedQueue also implements a thread-safe iterator
        for (Subscriber s : _broadcastSubscribers.get(b.getClass())) {
            _subscriberQueues.get(s).add(b);
        }
    }


    @Override
    /**
     * @pre none
     * @post all subscribers subscribed to e.getClass() queue's last element will be e.
     */
    public <T> Future<T> sendEvent(Event<T> e) {
        Future<T> f = new Future<>();
        Subscriber currentSub = null;
        Queue<Subscriber> q = _eventSubscribers.get(e.getClass());
        if (q == null) {
            f.resolve(null);
            return f;
        }
        synchronized (q) {
            currentSub = q.poll();
            if (currentSub == null) return null;
            q.add(currentSub);
        }
        try {
            synchronized (_subscriberQueues.get(currentSub)) {
                /*
                 * if the queue was deleted after subscriber was chosen, that means we are in the middle of
                 * termination process. so there is no point of registering this event to other subscriber. (it is going to
                 * be deleted as well), so we'll just return empty future, without adding it to the eventsFutures map.
                 */
				_subscriberQueues.get(currentSub).add(e);
				_eventFutures.put(e, f);
            }
            return f;
        } catch (NullPointerException ex) {
        	f.resolve(null);
            return f;
        }
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
        if (_subscriberQueues.get(m) == null) return;
        /*
        * Resolve all event's futures assigned to m with null, to avoid infinite wait for these futures
        * because of Concurrency, order is very important here.
        * first we'll unsubscribe the subscriber from all events and broadcasts.
        * then we'll remove the Subscribers queue (so no one will have access to it.
        * and only then we'll resolve all events in the queue.
         */
        for (ConcurrentLinkedQueue<Subscriber> q : _eventSubscribers.values()) {
            synchronized (q) {
                q.remove(m);
            }
        }
        for (ConcurrentLinkedQueue<Subscriber> q : _broadcastSubscribers.values()) {
            synchronized (q) {
                q.remove(m);
            }
        }
        BlockingQueue<Message> subscriberQueue = _subscriberQueues.remove(m);
        synchronized (subscriberQueue) {
            for (Message message : subscriberQueue) {
                if (message instanceof Event) {
                    _eventFutures.remove(message).resolve(null);
                }
            }
        }
    }

    @Override
    /**
     * @post: queue's @pre(Head) removed.
     */
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        return _subscriberQueues.get(m).take();
    }


}
