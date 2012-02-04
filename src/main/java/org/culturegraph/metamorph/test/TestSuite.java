/**
 * 
 */
package org.culturegraph.metamorph.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>, Markus Geipel
 *
 */
public final class TestSuite extends Suite {
	
	/**
	 * Annotation which defines the test definition files to run when
	 * the class is tested.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public static @interface TestDefinitions {
		/**
		 * @return the files containing the test case definitions
		 */
		String[] value();
	}
	
	public TestSuite(final Class<?> clazz) throws InitializationError {
		super(clazz, loadDefinitions(clazz));
	}
	
	private static List<Runner> loadDefinitions(final Class<?> clazz) throws InitializationError{
		final List<Runner> runners = new ArrayList<Runner>();
		for (String testDef: getTestDefinitionNames(clazz)) {
			runners.add(new TestCaseRunner(clazz, testDef));
		}
		return runners;
	}
	
	private static String[] getTestDefinitionNames(final Class<?> clazz){
		final TestDefinitions testDefs = 
				clazz.getAnnotation(TestDefinitions.class);
		if (testDefs == null) { // if no xmls are given assume an xml with the same name as the class
			return new String[]{clazz.getSimpleName() + ".xml"};
		}
		return testDefs.value();
	}
}