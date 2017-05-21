package JAXB;

import javax.xml.bind.annotation.*;
import java.util.Set;

//TODO: rename it to Project,
//
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserProject {

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "x")
    private int x;

    @XmlAttribute(name = "y")
    private int y;

    @XmlAttribute(name = "width")
    private int width;

    @XmlAttribute(name = "height")
    private int height;

    @XmlElement(name = "text")
    private Set<TextField> textFields;

    @XmlElement(name = "image")
    private Set<ImageField> imageFields;

    @XmlElement(name = "block")
    private Set<Block> blocks;


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public Set<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(Set<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserProject that = (UserProject) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        if (width != that.width) return false;
        if (height != that.height) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (textFields != null ? !textFields.equals(that.textFields) : that.textFields != null) return false;
        if (imageFields != null ? !imageFields.equals(that.imageFields) : that.imageFields != null) return false;
        return blocks != null ? blocks.equals(that.blocks) : that.blocks == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + (textFields != null ? textFields.hashCode() : 0);
        result = 31 * result + (imageFields != null ? imageFields.hashCode() : 0);
        result = 31 * result + (blocks != null ? blocks.hashCode() : 0);
        return result;
    }
}
