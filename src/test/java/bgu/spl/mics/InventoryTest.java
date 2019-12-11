package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {
    public static final String[] TEST_GADGETS= {"gadget1", "gadget2", "gadget3", "gadget4"};
    public static final String TEST_FILE= "./testInventory.json";
    Inventory inv;
    @BeforeEach
    public void setUp(){
        inv = Inventory.getInstance();
    }

    @Test
    public void testSingleton(){
        assertNotNull(inv, "singleton not created");
        Inventory otherInstance = Inventory.getInstance();
        assertSame(inv, otherInstance, "different instances of singleton Inventory exist!");
    }

    @Test
    public void testLoad(){
        inv.load(TEST_GADGETS);
        for (String gadget: TEST_GADGETS) {
            assertTrue(inv.getItem(gadget), "item is not in the inventory");
            assertFalse(inv.getItem(gadget), "item is in the inventory after it was acquired");
        }
    }
}
