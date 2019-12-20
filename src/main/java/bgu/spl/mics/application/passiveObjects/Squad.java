package bgu.spl.mics.application.passiveObjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6. You must not alter any of the given public
 * methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 *
 * @inv Squad is a singleton - only one instance of this class can be initialized
 */
public class Squad {
    private static Squad _instance = new Squad();
    private Map<String, Agent> _agents;

    /**
     * Retrieves the single instance of this class.
     *
     * @pre none
     * @post return != null
     */
    public static Squad getInstance() {
        return _instance;
    }

    private Squad() {
        // squad is accessed by Moneypenny, which my have several instances, so thread-safe structure is needed.
        _agents = new HashMap<>();
    }

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization of the squad.
     * @pre none
     * @post (for agent : agents { getAgents ( agent.getSerialNumber) == true})
     */
    public void load(Agent[] agents) {
        for (Agent agent : agents) {
            // shouldn't be synchronized because only main thread loads the squad, before multiThreading
            this._agents.put(agent.getSerialNumber(), agent);
        }
    }

    /**
     * Releases agents.
     *
     * @pre (for serial : serials { getAgents ( [serial]).state == BLOCKED})
     * @post (getAgents ( serials) == true)
     */
    public void releaseAgents(List<String> serials) {
        Agent agent;
        for (String serial : serials) {
            agent = _agents.get(serial);
            synchronized (agent) {
                agent.release();
                agent.notifyAll();
            }
        }
    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time milliseconds to sleep
     * @pre (for serial : serials { getAgents ( [serial]).state == BLOCKED})
     * @post (current_time > = start_time + time & & getAgents ( serials) == true)
     */
    public void sendAgents(List<String> serials, int time) {
        // A time-tick is 100ms and as stated in the forum this should be the implementation of sendAgents
        try {
            Thread.sleep(time * 100);
        } catch (InterruptedException ignored) {
        }
        releaseAgents(serials);
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     * @pre none
     * @post (for serial : serials { getAgents ( [serial]).state == BLOCKED})
     */
    public boolean getAgents(List<String> serials) throws InterruptedException {
        Agent agent;
        // First, make sure all agent exists in the squad, to avoid acquiring and releasing.
        for (String serial : serials) {
            agent = _agents.get(serial);
            if (agent == null)
                return false;
        }
        // after validating all agent exists in the
        for (String serial : serials) {
            agent = _agents.get(serial);
            synchronized (agent) {
                while (!agent.isAvailable()) {
                    agent.wait();
                    agent.acquire();
                }
                agent.acquire();
            }
        }
        return true;
    }

    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     * @pre none
     * @post none
     */
    public List<String> getAgentsNames(List<String> serials) {
        List<String> names = new ArrayList<>();
        for (String serial : serials) {
            names.add(_agents.get(serial).getName());
        }
        return names;
    }

}
