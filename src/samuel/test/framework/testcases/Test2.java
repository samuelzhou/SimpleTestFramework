package samuel.test.framework.testcases;

import samuel.test.framework.core.TestCase;

public class Test2 extends TestCase {
	@Override
	public void runTest() {
		System.out.println("The parameters passed to this test case: ");
		System.out.println("StartFromURL :" + getParamValue("StartFromURL"));
		System.out.println("AnotherParam :" + getParamValue("AnotherParam"));
		fail();
	}
}
