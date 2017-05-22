package model.db;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@NamedQueries({
        @NamedQuery(name = User.GET_ID_BY_USERNAME, query = "select id from User u where u.username = :user_name"),
        @NamedQuery(name = User.GET_BY_USERNAME, query = "from User u where u.username = :user_name")
})
public class User implements Serializable {
    public static final String GET_ID_BY_USERNAME = "User.getIdByUsername";
    public static final String GET_BY_USERNAME = "User.getByUsername";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @Size(min = 3, max = 50)
    private String username;

    @Column
    private String password;

    @Column
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public User() {
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
