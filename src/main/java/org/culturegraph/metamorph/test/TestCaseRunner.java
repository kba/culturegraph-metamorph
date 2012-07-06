package org.culturegraph.metamorph.test;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;

import org.culturegraph.metastream.util.FormatException;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;

/**
 * @author Christoph Böhme <c.boehme@dnb.de>, Markus Michael Geipel
 *
 */
final class TestCaseRunner extends ParentRunner<TestCase> {

	private final Class<?> clazz;
	private final List<TestCase> testCases;
	private final String testDefinition;

	
	public TestCaseRunner(final Class<?> clazz,  final String testDefinition) 
			throws InitializationError {
		super(clazz);
		this.clazz = clazz;
		final InputStream inputStream = clazz.getResourceAsStream(testDefinition);
		if(null==inputStream){
			throw new IllegalArgumentException("'" + testDefinition + "' does not exist!");
		}
		this.testCases = TestCaseLoader.load(inputStream);
		this.testDefinition = testDefinition;
	}
	
	@Override
	protected String getName() {
		return testDefinition;
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
			} catch (FormatException e) {
				notifier.fireTestFailure(new Failure(describeChild(child), 
						new AssertionError(e)));
			} catch (Throwable e) {
				notifier.fireTestFailure(new Failure(describeChild(child), e));
			} finally {
				notifier.fireTestFinished(describeChild(child));
			}
		}
	}
}