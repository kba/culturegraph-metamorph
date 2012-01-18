/**
 * 
 */
package org.culturegraph.metamorph.stream.receivers;

import org.culturegraph.metamorph.stream.StreamReceiver;
import org.junit.Test;


/**
 * @author Christoph Böhme <c.boehme@dnb.de>
 *
 */
public final class CheckWriterTest {

	@Test
	public void generalCheck() {
		final EventStreamWriter w = new EventStreamWriter();		
		w.startStream();
			generalCheckRecord(w);
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream());
		v.startStream();	
			generalCheckRecord(v);
		v.endStream();
	}
	
	private void generalCheckRecord(StreamReceiver r) {
		r.startRecord("01");
			r.literal("Name", "Karl");
			r.startEntity("Address");
				r.literal("Street", "Meteorstreet 23");
				r.literal("Town", "London");
			r.endEntity();
			r.startEntity("Address");
				r.literal("Town", "Los Angeles");
				r.literal("Street", "Sunrise Avenue 12");
			r.endEntity();
			r.startEntity("Numbers");
			r.startEntity("Number");
				r.literal("Name", "Home");
				r.literal("Number", "+49 1234");
			r.endEntity();
			r.startEntity("Number");
				r.literal("Number", "+49 123");
			r.endEntity();
			r.startEntity("Number");
				r.literal("Name", "Home");
				r.literal("Number", "+49 3453");
			r.endEntity();
		r.endEntity();
		r.endRecord();
		r.startRecord("02");
			r.startEntity("Name");
				r.literal("FirstName", "Franz");
			r.endEntity();
			r.startEntity("Name");
				r.literal("FirstName", "Franz");
				r.literal("LastName", "Kafka");
			r.endEntity();
			r.startEntity("Name");
				r.literal("FirstName", "Franz");
				r.literal("LastName", "Kafka");
			r.endEntity();
		r.endRecord();					
	}
	
	@Test(expected=IllegalStateException.class)
	public void missingLiteral() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord("01");
				w.literal("Name", "von Beethoven");
				w.literal("FirstName", "Ludwig");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream());
		v.startStream();	
			v.startRecord("01");
				v.literal("Name", "von Beethoven");
			v.endRecord();		
		v.endStream();
	}
	
	@Test(expected=IllegalStateException.class)
	public void invalidRecordIdentifier() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord("1");
				w.literal("LastName", "von Beethoven");
				w.literal("FirstName", "Ludwig");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream());
		v.startStream();	
			v.startRecord("0");
				v.literal("LastName", "von Beethoven");
				v.literal("FirstName", "Ludwig");
			v.endRecord();		
		v.endStream();
	}
	
	@Test
	public void changedRecordOrder() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord("1");
				w.literal("Name", "Karl");
			w.endRecord();
			w.startRecord("2");
				w.literal("Name", "Gustav");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream());
		v.startStream();	
			v.startRecord("2");
				v.literal("Name", "Gustav");
			v.endRecord();
			v.startRecord("1");
				v.literal("Name", "Karl");
			v.endRecord();
		v.endStream();		
	}
	
	@Test
	public void strictRecordOrder() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord("1");
				w.literal("Name", "Heinz");
			w.endRecord();
			w.startRecord("2");
				w.literal("Name", "Karl");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream(), true);
		v.startStream();	
			v.startRecord("1");
				v.literal("Name", "Heinz");
			v.endRecord();
			v.startRecord("2");
				v.literal("Name", "Karl");
			v.endRecord();
		v.endStream();			
	}
	
	@Test(expected=IllegalStateException.class)
	public void invalidRecordOrderByIdentifier() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord("1");
				w.literal("Name", "Heinz");
			w.endRecord();
			w.startRecord("2");
				w.literal("Name", "Karl");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream(), true);
		v.startStream();	
			v.startRecord("2");
				v.literal("Name", "Karl");
			v.endRecord();
			v.startRecord("1");
				v.literal("Name", "Heinz");
			v.endRecord();
		v.endStream();
	}
	
	@Test(expected=IllegalStateException.class)
	public void invalidRecordOrderByContent() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord(null);
				w.literal("Name", "Karl");
			w.endRecord();
			w.startRecord(null);
				w.literal("Name", "Heinz");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream(), true);
		v.startStream();	
			v.startRecord(null);
				v.literal("Name", "Heinz");
			v.endRecord();
			v.startRecord(null);
				v.literal("Name", "Karl");
			v.endRecord();
		v.endStream();
	}
	
	@Test
	public void changedEntityKeyOrder() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
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
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream());
		v.startStream();	
			v.startRecord("1");
				v.startEntity("Entity-1");
					v.literal("Literal-2", "B");
				v.endEntity();
				v.startEntity("Entity-2");
					v.literal("Literal-3", "C");
				v.endEntity();
				v.startEntity("Entity-1");
					v.literal("Literal-1", "A");
				v.endEntity();
			v.endRecord();
		v.endStream();
	}	
	
	@Test
	public void strictEntityKeyOrder() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
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
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream(), false, true);
		v.startStream();	
			v.startRecord("1");
				v.startEntity("Entity-1");
					v.literal("Literal-1", "A");
				v.endEntity();
				v.startEntity("Entity-1");
					v.literal("Literal-2", "B");
				v.endEntity();
				v.startEntity("Entity-2");
					v.literal("Literal-3", "C");
				v.endEntity();
			v.endRecord();
		v.endStream();
	}
	
	@Test(expected=IllegalStateException.class)
	public void invalidEntityKeyOrder() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
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
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream(), false, true);
		v.startStream();	
			v.startRecord("1");
				v.startEntity("Entity-1");
					v.literal("Literal-2", "B");
				v.endEntity();
				v.startEntity("Entity-2");
					v.literal("Literal-3", "C");
				v.endEntity();
				v.startEntity("Entity-1");
					v.literal("Literal-1", "A");
				v.endEntity();
			v.endRecord();
		v.endStream();
	}

	@Test
	public void changedLiteralValueOrder() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord("1");
				w.literal("Name", "Franz");
				w.literal("Name", "Gustav");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream());
		v.startStream();		
			v.startRecord("1");
				v.literal("Name", "Gustav");
				v.literal("Name", "Franz");
			v.endRecord();
		v.endStream();
	}

	@Test
	public void strictLiteralValueOrder() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord("1");
				w.literal("Name", "Franz");
				w.literal("Name", "Gustav");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream(), false, false, true);
		v.startStream();		
			v.startRecord("1");
				v.literal("Name", "Franz");
				v.literal("Name", "Gustav");
			v.endRecord();
		v.endStream();
	}

	@Test(expected=IllegalStateException.class)
	public void invalidLiteralValueOrder() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord("1");
				w.literal("Name", "Franz");
				w.literal("Name", "Gustav");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream(), false, false, true);
		v.startStream();		
			v.startRecord("1");
				v.literal("Name", "Gustav");
				v.literal("Name", "Franz");
			v.endRecord();
		v.endStream();
	}

	@Test
	public void strictLiteralValueOrderRandomRecordOrder() {
		final EventStreamWriter w = new EventStreamWriter();
		w.startStream();
			w.startRecord(null);
				w.literal("Name", "Franz");
				w.literal("Name", "Gustav");
			w.endRecord();
			w.startRecord(null);
				w.literal("Name", "Gustav");
				w.literal("Name", "Franz");
			w.endRecord();
		w.endStream();
		
		final EventStreamValidator v = new EventStreamValidator(w.getEventStream(), false, false, true);
		v.startStream();		
			v.startRecord(null);
				v.literal("Name", "Gustav");
				v.literal("Name", "Franz");
			v.endRecord();
			v.startRecord(null);
				v.literal("Name", "Franz");
				v.literal("Name", "Gustav");
			v.endRecord();
		v.endStream();
	}
}