package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class. You MAY change constructor signatures and even add new
 * public constructors.
 */
public class M extends Subscriber {
    private int _currTime = 0;
    private int _serial;
    private Diary diary;
    private CountDownLatch _signalInitialized;

    public M(int serial, CountDownLatch signalInitialized) {
        super("M" + serial);
        _serial = serial;
        diary = Diary.getInstance();
        _signalInitialized = signalInitialized;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> _currTime = b.getCurrTime());
        subscribeBroadcast(TerminationTickBroadcast.class, (TerminationTickBroadcast b) -> terminate());
        subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent e) -> {
            diary.incrementTotal();
            // get all mission details
            MissionInfo info = e.getMission();
            String missionName = info.getMissionName();
            int timeIssued = info.getTimeIssued();
            List<String> agentsSerials = info.getSerialAgentsNumbers();
            // ask for agents from Moneypenny
            AgentsAvailableObject agentsAvailableObject = acquireAgents(info, agentsSerials);
            if (agentsAvailableObject == null) {
                terminate();
                complete(e, null);
                return;
            }
            List<String> agentNames = agentsAvailableObject.getAgentsNames();
            // If the agents weren't initialized, meaning Moneypenny couldn't find them in the squad,
            // we'll return, and MoneyPenny knows not to wait to execute mission.
            if (agentNames == null) {
                complete(e, null);
                return;
            }
            int MoneypennySerial = agentsAvailableObject.getMoneypennySerial();
            // ask for gadget from Q
            GadgetAvailableObject gadgetAvailableObject = acquireGadget(info);
            if (gadgetAvailableObject == null || !gadgetAvailableObject.isGadgetExists()) {
                if (gadgetAvailableObject == null) terminate();
                synchronized (agentsAvailableObject) {
                    agentsAvailableObject.terminateMission();
                    agentsAvailableObject.notifyAll();
                    complete(e, null);
                    return;
                }
            }
            String gadget = gadgetAvailableObject.getGadget();
            int Qtime = gadgetAvailableObject.getQtime();
            // check if mission time expired
            if (_currTime > info.getTimeExpired()) {
                // signal Moneypenny she should release the agents.
                synchronized (agentsAvailableObject) {
                    agentsAvailableObject.terminateMission();
                    agentsAvailableObject.notifyAll();
                    complete(e, null);
                    return;
                }
            }
            // signal Moneypenny she should send the agents.
            agentsAvailableObject.sendMission();
            synchronized (agentsAvailableObject) {
                agentsAvailableObject.notifyAll();
            }

            // generate the mission report with all the allocated data.
            Report report = createReport(missionName, timeIssued, MoneypennySerial, agentsSerials,
                                         agentNames, Qtime, gadget);
            diary.addReport(report);
            complete(e, info);
        });
        _signalInitialized.countDown();
    }

    /**
     * a helper function to create a report with all the needed data.
     *
     * @param missionName      the name of the mission
     * @param timeIssued       the time-tick when the mission was sent by an Intelligence Publisher.
     * @param MoneypennySerial the Moneypenny instance who handled the mission will assigns its serial number to this
     *                         parameter.
     * @param agentsSerials    the serial numbers of the agents sent in the mission
     * @param agentsNames      the names of the agents sent to the mission
     * @param Qtime            the time-tick in which Q Received the GadgetAvailableEvent for that mission
     * @param gadget           the gadget which was used in the mission
     *
     * @return a detailed report of the mission
     */
    public Report createReport(String missionName, int timeIssued, int MoneypennySerial, List<String> agentsSerials, List<String> agentsNames,
                               int Qtime, String gadget) {
        Report report = new Report();
        report.setM(this._serial);
        report.setMissionName(missionName);
        report.setTimeIssued(timeIssued);
        report.setTimeCreated(this._currTime);
        report.setAgentsSerialNumbers(agentsSerials);
        report.setAgentsNames(agentsNames);
        report.setMoneypenny(MoneypennySerial);
        report.setGadgetName(gadget);
        report.setQTime(Qtime);
        return report;
    }

    private AgentsAvailableObject acquireAgents(MissionInfo info, List<String> agentsSerials) {
        AgentsAvailableObject agentsAvailableObject = new AgentsAvailableObject(agentsSerials, info.getDuration());
        Future<AgentsAvailableObject> agentsAvailableFuture;
        agentsAvailableFuture = this.getSimplePublisher().sendEvent(new AgentsAvailableEvent(agentsAvailableObject));
        // check whether Moneypenny acquired the agents, and allocate all relevant details. timeout is needed for the
        // case where all Moneypennys already terminated, and M is in the middle of processing a mission.
        if (agentsAvailableFuture == null) return null;
        agentsAvailableObject = agentsAvailableFuture.get(); // M and Moneypenny uses the same agentsObject
        return agentsAvailableObject;
    }

    private GadgetAvailableObject acquireGadget(MissionInfo info) {
        GadgetAvailableObject gadgetAvailableObject = new GadgetAvailableObject(info.getGadget());
        Future<GadgetAvailableObject> gadgetAvailableFuture;
        gadgetAvailableFuture = this.getSimplePublisher().sendEvent(new GadgetAvailableEvent(gadgetAvailableObject));
        // check whether Q acquired the gadget, and allocate all relevant details.
        if (gadgetAvailableFuture == null) return null;
        gadgetAvailableObject = gadgetAvailableFuture.get(); //M and Q uses the same gadgetObject
        return gadgetAvailableObject;
    }
}

