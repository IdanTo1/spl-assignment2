package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminationTickBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;

/**
 * A Publisher only. Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class. You MAY change constructor signatures and even add new
 * public constructors.
 */
public class Intelligence extends Subscriber {
    private int _serial;
    private PriorityQueue<MissionInfo> _missions;
    private CountDownLatch _signalInitialized;
    private LinkedList<MissionInfo> missionsExecuted;

    public Intelligence(int serial, CountDownLatch signalInitialized, List<MissionInfo> missions) {
        super("Intelligence" + serial);
        _serial = serial;
        _missions = new PriorityQueue<>(missions.size(), Comparator.comparingInt(MissionInfo::getTimeIssued));
        _missions.addAll(missions);
        _signalInitialized = signalInitialized;
        missionsExecuted = new LinkedList<>();
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> {
            int currTime = b.getCurrTime();
            while (_missions.peek() != null && currTime == _missions.peek().getTimeIssued()) {
                MissionInfo mission = _missions.poll();
                getSimplePublisher().sendEvent(new MissionReceivedEvent(mission));
                missionsExecuted.add(mission);
            }
        });
        subscribeBroadcast(TerminationTickBroadcast.class, (TerminationTickBroadcast b) -> {
            int lastIssued = 0;
            String lastIssuedName = "";
            for (MissionInfo mission : missionsExecuted) {
                if (lastIssued > mission.getTimeIssued())
                    System.err.println("Error in intelligence" + _serial + ": mission " + mission.getMissionName()
                            + " executed after " + lastIssuedName);
                lastIssued = mission.getTimeIssued();
                lastIssuedName = mission.getMissionName();
            }
            terminate();
        });
        _signalInitialized.countDown();
    }

}
