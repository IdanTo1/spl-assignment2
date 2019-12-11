package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    TestSubscriber s1;
    TestSubscriber s2;
    MessageBroker m;

    @BeforeEach
    public void setUp() {
        m = MessageBrokerImpl.getInstance();
        s1 = new TestSubscriber("s1");
        s2 = new TestSubscriber("s2");
    }

    @AfterEach
    public void tearDown() {
        m.unregister(s1);
        m.unregister(s2);
    }

    @Test
    public void testSingleton() {
        MessageBroker otherInstance = MessageBrokerImpl.getInstance();
        assertTrue(otherInstance == m, "different instances of singleton MessageBroker exist!");
    }

    private void safeAwaitMessage(boolean twoSubscribers) {
        try {
            m.awaitMessage(s1);
            if (twoSubscribers) m.awaitMessage(s2);
        } catch (InterruptedException e) {
            fail("Got interrupted with only 1 thread");
        }
    }

    @Test
    public void testEventSubscribe() {
        m.sendEvent(new IntEvent(0));
        m.sendEvent(new IntEvent(1));
        safeAwaitMessage(true);
        assertTrue(s1.getNum() == 0, "Subscriber 1 didn't get the first called event");
        assertTrue(s2.getNum() == 1, "Subscriber 2 didn't get the second called event");
        m.sendEvent(new IntEvent(2));
        safeAwaitMessage( false);
        assertTrue(s1.getNum() == 2, "Subscriber 1 didn't appear 3rd in a 2 object round-robin");
        assertTrue(s2.getNum() == 1, "Subscriber s2 received an event it shouldn't have");
    }

    @Test
    public void testBroadcastSubscribe() {
        m.sendBroadcast(new IntBroadcast(2));
        safeAwaitMessage(true);
        assertTrue(s1.getNum() == 2, "Subscriber s1 didn't get the broadcast");
        assertTrue(s2.getNum() == 2, "Subscriber s2 didn't get the broadcast");
        m.sendBroadcast(new IntBroadcast(3));
        safeAwaitMessage(true);
        assertTrue(s1.getNum() == 3, "Subscriber s1 didn't get the broadcast");
        assertTrue(s2.getNum() == 3, "Subscriber s2 didn't get the broadcast");
    }

    @Test
    public void testComplete() {
        Event<Integer> e = new IntEvent(1);
        Future<Integer> f = m.sendEvent(e);
        assertTrue(!f.isDone(), "Future object starts completed");
        m.complete(e, 2);
        assertTrue(f.isDone(), "Future object doesn't complete");
        assertTrue(f.get() == 2, "Future doesn't return the result");
    }

    @Test
    public void testUnregister() {
        m.unregister(s2);
        Future<Integer> f = m.sendEvent(new IntEvent(1));
        assertTrue(f != null, "sendEvent should return future object");
        m.unregister(s1);
        f = m.sendEvent(new IntEvent(1));
        assertTrue(f == null,
                "sendEvent should return null future event when" +
                        " no subscriber is subscribed to messages of certain type");
    }

    @Test
    public void testInterruption() {
        Thread th = new Thread(() -> {
            try {
                m.awaitMessage(s1);
            } catch (InterruptedException e) {
                assertTrue(true); // For maven test count
                return;
            }
            fail("didn't get interrupted while in infinite wait");
        });
        th.start();
        th.interrupt();
    }
}
