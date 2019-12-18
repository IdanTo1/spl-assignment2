package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	Timer timer;
	int _currentTick;
	int _maxTicks;

	public TimeService(int max_ticks) {
		super("TimerService");
		_currentTick = 0;
		_maxTicks = max_ticks;
		initialize();
	}

	@Override
	protected void initialize() {
		timer = new Timer();
	}

	@Override
	public void run() {
		while (_currentTick <= _maxTicks) {
			getSimplePublisher().sendBroadcast( new TickBroadcast(_currentTick));
			try {
				Thread.sleep(100);
			}
			catch (InterruptedException ignored) {}
		}
	}


}
