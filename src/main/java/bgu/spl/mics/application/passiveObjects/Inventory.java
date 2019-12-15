package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        if (instance == null)
            instance = new Inventory();
        return instance;
    }

    private Inventory() {
        // only one instance of Q exists, and only Q has access to inventory, so no need for synchronization.
        gadgets = new ArrayList<>();
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
        gadgets.addAll(Arrays.asList(inventory));
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
        if (gadgets.contains(gadget)) {
            gadgets.remove(gadget);
            return true;
        }
        return false;
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
        Gson gson = new Gson();
        String gadgetsToPrint = gson.toJson(gadgets);
        PrintWriter writer = null;
        Path file = Paths.get("the-file-name.txt");
        try {
            Files.write(file, Collections.singleton(gadgetsToPrint), StandardCharsets.UTF_8);
        } catch (IOException e) {}
    }
}
