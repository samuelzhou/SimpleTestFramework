package samuel.test.framework.core;

import java.util.HashMap;

import samuel.test.framework.config.Configuration;
import samuel.test.framework.exception.InvalidConfigurationException;

/**
 * Test Case base class, every test case should be derived from this class.
 * The class contains the routines that can be used by every test case.
 *
 * @author sazhou
 *
 */
public abstract class TestCase {
	private HashMap<String, String> testParamMap;
	protected boolean isSupportChrome;
	protected TestResult result = TestResult.UNRESOLVED;

	/**
	 * The constructor that each test case will use
	 */
	public TestCase() {
		try {
			Configuration config = Configuration.getConfiguration();
			testParamMap = config.getTestCaseConfigMap(this.getClass().getSimpleName());
			isSupportChrome = config.isSupportChrome();
		} catch (InvalidConfigurationException e) {
				e.printStackTrace();
		}
	}

	/**
	 * Get the parameter value by paramter name.
	 *
	 * @param paramName
	 * @return
	 * 		The parameter value.
	 */
	public String getParamValue(String paramName) {
		return testParamMap.get(paramName);
	}

	/**
	 * Print test result at the end of each test case
	 */
	protected void logResult() {
		System.out.println("================ " + this.getClass().getSimpleName() + " : " + result.name() + " ================");
	}

	protected void pass() {
		result = TestResult.PASS;
		logResult();
	}
	
	protected void fail() {
		result = TestResult.FAIL;
		logResult();
	}

	/**
	 * Get the result of a test case
	 * @return
	 * 		The string present the test result
	 */
	public String getResult() {
		return result.name();
	}

	/**
	 * The main function to execute test case, every subclass should implement it.
	 */
	public abstract void runTest();
}
