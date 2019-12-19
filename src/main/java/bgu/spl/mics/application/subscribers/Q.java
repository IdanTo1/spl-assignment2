package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableObject;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import sun.jvm.hotspot.debugger.win32.coff.COFFException;

import java.util.concurrent.CountDownLatch;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 * <p>
 * You can add private fields and public methods to this class. You MAY change constructor signatures and even add new
 * public constructors.
 */
public class Q extends Subscriber {

    private int _currTime = 0;
    private Inventory inventory;
    private CountDownLatch _signalInitialized;

    public Q(CountDownLatch signalInitialized) {
        super("Q");
        inventory = Inventory.getInstance();
        _signalInitialized = signalInitialized;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast b) -> _currTime = b.getCurrTime());
        subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent e) ->
        {
            GadgetAvailableObject result = e.getObj();
            String gadget = result.getGadget();
            if (inventory.getItem(gadget)) {
                result.setGadgetExists();
            }
            result.setQtime(_currTime);
            complete(e, result);
        });
        _signalInitialized.countDown();
    }

}
