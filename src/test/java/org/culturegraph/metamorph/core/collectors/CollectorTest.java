package org.culturegraph.metamorph.core.collectors;

import org.culturegraph.metamorph.test.TestSuite;
import org.culturegraph.metamorph.test.TestSuite.TestDefinitions;
import org.junit.runner.RunWith;

/**
 * @author Markus Michael Geipel
 */
@RunWith(TestSuite.class)
@TestDefinitions({"CombineTest.xml", "GroupTest.xml", "ChooseTest.xml", "EntityTest.xml", "ConcatTest.xml", "Nested.xml", "Misc.xml"})
public final class CollectorTest {/*bind to xml test*/}
