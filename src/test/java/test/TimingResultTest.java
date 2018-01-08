package test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TimingResultTest extends TestCase {

	public TimingResultTest(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(TimingResultTest.class);
	}

	public void testApp() {
		TimingResult tr1 = new TimingResult(123L, 456, "Test");
		TimingResult tr2 = new TimingResult(123L, 456, "Test");
		TimingResult tr3 = new TimingResult(4123L, 4567, "Test!");

		assertTrue(tr1.equals(tr2));
		assertFalse(tr1.equals(tr3));

	}
}
