package model.xml;

import javax.xml.bind.annotation.*;
import java.util.Set;

@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute
    private int width;

    @XmlAttribute
    private int height;

    @XmlElement(name = "text")
    private Set<TextField> textFields;

    @XmlElement(name = "image")
    private Set<ImageField> imageFields;

    @XmlElement(name = "block")
    private Set<BlockField> blocks;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Set<TextField> getTextFields() {
        return textFields;
    }

    public void setTextFields(Set<TextField> textFields) {
        this.textFields = textFields;
    }

    public Set<ImageField> getImageFields() {
        return imageFields;
    }

    public void setImageFields(Set<ImageField> imageFields) {
        this.imageFields = imageFields;
    }

    public Set<BlockField> getBlocks() {
        return blocks;
    }

    public void setBlocks(Set<BlockField> blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project that = (Project) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (textFields != null ? !textFields.equals(that.textFields) : that.textFields != null) return false;
        if (imageFields != null ? !imageFields.equals(that.imageFields) : that.imageFields != null) return false;
        return blocks != null ? blocks.equals(that.blocks) : that.blocks == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (textFields != null ? textFields.hashCode() : 0);
        result = 31 * result + (imageFields != null ? imageFields.hashCode() : 0);
        result = 31 * result + (blocks != null ? blocks.hashCode() : 0);
        return result;
    }
}
