package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Runner implements Runnable {
	private int threadNumber;
	private String sourceFile;
	private String destinationFile;
	private List<TimingResult> listWithResults;
	private CountDownLatch countdown;
	private Runtime rt ;

	public Runner(int threadNumber, String sourceFile,  List<TimingResult> listWithResults, CountDownLatch cdl, Runtime rt) {
		super();
		this.threadNumber = threadNumber;
		this.sourceFile = sourceFile;
		this.destinationFile = sourceFile+Test.FILE_NAME_SUFFIX;
		this.listWithResults = listWithResults;
		this.countdown = cdl;
		this.rt = rt;
	}

	@Override
	public void run() {
		try {
			original();
			this.countdown.countDown();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void original() throws IOException, InterruptedException {
		long startTime = System.currentTimeMillis();
		
		Process proc = rt.exec("mv "+this.sourceFile+" " + this.destinationFile, null, new File("."));
		BufferedInputStream bout = new BufferedInputStream(proc.getInputStream());
		BufferedInputStream berr = new BufferedInputStream(proc.getErrorStream());
		int exitVal = proc.waitFor();
//		printIS(berr);
//		printIS(bout);
//		System.out.println("Exit " + exitVal);
		long endTime = System.currentTimeMillis();
		long duration = endTime - startTime;
		TimingResult tr = new TimingResult();
		tr.threadNumber=this.threadNumber;
		tr.fileName = this.sourceFile;
		tr.time = duration;
		System.out.println("Duration in thread "+threadNumber+" is " + duration);
		this.listWithResults.add(tr);
	}

	public void printIS(InputStream is) throws IOException {
		while (is.available()>0) {
			System.out.print(Character.valueOf((char)is.read()));
		}
	}
}
