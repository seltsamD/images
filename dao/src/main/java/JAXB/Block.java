package JAXB;

import javax.xml.bind.annotation.*;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
public class Block{


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Block block = (Block) o;

        if (textFields != null ? !textFields.equals(block.textFields) : block.textFields != null) return false;
        if (imageFields != null ? !imageFields.equals(block.imageFields) : block.imageFields != null) return false;
        return blocks != null ? blocks.equals(block.blocks) : block.blocks == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (textFields != null ? textFields.hashCode() : 0);
        result = 31 * result + (imageFields != null ? imageFields.hashCode() : 0);
        result = 31 * result + (blocks != null ? blocks.hashCode() : 0);
        return result;
    }
}