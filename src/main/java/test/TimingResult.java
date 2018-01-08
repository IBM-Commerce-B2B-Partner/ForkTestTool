package test;

public class TimingResult {
	long time;
	int threadNumber;
	String fileName;

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TimingResult) {
			TimingResult that = (TimingResult) obj;
			if (this.time != that.time)
				return false;
			if (this.threadNumber != that.threadNumber)
				return false;
			if (!this.fileName.equals(fileName))
				return false;
			return true;
		} else {
			return false;
		}
	}

	public TimingResult(long time, int threadNumber, String fileName) {
		super();
		this.time = time;
		this.threadNumber = threadNumber;
		this.fileName = fileName;
	}
}
