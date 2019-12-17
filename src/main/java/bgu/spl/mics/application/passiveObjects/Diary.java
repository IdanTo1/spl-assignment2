package bgu.spl.mics.application.passiveObjects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.gson.Gson;
import java.io.PrintWriter;
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
	private List<Report> _diary;
	private int _total;
	// No need to use anything other than synchronization because there will only be a write to the diary twice per
	// mission, once in the increment and the other when adding the report to the diary

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Diary getInstance() {
		return _instance;
	}

	private Diary() {
		_diary = new ArrayList<>();
		// Using Read-Write Lock to safeguard the diary from multiple threads accessing it at the same time
		_total = 0;
	}

	/**
	 *
	 * @return The list of reports
	 */
	public synchronized List<Report> getReports() {
		return _diary;
	}

	/**
	 * adds a report to the diary
	 * @param reportToAdd - the report to add
	 */
	public synchronized void addReport(Report reportToAdd){
		_diary.add(reportToAdd);
	}

	/**
	 *
	 * <p>
	 * Prints to a file name @filename a serialized object List<Report> which is a
	 * List of all the reports in the diary.
	 * This method is called by the main method in order to generate the output.
	 */
	public synchronized void printToFile(String filename) throws IOException {
		Gson gson = new Gson();
		String reportsToPrint = gson.toJson(_diary);
		Path file = Paths.get(filename);
		Files.write(file, Collections.singleton(reportsToPrint), StandardCharsets.UTF_8);
	}

	/**
	 * Gets the total number of received missions (executed / aborted) be all the M-instances.
	 * @return the total number of received missions (executed / aborted) be all the M-instances.
	 */
	public synchronized int getTotal(){
		return _total;
	}

	/**
	 * Increments the total number of received missions by 1
	 */
	public synchronized void incrementTotal(){
		_total++;
	}
}
