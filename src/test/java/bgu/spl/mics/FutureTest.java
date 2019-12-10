package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.TimeUnit;

public class FutureTest {
    Future<String> f;
    TimeUnit t;
    public static final String TEST_STRING = "I'm working";

    @BeforeEach
    public void setUp() {
        f = new Future<String>();
        t = TimeUnit.MILLISECONDS;
    }

    @Test
    public void test() {
        assertTrue(f.get(100, t) == null, "unresolved future differs from null");
        assertTrue(f.isDone() == false, "unresolved future marked as done");
        f.resolve(TEST_STRING);
        assertTrue(f.get(100, t) == TEST_STRING, "unresolved future differs from null");
        assertTrue(f.get() == TEST_STRING, "unresolved future differs from null");
        assertTrue(f.isDone() == true, "resolved future marked as undone");
    }
}
