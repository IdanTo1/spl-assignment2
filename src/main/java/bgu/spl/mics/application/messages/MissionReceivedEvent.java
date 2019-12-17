package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

/**
 * a message sent from (@code Intelligence) to (@code M) informing about a mission to be executed.
 */
public class MissionReceivedEvent implements Event<MissionInfo> {
    private MissionInfo _mission;

    public MissionReceivedEvent(MissionInfo mission) {
        this._mission = mission;
    }

    /**
     * Retrieves the mission to be executed information
     * @return mission information
     */
    public MissionInfo getMission() {
        return _mission;
    }
}
