/**
 * 
 */
package org.culturegraph.metamorph.test;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.culturegraph.metamorph.stream.receivers.ValidationException;
import org.culturegraph.metamorph.stream.receivers.WellformednessException;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class MetamorphTestRunner extends Suite {
	
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
	
	private static class TestCaseRunner extends ParentRunner<TestCase> {

		private final Class<?> clazz;
		private final List<TestCase> testCases;
		
		public TestCaseRunner(final Class<?> clazz, final List<TestCase> testCases) 
				throws InitializationError {
			super(clazz);
			
			this.clazz = clazz;
			this.testCases = testCases;
		}

		@Override
		protected List<TestCase> getChildren() {
			return testCases;
		}

		@Override
		protected Description describeChild(final TestCase child) {
			return Description.createTestDescription(clazz, child.getName(), (Annotation[]) null);
		}

		@Override
		protected void runChild(final TestCase child, final RunNotifier notifier) {
			if (child.isIgnore()) {
				notifier.fireTestIgnored(describeChild(child));
			} else {
				notifier.fireTestStarted(describeChild(child));
				try {
					child.run();
				} catch (WellformednessException e) {
					notifier.fireTestFailure(new Failure(describeChild(child), 
							new AssertionError(e)));
				} catch (ValidationException e) {
					notifier.fireTestFailure(new Failure(describeChild(child), 
							new AssertionError(e)));
				} catch (Exception e) {
					notifier.fireTestFailure(new Failure(describeChild(child), e));
				} finally {
					notifier.fireTestFinished(describeChild(child));
				}
			}
		}
	}
	
	private final List<Runner> runners = new ArrayList<Runner>();
	
	public MetamorphTestRunner(final Class<?> clazz) throws InitializationError {
		super(clazz, Collections.<Runner>emptyList());
		for (String testDef: getTestDefinitions(clazz)) {
			final InputStream inputStream = clazz.getResourceAsStream(testDef);
			if(null==inputStream){
				throw new IllegalArgumentException("'" + testDef + "' does not exist!");
			}
			runners.add(new TestCaseRunner(clazz, TestCaseLoader.load(inputStream)));
		}
	}

	@Override
	protected List<Runner> getChildren() {
		return runners;
	}
	
	private String[] getTestDefinitions(final Class<?> clazz){
		final TestDefinitions testDefs = 
				clazz.getAnnotation(TestDefinitions.class);
		if (testDefs == null) {
			return new String[]{clazz.getSimpleName() + ".xml"};
		}
		return testDefs.value();
	}
}