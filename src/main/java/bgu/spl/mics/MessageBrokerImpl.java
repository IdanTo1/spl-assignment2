package bgu.spl.mics;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 * @inv MessageBrokerImpl is a singleton - only one instance of this class can be initialized
 */
public class MessageBrokerImpl implements MessageBroker {
	private static MessageBrokerImpl instance = null;
	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		//TODO: Implement this
		return null;
	}

	private MessageBrokerImpl() {
		// TODO complete
	}

	@Override
	/**
	@pre: none
	 @post: m's queue will receive Event<T> objects.
	 */
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * @pre: none
	 * @post: m's queue will receive Broadcast<T> objects.
	 */
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * @pre: e's associated future object isDone() is false
	 * @post: e's associated future object isDone() is true
	 */
	public <T> void complete(Event<T> e, T result) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * @pre none
	 * @post all subscribers subscribed to b.getClass() queue's last element will be b.
	 */
	public void sendBroadcast(Broadcast b) {
		// TODO Auto-generated method stub

	}

	
	@Override
	/**
	 * @pre none
	 * @post all subscribers subscribed to e.getClass() queue's last element will be e.
	 */
	public <T> Future<T> sendEvent(Event<T> e) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	/**
	 * @pre: no queue associated with m
	 * @post: exists queue associated with m
	 */
	public void register(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * @post: no queue associated with m
	 */
	public void unregister(Subscriber m) {
		// TODO Auto-generated method stub

	}

	@Override
	/**
	 * @post: queue's @pre(Head) removed.
	 */
	public Message awaitMessage(Subscriber m) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
