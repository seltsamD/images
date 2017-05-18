package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Size;


@Entity
@Table(name = "configs", uniqueConstraints = @UniqueConstraint(columnNames = "key"))
@NamedQueries({
        @NamedQuery(name = Config.GET_BY_KEY, query = "from Project where user_id = :id")
})
public class Config implements Serializable {
    public static final String GET_BY_KEY = "Config.getByKey";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @Size(min = 3, max = 50)
    private String key;

    @Column
    private String value;

    public Config() {
    }

    public Config( String key, String value) {
        this.key = key;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return id == config.id &&
                Objects.equals(key, config.key) &&
                Objects.equals(value, config.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, value);
    }

    @Override
    public String toString() {
        return "Config{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
