package test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {
	public final static int NUM = 2000;
	public final static String FILE_NAME_PREFIX = "TheQuickBrownFoxJumpsOverTheLazyDog";
	public final static String FILE_NAME_SUFFIX = ".moved";
	ExecutorService executor = Executors.newFixedThreadPool(20);

	public static void main(String[] args) {
		long overallStarttime= System.currentTimeMillis();
		int numberOfFiles = NUM;
		List<TimingResult> listWithResults = Collections.synchronizedList(new ArrayList<TimingResult>());

		Test t = new Test();
		if (args.length == 1) {
			numberOfFiles = Integer.parseInt(args[0]);
		}
		System.out.println("Number of files=" + numberOfFiles);
		CountDownLatch cdl = new CountDownLatch(numberOfFiles);

		List<String> names = t.createFileNames(numberOfFiles);
		try {
			t.createFiles(names);
		
			t.runThem(numberOfFiles, names, listWithResults, cdl);
			cdl.await(180,TimeUnit.SECONDS);
			t.deleteFiles(names);
			t.checkResults(listWithResults);
			System.out.println("This took ms:"+ (System.currentTimeMillis()-overallStarttime));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void checkResults(List<TimingResult> listWithResults) {
		Collections.sort(listWithResults, new Comparator<TimingResult>() {
			@Override
			public int compare(TimingResult o1, TimingResult o2) {
				return (int) (o1.time - o2.time);
			}
		});
		for (TimingResult timingResult : listWithResults) {
			System.out.println(timingResult.fileName + " took " + timingResult.time);
		}
		System.out.println("Instances: " + listWithResults.size());
		this.executor.shutdown();
	}

	private void runThem(int numberOfThreads, List<String> names, List<TimingResult> listWithResults,
			CountDownLatch cdl) {
		Runtime rt = Runtime.getRuntime();
		for (int i = 0; i < names.size(); i++) {
			String filename = names.get(i);
			executor.submit(new Thread(new Runner(i, filename, listWithResults, cdl,rt)));
		}
	}

	public List<String> createFileNames(int numberOfFiles) {
		List<String> names = new LinkedList<>();
		for (int i = 0; i < numberOfFiles; i++) {
			names.add(FILE_NAME_PREFIX + "." + i);
		}
		return names;
	}

	public void createFiles(List<String> files) throws Exception {
		for (String string : files) {
			FileOutputStream fos = new FileOutputStream(string);
			fos.write(FILE_NAME_PREFIX.getBytes());
			fos.flush();
			fos.close();
		}
	}

	public void deleteFiles(List<String> files) throws Exception {
		for (String sourceFile : files) {
			String destFile = sourceFile + Test.FILE_NAME_SUFFIX;
			File f = new File(destFile);
			f.delete();
		}
	}

}

