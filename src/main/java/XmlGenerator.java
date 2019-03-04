import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.*;

public class XmlGenerator {

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
        Map<String, XmlField> hierarchyMap = XmlHierarchySource.getHierarchyMap();

        Map<String, String> input = getInputData();

        XmlNode root = generateTreeAndGetRoot(hierarchyMap);

        printXml(root, input);
    }

    private static void printXml(XmlNode node, Map<String, String> input) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = document.createElement("root");
        document.appendChild(root);
        updateDocument(root, document, node, input);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new StringWriter());

        transformer.transform(domSource, streamResult);
        System.out.println(((StringWriter) streamResult.getWriter()).getBuffer().toString());
    }

    private static void updateDocument(Element element, Document document, XmlNode node, Map<String, String> input) {
        if (!"root".equals(node.getXmlField().getName())) {
            if (node.getChildNodes().isEmpty() && "attr".equals(node.getXmlField().getType())) {
                if (input.containsKey(node.getXmlField().getName())) {
                    element.setAttribute(node.getXmlField().getName(), input.get(node.getXmlField().getName()));
                } else if (input.containsKey(node.getXmlField().getName() + "#1")) {
                    element.setAttribute(node.getXmlField().getName(), input.get(node.getXmlField().getName() + "#1"));
                    int counter = 1;
                    Node nextSibling = element.getNextSibling();
                    while (input.containsKey(node.getXmlField().getName() + "#" + ++counter)) {
                        if (null == nextSibling) {
                            nextSibling = document.createElement(element.getTagName());
                            element.getParentNode().appendChild(nextSibling);
                        }
                        ((Element) nextSibling).setAttribute(node.getXmlField().getName(), input.get(node.getXmlField().getName() + "#" + counter));
                        nextSibling = nextSibling.getNextSibling();
                    }
                }
            } else if (node.getChildNodes().isEmpty() && "node".equals(node.getXmlField().getType())) {
                if (input.containsKey(node.getXmlField().getName())) {
                    Element child = document.createElement(node.getXmlField().getName());
                    child.appendChild(document.createTextNode(input.get(node.getXmlField().getName())));
                    element.appendChild(child);
                } else if (input.containsKey(node.getXmlField().getName() + "#1")) {
                    Element child = document.createElement(node.getXmlField().getName());
                    child.appendChild(document.createTextNode(input.get(node.getXmlField().getName() + "#1")));
                    element.appendChild(child);
                    int counter = 1;
                    Node nextSibling = element.getNextSibling();
                    while (input.containsKey(node.getXmlField().getName() + "#" + ++counter)) {
                        if (null == nextSibling) {
                            nextSibling = document.createElement(element.getTagName());
                            element.getParentNode().appendChild(nextSibling);
                        }
                        child = document.createElement(node.getXmlField().getName());
                        child.appendChild(document.createTextNode(input.get(node.getXmlField().getName() + "#" + counter)));
                        nextSibling.appendChild(child);
                        nextSibling = nextSibling.getNextSibling();
                    }
                }
            } else {
                Element tmp = document.createElement(node.getXmlField().getName());
                element.appendChild(tmp);
                element = tmp;
            }
        }
        Element finalElement = element;
        node.getChildNodes().values().forEach(childNode -> updateDocument(finalElement, document, childNode, input));
    }

    private static Map<String, String> getInputData() {
        Map<String, String> input = new HashMap<>();
        input.put("name#1", "tazz");
        input.put("name#2", "casper");
        input.put("itemName#1", "sange");
        input.put("itemName#2", "yasha");
        input.put("version", "7.1.5");
        input.put("id#1", "1");
        input.put("id#2", "2");

        return input;
    }

    private static XmlNode generateTreeAndGetRoot(Map<String, XmlField> hierarchyMap) {
        XmlNode[] root = {new XmlNode(new XmlField("root", "root", "node"), new HashMap<>())};

        Map<String, XmlNode> lookupMap = new HashMap<>();

        hierarchyMap.forEach((fieldName, xmlField) -> updateTree(root, xmlField, hierarchyMap, lookupMap));

        return root[0];
    }

    private static void mergeTempRootOnRoot(XmlNode tempRoot, XmlNode root) {
        root.getChildNodes().put(tempRoot.getXmlField(), tempRoot);
        if (!tempRoot.getChildNodes().isEmpty()) {
            mergeTempRootOnRoot((XmlNode) tempRoot.getChildNodes().values().toArray()[0], root.getChildNodes().get(tempRoot.getXmlField()));
        }
    }

    private static XmlNode updateTree(XmlNode[] root, XmlField xmlField, Map<String, XmlField> hierarchyMap, Map<String, XmlNode> lookupMap) {
        if (xmlField.getName().equals(xmlField.getParentName())) {
            if (root[0].getChildNodes().isEmpty()) {
                root[0].getChildNodes().put(xmlField, new XmlNode(xmlField, new HashMap<>()));
            }
            return root[0].getChildNodes().get(xmlField);
        } else {
            XmlNode parentNode = lookupMap.containsKey(xmlField.getParentName()) ? lookupMap.get(xmlField.getParentName()) : updateTree(root, hierarchyMap.get(xmlField.getParentName()), hierarchyMap, lookupMap);
            if (!parentNode.getChildNodes().containsKey(xmlField)) {
                parentNode.getChildNodes().put(xmlField, new XmlNode(xmlField, new HashMap<>()));
            }
            return parentNode.getChildNodes().get(xmlField);
        }
    }
}
