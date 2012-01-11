/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public class CheckWriterTest {

	@Test
	public void testSuccessfulChecking() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("01");
			w.literal("Name", "Karl");
			w.startEntity("Address");
				w.literal("Street", "Meteorstreet 23");
				w.literal("Town", "London");
			w.endEntity();
			w.startEntity("Address");
				w.literal("Street", "Sunrise Avenue 12");
				w.literal("Town", "Los Angeles");
			w.endEntity();
			w.startEntity("Numbers");
				w.startEntity("Number");
					w.literal("Name", "Home");
					w.literal("Number", "+49 1234");
				w.endEntity();
				w.startEntity("Number");
					w.literal("Name", "Home");
					w.literal("Number", "+49 3453");
				w.endEntity();
				w.startEntity("Number");
					w.literal("Number", "+49 123");
				w.endEntity();
			w.endEntity();
		w.endRecord();
		w.startRecord("02");
			w.startEntity("Name");
				w.literal("FirstName", "Franz");
				w.literal("LastName", "Kafka");
			w.endEntity();
			w.startEntity("Name");
				w.literal("FirstName", "Franz");
			w.endEntity();
			w.startEntity("Name");
				w.literal("FirstName", "Franz");
				w.literal("LastName", "Kafka");
			w.endEntity();
		w.endRecord();
		
		w.startChecking();	
			w.startRecord("01");
				w.literal("Name", "Karl");
				w.startEntity("Address");
					w.literal("Street", "Meteorstreet 23");
					w.literal("Town", "London");
				w.endEntity();
				w.startEntity("Address");
					w.literal("Town", "Los Angeles");
					w.literal("Street", "Sunrise Avenue 12");
				w.endEntity();
				w.startEntity("Numbers");
				w.startEntity("Number");
					w.literal("Name", "Home");
					w.literal("Number", "+49 1234");
				w.endEntity();
				w.startEntity("Number");
					w.literal("Number", "+49 123");
				w.endEntity();
				w.startEntity("Number");
					w.literal("Name", "Home");
					w.literal("Number", "+49 3453");
				w.endEntity();
			w.endEntity();
			w.endRecord();
			w.startRecord("02");
				w.startEntity("Name");
					w.literal("FirstName", "Franz");
				w.endEntity();
				w.startEntity("Name");
					w.literal("FirstName", "Franz");
					w.literal("LastName", "Kafka");
				w.endEntity();
				w.startEntity("Name");
					w.literal("FirstName", "Franz");
					w.literal("LastName", "Kafka");
				w.endEntity();
			w.endRecord();			
		assertTrue(w.endChecking());
	}
	
	@Test(expected=IllegalStateException.class)
	public void missingLiteral() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("01");
			w.literal("Name", "von Beethoven");
			w.literal("FirstName", "Ludwig");
		w.endRecord();
		
		w.startChecking();
			w.startRecord("01");
				w.literal("Name", "von Beethoven");
			w.endRecord();		
		assertTrue(w.endChecking());
	}
}
