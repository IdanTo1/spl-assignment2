package bgu.spl.mics.application.passiveObjects;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 * @inv Squad is a singleton - only one instance of this class can be initialized
 */
public class Squad {
	private static Squad instance = null;
	private Map<String, Agent> agents;

	/**
	 * Retrieves the single instance of this class.
	 * @pre none
	 * @post return != null
	 */
	public static Squad getInstance() {
		if (instance == null)
			instance = new Squad();
		return instance;
	}

	private Squad() {
		// squad is accessed by Moneypenny, which my have several instances, so thread-safe structure is needed.
		agents = new ConcurrentHashMap<String, Agent>();
	}
	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 * @pre none
	 * @post (for agent : agents {getAgents(agent.getSerialNumber) == true})
	 */
	public void load (Agent[] agents) {
		// TODO Implement this
	}

	/**
	 * Releases agents.
	 * @pre (for serial : serials {getAgents([serial]).state == BLOCKED})
	 * @post (getAgents(serials) == true)
	 */
	public void releaseAgents(List<String> serials){
		// TODO Implement this
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 * @pre (for serial : serials {getAgents([serial]).state == BLOCKED})
	 * @post (current_time >= start_time + time && getAgents(serials) == true)
	 */
	public void sendAgents(List<String> serials, int time){
		// TODO Implement this
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 * @pre none
	 * @post (for serial : serials {getAgents([serial]).state == BLOCKED})
	 */
	public boolean getAgents(List<String> serials){
		// TODO Implement this
		return false;
	}

    /**
     * gets the agents names
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
	 * @pre none
	 * @post none
     */
    public List<String> getAgentsNames(List<String> serials){
        // TODO Implement this
	    return null;
    }

}
