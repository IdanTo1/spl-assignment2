package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    Future<String> f;
    TimeUnit t;
    public static final String TEST_STRING = "I'm working";

    @BeforeEach
    public void setUp() {
        f = new Future<>();
        t = TimeUnit.MILLISECONDS;
    }

    @Test
    public void test() {
        assertNull(f.get(100, t), "unresolved future differs from null");
        assertFalse(f.isDone(), "unresolved future marked as done");
        f.resolve(TEST_STRING);
        assertTrue(f.get(100, t).equals(TEST_STRING), "resolved future doesn't contain proper result");
        assertTrue(f.get().equals(TEST_STRING), "resolved future doesn't contain proper result");
        assertTrue(f.isDone(), "resolved future marked as undone");
    }
}
