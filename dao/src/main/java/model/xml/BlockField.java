package model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
public class BlockField extends Field{

    @XmlElement(name = "text")
    private Set<TextField> textFields;

    @XmlElement(name = "image")
    private Set<ImageField> imageFields;

    @XmlElement(name = "block")
    private Set<BlockField> blocks;

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
        if (!super.equals(o)) return false;

        BlockField BlockField = (BlockField) o;

        if (textFields != null ? !textFields.equals(BlockField.textFields) : BlockField.textFields != null) return false;
        if (imageFields != null ? !imageFields.equals(BlockField.imageFields) : BlockField.imageFields != null) return false;
        return blocks != null ? blocks.equals(BlockField.blocks) : BlockField.blocks == null;
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
