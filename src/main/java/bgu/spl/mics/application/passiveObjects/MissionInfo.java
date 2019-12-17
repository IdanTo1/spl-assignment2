package bgu.spl.mics.application.passiveObjects;

import javax.print.DocFlavor;
import java.util.List;

/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {
	private String _missionName, _gadget;
	private List<String>  _serialAgentsNumber;
	private int _timeIssued, _timeExpired, _duration;

    /**
     * Sets the name of the mission.
     */
    public void setMissionName(String missionName) {
        _missionName = missionName;
    }

	/**
     * Retrieves the name of the mission.
     */
	public String getMissionName() {
		return _missionName;
	}

    /**
     * Sets the serial agent number.
     */
    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
        _serialAgentsNumber = serialAgentsNumbers;
    }

	/**
     * Retrieves the serial agent number.
     */
	public List<String> getSerialAgentsNumbers() {
		return _serialAgentsNumber;
	}

    /**
     * Sets the gadget name.
     */
    public void setGadget(String gadget) {
        _gadget = gadget;
    }

	/**
     * Retrieves the gadget name.
     */
	public String getGadget() {
		return _gadget;
	}

    /**
     * Sets the time the mission was issued in milliseconds.
     */
    public void setTimeIssued(int timeIssued) {
        _timeIssued = timeIssued;
    }

	/**
     * Retrieves the time the mission was issued in milliseconds.
     */
	public int getTimeIssued() {
		return _timeIssued;
	}

    /**
     * Sets the time that if it that time passed the mission should be aborted.
     */
    public void setTimeExpired(int timeExpired) {
        _timeExpired = timeExpired;
    }

	/**
     * Retrieves the time that if it that time passed the mission should be aborted.
     */
	public int getTimeExpired() {
		return _timeExpired;
	}

    /**
     * Sets the duration of the mission in time-ticks.
     */
    public void setDuration(int duration) {
        _duration = duration;
    }

	/**
	 * Retrieves the duration of the mission in time-ticks.
	 */
	public int getDuration() {
		return _duration;
	}
}
