package bgu.spl.mics.application.messages;

public class GadgetAvailableObject {
    private String _gadget;
    int _Qtime = 0;
    boolean _gadgetExists = false;

    GadgetAvailableObject(String gadget) {
        _gadget = gadget;
    }

    /**
     * Retrieves the gadget to the mission
     * @return gadget string
     */
    public String getGadget () {
        return _gadget;
    }

    /**
     * Retrieves the information whether Q has the gadget in inventory or not.
     * @return true if gadget exists, false otherwise
     */
    public boolean isGadgetExists() {
        return _gadgetExists;
    }

    /**
     * sets the gadgetExists flag to true if Q has it in the inventory.
     */
    public void setGadgetExists() {
        this._gadgetExists = true;
    }

    /**
     * Retrieves the time Q got the request
     * @return time in time-ticks.
     */
    public int getQtime() {
        return _Qtime;
    }

    /**
     * sets time Q got the request
     * @param Qtime time in time-ticks.
     */
    public void setQtime(int Qtime) {
        this._Qtime = Qtime;
    }
}
