package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;


/**
 * a message sent from (@code M) to (@code Moneypenny) requesting for the agents for a mission.
 */
public class AgentsAvailableEvent implements Event<AgentsAvailableObject> {
    private AgentsAvailableObject _obj;

    public AgentsAvailableEvent(AgentsAvailableObject obj) {
        _obj = obj;
    }

    public AgentsAvailableObject getObj() {
        return _obj;
    }
}
