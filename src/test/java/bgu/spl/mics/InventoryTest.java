package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    public static final String[] TEST_GADGETS= {"gadget1", "gadget2", "gadget3", "gadget4"};
    public static final String TEST_FILE= "./testInventory.json";
    Inventory inv;
    @BeforeEach
    public void setUp(){
        inv = Inventory.getInstance();
    }

    @Test
    public void testSingleTon(){
        assertTrue(inv != null, "singleTon not created");
        Inventory otherInstance = Inventory.getInstance();
        assertTrue(inv == otherInstance, "different instances of singleton Inventory exist!");
    }

    @Test
    public void testLoad(){
        inv.load(TEST_GADGETS);
        for (String gadget: TEST_GADGETS) {
            assertTrue(inv.getItem(gadget) == true, "item is not in the inventory");
            assertTrue(inv.getItem(gadget) == false, "item is in the inventory after it was acquired");
        }
    }

    @Test
    public void testPrintToFile(){
        inv.load(TEST_GADGETS);
        inv.printToFile(TEST_FILE);
        for (String gadget: TEST_GADGETS) {
            inv.getItem(gadget); // flush inventory
        }
        String[] tmp = {};
        inv.load(tmp); //TODO complete read to string array json
        for (String gadget: TEST_GADGETS) {
            assertTrue(inv.getItem(gadget) == true, "item is not in the inventory");
        }
    }
}
