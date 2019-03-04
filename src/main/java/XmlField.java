public class XmlField {

    private final String name;
    private final String parentName;
    private final String type;

    public XmlField(String name, String parentName, String type) {
        this.name = name;
        this.parentName = parentName;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        XmlField xmlField = (XmlField) o;
        return name.equals(xmlField.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "XmlField{" +
                "name='" + name + '\'' +
                ", parentName='" + parentName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
