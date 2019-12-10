package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class MessageBrokerTest {
    @BeforeEach
    public void setUp(){

    }

    @Test
    public void testSingleton(){
        MessageBroker instance1 = MessageBrokerImpl.getInstance();
        MessageBroker instance2 = MessageBrokerImpl.getInstance();
        assertTrue(instance1==instance2, "different instances of singleton MessageBroker exist!");
    }
}
