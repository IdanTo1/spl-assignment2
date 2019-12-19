package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;

/**
 * A Publisher only.
 * Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
    private int _serial;
    private PriorityQueue<MissionInfo> _missions;
    private CountDownLatch _signalInitialized;

    public Intelligence(int serial, CountDownLatch signalInitialized, List<MissionInfo> missions) {
        super("Intelligence" + serial);
        _serial = serial;
        _missions = new PriorityQueue<>(missions.size(), Comparator.comparingInt(MissionInfo::getTimeIssued));
        _missions.addAll(missions);
        _signalInitialized = signalInitialized;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
            while (b.getCurrTime() == _missions.peek().getTimeIssued())
                getSimplePublisher().sendEvent(new MissionReceivedEvent(_missions.poll()));
        });
        _signalInitialized.countDown();
    }

}
