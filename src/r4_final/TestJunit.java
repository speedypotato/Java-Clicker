package r4_final;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Class for JUnit Testing
 */
public class TestJunit {
	public static void main(String[] args) {
		Class[] testClasses = {StartFrame.class, EditorFrame.class};
		for (Class c : testClasses) {
			System.out.println("Testing " + c.toString());
			Result r = JUnitCore.runClasses(c);
			for (Failure f : r.getFailures()) System.out.println(f.toString());
			if (r.wasSuccessful()) System.out.println(c.toString() + " passed testing.");
			else System.out.println(c.toString() + " failed testing.");
			System.out.println();
		}
	}
}
