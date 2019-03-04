import java.util.Map;
import java.util.Objects;

public class XmlNode {

    private final XmlField xmlField;
    private final Map<XmlField, XmlNode> childNodes;

    public XmlNode(XmlField xmlField, Map<XmlField, XmlNode> childNodes) {
        this.xmlField = xmlField;
        this.childNodes = childNodes;
    }

    public XmlField getXmlField() {
        return xmlField;
    }

    public Map<XmlField, XmlNode> getChildNodes() {
        return childNodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlNode xmlNode = (XmlNode) o;
        return Objects.equals(xmlField, xmlNode.xmlField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xmlField);
    }

    @Override
    public String toString() {
        return "XmlNode{" +
                "xmlField=" + xmlField +
                ", childNodes=" + childNodes +
                '}';
    }
}