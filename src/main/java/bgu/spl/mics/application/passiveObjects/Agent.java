package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Passive data-object representing a information about an agent in MI6. You must not alter any of the given public
 * methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent {

    private AtomicReference<String> _serial;
    private AtomicReference<String> _name;
    private AtomicBoolean _available;

    public Agent(String serial, String name) {
        _name = new AtomicReference<>(name);
        _serial = new AtomicReference<>(serial);
        _available = new AtomicBoolean(true);
    }

    /**
     * Sets the serial number of an agent.
     */
    public void setSerialNumber(String serialNumber) {
        String currentSerial;
        do {
            currentSerial = _serial.get();
        }
        while (!_serial.compareAndSet(currentSerial, serialNumber));

    }

    /**
     * Retrieves the serial number of an agent.
     * <p>
     *
     * @return The serial number of an agent.
     */
    public String getSerialNumber() {
        return _serial.get();
    }

    /**
     * Sets the name of the agent.
     */
    public void setName(String name) {
        String currentName;
        do {
            currentName = _name.get();
        }
        while (!_name.compareAndSet(currentName, name));
    }

    /**
     * Retrieves the name of the agent.
     * <p>
     *
     * @return the name of the agent.
     */
    public String getName() {
        return _name.get();
    }

    /**
     * Retrieves if the agent is available.
     * <p>
     *
     * @return if the agent is available.
     */
    public boolean isAvailable() {
        return _available.get();
    }

    /**
     * Acquires an agent.
     */
    public void acquire() {
        _available.set(false);
    }

    /**
     * Releases an agent.
     */
    public void release() {
        _available.set(true);
    }
}
