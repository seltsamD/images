package model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImageField extends Field{

    @XmlAttribute(name = "imageRef")
    private String imageRef;


    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImageField that = (ImageField) o;

        return imageRef != null ? imageRef.equals(that.imageRef) : that.imageRef == null;
    }

    @Override
    public int hashCode() {
        return imageRef != null ? imageRef.hashCode() : 0;
    }
}
