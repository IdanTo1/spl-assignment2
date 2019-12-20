package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    TestSubscriber s1;
    TestSubscriber s2;
    MessageBroker m;

    @BeforeEach
    public void setUp() {
        m = MessageBrokerImpl.getInstance();
        s1 = new TestSubscriber("s1");
        s2 = new TestSubscriber("s2");
        m.register(s1);
        m.register(s2);
    }

    @AfterEach
    public void tearDown() {
        m.unregister(s1);
        m.unregister(s2);
    }

    @Test
    public void testSingleton() {
        MessageBroker otherInstance = MessageBrokerImpl.getInstance();
        assertSame(otherInstance, m, "different instances of singleton MessageBroker exist!");
    }


    @Test
    public void testEventSubscribe() {
        m.sendEvent(new IntEvent(0));
        m.sendEvent(new IntEvent(1));
        try {
            s1.setNum(((IntEvent) m.awaitMessage(s1)).getNum());
            s2.setNum(((IntEvent) m.awaitMessage(s2)).getNum());
        } catch (InterruptedException ignored) {}
        assertEquals(0, s1.getNum(), "Subscriber 1 didn't get the first called event");
        assertEquals(1, s2.getNum(), "Subscriber 2 didn't get the second called event");
        m.sendEvent(new IntEvent(2));
        try {s1.setNum(((IntEvent) m.awaitMessage(s1)).getNum());} catch (InterruptedException ignored) {}
        assertEquals(2, s1.getNum(), "Subscriber 1 didn't appear 3rd in a 2 object round-robin");
        assertEquals(1, s2.getNum(), "Subscriber s2 received an event it shouldn't have");
    }

    @Test
    public void testBroadcastSubscribe() {
        m.sendBroadcast(new IntBroadcast(2));
        try {
            s1.setNum(((IntBroadcast) m.awaitMessage(s1)).getNum());
            s2.setNum(((IntBroadcast) m.awaitMessage(s2)).getNum());
        } catch (InterruptedException ignored) {}
        assertEquals(2, s1.getNum(), "Subscriber s1 didn't get the broadcast");
        assertEquals(2, s2.getNum(), "Subscriber s2 didn't get the broadcast");
        m.sendBroadcast(new IntBroadcast(3));
        try {
            s1.setNum(((IntBroadcast) m.awaitMessage(s1)).getNum());
            s2.setNum(((IntBroadcast) m.awaitMessage(s2)).getNum());
        } catch (InterruptedException ignored) {}
        assertEquals(3, s1.getNum(), "Subscriber s1 didn't get the broadcast");
        assertEquals(3, s2.getNum(), "Subscriber s2 didn't get the broadcast");
    }

    @Test
    public void testComplete() {
        Event<Integer> e = new IntEvent(1);
        Future<Integer> f = m.sendEvent(e);
        assertFalse(f.isDone(), "Future object starts completed");
        m.complete(e, 2);
        assertTrue(f.isDone(), "Future object doesn't complete");
        assertEquals(2, (int) f.get(), "Future doesn't return the result");
    }

    @Test
    public void testUnregister() {
        m.unregister(s2);
        Future<Integer> f = m.sendEvent(new IntEvent(1));
        assertNotNull(f, "sendEvent should return future object");
        m.unregister(s1);
        f = m.sendEvent(new IntEvent(1));
        assertNull(f, "sendEvent should return null future event when" +
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
