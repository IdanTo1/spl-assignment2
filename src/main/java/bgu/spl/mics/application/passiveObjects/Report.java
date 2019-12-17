package bgu.spl.mics.application.passiveObjects;

import java.util.List;

/**
 * Passive data-object representing a delivery vehicle of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Report {
    private String _missionName;
    private int _M;
    private List<String> _agentsSerialNumbers;
    private String _gadgetName;
    private int _timeIssued;
    private int _timeCreated;
    private int _MoneyPenny;
    private List<String> _agentsNames;
    private int _qTime;

    private void initialize(String missionName, int M, List<String> agentsSerialNumbers, String gadgetName, int timeIssued,
                            int timeCreated) {
        _missionName = missionName;
        _M = M;
        _agentsSerialNumbers = agentsSerialNumbers;
        _gadgetName = gadgetName;
        _timeIssued = timeIssued;
        _timeCreated = timeCreated;
        _MoneyPenny = -1;
        _agentsNames = null;
        _qTime = -1;
    }

    public Report(MissionInfo missionInfo, int M, int timeCreated) {
        initialize(missionInfo.getMissionName(), M, missionInfo.getSerialAgentsNumbers(), missionInfo.getGadget(),
                missionInfo.getTimeIssued(), timeCreated);
    }

    /**
     * Retrieves the mission name.
     */
    public String getMissionName() {
        return _missionName;
    }

    /**
     * Sets the mission name.
     */
    public void setMissionName(String missionName) {
        _missionName = missionName;
    }

    /**
     * Retrieves the M's id.
     */
    public int getM() {
        return _M;
    }

    /**
     * Sets the M's id.
     */
    public void setM(int m) {
        _M = m;
    }

    /**
     * Retrieves the Moneypenny's id.
     */
    public int getMoneypenny() {
        return _MoneyPenny;
    }

    /**
     * Sets the Moneypenny's id.
     */
    public void setMoneypenny(int moneypenny) {
        _MoneyPenny = moneypenny;
    }

    /**
     * Retrieves the serial numbers of the agents.
     * <p>
     *
     * @return The serial numbers of the agents.
     */
    public List<String> getAgentsSerialNumbers() {
        return _agentsSerialNumbers;
    }

    /**
     * Sets the serial numbers of the agents.
     */
    public void setAgentsSerialNumbers(List<String> agentsSerialNumbers) {
        // Shallow copy is fine here because the list doesn't change over time
        _agentsSerialNumbers = agentsSerialNumbers;
    }

    /**
     * Retrieves the agents names.
     * <p>
     *
     * @return The agents names.
     */
    public List<String> getAgentsNames() {
        return _agentsNames;
    }

    /**
     * Sets the agents names.
     */
    public void setAgentsNames(List<String> agentsNames) {
        _agentsNames = agentsNames;
    }

    /**
     * Retrieves the name of the gadget.
     * <p>
     *
     * @return the name of the gadget.
     */
    public String getGadgetName() {
        return _gadgetName;
    }

    /**
     * Sets the name of the gadget.
     */
    public void setGadgetName(String gadgetName) {
        _gadgetName = gadgetName;
    }

    /**
     * Retrieves the time-tick in which Q Received the GadgetAvailableEvent for that mission.
     */
    public int getQTime() {
        return _qTime;
    }

    /**
     * Sets the time-tick in which Q Received the GadgetAvailableEvent for that mission.
     */
    public void setQTime(int qTime) {
        _qTime = qTime;
    }

    /**
     * Retrieves the time when the mission was sent by an Intelligence Publisher.
     */
    public int getTimeIssued() {
        return _timeIssued;
    }

    /**
     * Sets the time when the mission was sent by an Intelligence Publisher.
     */
    public void setTimeIssued(int timeIssued) {
        _timeIssued = timeIssued;
    }

    /**
     * Retrieves the time-tick when the report has been created.
     */
    public int getTimeCreated() {
        return _timeCreated;
    }

    /**
     * Sets the time-tick when the report has been created.
     */
    public void setTimeCreated(int timeCreated) {
        _timeCreated = timeCreated;
    }
}
