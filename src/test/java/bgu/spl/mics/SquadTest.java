package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {

    public static final Agent agents[] = {new Agent("001", "a"), new Agent("002", "b"),
            new Agent("003", "c"), new Agent("004", "d")};
    public static final ArrayList<String> agentsNames = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
    public static final ArrayList<String> agentsSerials = new ArrayList<>(Arrays.asList("001", "002", "003", "004"));
    Squad squad;

    @BeforeEach
    public void setUp() {
        squad = Squad.getInstance();
        squad.load(agents);
    }

    @Test
    public void testSingleTon(){
        assertNotNull(squad, "singleTon not created");
        Squad otherInstance = Squad.getInstance();
        assertSame(squad, otherInstance, "different instances of singleton Squad exist!");
    }

    @Test
    public void testLoad(){
        ArrayList<String> testSerials = new ArrayList<>();
        ArrayList<String> testNames = new ArrayList<>();
        for (Agent agent: agents) {
            testSerials.add(agent.getSerialNumber());
            testNames.add(agent.getName());
        }
        assertTrue(squad.getAgentsNames(testSerials).equals(agentsNames), "serials in squad doesn't match inserted agents' names");
        assertTrue(squad.getAgentsNames(agentsSerials).equals(testNames), "names in squad doesn't match inserted agents' serials");
    }

    @Test
    public void testGetSetAgents() {
        squad.getAgents(agentsSerials);
        for (Agent agent : agents) {
            assertFalse(agent.isAvailable(), "agent is still available after being acquired");
        }
        squad.sendAgents(agentsSerials, 2);
        for (Agent agent : agents) {
            assertTrue(agent.isAvailable(), "agent is not available after being sent and released");
        }
    }
}
