package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

/**
 * a message sent from (@code M) to (@code Q) requesting for the gadget for a mission.
 */
public class GadgetAvailableEvent implements Event<GadgetAvailableObject> {
    private GadgetAvailableObject _obj;

    public GadgetAvailableEvent(GadgetAvailableObject obj) {
        _obj = obj;
    }

    public GadgetAvailableObject getObj() {
        return _obj;
    }
}
