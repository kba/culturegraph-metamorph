/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

import org.junit.Test;


/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public class CheckWriterTest {

	@Test
	public void generalCheck() {
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
		w.endChecking();
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
		w.endChecking();
	}
	
	@Test(expected=IllegalStateException.class)
	public void invalidRecordIdentifier() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.literal("LastName", "von Beethoven");
			w.literal("FirstName", "Ludwig");
		w.endRecord();
		
		w.startChecking();
			w.startRecord("0");
				w.literal("LastName", "von Beethoven");
				w.literal("FirstName", "Ludwig");
			w.endRecord();		
		w.endChecking();
	}
	
	@Test
	public void changedRecordOrder() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.literal("Name", "Karl");
		w.endRecord();
		w.startRecord("2");
			w.literal("Name", "Gustav");
		w.endRecord();
		
		w.startChecking();		
			w.startRecord("2");
				w.literal("Name", "Gustav");
			w.endRecord();
			w.startRecord("1");
				w.literal("Name", "Karl");
			w.endRecord();
		w.endChecking();		
	}
	
	@Test
	public void strictRecordOrder() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.literal("Name", "Heinz");
		w.endRecord();
		w.startRecord("2");
			w.literal("Name", "Karl");
		w.endRecord();
		
		w.setStrictRecordOrder(true);
		
		w.startChecking();		
			w.startRecord("1");
				w.literal("Name", "Heinz");
			w.endRecord();
			w.startRecord("2");
				w.literal("Name", "Karl");
			w.endRecord();
		w.endChecking();			
	}
	
	@Test(expected=IllegalStateException.class)
	public void invalidRecordOrderByIdentifier() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.literal("Name", "Heinz");
		w.endRecord();
		w.startRecord("2");
			w.literal("Name", "Karl");
		w.endRecord();
		
		w.setStrictRecordOrder(true);
		
		w.startChecking();		
			w.startRecord("2");
				w.literal("Name", "Karl");
			w.endRecord();
			w.startRecord("1");
				w.literal("Name", "Heinz");
			w.endRecord();
		w.endChecking();			
	}
	
	@Test(expected=IllegalStateException.class)
	public void invalidRecordOrderByContent() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord(null);
			w.literal("Name", "Karl");
		w.endRecord();
		w.startRecord(null);
			w.literal("Name", "Heinz");
		w.endRecord();
		
		w.setStrictRecordOrder(true);
		
		w.startChecking();		
			w.startRecord(null);
				w.literal("Name", "Heinz");
			w.endRecord();
			w.startRecord(null);
				w.literal("Name", "Karl");
			w.endRecord();
		w.endChecking();			
	}
	
	@Test
	public void changedEntityKeyOrder() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.startEntity("Entity-1");
				w.literal("Literal-1", "A");
			w.endEntity();
			w.startEntity("Entity-1");
				w.literal("Literal-2", "B");
			w.endEntity();
			w.startEntity("Entity-2");
				w.literal("Literal-3", "C");
			w.endEntity();
		w.endRecord();
		
		w.startChecking();
		w.startRecord("1");
			w.startEntity("Entity-1");
				w.literal("Literal-2", "B");
			w.endEntity();
			w.startEntity("Entity-2");
				w.literal("Literal-3", "C");
			w.endEntity();
			w.startEntity("Entity-1");
				w.literal("Literal-1", "A");
			w.endEntity();
		w.endRecord();
		w.endChecking();
	}	
	
	@Test
	public void strictEntityKeyOrder() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.startEntity("Entity-1");
				w.literal("Literal-1", "A");
			w.endEntity();
			w.startEntity("Entity-1");
				w.literal("Literal-2", "B");
			w.endEntity();
			w.startEntity("Entity-2");
				w.literal("Literal-3", "C");
			w.endEntity();
		w.endRecord();
		
		w.setStrictKeyOrder(true);
		
		w.startChecking();
		w.startRecord("1");
			w.startEntity("Entity-1");
				w.literal("Literal-1", "A");
			w.endEntity();
			w.startEntity("Entity-1");
				w.literal("Literal-2", "B");
			w.endEntity();
			w.startEntity("Entity-2");
				w.literal("Literal-3", "C");
			w.endEntity();
		w.endRecord();
		w.endChecking();
	}
	
	@Test(expected=IllegalStateException.class)
	public void invalidEntityKeyOrder() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.startEntity("Entity-1");
				w.literal("Literal-1", "A");
			w.endEntity();
			w.startEntity("Entity-1");
				w.literal("Literal-2", "B");
			w.endEntity();
			w.startEntity("Entity-2");
				w.literal("Literal-3", "C");
			w.endEntity();
		w.endRecord();
		
		w.setStrictKeyOrder(true);
		
		w.startChecking();
		w.startRecord("1");
			w.startEntity("Entity-1");
				w.literal("Literal-2", "B");
			w.endEntity();
			w.startEntity("Entity-2");
				w.literal("Literal-3", "C");
			w.endEntity();
			w.startEntity("Entity-1");
				w.literal("Literal-1", "A");
			w.endEntity();
		w.endRecord();
		w.endChecking();
	}

	@Test
	public void changedLiteralValueOrder() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.literal("Name", "Franz");
			w.literal("Name", "Gustav");
		w.endRecord();
		
		w.startChecking();
			w.startRecord("1");
				w.literal("Name", "Gustav");
				w.literal("Name", "Franz");
			w.endRecord();
		w.endChecking();
	}

	@Test
	public void strictLiteralValueOrder() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.literal("Name", "Franz");
			w.literal("Name", "Gustav");
		w.endRecord();
		
		w.setStrictValueOrder(true);
		
		w.startChecking();
			w.startRecord("1");
				w.literal("Name", "Franz");
				w.literal("Name", "Gustav");
			w.endRecord();
		w.endChecking();
	}

	@Test(expected=IllegalStateException.class)
	public void invalidLiteralValueOrder() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord("1");
			w.literal("Name", "Franz");
			w.literal("Name", "Gustav");
		w.endRecord();
		
		w.setStrictValueOrder(true);
		
		w.startChecking();
			w.startRecord("1");
				w.literal("Name", "Gustav");
				w.literal("Name", "Franz");
			w.endRecord();
		w.endChecking();
	}

	@Test
	public void strictLiteralValueOrderRandomRecordOrder() {
		CheckWriter w = new CheckWriter();
		
		w.startRecord(null);
			w.literal("Name", "Franz");
			w.literal("Name", "Gustav");
		w.endRecord();
		w.startRecord(null);
			w.literal("Name", "Gustav");
			w.literal("Name", "Franz");
		w.endRecord();
		
		w.setStrictValueOrder(true);
		
		w.startChecking();
			w.startRecord(null);
				w.literal("Name", "Gustav");
				w.literal("Name", "Franz");
			w.endRecord();
			w.startRecord(null);
				w.literal("Name", "Franz");
				w.literal("Name", "Gustav");
			w.endRecord();
		w.endChecking();
	}
}