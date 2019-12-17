package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

/**
 * a message sent from (@code TimeService) to to whom it may concern, the current time.
 */
public class TickBroadcast implements Broadcast {
    int _currTime;

    TickBroadcast(int time) {
        _currTime = time;
    }

    /**
     * Retrieves the current time of the simulation
     * @return the current time
     */
    public int getCurrTime() {
        return _currTime;
    }
}
