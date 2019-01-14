package dbService.dataSets;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class UsersDataSet {
    private static final long serialVersionUID = -8706689714326132798L;
    @Id
    @Column(name = "id", unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true, updatable = false)
    private String name;

    @SuppressWarnings("UnusedDeclaration")
    public UsersDataSet() {

    }

    @Column(name = "login", unique = true, updatable = false)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @SuppressWarnings("UnusedDeclaration")
    public UsersDataSet(long id, String name) {
        this.setId(id);
        this.setName(name);
    }
    @SuppressWarnings("UnusedDeclaration")
    public UsersDataSet(String login,String password){
        this.login = login;
        this.password = password;
    }

    public UsersDataSet(String name) {
   //     this.setId(-1);
        this.setName(name);
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
