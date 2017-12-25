package phaser.fileSearch;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class FileSearchTask implements Runnable{
	private String pathToSearch;			//folder to search
	private String extensionToSearch;		// extension we are looking for e.g. .log
	private List<String> results;			// list of full path of the files we need.
	private final Phaser phaser;
	
	public FileSearchTask(String pathToSearch, String extensionToSearch, Phaser phaser) {
		super();
		this.pathToSearch = pathToSearch;
		this.extensionToSearch = extensionToSearch;
		this.phaser = phaser;
		results = new ArrayList<String>();
	}

	@Override
	public void run() {
		phaser.arriveAndAwaitAdvance();			//search wont begin until all threads have been created.

		// Phase One
		List<String> allFilesList = findAllFilesWithDesiredExtension(pathToSearch, extensionToSearch);				
		
		// Phase Two
		List<String> requiredFilesList = filterFilesModifiedInLast24Hours(allFilesList);							
		displayFilesInfo(requiredFilesList);
		
		phaser.arriveAndDeregister();
		System.out.printf("%s: Work completed.\n",Thread.currentThread().getName());
	}
	
	private List<String> findAllFilesWithDesiredExtension(String pathToSearch, String extensionToSearch) {
		printStatus();
		
		File fileAtPath = new File(pathToSearch);
		List<String> filesList = Collections.emptyList();
		if(fileAtPath.isDirectory()) {
			processdirectory(fileAtPath);
		}
		
		// Now we have all the files in results list.
		if(results.isEmpty()) {
			System.out.printf("Thread: %s returned no results. \n", Thread.currentThread().getName());
			phaser.arriveAndAwaitAdvance();
			//phaser.arriveAndDeregister();
		} else {
			filesList = results;
			System.out.printf("Thread: %s returned results of size %d. Now waiting to get into next phase. \n", Thread.currentThread().getName(), results.size());
			phaser.arriveAndAwaitAdvance();
		}
		return filesList;
	}
	
	private void processdirectory(File file) {
		File[] listOfFiles = file.listFiles();
		if(listOfFiles != null) {
			for(int index=0; index<listOfFiles.length; index++) {
				if(listOfFiles[index].isDirectory()) {
					processdirectory(listOfFiles[index]);
				} else {
					processFile(listOfFiles[index]);
				}
			}
		}
	}
	
	private void processFile(File file) {
		if(file.getName().endsWith(extensionToSearch)) {
			results.add(file.getAbsolutePath());
		}
	}
	
	private List<String> filterFilesModifiedInLast24Hours(List<String> filesList) {
		printStatus();

		List<String> filteredFiles = new ArrayList<String>();
		long actualTime = new Date().getTime();
		for(int fileIndex = 0; fileIndex<filesList.size(); fileIndex++) {
			File file = new File (filesList.get(fileIndex));
			long lastModifiedTimeOfFile = file.lastModified();
			if (actualTime - lastModifiedTimeOfFile < TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS)) {
				filteredFiles.add(filesList.get(fileIndex));
			}
		}
		return filteredFiles; 
	}
	
	private void displayFilesInfo(List<String> filesToDisplay) {
		for (int i=0; i<filesToDisplay.size(); i++){
			File file=new File(filesToDisplay.get(i));
			System.out.printf("%s: %s\n",Thread.currentThread().getName(),file.getAbsolutePath());
		}
		phaser.arriveAndAwaitAdvance();		// Current thread will wait here until all threads have reached.
	}
	
	private void printStatus() {
		System.out.printf("Phase: %d Thread: %s. \n", phaser.getPhase(), Thread.currentThread().getName());
	}
}
