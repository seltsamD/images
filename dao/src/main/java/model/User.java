package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Size;


@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @Size(min = 3, max = 50)
    private String username;

    @Column
    private String password_hash;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public User() {
    }

    public User(long id, String username, String password_hash) {
        this.id = id;
        this.username = username;
        this.password_hash = password_hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(username, user.username) &&
                Objects.equals(password_hash, user.password_hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password_hash);
    }
}
