package mat.client.clause.clauseworkspace.view;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.presenter.ClauseConstants;
import mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.WarningMessageDisplay;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.client.SafeHtmlTemplates.Template;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * The Class XmlTreeView.
 */
public class XmlTreeView extends Composite implements  XmlTreeDisplay, TreeViewModel, KeyDownHandler, FocusHandler{

	/**
	 * The Interface Template.
	 */
	interface Template extends SafeHtmlTemplates {
	    
    	/**
		 * Outer div.
		 * 
		 * @param classes
		 *            the classes
		 * @param title
		 *            the title
		 * @param content
		 *            the content
		 * @return the safe html
		 */
    	@Template("<div class=\"{0}\" title=\"{1}\">{2}</div>")
	    SafeHtml outerDiv(String classes, String title, String content);
	}
	
	/** The Constant template. */
	private static final Template template = GWT.create(Template.class);
	 
	/** The main panel. */
	private FlowPanel  mainPanel = new FlowPanel();
	
	/** The is valid. */
	boolean isValid = true;
	
	
	/** The focus panel. */
	FocusPanel focusPanel = new FocusPanel(mainPanel);

	/** The cell tree. */
	CellTree cellTree;

	/** The save btn. */
	private Button saveBtn = new PrimaryButton("Save","primaryButton");
	
	/** The validate btn. */
	private Button validateBtn = new SecondaryButton("Validate");

	/** The button expand. */
	private Button buttonExpand = new Button();
	
	/** The button collapse. */
	private Button buttonCollapse = new Button();

	/** The error message display. */
	private ErrorMessageDisplay errorMessageDisplay = new ErrorMessageDisplay();

	/** The success message display. */
	private SuccessMessageDisplay successMessageDisplay = new SuccessMessageDisplay();
	
	/** The warning message display. */
	private WarningMessageDisplay warningMessageDisplay = new WarningMessageDisplay();

	/** The node data provider. */
	private ListDataProvider<CellTreeNode> nodeDataProvider;

	/** The selected node. */
	CellTreeNode selectedNode;

	/** The selection model. */
	final SingleSelectionModel<CellTreeNode> selectionModel = new SingleSelectionModel<CellTreeNode>();

	/** The copied node. */
	CellTreeNode copiedNode;

	/** The popup panel. */
	PopupPanel popupPanel = new PopupPanel(true);

	/** The clause workspace context menu. */
	ClauseWorkspaceContextMenu clauseWorkspaceContextMenu = new ClauseWorkspaceContextMenu(this, popupPanel);

	/** The enabled. */
	boolean enabled;
	
	/** The is dirty. */
	boolean isDirty = false;
	

	/**
	 * Instantiates a new xml tree view.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	public XmlTreeView(CellTreeNode cellTreeNode) {
		clearMessages();
		createRootNode(cellTreeNode);
		addHandlers();
		mainPanel.getElement().setId("mainPanel_FlowPanel");
		saveBtn.getElement().setId("saveBtn_Button");
		buttonExpand.getElement().setId("buttonExpand_Button");
		buttonCollapse.getElement().setId("buttonCollapse_Button");
	}


	/**
	 * Creates the Root Node in the CellTree. Sets the Root node to the ListData
	 * Provider.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 */
	private void createRootNode(CellTreeNode cellTreeNode) {
		nodeDataProvider = new ListDataProvider<CellTreeNode>(cellTreeNode.getChilds());
	}


	/**
	 * Page widgets.
	 * 
	 * @param cellTree
	 *            the cell tree
	 */
	public void createPageView(CellTree cellTree) {
		this.cellTree = cellTree;
		mainPanel.setStyleName("div-wrapper");//main div
		
		SimplePanel leftPanel = new SimplePanel();
		leftPanel.getElement().setId("leftPanel_SimplePanel");
		leftPanel.setStyleName("div-first bottomPadding10px");//left side div which will  have tree

		SimplePanel rightPanel = new SimplePanel();
		rightPanel.getElement().setId("rightPanel_SimplePanel");
		rightPanel.setStyleName("div-second");//right div having tree creation inputs.

		VerticalPanel treePanel =  new VerticalPanel();
		treePanel.getElement().setId("treePanel_VerticalPanel");
		HorizontalPanel expandCollapse  = new HorizontalPanel();
		expandCollapse.getElement().setId("expandCollapse_HorizontalPanel");
		expandCollapse.setStyleName("leftAndTopPadding");
		expandCollapse.setSize("100px", "20px");
		buttonExpand.setStylePrimaryName("expandAllButton");
		buttonCollapse.setStylePrimaryName("collapseAllButton");
		buttonExpand.setTitle("Expand All (Shift +)");
		buttonCollapse.setTitle("Collapse All (Shift -)");
		expandCollapse.add(buttonExpand);
		expandCollapse.add(buttonCollapse);
		buttonExpand.setFocus(true);
		buttonCollapse.setVisible(true);

		treePanel.add(expandCollapse);
		treePanel.add(cellTree);
		leftPanel.add(treePanel);

		SimplePanel bottomSavePanel = new SimplePanel();
		bottomSavePanel.getElement().setId("bottomSavePanel_SimplePanel");
		bottomSavePanel.setStyleName("div-first buttonPadding");
		VerticalPanel vp = new VerticalPanel();
		HorizontalPanel savePanel = new HorizontalPanel();
		savePanel.getElement().setId("savePanel_VerticalPanel");
		savePanel.add(new SpacerWidget());
//		savePanel.add(errorMessageDisplay);
		vp.add(successMessageDisplay);
//		saveBtn.setTitle("Ctrl+Alt+s");
		savePanel.add(saveBtn);
		validateBtn.setTitle("Validate");
		savePanel.add(validateBtn);
		vp.add(warningMessageDisplay);
		vp.add(savePanel);
		bottomSavePanel.add(vp);

		SimplePanel errPanel = new SimplePanel();
		errPanel.getElement().setId("errPanel_SimplePanel");
		errPanel.add(errorMessageDisplay);
		mainPanel.add(errPanel);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		mainPanel.add(bottomSavePanel);	
		focusPanel.addKeyDownHandler(this);
		focusPanel.addFocusHandler(this);
		cellTreeHandlers();
	}

	/**
	 * Selection Handler, Tree Open and Close Handlers Defined.
	 */
	private void cellTreeHandlers() {
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedNode = selectionModel.getSelectedObject(); // assigning the selected object to the selectedNode variable.
			}
		});

		/**
		 * This handler is implemented to save the open state of the Celltree in CellTreeNode Object
		 * Set to isOpen boolean in CellTreeNode.
		 * After adding/removing/editing a node to the celltree 
		 * Manually  we have to close and open all nodes to see the new node, 
		 * so using this boolean we will know which node was already in opened state and closed state.
		 */
		this.cellTree.addOpenHandler(new OpenHandler<TreeNode>() {

			@Override
			public void onOpen(OpenEvent<TreeNode> event) {
				CellTreeNode node = (CellTreeNode)event.getTarget().getValue();
				node.setOpen(true);
				clearMessages();
			}
		});

		this.cellTree.addCloseHandler(new CloseHandler<TreeNode>() {
			@Override
			public void onClose(CloseEvent<TreeNode> event) {
				CellTreeNode node = (CellTreeNode)event.getTarget().getValue();
				setOpenToFalse(node);// when a node is closed set all the child nodes isOpen boolean to false.
				node.setOpen(false);
				clearMessages();
			}

		});
	}


	/**
	 * Iterating through all the child nodes and setting the isOpen boolean to
	 * false.
	 * 
	 * @param node
	 *            the new open to false
	 */
	private void setOpenToFalse(CellTreeNode node) {
		if(node.hasChildren()){
			for (CellTreeNode child : node.getChilds()) {
				child.setOpen(false);
				setOpenToFalse(child);
			}
		}
	}
	
	/**
	 * Closing all nodes in the CellTree except for the Master Root Node which
	 * is the Population node. This method is called when '-' Collapse All
	 * button is clicked on the Screen
	 * 
	 * @param node
	 *            the node
	 */
	@Override
	public void closeNodes(TreeNode node) {
		if(node != null){
			for (int i = 0; i < node.getChildCount(); i++) {
				TreeNode subTree  = null;
				if (((CellTreeNode)node.getChildValue(i)).getNodeType() == CellTreeNode.MASTER_ROOT_NODE){
					subTree =  node.setChildOpen(i, true, true);
				}else{
					subTree =  node.setChildOpen(i, false, true);
				}

				if (subTree != null && ((TreeNode) subTree).getChildCount() > 0){
					closeNodes(subTree);
				}
			}

		}
	}

	/**
	 * This method is called after removing or editing of the node. When a node
	 * is removed, parent node is closed first and then opened. Remaining all
	 * nodes will be opened or closed based on the isOpen boolean in
	 * CellTreeNode
	 * 
	 * @param treeNode
	 *            the tree node
	 */
	private void closeParentOpenNodes(TreeNode treeNode) {
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				if(treeNode.getChildValue(i).equals(selectedNode.getParent())){// this check is performed since IE was giving JavaScriptError after removing a node and closing all nodes.
					// to avoid that we are closing the parent of the removed node.
					subTree = treeNode.setChildOpen(i, false, false);
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode)treeNode.getChildValue(i)).isOpen());
				if (subTree != null && subTree.getChildCount() > 0){
					closeParentOpenNodes(subTree);
				}
			}  
		}
	}


	/**
	 * This method is called after adding a child node to the parent. After
	 * adding a child node, close the Parent node and open. Remaining all nodes
	 * will be opened or closed based on the isOpen boolean in CellTreeNode
	 * 
	 * @param treeNode
	 *            the tree node
	 */
	private void closeSelectedOpenNodes(TreeNode treeNode) {
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				if(treeNode.getChildValue(i).equals(selectedNode)){// this check is performed since IE was giving JavaScriptError after removing a node and closing all nodes.
					// to avoid that we are closing the parent of the removed node.
					subTree = treeNode.setChildOpen(i, false, false);
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode)treeNode.getChildValue(i)).isOpen());
				if (subTree != null && subTree.getChildCount() > 0){
					closeSelectedOpenNodes(subTree);
				}
			}  
		}
	}

	/**
	 * Opens all nodes. this is called when '+' Expand All button is clicked on
	 * the screen
	 * 
	 * @param treeNode
	 *            the tree node
	 */
	@Override
	public void openAllNodes(TreeNode treeNode){		
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = treeNode.setChildOpen(i, true);	      			
				if (subTree != null && subTree.getChildCount() > 0){
					openAllNodes(subTree);
				}
			}  
		}
	}

	/**
	 * Expand / Collapse Link - Click Handlers.
	 */
	private void addHandlers(){
		
		buttonExpand.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				openAllNodes(cellTree.getRootTreeNode());
				buttonExpand.setVisible(true);
				buttonCollapse.setVisible(true);
			}
		});

		buttonCollapse.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clearMessages();
				closeNodes(cellTree.getRootTreeNode());
				buttonExpand.setVisible(true);
				buttonCollapse.setVisible(true);
			}
		});
	}


	/* (non-Javadoc)
	 * @see com.google.gwt.view.client.TreeViewModel#getNodeInfo(java.lang.Object)
	 */
	@Override
	public <T> NodeInfo<?> getNodeInfo(T value) {
		if (value == null) {
			NodeCell nodeCell = new NodeCell();
			return new DefaultNodeInfo<CellTreeNode>(nodeDataProvider, nodeCell, selectionModel, null);
		} else {
			CellTreeNode myValue = (CellTreeNode) value;
			ListDataProvider<CellTreeNode> dataProvider = new ListDataProvider<CellTreeNode>(myValue.getChilds());
			NodeCell nodeCell = new NodeCell();
			return new DefaultNodeInfo<CellTreeNode>(dataProvider, nodeCell, selectionModel, null);
		}
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.view.client.TreeViewModel#isLeaf(java.lang.Object)
	 */
	@Override
	public boolean isLeaf(Object value) {
		if (value instanceof CellTreeNode) {
			CellTreeNode t = (CellTreeNode) value;
			if (!t.hasChildren()){
				return true;
			}
		}
		return false;
	}


	/**
	 * The Class NodeCell.
	 */
	public class NodeCell extends AbstractCell<CellTreeNode> {

		/**
		 * Instantiates a new node cell.
		 */
		public NodeCell() {
			super(BrowserEvents.CONTEXTMENU);
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.AbstractCell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
		@Override
		public void render(Context context, CellTreeNode cellTreeNode, SafeHtmlBuilder sb) {
			if (cellTreeNode == null) {
				return;
			}			
			//TODO :  We can add classes based on the NodeType with the specified image. The classes will be picked up from Mat.css
			sb.append(template.outerDiv(getStyleClass(cellTreeNode), cellTreeNode.getTitle(), cellTreeNode.getLabel() != null ? 
					cellTreeNode.getLabel() : cellTreeNode.getName()));
		}

		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
		 */
		@Override
		public void onBrowserEvent(Context context, Element parent, CellTreeNode value,
				NativeEvent event, ValueUpdater<CellTreeNode> valueUpdater) {
			if(event.getType().equals(BrowserEvents.CONTEXTMENU)){
				event.preventDefault();
				event.stopPropagation();
				if(MatContext.get().getMeasureLockService().checkForEditPermission()){
					onRightClick(value, (Event)event, parent);
				}					
			}else{
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
			}

		}
	} 

	
	/**
	 * Gets the style class.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 * @return the style class
	 */
	private String getStyleClass(CellTreeNode cellTreeNode){

		if (cellTreeNode.getValidNode() != false) {
			switch (cellTreeNode.getNodeType()) {
				case CellTreeNode.ROOT_NODE:
					return "cellTreeRootNode";
				default:
					break;
			}
		} else {
			return "clauseWorkSpaceInvalidNode";
		}
		return "";
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#addNode(java.lang.String, java.lang.String, short)
	 */
	@Override
	public CellTreeNode addNode(String value, String label, short nodeType) {
		CellTreeNode childNode = null;
		if(selectedNode != null &&  value != null && value.trim().length() > 0){//if nodeTex textbox is not empty
			childNode = selectedNode.createChild(value, label, nodeType);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);					
		}
		return childNode;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#refreshCellTreeAfterAdding(mat.client.clause.clauseworkspace.model.CellTreeNode)
	 */
	@Override
	public void refreshCellTreeAfterAdding(CellTreeNode selectedNode){
		closeSelectedOpenNodes(cellTree.getRootTreeNode());
		selectionModel.setSelected(selectedNode, true);			
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#removeNode()
	 */
	@Override
	public void removeNode() {
		if(selectedNode != null){
			CellTreeNode parent = selectedNode.getParent();
			parent.removeChild(selectedNode);
			closeParentOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(parent, true);
		}
	}

	/**
	 * On right click.
	 * 
	 * @param value
	 *            the value
	 * @param event
	 *            the event
	 * @param element
	 *            the element
	 */
	public void onRightClick(CellTreeNode value, Event event, Element element) {
		clearMessages();
		selectedNode = value;
		selectionModel.setSelected(selectedNode, true);
		int x = element.getAbsoluteRight() - 10;
		int y = element.getAbsoluteBottom() + 5;
		popupPanel.setPopupPosition(x, y);
		popupPanel.setAnimationEnabled(true);
//		popupPanel.setSize("175px", "75px");
		popupPanel.show();
		popupPanel.setStyleName("popup");
		clauseWorkspaceContextMenu.displayMenuItems(popupPanel);
	}



	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getXmlTree()
	 */
	@Override
	public CellTree getXmlTree() {
		return cellTree;
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getSaveButton()
	 */
	@Override
	public Button getSaveButton() {
		return saveBtn;
	}


	/**
	 * Gets the validate btn.
	 * 
	 * @return the validateBtn
	 */
	@Override
	public Button getValidateBtn() {
		return validateBtn;
	}


	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return focusPanel;
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessageDisplay;
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplay getErrorMessageDisplay() {
		return errorMessageDisplay;
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#clearMessages()
	 */
	@Override
	public void clearMessages() {
		successMessageDisplay.clear();
		errorMessageDisplay.clear();
		warningMessageDisplay.clear();
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		saveBtn.setEnabled(enabled);
		this.enabled = enabled;
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getSelectedNode()
	 */
	@Override
	public CellTreeNode getSelectedNode() {
		return this.selectedNode;
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#copy()
	 */
	@Override
	public void copy() {
		this.copiedNode = selectedNode;		
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#paste()
	 */
	@Override
	public void paste() {
		if(selectedNode != null){
			CellTreeNode pasteNode = copiedNode.cloneNode();
			selectedNode.appendChild(pasteNode);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);		
			this.copiedNode = pasteNode;
		}
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getCopiedNode()
	 */
	@Override
	public CellTreeNode getCopiedNode() {
		return this.copiedNode;
	}


	/**
	 * Gets the cell tree.
	 * 
	 * @return the cell tree
	 */
	public CellTree getCellTree() {
		return cellTree;
	}


	/**
	 * Sets the cell tree.
	 * 
	 * @param cellTree
	 *            the new cell tree
	 */
	public void setCellTree(CellTree cellTree) {
		this.cellTree = cellTree;
	}



	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#editNode(java.lang.String, java.lang.String)
	 */
	@Override
	public void editNode(String name, String label) {
		if(selectedNode != null){
			selectedNode.setName(name);
			selectedNode.setLabel(label);
			closeParentOpenNodes(cellTree.getRootTreeNode());
		}
	}


	/**
	 * Gets the clause workspace context menu.
	 * 
	 * @return the clauseWorkspaceContextMenu
	 */
	public ClauseWorkspaceContextMenu getClauseWorkspaceContextMenu() {
		return clauseWorkspaceContextMenu;
	}


	/**
	 * Sets the clause workspace context menu.
	 * 
	 * @param clauseWorkspaceContextMenu
	 *            the clauseWorkspaceContextMenu to set
	 */
	public void setClauseWorkspaceContextMenu(
			ClauseWorkspaceContextMenu clauseWorkspaceContextMenu) {
		this.clauseWorkspaceContextMenu = clauseWorkspaceContextMenu;
	}


	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.KeyDownHandler#onKeyDown(com.google.gwt.event.dom.client.KeyDownEvent)
	 */
	@Override
	public void onKeyDown(KeyDownEvent event) {
//		System.out.println(event.getNativeKeyCode());
		int keyCode = event.getNativeKeyCode();
		if(selectedNode != null){
			short nodeType = selectedNode.getNodeType();
			if(event.isControlKeyDown()){
				if(keyCode == ClauseConstants.COPY_C){//COPY 
					if(nodeType != CellTreeNode.MASTER_ROOT_NODE && nodeType != CellTreeNode.ROOT_NODE){
						popupPanel.hide();
						copy();	
					}
					
				}else if(keyCode == ClauseConstants.PASTE_V){//PASTE
					boolean canPaste = false;
					popupPanel.hide();
					if(copiedNode != null){
						switch (selectedNode.getNodeType()) {
						case CellTreeNode.ROOT_NODE:
							if(selectedNode.equals(copiedNode.getParent())){
								clauseWorkspaceContextMenu.pasteRootNodeTypeItem();
								isDirty = true;
							}
							break;
						case CellTreeNode.LOGICAL_OP_NODE: case CellTreeNode.FUNCTIONS_NODE:
							if(copiedNode.getNodeType() != CellTreeNode.CLAUSE_NODE){
								canPaste = true;
							}
							break;
						case CellTreeNode.TIMING_NODE:
							if(copiedNode.getNodeType() != CellTreeNode.CLAUSE_NODE
									&& (selectedNode.getChilds() == null || selectedNode.getChilds().size() < 2)){
								canPaste = true;
							}
							break;
						default:
							break;
						}
						if(canPaste){
							paste();
							isDirty = true;
						}
					}
					
				}else if(keyCode == ClauseConstants.CUT_X){//CUT
					popupPanel.hide();
					if(selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE
								&& selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE 
								&& selectedNode.getNodeType() != CellTreeNode.ROOT_NODE 
								&& selectedNode.getParent().getNodeType() != CellTreeNode.CLAUSE_NODE){
						copy();
						removeNode();
						isDirty = true;
					}
				}
			}else if(keyCode == ClauseConstants.DELETE_DELETE){//DELETE
				popupPanel.hide();
				if((selectedNode.getNodeType() != CellTreeNode.MASTER_ROOT_NODE
						&& selectedNode.getNodeType() != CellTreeNode.ROOT_NODE 
						&& selectedNode.getParent().getNodeType() != CellTreeNode.CLAUSE_NODE
						&& selectedNode.getNodeType() != CellTreeNode.CLAUSE_NODE)
						|| (selectedNode.getNodeType() == CellTreeNode.CLAUSE_NODE && selectedNode.getParent().getChilds().size() > 1 )){
					removeNode();
					isDirty = true;
				}
			}
		}
		if((event.isShiftKeyDown() && (keyCode == ClauseConstants.PLUS_FF || keyCode == ClauseConstants.PLUS_IE))){
			//EXPAND/COLLAPSE (+(Shift +) Expand| - Collapse)
			popupPanel.hide();
			openAllNodes(cellTree.getRootTreeNode());
		}else if((event.isShiftKeyDown() && (keyCode == ClauseConstants.MINUS_FF || keyCode == ClauseConstants.MINUS_IE))){
			popupPanel.hide();
			closeNodes(cellTree.getRootTreeNode());
		}
		/*if(event.isControlKeyDown() && event.isAltKeyDown() && keyCode == 83){
			saveBtn.getElement().focus();
			saveBtn.click();
		}*/
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#setCopiedNode(mat.client.clause.clauseworkspace.model.CellTreeNode)
	 */
	@Override
	public void setCopiedNode(CellTreeNode cellTreeNode) {
		this.copiedNode = cellTreeNode;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.FocusHandler#onFocus(com.google.gwt.event.dom.client.FocusEvent)
	 */
	@Override
	public void onFocus(FocusEvent event) {
		focusPanel.setStyleName("focusPanel");
	}



	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#setDirty(boolean)
	 */
	@Override
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return this.isDirty;
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#expandSelected(com.google.gwt.user.cellview.client.TreeNode)
	 */
	@Override
	public void expandSelected(TreeNode treeNode) {
		if(treeNode != null){
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				if(treeNode.getChildValue(i).equals(selectedNode)){// this check is performed since IE was giving JavaScriptError after removing a node and closing all nodes.
					// to avoid that we are closing the parent of the removed node.
					subTree = treeNode.setChildOpen(i, true, true);
					if (subTree != null && subTree.getChildCount() > 0){
						openAllNodes(subTree);
					}
					break;
				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode)treeNode.getChildValue(i)).isOpen(), ((CellTreeNode)treeNode.getChildValue(i)).isOpen());
				if (subTree != null && subTree.getChildCount() > 0){
					expandSelected(subTree);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#validateCellTreeNodes(com.google.gwt.user.cellview.client.TreeNode)
	 */
	@Override
	public boolean validateCellTreeNodes(TreeNode treeNode) {

		if (treeNode != null) {
			openAllNodes(treeNode);
			for (int i = 0; i < treeNode.getChildCount(); i++) {
				TreeNode subTree = null;
				CellTreeNode node = (CellTreeNode) treeNode.getChildValue(i);
				if (node.getNodeType()
						== CellTreeNode.TIMING_NODE || node.getNodeType() == CellTreeNode.RELATIONSHIP_NODE) {
				// this check is performed since IE was giving JavaScriptError after removing a node and closing all nodes.
					subTree = treeNode.setChildOpen(i, true, true);
					if (subTree != null && subTree.getChildCount() == 2) {
						if (!node.getValidNode()) {
							editNode(true, node, subTree);
						}
					} else {
						editNode(false, node, subTree);
						if (isValid) {
							isValid = false;
						}
					}

				}
				subTree = treeNode.setChildOpen(i, ((CellTreeNode) treeNode.getChildValue(i)).isOpen(),
						((CellTreeNode) treeNode.getChildValue(i)).isOpen());
				if (subTree != null && subTree.getChildCount() > 0) {
					validateCellTreeNodes(subTree);
				}
			}
		}
		return isValid;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#addNode(java.lang.String, java.lang.String, java.lang.String, short)
	 */
	@Override
	public CellTreeNode addNode(String name, String label, String uuid,
			short nodeType) {
		CellTreeNode childNode = null;
		if(selectedNode != null &&  name != null && name.trim().length() > 0){//if nodeTex textbox is not empty
			childNode = selectedNode.createChild(name, label, nodeType);
			childNode.setUUID(uuid);
			closeSelectedOpenNodes(cellTree.getRootTreeNode());
			selectionModel.setSelected(selectedNode, true);					
		}
		return childNode;
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#editNode(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void editNode(String name, String label, String uuid) {
		if(selectedNode != null){
			selectedNode.setName(name);
			selectedNode.setLabel(label);
			selectedNode.setUUID(uuid);
			closeParentOpenNodes(cellTree.getRootTreeNode());
		}		
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#editNode(boolean, mat.client.clause.clauseworkspace.model.CellTreeNode, com.google.gwt.user.cellview.client.TreeNode)
	 */
	@Override
	public void editNode(boolean isValideNodeValue, CellTreeNode node, TreeNode subTree) {
		node.setValidNode(isValideNodeValue);
		selectedNode = node;
		closeParentOpenNodes(cellTree.getRootTreeNode());
	}


	/* (non-Javadoc)
	 * @see mat.client.clause.clauseworkspace.presenter.XmlTreeDisplay#getWarningMessageDisplay()
	 */
	@Override
	public WarningMessageDisplay getWarningMessageDisplay() {
		return warningMessageDisplay;
	}
	
	/**
	 * Checks if is valid.
	 * 
	 * @return the isValid
	 */
	public boolean isValid() {
		return isValid;
	}


	/**
	 * Sets the valid.
	 * 
	 * @param isValid
	 *            the isValid to set
	 */
	@Override
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}
}
