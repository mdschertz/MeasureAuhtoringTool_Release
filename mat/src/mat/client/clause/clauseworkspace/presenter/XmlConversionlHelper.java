package mat.client.clause.clauseworkspace.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.client.clause.clauseworkspace.model.CellTreeNode;
import mat.client.clause.clauseworkspace.model.CellTreeNodeImpl;
import mat.client.shared.MatContext;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * The Class XmlConversionlHelper.
 */
public class XmlConversionlHelper {

	/** The Constant NAMESPACE_XML. */
	private static final String NAMESPACE_XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\r\n";

	/**
	 * Creates CellTreeNode object which has list of children objects and a
	 * parent object from the XML.
	 * 
	 * @param xml
	 *            the xml
	 * @param tagName
	 *            the tag name
	 * @return CellTreeNode
	 */
	public static CellTreeNode createCellTreeNode(String xml, String tagName) {
		Node node = null;
		CellTreeNode mainNode = new CellTreeNodeImpl();
		List<CellTreeNode> childs = new ArrayList<CellTreeNode>();
		Document document = null;
		if (xml != null && xml.trim().length() > 0) {
			document = XMLParser.parse(xml);
			NodeList nodeList = document.getElementsByTagName(tagName);
			if (nodeList.getLength() > 0) {
				node = nodeList.item(0);
			}
			if (node != null) {
				mainNode.setName(tagName);
				createCellTreeNodeChilds(mainNode, node, childs);
			}
		}
		if (node == null) {
			mainNode.setName(tagName);
			childs.add(createRootNode(tagName));
			mainNode.setChilds(childs);
		}
		return mainNode;
	}

	/**
	 * Creates the root node.
	 * 
	 * @param tagName
	 *            the tag name
	 * @return the cell tree node
	 */
	private static CellTreeNode createRootNode(String tagName) {
		CellTreeNode parent = new CellTreeNodeImpl();
		List<CellTreeNode> childs = new ArrayList<CellTreeNode>();
		if (tagName.equalsIgnoreCase("populations")) {
			parent.setName(ClauseConstants.get(tagName));
			parent.setLabel(ClauseConstants.get(tagName));
			parent.setNodeType(CellTreeNode.MASTER_ROOT_NODE);
			for (int i = 0; i < ClauseConstants.getPopulationsChildren().length; i++) {
				String nodeValue = ClauseConstants.getPopulationsChildren()[i];
				//Adding Root nodes under Population.
				CellTreeNode child = createChild(nodeValue, nodeValue, CellTreeNode.ROOT_NODE, parent);
				childs.add(child);
				//Clause Nodes should not have 's' in end. For example 'Numerators' child should be 'Numerator'.
				String name = nodeValue.substring(0, nodeValue.lastIndexOf('s')) + " "  + 1;
				//Adding Clause Nodes
				List<CellTreeNode> subChilds = new ArrayList<CellTreeNode>();
				subChilds.add(createChild(name, name, CellTreeNode.CLAUSE_NODE, child));
				// Adding First 'AND' under clause node.
				for (int j = 0; j < subChilds.size(); j++) {
					List<CellTreeNode> logicalOp = new ArrayList<CellTreeNode>();
					logicalOp.add(createChild(ClauseConstants.AND, ClauseConstants.AND,
							CellTreeNode.LOGICAL_OP_NODE, subChilds.get(j)));
					subChilds.get(j).setChilds(logicalOp);
				}
				child.setChilds(subChilds);
			}
			parent.setChilds(childs);
		} else if ("measureObservations".equals(tagName)) {
			parent.setName(ClauseConstants.get(tagName));
			parent.setLabel(ClauseConstants.get(tagName));
			parent.setNodeType(CellTreeNode.ROOT_NODE);
			CellTreeNode clauseNode = createChild("Measure Observation 1",
					"Measure Observation 1", CellTreeNode.CLAUSE_NODE, parent);
			childs.add(clauseNode);
			parent.setChilds(childs);
			List<CellTreeNode> logicalOp = new ArrayList<CellTreeNode>();
			logicalOp.add(createChild(ClauseConstants.AND, ClauseConstants.AND,
					CellTreeNode.LOGICAL_OP_NODE, clauseNode));
			clauseNode.setChilds(logicalOp);
		} else if ("strata".equalsIgnoreCase(tagName)) {
			parent.setName(ClauseConstants.get(tagName));
			parent.setLabel(ClauseConstants.get(tagName));
			parent.setNodeType(CellTreeNode.ROOT_NODE);
			CellTreeNode clauseNode = createChild("Stratum 1", "Stratum 1", CellTreeNode.CLAUSE_NODE, parent);
			childs.add(clauseNode);
			parent.setChilds(childs);
			List<CellTreeNode> logicalOp = new ArrayList<CellTreeNode>();
			logicalOp.add(createChild(ClauseConstants.AND, ClauseConstants.AND, CellTreeNode.LOGICAL_OP_NODE, clauseNode));
			clauseNode.setChilds(logicalOp);
		}
		return parent;
	}

	/**
	 * Creates the child.
	 * 
	 * @param name
	 *            the name
	 * @param label
	 *            the label
	 * @param nodeType
	 *            the node type
	 * @param parent
	 *            the parent
	 * @return the cell tree node
	 */
	private static CellTreeNode createChild(String name, String label, short nodeType, CellTreeNode parent) {
		CellTreeNode child = new CellTreeNodeImpl();
		child.setName(name);
		child.setLabel(label);
		child.setParent(parent);
		child.setNodeType(nodeType);
		return child;
	}

	/**
	 * Creates the cell tree node childs.
	 * 
	 * @param parent
	 *            the parent
	 * @param root
	 *            the root
	 * @param childs
	 *            the childs
	 */
	private static void createCellTreeNodeChilds(CellTreeNode parent, Node root, List<CellTreeNode> childs) {
		String nodeName = root.getNodeName();
		String nodeValue = root.hasAttributes()
				? root.getAttributes().getNamedItem(ClauseConstants.DISPLAY_NAME).getNodeValue() : nodeName;

		CellTreeNode child = new CellTreeNodeImpl(); //child Object
		if (nodeValue.length() > 0) {
			setCellTreeNodeValues(root, parent, child, childs); // Create complete child Object with parent and sub Childs
		}

		parent.setChilds(childs); // set parent's childs
		NodeList nodes = root.getChildNodes(); // get Child nodes for the Processed node and repeat the process
		for (int i = 0; i < nodes.getLength(); i++) {
			if (i == 0) {
				if (child.getChilds() == null) {
					childs = new ArrayList<CellTreeNode>();
				} else {
					childs  = child.getChilds();
				}
			}
			Node node = nodes.item(i);
			String name = node.getNodeName().replaceAll("\n\r", "").trim();
			//if(!(name.equalsIgnoreCase("#text") && name.isEmpty())){
			if (name.length() > 0 && !name.equalsIgnoreCase("#text") && !name.equalsIgnoreCase("attribute")) {
				createCellTreeNodeChilds(child, node, childs);
			}
			/**
			 * This part for QDM node attributes. The attribute XML node is not to be displayed on the CellTree.
			 */
			else if (name.equalsIgnoreCase("attribute")) {
				Object attributes = child.getExtraInformation("attributes");
				if (attributes == null) {
					attributes = new ArrayList<CellTreeNode>();
					child.setExtraInformation("attributes", attributes);
				}
				List<CellTreeNode> attributeList = (List<CellTreeNode>) attributes;
				CellTreeNode cellNode = new CellTreeNodeImpl();
				NamedNodeMap attNodeMap = node.getAttributes();
				for (int j = 0; j < attNodeMap.getLength(); j++) {
					Node attrib = attNodeMap.item(j);
					cellNode.setExtraInformation(attrib.getNodeName(), attrib.getNodeValue());
				}
				attributeList.add(cellNode);
			}
		}
	}

	/**
	 * Creating XML from GWT tree using GWT Document object.
	 * 
	 * @param model
	 *            the model
	 * @return XML String
	 */
	public static String createXmlFromTree(CellTreeNode model) {
		Document doc = XMLParser.createDocument();
		if (model != null) {
			String returnXml = NAMESPACE_XML + createXmlFromTree(model, doc, null);
			return returnXml;
		}

		return null;
	}

	/**
	 * Iterating through the Tree's Children to create the Document Element,
	 * Nodes and Attributes.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 * @param doc
	 *            the doc
	 * @param node
	 *            the node
	 * @return the string
	 */
	private static String createXmlFromTree(CellTreeNode cellTreeNode, Document doc, Node node) {
		Element element = getNodeName(cellTreeNode, doc);

		if (node != null) {
			node = node.appendChild(element);
		} else {
			node = doc.appendChild(element);
		}

		if (cellTreeNode.getChilds() != null && cellTreeNode.getChilds().size() > 0) {
			for (CellTreeNode model : cellTreeNode.getChilds()) {
				createXmlFromTree(model, doc, node);
			}
		}
		return doc.toString();
	}

	/**
	 * Sets the cell tree node values.
	 * 
	 * @param node
	 *            the node
	 * @param parent
	 *            the parent
	 * @param child
	 *            the child
	 * @param childs
	 *            the childs
	 */
	private static void setCellTreeNodeValues(Node node, CellTreeNode parent, CellTreeNode child, List<CellTreeNode> childs) {
		String nodeName = node.getNodeName();
		String nodeValue = node.hasAttributes()
				? node.getAttributes().getNamedItem(ClauseConstants.DISPLAY_NAME).getNodeValue() : nodeName;
		short cellTreeNodeType = 0;
		String uuid = "";
		if (nodeName.equalsIgnoreCase(ClauseConstants.MASTER_ROOT_NODE_POPULATION)) {
			cellTreeNodeType =  CellTreeNode.MASTER_ROOT_NODE;
		} else if (ClauseConstants.ROOT_NODES.contains(nodeName)) {
			cellTreeNodeType =  CellTreeNode.ROOT_NODE;
		} else if (nodeName.equalsIgnoreCase(ClauseConstants.CLAUSE_TYPE)) {
			cellTreeNodeType =  CellTreeNode.CLAUSE_NODE;
			uuid = node.getAttributes().getNamedItem(ClauseConstants.UUID).getNodeValue();
		} else if (nodeName.equalsIgnoreCase(ClauseConstants.LOG_OP)) {
			cellTreeNodeType = CellTreeNode.LOGICAL_OP_NODE;
		} else if (nodeName.equalsIgnoreCase(ClauseConstants.RELATIONAL_OP)) {
			String type = node.getAttributes().getNamedItem(ClauseConstants.TYPE).getNodeValue();
			String longName = MatContext.get().operatorMapKeyShort.get(type);
			if (MatContext.get().relationships.contains(longName)) {
				cellTreeNodeType = CellTreeNode.RELATIONSHIP_NODE;
			} else {
				cellTreeNodeType = CellTreeNode.TIMING_NODE;
				NamedNodeMap nodeMap = node.getAttributes();
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < nodeMap.getLength(); i++) {
					String key = nodeMap.item(i).getNodeName();
					String value = nodeMap.item(i).getNodeValue();
					map.put(key, value);
				}
				child.setExtraInformation(ClauseConstants.EXTRA_ATTRIBUTES, map);
			}
		} else if (nodeName.equalsIgnoreCase(ClauseConstants.ELEMENT_REF)) {
			cellTreeNodeType = CellTreeNode.ELEMENT_REF_NODE;
			uuid =  node.getAttributes().getNamedItem(ClauseConstants.ID).getNodeValue();
		} else if (nodeName.equalsIgnoreCase(ClauseConstants.FUNC_NAME)) {
			cellTreeNodeType = CellTreeNode.FUNCTIONS_NODE;
			HashMap<String, String> map = new HashMap<String, String>();
			if (node.hasAttributes()) {
				NamedNodeMap nodeMap = node.getAttributes();
				for (int i = 0; i < nodeMap.getLength(); i++) {
					String key = nodeMap.item(i).getNodeName();
					String value = nodeMap.item(i).getNodeValue();
					map.put(key, value);
				}
			}
			child.setExtraInformation(ClauseConstants.EXTRA_ATTRIBUTES, map);
		}
		child.setName(nodeValue); //set the name to Child
		child.setLabel(nodeValue);
		child.setNodeType(cellTreeNodeType);
		child.setParent(parent); // set parent in child
		child.setUUID(uuid);
		childs.add(child); // add child to child list
	}

	/**
	 * Append attribute to qdm name.
	 * 
	 * @param node
	 *            the node
	 * @return the string
	 */
	private static String appendAttributeToQdmName(Node node) {
		NamedNodeMap namedNodeMap = node.getChildNodes().item(0).getAttributes();
		StringBuilder stringBuilder = new StringBuilder(namedNodeMap.getNamedItem("name").getNodeValue());
		String modeName = namedNodeMap.getNamedItem("mode") != null ? namedNodeMap.getNamedItem("mode").getNodeValue() : "";

		if ("Check if Present".equalsIgnoreCase(modeName)) {
			stringBuilder.append(" is present ");
		} else if ("Value Set".equalsIgnoreCase(modeName)) {
			Node qdm = namedNodeMap.getNamedItem("qdmUUID");
			if (null != qdm) {
				String qdmId = namedNodeMap.getNamedItem("qdmUUID").getNodeValue();
				String qdmName = ClauseConstants.getElementLookUpName().get(qdmId);
				stringBuilder.append(": '").append(qdmName).append("'");
			}
		} else {
			if ("Less Than".equalsIgnoreCase(modeName)) {
				stringBuilder.append(" < ");
			} else if ("Less Than Or Equal To".equalsIgnoreCase(modeName)) {
				stringBuilder.append(" <= ");
			} else if ("Greater Than".equalsIgnoreCase(modeName)) {
				stringBuilder.append(" > ");
			} else if ("Greater Than Or Equal To".equalsIgnoreCase(modeName)) {
				stringBuilder.append(" >= ");
			} else if ("Equal To".equalsIgnoreCase(modeName)) {
				stringBuilder.append(" = ");
			}

			Node comparisonValue = namedNodeMap.getNamedItem("comparisonValue");
			if (null != comparisonValue) {
				stringBuilder.append(comparisonValue.getNodeValue());
			}
			Node unit = namedNodeMap.getNamedItem("unit");
			if (null != unit) {
				stringBuilder.append(" ").append(unit.getNodeValue());
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * Gets the node name.
	 * 
	 * @param cellTreeNode
	 *            the cell tree node
	 * @param document
	 *            the document
	 * @return the node name
	 */
	private static Element getNodeName(CellTreeNode cellTreeNode, Document document) {
		Element element = null;
		switch (cellTreeNode.getNodeType()) {
		case CellTreeNode.MASTER_ROOT_NODE:
			element = document.createElement(ClauseConstants.get(cellTreeNode.getName()));
			element.setAttribute(ClauseConstants.DISPLAY_NAME, cellTreeNode.getName());
			break;
		case CellTreeNode.ROOT_NODE:
			element = document.createElement(ClauseConstants.get(cellTreeNode.getName()));
			element.setAttribute(ClauseConstants.DISPLAY_NAME, cellTreeNode.getName());
			break;
		case CellTreeNode.CLAUSE_NODE:
			element = document.createElement(ClauseConstants.CLAUSE_TYPE);
			element.setAttribute(ClauseConstants.DISPLAY_NAME, cellTreeNode.getName());
			element.setAttribute(ClauseConstants.TYPE,
					toCamelCase(cellTreeNode.getName().substring(0, cellTreeNode.getName().lastIndexOf(" "))));
			element.setAttribute(ClauseConstants.UUID, cellTreeNode.getUUID());
			break;
		case CellTreeNode.LOGICAL_OP_NODE:
			element = document.createElement(ClauseConstants.LOG_OP);
			element.setAttribute(ClauseConstants.DISPLAY_NAME, cellTreeNode.getName());
			element.setAttribute(ClauseConstants.TYPE, toCamelCase(cellTreeNode.getName()));
			break;
		case CellTreeNode.TIMING_NODE:
			element = document.createElement(ClauseConstants.RELATIONAL_OP);

			@SuppressWarnings("unchecked")
			HashMap<String, String> map = (HashMap<String, String>) cellTreeNode.getExtraInformation(
												ClauseConstants.EXTRA_ATTRIBUTES);
			if (map != null) {
				element.setAttribute(ClauseConstants.DISPLAY_NAME, map.get(ClauseConstants.DISPLAY_NAME));
//				String typeValue = ClauseConstants.getTimingOperators().containsKey(map.get(ClauseConstants.TYPE))
//						? ClauseConstants.getTimingOperators().get(map.get(ClauseConstants.TYPE)): map.get(ClauseConstants.DISPLAY_NAME);
				String typeValue = map.get(ClauseConstants.TYPE);
				element.setAttribute(ClauseConstants.TYPE, typeValue);
				if (map.containsKey(ClauseConstants.OPERATOR_TYPE)) {
					element.setAttribute(ClauseConstants.OPERATOR_TYPE, map.get(ClauseConstants.OPERATOR_TYPE));
				}
				if (map.containsKey(ClauseConstants.QUANTITY)) {
					element.setAttribute(ClauseConstants.QUANTITY, map.get(ClauseConstants.QUANTITY));
				}
				if (map.containsKey(ClauseConstants.UNIT)) {
					element.setAttribute(ClauseConstants.UNIT, map.get(ClauseConstants.UNIT));
				}
			} else {
				element.setAttribute(ClauseConstants.DISPLAY_NAME, cellTreeNode.getName());
				element.setAttribute(ClauseConstants.TYPE, MatContext.get().operatorMapKeyLong.get(cellTreeNode.getName()));
			}
			break;
		case CellTreeNode.RELATIONSHIP_NODE:
			element = document.createElement(ClauseConstants.RELATIONAL_OP);
			element.setAttribute(ClauseConstants.DISPLAY_NAME, cellTreeNode.getName());
			element.setAttribute(ClauseConstants.TYPE, MatContext.get().operatorMapKeyLong.get(cellTreeNode.getName()));
			break;
		case CellTreeNode.ELEMENT_REF_NODE:
			element = document.createElement(ClauseConstants.ELEMENT_REF);
//			Node idNode = ClauseConstants.getElementLookUps().get(cellTreeNode.getName()).getAttributes().getNamedItem("uuid");
			element.setAttribute(ClauseConstants.ID, cellTreeNode.getUUID()); // TBD if we need this
			element.setAttribute(ClauseConstants.DISPLAY_NAME, cellTreeNode.getName());
			element.setAttribute(ClauseConstants.TYPE, "qdm"); //this can change

			List<CellTreeNode> attributeList = (List<CellTreeNode>) cellTreeNode.getExtraInformation("attributes");
			if (attributeList != null) {
				for (CellTreeNode attribNode:attributeList) {
					Element attribElement = document.createElement(ClauseConstants.ATTRIBUTE);
					for (String name:((CellTreeNodeImpl) attribNode).getExtraInformationMap().keySet()) {
						attribElement.setAttribute(name, (String) attribNode.getExtraInformation(name));
					}
					element.appendChild(attribElement);
				}
			}
			break;
		case CellTreeNode.FUNCTIONS_NODE:
			element = document.createElement(ClauseConstants.FUNC_NAME);

			@SuppressWarnings("unchecked")
			HashMap<String, String> functionMap = (HashMap<String, String>) cellTreeNode.getExtraInformation(
													ClauseConstants.EXTRA_ATTRIBUTES);
			if (functionMap != null) {
				element.setAttribute(ClauseConstants.DISPLAY_NAME, functionMap.get(ClauseConstants.DISPLAY_NAME));
				element.setAttribute(ClauseConstants.TYPE, functionMap.get(ClauseConstants.TYPE));
				if (functionMap.containsKey(ClauseConstants.OPERATOR_TYPE)) {
					element.setAttribute(ClauseConstants.OPERATOR_TYPE, functionMap.get(ClauseConstants.OPERATOR_TYPE));
				}
				if (functionMap.containsKey(ClauseConstants.QUANTITY)) {
					element.setAttribute(ClauseConstants.QUANTITY, functionMap.get(ClauseConstants.QUANTITY));
				}
				if (functionMap.containsKey(ClauseConstants.UNIT)) {
					element.setAttribute(ClauseConstants.UNIT, functionMap.get(ClauseConstants.UNIT));
				}
			} else {
				element.setAttribute(ClauseConstants.DISPLAY_NAME, cellTreeNode.getName());
				element.setAttribute(ClauseConstants.TYPE, MatContext.get().operatorMapKeyLong.get(cellTreeNode.getName()));
			}
			break;
		default:
			element = document.createElement(cellTreeNode.getName());
			break;
		}
		return element;
	}

	/**
	 * Method to convert case of string into camel case.
	 * 
	 * @param name
	 *            the name
	 * @return the string
	 */
	private static String toCamelCase(String name) {
		name = name.toLowerCase();
		String[] parts = name.split(" ");
		String camelCaseString = parts[0].substring(0, 1).toLowerCase() + parts[0].substring(1);
		for (int i = 1; i < parts.length; i++) {
			camelCaseString = camelCaseString + toProperCase(parts[i]);
		}
		return camelCaseString;
	}

	/**
	 * To proper case.
	 * 
	 * @param s
	 *            the s
	 * @return the string
	 */
	private static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase()
		 + s.substring(1).toLowerCase();
	}
}
