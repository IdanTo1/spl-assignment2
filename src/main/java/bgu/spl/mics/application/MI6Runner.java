package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.passiveObjects.MI6RunnerInfo.AgentInfo;
import bgu.spl.mics.application.passiveObjects.MI6RunnerInfo.MI6RunnerInfo;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * This is the Main class of the application. You should parse the input file, create the different instances of the
 * objects, and run the system. In the end, you should output serialized objects.
 */
public class MI6Runner {
    private static final int ARGS_NUM = 3;
    private static final int INPUT_FILE = 0;
    private static final int INVENTORY_FILE = 1;
    private static final int DIARY_FILE = 2;

    public static void main(String[] args) {
        if (args.length != ARGS_NUM) {
            System.err.println("Invalid num of arguments");
            return;
        }
        MI6RunnerInfo info;
        try {
            info = parseInput(args[INPUT_FILE]);
        } catch (FileNotFoundException e) {
            System.err.println("Input file not found");
            return;
        }
        Inventory.getInstance().load(info.getInventory().toArray(new String[]{}));
        List<Agent> tempSquad = new ArrayList<>();
        for (AgentInfo agentInfo : info.getSquad()) {
            tempSquad.add(new Agent(agentInfo.getSerialNumber(), agentInfo.getName()));
        }
        Squad.getInstance().load(tempSquad.toArray(new Agent[]{}));
        int subscribersNum = info.getServices().getMoneypenny() + info.getServices().getM() +
                info.getServices().getIntelligence().size() + 1; // + 1 for Q
        CountDownLatch latch = new CountDownLatch(subscribersNum);
        ExecutorService e = createServices(info, subscribersNum, latch);
        TimeService timeService = new TimeService(info.getServices().getTime(), latch);
        e.execute(timeService);
        while (!timeService.isToTerminate()) {
            try {
                synchronized (timeService) {
                    timeService.wait();
                }
            } catch (InterruptedException ignored) { //main thread will never be interrupted
            }
        }
        e.shutdown();
        try {
            // time is not really needed because all threads will die gracefully, and so random time was chosen
            e.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {}
        // create output files
        try {
            Inventory.getInstance().printToFile(args[INVENTORY_FILE]);
            Diary.getInstance().printToFile(args[DIARY_FILE]);
        } catch (IOException ex) {
            System.err.println("output file caused an IO exception");
            ex.printStackTrace();
        }
    }
    private static MI6RunnerInfo parseInput (String inputFile) throws FileNotFoundException {
        FileReader json;
        json = new FileReader(inputFile);
        Gson gson = new Gson();
        return gson.fromJson(json, MI6RunnerInfo.class);
    }

    private static ExecutorService createServices (MI6RunnerInfo info, int subscribersNum, CountDownLatch latch) {
        ExecutorService e = Executors.newFixedThreadPool(subscribersNum + 1); // + 1 for Time service.
        e.execute(new Q(latch));
        for (int i = 0; i < info.getServices().getMoneypenny(); i++) {
            e.execute(new Moneypenny(i, latch));
        }
        for (int i = 0; i < info.getServices().getM(); i++) {
            e.execute(new M(i, latch));
        }
        for (int i = 0; i < info.getServices().getIntelligence().size(); i++) {
            e.execute(new Intelligence(i, latch, info.getServices().getIntelligence().get(i).getMissions()));
        }
        return e;
    }
}
