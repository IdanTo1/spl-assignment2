package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.AgentsAvailableObject;
import bgu.spl.mics.application.messages.TerminationTickBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Only this type of Subscriber can access the squad. Three are several Moneypenny-instances - each of them holds a
 * unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class. You MAY change constructor signatures and even add new
 * public constructors.
 */
public class Moneypenny extends Subscriber {
    private Squad squad;
    private int _serial;
    private CountDownLatch _signalInitialized;

    public Moneypenny(int serial, CountDownLatch signalInitialized) {
        super(("Moneypenny" + serial));
        squad = Squad.getInstance();
        _serial = serial;
        _signalInitialized = signalInitialized;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminationTickBroadcast.class, (TerminationTickBroadcast b) -> terminate());
        subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent e) -> {
            AgentsAvailableObject result = e.getObj();
            List<String> agentsSerials = result.getAgentsSerials();
            java.util.Collections.sort(agentsSerials);
            result.setMoneypennySerial(_serial);

            try {
                if (squad.getAgents(agentsSerials)) {
                    result.setAgentsNames(squad.getAgentsNames(agentsSerials));
                } else return; // if agents don't exists. mission will never be executed, so no reason to wait.
            } catch (InterruptedException ex) {
                complete(e, null);
                squad.releaseAgents(agentsSerials);
                return;
            }
            complete(e, result);
            synchronized (result) {
                while (!result.isSendMission() && !result.isTerminateMission()) {
                    try {
                        result.wait();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            if (result.isSendMission()) squad.sendAgents(agentsSerials, result.getMissionDuration());
            if (result.isTerminateMission()) squad.releaseAgents(agentsSerials);
        });
        _signalInitialized.countDown();
    }

}
