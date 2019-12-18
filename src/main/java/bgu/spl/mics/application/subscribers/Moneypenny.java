package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.AgentsAvailableObject;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	private int _currTime = 0;
	private Squad squad;
	private int _serial;

	public Moneypenny(int serial) {
		super(("Moneypenny" + serial));
		squad = Squad.getInstance();
		_serial = serial;
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast b)->_currTime = b.getCurrTime());
		subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent e)-> {
			AgentsAvailableObject result = e.getObj();
			List<String> agentsSerials = result.getAgentsSerials();
			java.util.Collections.sort(agentsSerials);
			result.setMoneypennySerial(_serial);
			if(squad.getAgents(agentsSerials)) {
				result.setAgentsNames(squad.getAgentsNames(agentsSerials));
			}
			complete(e, result);
			while (!result.isSendMission() || !result.isTerminateMission()) {
				try {result.wait();} catch (InterruptedException ignored) {}
			}
			if (result.isSendMission()) squad.sendAgents(agentsSerials, result.getMissionDuration());
			if (result.isTerminateMission()) squad.releaseAgents(agentsSerials);
		});
		
	}

}
