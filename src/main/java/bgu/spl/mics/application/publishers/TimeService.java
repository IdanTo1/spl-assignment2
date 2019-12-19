package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private int _currentTick;
    private final int _maxTicks;
    private final CountDownLatch _signalStarted;

    public TimeService(int max_ticks, CountDownLatch signalStarted) {
        super("TimerService");
        _currentTick = 0;
        _maxTicks = max_ticks;
        _signalStarted = signalStarted;
    }

    @Override
    protected void initialize() { }

    @Override
    public void run() {
    	while(_signalStarted.getCount() > 0) {
			try {
				_signalStarted.await();
			} catch (InterruptedException ignored) { }
		}
        while (_currentTick <= _maxTicks) {
            getSimplePublisher().sendBroadcast(new TickBroadcast(_currentTick));
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            _currentTick++;
        }
    }

    public boolean isToTerminate() {
        return _currentTick > _maxTicks;
    }


}
