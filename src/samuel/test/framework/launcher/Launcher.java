package samuel.test.framework.launcher;

import java.util.HashMap;

import samuel.test.framework.config.Configuration;
import samuel.test.framework.core.*;

public class Launcher {
	/**
	 * The main entry for launching the test suite
	 * @param args
	 * 		The configuration file of the test suite.
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("Cannot get the configuration file from argument, exiting ...");
			System.exit(1);
		}
		HashMap<String, String> testResults = new HashMap<String, String>();
		try {
			Configuration conf = Configuration.getConfiguration(args[0]);
			TestCase tc;
			for (String tcName : conf.getTestCaseNames()) {
				Class<?> clazz = Class.forName("samuel.test.framework.testcases." + tcName);
				tc = (TestCase)clazz.newInstance();
				tc.runTest();
				testResults.put(tcName, tc.getResult());
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Test case: " + cnfe.getMessage() + " doesn't exist!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println();
		System.out.println("Summary:");
		HashMap<String, Integer> rSummary = new HashMap<String, Integer>();
		for (String str : testResults.keySet()) {
			String result = testResults.get(str);
			if (rSummary.containsKey(result)) {
				rSummary.put(result, rSummary.get(result) + 1);
			} else {
				rSummary.put(result, 1);
			}
			System.out.println("\t" + str + " :\t" + result);
		}
		System.out.println();
		for (String str : rSummary.keySet()) {
			System.out.print(str + " : " + rSummary.get(str) + " ");
		}
		System.out.println();
	}
}
