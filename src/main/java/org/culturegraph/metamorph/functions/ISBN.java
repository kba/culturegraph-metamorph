package org.culturegraph.metamorph.functions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.culturegraph.metamorph.core.MetamorphException;


/**
 * @author Markus Michael Geipel
 * @status Experimental
 */
public final class ISBN extends AbstractFunction {
	private static final String CHECK = "0123456789X0";
	private static final String INVALID_INPUT = "invalid ISBN: '";
	private static final String ISBN10 = "isbn10";
	private static final String ISBN13 = "isbn13";
    private static final Pattern ISBN_PATTERN = Pattern.compile( "[\\dX]{9,13}" );
	private static final Pattern DIRT_PATTERN = Pattern.compile( "[\\.\\-\\s]" );
	private static final int ISBN10_SIZE = 10;
	private static final int ISBN10_MAGIC = 10;
	private static final int ISBN13_SIZE = 13;
	private static final int ISBN13_MAGIC = 3;
	private static final int ISBN10_MOD = 11;
	private static final int ISBN13_MOD = 10;
	 
	private boolean to10;
	private boolean to13;

	@Override
	public String process(final String value) {
		String result = cleanse(value);
		final int size = result.length();
		
		if(to10 && ISBN13_SIZE==size){
			result = isbn13to10(result);
		}else if(to13 && ISBN10_SIZE==size){
			result = isbn10to13(result);
		}
		return result;
	}
	
	public static String cleanse(final String isbn){
		
		String normValue = isbn.replace('x','X');
		normValue=DIRT_PATTERN.matcher(normValue).replaceAll("");
		final Matcher matcher = ISBN_PATTERN.matcher(normValue);
		if (matcher.find()) {
			return matcher.group();
		}else{
			throw new MetamorphException(INVALID_INPUT + isbn + "'");
		}
	}
	
	public void setTo(final String toString){
		to13 = toString.equalsIgnoreCase(ISBN13);
		to10 = toString.equalsIgnoreCase(ISBN10);
	}

	private static int charToInt(final char cha) {
		return (byte) cha - (byte) '0';
	}


	public static String isbn13to10(final String isbn) {
		final String isbnData = isbn.substring(3, 12);
		int check = 0;
		for (int i = 0; i < ISBN10_SIZE-1; ++i) {
			final int digit = charToInt(isbnData.charAt(i));
			check = check + (ISBN10_MAGIC - i) * digit;
		}
		check = ISBN10_MOD - (check % ISBN10_MOD);
		return isbnData + CHECK.charAt(check);
	}

	public static String isbn10to13(final String isbn) {

		final String isbnData = "978" + isbn.substring(0, ISBN10_SIZE-1);
		int accumulator = 0;
		for (int i = 0; i < ISBN13_SIZE-1; ++i) {
			final int digit = charToInt(isbnData.charAt(i));
			if ((i % 2) == 0) {
				accumulator = accumulator + digit;
			} else {
				accumulator = accumulator + ISBN13_MAGIC * digit;
			}
		}

		accumulator = accumulator % ISBN13_MOD;
		if (accumulator != 0) {
			accumulator = ISBN13_MOD - accumulator;
		}
		return isbnData + CHECK.charAt(accumulator);
	}

	/**
	 * @param isbnIncorrect
	 * @return
	 */
	public static boolean isValid(final String isbnIncorrect) {
		throw new UnsupportedOperationException();
		//return false;
	}
}
