package bgu.spl.mics.application.passiveObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
	private static Diary _instance = new Diary();
	private List<Report> reports;
	private AtomicInteger _total;
	// No need to use anything other than synchronization because there will only be a write to the diary twice per
	// mission, once in the increment and the other when adding the report to the diary

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return _instance;
	}

	private Diary() {
		reports = new ArrayList<>();
		_total = new AtomicInteger(0);
	}

	// The synchronization in the class is only when accessing the _diary object, since _total is implemented lock-free
	/**
	 *
	 * @return The list of reports
	 */
	public synchronized List<Report> getReports() {
		return reports;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public synchronized void addReport(Report reportToAdd){
		reports.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public synchronized void printToFile(String filename) throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String reportsToPrint = gson.toJson(this);
		Path file = Paths.get(filename);
		Files.write(file, Collections.singleton(reportsToPrint), StandardCharsets.UTF_8);
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public int getTotal(){
		return _total.get();
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public void incrementTotal(){
		int oldTotal;
		do {
			oldTotal = _total.get();
		} while(!_total.compareAndSet(oldTotal, oldTotal+1));
	}
}
