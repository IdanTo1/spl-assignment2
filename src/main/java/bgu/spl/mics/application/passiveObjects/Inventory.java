package bgu.spl.mics.application.passiveObjects;

import java.util.*;

/**
 * That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton. You must not alter any of the given public methods
 * of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 *
 * @inv Inventory is a singleton - only one instance of this class can be initialized
 */
public class Inventory {
    private List<String> gadgets;
    private static Inventory instance = null;

    /**
     * Retrieves the single instance of this class.
     *
     * @pre none
     * @post return != null
     */
    public static Inventory getInstance() {
        //TODO: Implement this
        return null;
    }

    private Inventory() {
        // only one instance of Q exists, and only Q has access to inventory, so no need for synchronization.
        gadgets = new ArrayList<String>();
    }

    /**
     * Initializes the inventory. This method adds all the items given to the gadget inventory.
     * <p>
     *
     * @param inventory Data structure containing all data necessary for initialization of the inventory.
     *
     * @pre none
     * @post (for str : inventory { getItem ( str)==true)
     */
    public void load(String[] inventory) {
        //TODO: Implement this
    }

    /**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     *
     * @param gadget Name of the gadget to check if available
     *
     * @return ‘false’ if the gadget is missing, and ‘true’ otherwise
     *
     * @pre none
     * @post getItem(gadget) == false
     */
    public boolean getItem(String gadget) {
        //TODO: Implement this
        return true;
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<Gadget> which is a List of all the reports in the diary.
     * This method is called by the main method in order to generate the output.
     *
     * @pre none
     * @post none
     */
    public void printToFile(String filename) {
        //TODO: Implement this
    }
}
