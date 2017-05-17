package model;


import constants.ProjectConstants;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "projects")
@NamedQueries({
        @NamedQuery(name = ProjectConstants.getByUser, query = "from Project where user_id = :id"),
        @NamedQuery(name = ProjectConstants.getUserByProject, query = "select p.user.id from Project p where p.id = :id "),
        @NamedQuery(name = ProjectConstants.getByUsername, query = "select p from Project p where p.user.username = :name")
})
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @Size(min = 3, max = 50)
    private String projectName;


    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column
    private String username;

    @PrePersist
    protected void onCreate() {
        date = new Date();
    }


    public Project() {
    }

    public Project(String projectName, User user) {
        this.user = user;
        this.projectName = projectName;
        this.username = user.getUsername();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id &&
                Objects.equals(projectName, project.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectName);
    }
}