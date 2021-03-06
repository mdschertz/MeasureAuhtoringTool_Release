package mat.client.clause.clauseworkspace.view;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.MatContext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.MenuItemSeparator;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.xml.client.Node;

/**
 * The Class ClauseWorkspaceContextMenu.
 */
public class ClauseWorkspaceContextMenu {
	
	/**
	 * The Interface Template.
	 */
	interface Template extends SafeHtmlTemplates {
	    
    	/**
		 * Menu table.
		 * 
		 * @param name
		 *            the name
		 * @param shortCut
		 *            the short cut
		 * @return the safe html
		 */
    	@Template("<table width=\"100%\"><tr><td>{0}</td><td align=\"right\">{1}</td></tr></table>")
	    SafeHtml menuTable(String name, String shortCut);
	}
	
	/** The Constant template. */
	private static final Template template = GWT.create(Template.class);

	/** The xml tree display. */
	XmlTreeDisplay xmlTreeDisplay;

	/** The add menu. */
	MenuItem addMenu;
	
	/** The add menu lhs. */
	MenuItem addMenuLHS;
	
	/** The add menu rhs. */
	MenuItem addMenuRHS;

	/** The copy menu. */
	MenuItem copyMenu;

	/** The paste menu. */
	MenuItem pasteMenu;

	/** The delete menu. */
	MenuItem deleteMenu;
	
	/** The cut menu. */
	MenuItem cutMenu;
	
	/** The edit menu. */
	MenuItem editMenu;
	
	/** The edit qdm menu. */
	MenuItem editQDMMenu;
	
	/** The popup menu bar. */
	MenuBar popupMenuBar = new MenuBar(true);
	
	/** The sub menu bar. */
	MenuBar subMenuBar;
	
	/** The separator. */
	MenuItemSeparator separator = new MenuItemSeparator();

	/** The popup panel. */
	PopupPanel popupPanel;
	
	/** The paste cmd. */
	Command pasteCmd;
	
	/** The expand menu. */
	MenuItem expandMenu;
	
	
	/**
	 * Instantiates a new clause workspace context menu.
	 * 
	 * @param treeDisplay
	 *            the tree display
	 * @param popPanel
	 *            the pop panel
	 */
	public ClauseWorkspaceContextMenu(XmlTreeDisplay treeDisplay, PopupPanel popPanel) {
		this.xmlTreeDisplay = treeDisplay;
		xmlTreeDisplay.setDirty(false);
		this.popupPanel = popPanel;
		Command copyCmd = new Command() {
			public void execute( ) {
				popupPanel.hide();
				xmlTreeDisplay.copy();
			}
		};
		copyMenu = new MenuItem(template.menuTable("Copy", "Ctrl+C"), copyCmd);
		
		Command deleteCmd = new Command() {
			public void execute( ) {
				xmlTreeDisplay.setDirty(true);
				popupPanel.hide();
				xmlTreeDisplay.removeNode();
			}
		};
		deleteMenu = new MenuItem(template.menuTable("Delete", "Delete"), deleteCmd);
		Command cutCmd = new Command() {
			public void execute( ) {
				xmlTreeDisplay.setDirty(true);
				popupPanel.hide();
				xmlTreeDisplay.copy();
				xmlTreeDisplay.removeNode();
			}
		};
		cutMenu = new MenuItem(template.menuTable("Cut", "Ctrl+X"), cutCmd);
		
		Command pasteCmd = new Command() {
			public void execute() {
				xmlTreeDisplay.setDirty(true);
				if(xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.ROOT_NODE){
					pasteRootNodeTypeItem();
				}else{
					xmlTreeDisplay.paste();	
				}
				popupPanel.hide();
			}
		};
		pasteMenu = new MenuItem(template.menuTable("Paste", "Ctrl+V"), pasteCmd);
		
		Command expandCmd = new Command() {
			public void execute( ) {
				popupPanel.hide();
				xmlTreeDisplay.expandSelected(xmlTreeDisplay.getXmlTree().getRootTreeNode());
			}
		};
		expandMenu = new MenuItem(template.menuTable("Expand", ""), expandCmd);
		
	}


	/**
	 * Method displays the rightClick options based on the nodeType of the node
	 * selected on CellTree.
	 * 
	 * @param popupPanel
	 *            the popup panel
	 */
	public void displayMenuItems( final PopupPanel popupPanel){
		popupMenuBar.clearItems();
		popupPanel.clear();
		copyMenu.setEnabled(false);
		deleteMenu.setEnabled(false);
		pasteMenu.setEnabled(false);
		cutMenu.setEnabled(false);
		showHideExpandMenu();
		switch (xmlTreeDisplay.getSelectedNode().getNodeType()) {

		case CellTreeNode.MASTER_ROOT_NODE:
			addCommonMenus();
			break;
			
		case CellTreeNode.ROOT_NODE:
			Command addNodeCmd = new Command() {
				public void execute( ) {
					xmlTreeDisplay.setDirty(true);
					popupPanel.hide();
					addRootNodeTypeItem();
				}
			};
			addMenu = new MenuItem(getAddMenuName(xmlTreeDisplay.getSelectedNode().getChilds().get(0)) , true, addNodeCmd);
			popupMenuBar.addItem(addMenu);
			popupMenuBar.addSeparator(separator);
			addCommonMenus();
			addMenu.setEnabled(true);
			copyMenu.setEnabled(false);
			deleteMenu.setEnabled(false);
			if(xmlTreeDisplay.getCopiedNode() != null && 
					xmlTreeDisplay.getCopiedNode().getParent().equals(xmlTreeDisplay.getSelectedNode())){
				pasteMenu.setEnabled(true);
			}
			cutMenu.setEnabled(false);
			break;

		case CellTreeNode.CLAUSE_NODE:
			addCommonMenus();
			copyMenu.setEnabled(true);
			pasteMenu.setEnabled(false);
			if(xmlTreeDisplay.getSelectedNode().getParent().getChilds().size() > 1){
				deleteMenu.setEnabled(true);
			}
			cutMenu.setEnabled(false);
			break;

		case CellTreeNode.LOGICAL_OP_NODE:	
			subMenuBar = new MenuBar(true);
			popupMenuBar.setAutoOpen(true);
			subMenuBar.setAutoOpen(true);
			createAddMenus(MatContext.get().logicalOps, CellTreeNode.LOGICAL_OP_NODE, subMenuBar);// creating logical Operators Menu 2nd level
			createAddQDM_MenuItem(subMenuBar);
			MenuBar timingMenuBar = new MenuBar(true); 
			subMenuBar.addItem("Timing", timingMenuBar);//Timing menu 2nd level
			createAddMenus(MatContext.get().timings, CellTreeNode.TIMING_NODE, timingMenuBar);// Timing sub menus 3rd level
			MenuBar functionsMenuBar = new MenuBar(true); 
			subMenuBar.addItem("Functions", functionsMenuBar);//functions menu 2nd level
			createAddMenus(MatContext.get().functions, CellTreeNode.FUNCTIONS_NODE, functionsMenuBar);// functions sub menus 3rd level	
			MenuBar relMenuBar = new MenuBar(true); 
			subMenuBar.addItem("Relationship", relMenuBar);//functions menu 2nd level
			createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE, relMenuBar);// Timing sub menus 3rd level
			addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
			popupMenuBar.addItem(addMenu);
			popupMenuBar.addSeparator(separator);
			addCommonMenus();
			copyMenu.setEnabled(true);
			if(xmlTreeDisplay.getCopiedNode() != null 
					&& xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE){//can paste LOGOP,RELOP, QDM, TIMING & FUNCS
				pasteMenu.setEnabled(true);
			}
			
			if(xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE){
				deleteMenu.setEnabled(true);
			}
			
			if(xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE
					&& xmlTreeDisplay.getSelectedNode().getNodeType() == CellTreeNode.LOGICAL_OP_NODE){
				cutMenu.setEnabled(true);
				subMenuBar = new MenuBar(true);
				createEditMenus(MatContext.get().logicalOps, subMenuBar);
				editMenu = new MenuItem("Edit", true, subMenuBar);
				popupMenuBar.addItem(editMenu);				
			}
			break;
		case CellTreeNode.TIMING_NODE:
			MenuBar subMenuBarLHS = createMenuBarWithTimingFuncAndQDM();			
			addMenuLHS = new MenuItem("Add LHS", subMenuBarLHS); //LHS Sub Menu
			
			MenuBar subMenuBarRHS = createMenuBarWithTimingFuncAndQDM();
			addMenuRHS = new MenuItem("Add RHS", subMenuBarRHS);//RHS Sub Menu
			
			//Disable  RHS by default.
			if(xmlTreeDisplay.getSelectedNode().getChilds()==null){
				addMenuRHS.setEnabled(false);
			}
			//Disable LHS when One element is added and disable RHS when two elements are added.
			if(xmlTreeDisplay.getSelectedNode().getChilds()!=null){
				if(xmlTreeDisplay.getSelectedNode().getChilds().size()>=1){
					addMenuLHS.setEnabled(false);
				}
				if(xmlTreeDisplay.getSelectedNode().getChilds().size()==0 || xmlTreeDisplay.getSelectedNode().getChilds().size()>=2){
					addMenuRHS.setEnabled(false);
				}
			}
			popupMenuBar.addItem(addMenuLHS);
			popupMenuBar.addItem(addMenuRHS);
			popupMenuBar.addSeparator(separator);
			addCommonMenus();
			copyMenu.setEnabled(true);
			if(xmlTreeDisplay.getCopiedNode() != null 
					&& xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE
							&& (xmlTreeDisplay.getSelectedNode().getChilds() == null || xmlTreeDisplay.getSelectedNode().getChilds().size() < 2)){
				pasteMenu.setEnabled(true);
			}
			
			if(xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE){
				deleteMenu.setEnabled(true);
			}
			Command editCmd = new Command() {
				public void execute() {
					popupPanel.hide();
					ComparisonDialogBox.showComparisonDialogBox(xmlTreeDisplay,xmlTreeDisplay.getSelectedNode());
				}
			};
			editMenu = new MenuItem("Edit", true, editCmd);
			popupMenuBar.addItem(editMenu);				
			cutMenu.setEnabled(true);
			break;
		
		case CellTreeNode.ELEMENT_REF_NODE:
			Command editQDMCmd = new Command() {
				public void execute( ) {
					popupPanel.hide();
					//To edit the QDM element
					showQDMPopup(false);
				}
			};
			editQDMMenu = new MenuItem("Edit", true, editQDMCmd);
			popupMenuBar.addItem(editQDMMenu);
			createQDMAttributeMenuItem(popupMenuBar,xmlTreeDisplay.getSelectedNode());
			addCommonMenus();
			copyMenu.setEnabled(true);
			pasteMenu.setEnabled(false);
			cutMenu.setEnabled(true);
			deleteMenu.setEnabled(true);
			break;
			
		case CellTreeNode.FUNCTIONS_NODE:
			subMenuBar = new MenuBar(true);
			popupMenuBar.setAutoOpen(true);
			subMenuBar.setAutoOpen(true);
			createAddMenus(MatContext.get().logicalOps, CellTreeNode.LOGICAL_OP_NODE, subMenuBar);// creating logical Operators Menu 2nd level
			createAddQDM_MenuItem(subMenuBar);
			MenuBar timing = new MenuBar(true); 
			subMenuBar.addItem("Timing", timing);//Timing menu 2nd level
			createAddMenus(MatContext.get().timings, CellTreeNode.TIMING_NODE, timing);// Timing sub menus 3rd level
			MenuBar functions = new MenuBar(true); 
			subMenuBar.addItem("Functions", functions);//functions menu 2nd level
			createAddMenus(MatContext.get().functions, CellTreeNode.FUNCTIONS_NODE, functions);// functions sub menus 3rd level			
			addMenu = new MenuItem("Add", subMenuBar); // 1st level menu
			popupMenuBar.addItem(addMenu);
			popupMenuBar.addSeparator(separator);
			addCommonMenus();
			copyMenu.setEnabled(true);
			if(xmlTreeDisplay.getCopiedNode() != null 
					&& xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE){//can paste LOGOP, RELOP, QDM, TIMING & FUNCS
				pasteMenu.setEnabled(true);
			}
			cutMenu.setEnabled(true);
			deleteMenu.setEnabled(true);
			Command editFunctionsCmd = new Command() {
				public void execute() {
					popupPanel.hide();
					ComparisonDialogBox.showComparisonDialogBox(xmlTreeDisplay,xmlTreeDisplay.getSelectedNode());
					
				}
			};
			editMenu = new MenuItem("Edit", true, editFunctionsCmd);
			popupMenuBar.addItem(editMenu);	
			
			break;
		case CellTreeNode.RELATIONSHIP_NODE:
			MenuBar subMenuBarRelLHS = createMenuBarWithTimingFuncAndQDM();			
			
			MenuBar RelAssociationMenuBar = new MenuBar(true); 
			subMenuBarRelLHS.addItem("Relationship", RelAssociationMenuBar);//Relationship menu 2nd level
			createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE, RelAssociationMenuBar);// Relationship sub menus 3rd level
			addMenuLHS = new MenuItem("Add LHS", subMenuBarRelLHS); //LHS Sub Menu
			
			MenuBar subMenuBarRelRHS = createMenuBarWithTimingFuncAndQDM();
			MenuBar RelAssociationMenuBarRHS = new MenuBar(true); 
			subMenuBarRelRHS.addItem("Relationship", RelAssociationMenuBar);//Relationship menu 2nd level
			createAddMenus(MatContext.get().relationships, CellTreeNode.RELATIONSHIP_NODE, RelAssociationMenuBarRHS);// Relationship sub menus 3rd level
			addMenuRHS = new MenuItem("Add RHS", subMenuBarRelRHS);//RHS Sub Menu
			
			//Disable  RHS by default.
			if(xmlTreeDisplay.getSelectedNode().getChilds()==null){
				addMenuRHS.setEnabled(false);
			}
			//Disable LHS when One element is added and disable RHS when two elements are added.
			if(xmlTreeDisplay.getSelectedNode().getChilds()!=null){
				if(xmlTreeDisplay.getSelectedNode().getChilds().size()>=1){
					addMenuLHS.setEnabled(false);
				}
				if(xmlTreeDisplay.getSelectedNode().getChilds().size()==0 || xmlTreeDisplay.getSelectedNode().getChilds().size()>=2){
					addMenuRHS.setEnabled(false);
				}
			}
			popupMenuBar.addItem(addMenuLHS);
			popupMenuBar.addItem(addMenuRHS);
			popupMenuBar.addSeparator(separator);
			addCommonMenus();
			copyMenu.setEnabled(true);
			if(xmlTreeDisplay.getCopiedNode() != null 
					&& xmlTreeDisplay.getCopiedNode().getNodeType() != CellTreeNode.CLAUSE_NODE
							&& (xmlTreeDisplay.getSelectedNode().getChilds() == null || xmlTreeDisplay.getSelectedNode().getChilds().size() < 2)){
				pasteMenu.setEnabled(true);
			}
			
			if(xmlTreeDisplay.getSelectedNode().getParent().getNodeType() != CellTreeNode.CLAUSE_NODE){
				deleteMenu.setEnabled(true);
			}
			MenuBar subMenuBarEdit = new MenuBar(true);
			createEditMenus(MatContext.get().relationships, subMenuBarEdit);
			editMenu = new MenuItem("Edit", true, subMenuBarEdit);
			popupMenuBar.addItem(editMenu);	
			cutMenu.setEnabled(true);
		break;
		default:
			break;
		}
	}

	/**
	 * Show hide expand menu.
	 */
	private void showHideExpandMenu(){
		if(xmlTreeDisplay.getSelectedNode().hasChildren()){
			expandMenu.setEnabled(true);
		}else{
			expandMenu.setEnabled(false);
		}
	}
	
	/**
	 * Creates the qdm attribute menu item.
	 * 
	 * @param menuBar
	 *            the menu bar
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	private void createQDMAttributeMenuItem(MenuBar menuBar, final CellTreeNode cellTreeNode) {
		Command addQDMAttributeCmd = new Command() {
			public void execute() {
				popupPanel.hide();
				showQDMAttributePopup(cellTreeNode);
			}
		};
		MenuItem item = new MenuItem(template.menuTable("Edit Attributes", ""),addQDMAttributeCmd);
		menuBar.addItem(item);	
		checkForTimingElementDataType(cellTreeNode,item);
	}
	
	/**
	 * Check if the Data Type for the selected node is "Timing Element" and
	 * disable the menuItem.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 * @param item
	 *            the item
	 */
	private void checkForTimingElementDataType(CellTreeNode cellTreeNode,
			MenuItem item) {
		if(cellTreeNode.getNodeType() != CellTreeNode.ELEMENT_REF_NODE){
			return;
		}
		String qdmName = ClauseConstants.getElementLookUpName().get(cellTreeNode.getUUID());
		Node qdmNode = ClauseConstants.getElementLookUpNode().get(qdmName + "~" + cellTreeNode.getUUID());
		//Could not find the qdm node in elemenentLookup tag 
		if(qdmNode == null){
			return;
		}
		String qdmDataType = qdmNode.getAttributes().getNamedItem("datatype").getNodeValue();
		if("Timing Element".equals(qdmDataType)){
			item.setEnabled(false);
		}
	}

	/**
	 * Show qdm attribute popup.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	protected void showQDMAttributePopup(CellTreeNode cellTreeNode) {
		QDMAttributeDialogBox.showQDMAttributeDialogBox(xmlTreeDisplay,cellTreeNode);
	}


	/**
	 * Creates the menu bar with timing func and qdm.
	 * 
	 * @return the menu bar
	 */
	private MenuBar createMenuBarWithTimingFuncAndQDM(){
		MenuBar menuBar = new MenuBar(true);
		popupMenuBar.setAutoOpen(true);
		menuBar.setAutoOpen(true);
		createAddMenus(MatContext.get().logicalOps, CellTreeNode.LOGICAL_OP_NODE, menuBar);// creating logical Operators Menu 2nd level
		createAddQDM_MenuItem(menuBar);
		MenuBar timingMenuBar = new MenuBar(true); 
		menuBar.addItem("Timing", timingMenuBar);//Timing menu 2nd level
		createAddMenus(MatContext.get().timings, CellTreeNode.TIMING_NODE, timingMenuBar);// Timing sub menus 3rd level
		MenuBar functionsMenuBar = new MenuBar(true); 
		menuBar.addItem("Functions", functionsMenuBar);//functions menu 2nd level
		createAddMenus(MatContext.get().functions, CellTreeNode.FUNCTIONS_NODE, functionsMenuBar);// functions sub menus 3rd level
		return menuBar;
	}
	
	/**
	 * Creates the add qdm_ menu item.
	 * 
	 * @param menuBar
	 *            the menu bar
	 */
	private void createAddQDM_MenuItem(MenuBar menuBar) {
		Command addQDMCmd = new Command() {
			public void execute() {
				popupPanel.hide();
				showQDMPopup(true);
			}
		};
		MenuItem item = new MenuItem("QDM Element",true,addQDMCmd);		
		menuBar.addItem(item);
	}

	/**
	 * Show qdm popup.
	 * 
	 * @param isAdd
	 *            the is add
	 */
	protected void showQDMPopup(boolean isAdd) {
		QDMDialogBox.showQDMDialogBox(xmlTreeDisplay,isAdd);
	}

	/**
	 * Method iterates through the menuNames and creates MenuItems, on selection
	 * of a MenuItem , a child node is created and added to the selected node
	 * with the name and label set to selected menuItem's name and the nodeType
	 * set to the passed nodeType value.
	 * 
	 * and @param nodeType passed
	 * 
	 * @param menuNames
	 *            the menu names
	 * @param nodeType
	 *            the node type
	 * @param menuBar
	 *            the menu bar
	 */
	private void createAddMenus(List<String> menuNames, final short nodeType, MenuBar menuBar){
		for (final String name : menuNames) {
			Command addCmd = new Command() {
				public void execute() {
					xmlTreeDisplay.setDirty(true);
					popupPanel.hide();
					xmlTreeDisplay.addNode(name, name, nodeType);
				}
			};
			MenuItem menu = new MenuItem(name, true, addCmd);
			menuBar.addItem(menu);
		}
	}
	
	
	/**
	 * Method iterates through the editMenuNames and creates MenuItems, on
	 * selection of a MenuItem, the selected node's name and label is updated
	 * with the selected menuItem's name.
	 * 
	 * @param editMenuNames
	 *            the edit menu names
	 * @param subMenuBar
	 *            the sub menu bar
	 */
	private void createEditMenus(List<String> editMenuNames, MenuBar subMenuBar){
		for (final String editMenuName : editMenuNames) {
			Command timingCmd = new Command() {
				
				@Override
				public void execute() {
					xmlTreeDisplay.setDirty(true);
					popupPanel.hide();
					xmlTreeDisplay.editNode(editMenuName, editMenuName);
				}
			};
			MenuItem timingMenus = new MenuItem(editMenuName, true, timingCmd);
			subMenuBar.addItem(timingMenus);
		}
	}
	

	/**
	 * Paste root node type item.
	 */
	protected void pasteRootNodeTypeItem() {
		String clauseNodeName = xmlTreeDisplay.getCopiedNode().getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name = clauseNodeName.substring(0, clauseNodeName.lastIndexOf(" ")) + " " + seqNumber ;
		CellTreeNode pasteNode = xmlTreeDisplay.getCopiedNode().cloneNode();
		pasteNode.setName(name);
		pasteNode.setLabel(name);
		xmlTreeDisplay.getSelectedNode().appendChild(pasteNode);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
		xmlTreeDisplay.setCopiedNode(pasteNode);//make the new pasted node as the copied node
	}


	/**
	 * Adds the root node type item.
	 */
	protected void addRootNodeTypeItem() {
		String clauseNodeName = xmlTreeDisplay.getSelectedNode().getChilds().get(0).getName();
		int seqNumber = getNextHighestSequence(xmlTreeDisplay.getSelectedNode());
		String name =clauseNodeName.substring(0, clauseNodeName.lastIndexOf(" ")) + " " + seqNumber ;

		CellTreeNode clauseNode  = xmlTreeDisplay.getSelectedNode().createChild(name, name, CellTreeNode.CLAUSE_NODE);
		clauseNode.createChild(ClauseConstants.AND, ClauseConstants.AND, CellTreeNode.LOGICAL_OP_NODE);
		xmlTreeDisplay.refreshCellTreeAfterAdding(xmlTreeDisplay.getSelectedNode());
	}


	/**
	 * Adds the common menus.
	 */
	private void addCommonMenus(){
		popupMenuBar.addItem(copyMenu);
		popupMenuBar.addItem(pasteMenu);
		popupMenuBar.addItem(cutMenu);
		popupMenuBar.addItem(deleteMenu);
		popupMenuBar.addItem(expandMenu);
		popupMenuBar.setVisible(true);		  
		popupPanel.add(popupMenuBar);
	}


	/**
	 * Gets the adds the menu name.
	 * 
	 * @param selectedNode
	 *            the selected node
	 * @return the adds the menu name
	 */
	private String getAddMenuName(CellTreeNode selectedNode){
		return "Add" + " " +  selectedNode.getName().substring(0, selectedNode.getName().lastIndexOf(" "));
	}


	/**
	 * Gets the next highest sequence.
	 * 
	 * @param selectedNode
	 *            the selected node
	 * @return the next highest sequence
	 */
	private int getNextHighestSequence(CellTreeNode selectedNode){
		SortedSet<Integer> sortedName = new TreeSet<Integer>();
		Integer lastInt = 0;
		sortedName.add(lastInt);
		if(selectedNode.getNodeType() == CellTreeNode.ROOT_NODE){
			if(selectedNode.hasChildren()){
				for (CellTreeNode treeNode : selectedNode.getChilds()) {
					String clauseNodeName = treeNode.getName().substring(treeNode.getName().lastIndexOf(" ")).trim();
					try {
						lastInt = Integer.parseInt(clauseNodeName);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					if(lastInt > 0){
						sortedName.add(lastInt);
					}
				}
			}else{
				return 1;
			}

		}
		return sortedName.last() + 1;
	}
}
