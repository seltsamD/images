package model.db;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "projects")
@NamedQueries({
        @NamedQuery(name = Project.GET_BY_USER, query = "from Project where user_id = :id"),
        @NamedQuery(name = Project.GET_USER_BY_PROJECT, query = "select p.user.id from Project p where p.id = :id "),
        @NamedQuery(name = Project.GET_BY_USERNAME, query = "from Project p where p.user.username = :name"),
        @NamedQuery(name = Project.GET_BY_PROJECTNAME, query = "from Project p where p.projectName = :name"),
        @NamedQuery(name = Project.IS_UNIQUE_NAME, query = "select count(id) from Project p where p.projectName = :name"),
})
public class Project implements Serializable {
    public static final String GET_BY_USER = "Project.getByUser";
    public static final String GET_USER_BY_PROJECT = "Project.getUserByProject";
    public static final String GET_BY_USERNAME = "Project.getByUsername";
    public static final String GET_BY_PROJECTNAME = "Project.getByProjectName";
    public static final String IS_UNIQUE_NAME = "Project.isUniqueName";

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

    private User user;


    @PrePersist
    protected void onCreate() {
        date = new Date();
    }


    public Project() {
    }

    public Project(String projectName, User user) {
        this.user = user;
        this.projectName = projectName;
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