package org.culturegraph.metamorph.core2.functions;


/**
 * @author Markus Michael Geipel
 */
public final class HtmlAnchor extends AbstractCompose {
	
	private String title;
	
	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public String process(final String value) {
		final String title;
		if(this.title==null){
			title=value;
		}else{
			title = this.title;
		}
		
		return "<a href=\""+getPrefix() + value + getPostfix() + "\">" + title + "</a>";
	}
}
