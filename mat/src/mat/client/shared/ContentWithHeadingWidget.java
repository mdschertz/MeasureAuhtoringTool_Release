package mat.client.shared;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ContentWithHeadingWidget.
 */
public class ContentWithHeadingWidget extends Composite{
	
	
	HorizontalPanel buttonPanel = new HorizontalPanel();
	
	/** The code list info. */
	SimplePanel codeListInfo = new SimplePanel();
	
	/** The content. */
	SimplePanel content = new SimplePanel();
	
	/** The embedded link holder. */
	SimplePanel embeddedLinkHolder = new SimplePanel();
	
	/** The footer. */
	SimplePanel footer = new SimplePanel();
	
	/** The heading. */
	HTML heading = new HTML();
	/** The header holder. */
	FocusableWidget headingHolder = new FocusableWidget(heading);
	FlowPanel vPanel = new FlowPanel();
	/**
	 * Instantiates a new content with heading widget.
	 */
	public ContentWithHeadingWidget() {
		codeListInfo.getElement().setId("codeListInfo_SimplePanel");
		content.getElement().setId("content_SimplePanel");
		footer.getElement().setId("footer_SimplePanel");
		vPanel.getElement().setId("vPanel_FlowPanel");
		
		heading.addStyleName("contentWithHeadingHeader");
		heading.addStyleName("leftAligned");
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.add(heading);
		buttonPanel.setStylePrimaryName("headingButton");
		mainPanel.add(buttonPanel);
		vPanel.add(mainPanel);
		vPanel.add(codeListInfo);
		vPanel.add(content);
		vPanel.addStyleName("contentWithHeadingPanel");
		vPanel.add(footer);
		footer.addStyleName("returnLink");
		SimplePanel sPanel = new SimplePanel();
		sPanel.getElement().setId("sPanel_SimplePanel");
		sPanel.add(vPanel);
		initWidget(sPanel);
	}
	
	/**
	 * Instantiates a new content with heading widget.
	 * 
	 * @param contentWidget
	 *            the content widget
	 * @param headingStr
	 *            the heading str
	 * @param linkName
	 *            the link name
	 */
	public ContentWithHeadingWidget(Widget contentWidget, String headingStr,String linkName) {
		this();
		setHeading(headingStr,linkName);
		setContent(contentWidget);
	}
	
	/**
	 * @return the buttonPanel
	 */
	public HorizontalPanel getButtonPanel() {
		return buttonPanel;
	}
	
	/*public void setEmbeddedLink(String linkName){
		Widget w = SkipListBuilder.buildEmbeddedLink(linkName);
		embeddedLinkHolder.clear();
		embeddedLinkHolder.add(w);
	}*/
	
	
	/** @param text
	 * @param linkName
	 * @param createElement
	 * @param searchButton */
	public void setButtonPanel( Button createElement, Button searchButton) {
		
		buttonPanel.add(createElement);
		buttonPanel.add(searchButton);
	}
	
	/**
	 * @param buttonPanel the buttonPanel to set
	 */
	public void setButtonPanel(HorizontalPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
	}
	
	/**
	 * Sets the code list info.
	 * 
	 * @param w
	 *            the new code list info
	 */
	public void setCodeListInfo(Widget w){
		codeListInfo.clear();
		codeListInfo.add(w);
	}
	
	/**
	 * Sets the content.
	 * 
	 * @param w
	 *            the new content
	 */
	public void setContent(Widget w) {
		content.clear();
		content.add(w);
	}
	
	/** Sets the footer.
	 * 
	 * @param w the new footer */
	public void setFooter(Widget w) {
		footer.clear();
		footer.add(w);
	}
	
	/**
	 * Sets the heading.
	 * 
	 * @param text
	 *            the text
	 * @param linkName
	 *            the link name
	 */
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h1>" + text + "</h1>");
	}
	
}
