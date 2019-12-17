package bgu.spl.mics.application.messages;

import java.util.List;

public class AgentsAvailableObject {
    private List<String> _agentsSerials;
    private List<String> _agentsNames;
    private int _missionDuration;
    private int _MoneypennySerial;
    private boolean _sendMission = false;
    private boolean _terminateMission = false;

    public AgentsAvailableObject(List<String> agentsSerials, int missionDuration) {
        _agentsSerials = agentsSerials;
        _missionDuration = missionDuration;
    }

    /**
     * Retrieves the serial numbers of the agents.
     * <p>
     *
     * @return The serial numbers of the agents.
     */
    public List<String> getAgentsSerials() {
        return _agentsSerials;
    }

    /**
     * Retrieves the names of the agents.
     * <p>
     *
     * @return The names of the agents.
     */
    public List<String> getAgentsNames() {
        return _agentsNames;
    }

    /**
     * sets the names of the agents, Moneypenny acquired for M
     * @param agentsNames agents' names
     */
    public void setAgentsNames(List<String> agentsNames) {
        this._agentsNames = agentsNames;
    }

    /**
     * Retrieves the mission duration of the mission the agents are requested for.
     *
     * @return the time the mission will take in time-ticks
     */
    public int getMissionDuration() {
        return _missionDuration;
    }

    /**
     * Retrieves the unique serial number of Moneypenny who acquired the agents
     * @return Moneypenny's serial
     */
    public int getMoneypennySerial() {
        return _MoneypennySerial;
    }

    /**
     * sets the serial number of the Moneypenny who acquired the agents.
     * @param MoneypennySerial Moneypenny's serial
     */
    public void setMoneypennySerial(int MoneypennySerial) {
        this._MoneypennySerial = MoneypennySerial;
    }

    /**
     * check whether a mission should be sent
     *
     * @return true if mission needs to be sent, false otherwise
     */
    public boolean isSendMission() {
        return _sendMission;
    }

    /**
     * signaling Moneypenny, she should send the agents to the mission
     */
    public void sendMission() {
        _sendMission = true;
    }

    /**
     * check whether a mission should be terminated
     *
     * @return true if mission needs to be terminated, false otherwise
     */
    public boolean isTerminateMission() {
        return _terminateMission;
    }

    /**
     * signaling Moneypenny, she should release the agents acquired to the mission
     */
    public void terminateMission() {
        _terminateMission = true;
    }
}
