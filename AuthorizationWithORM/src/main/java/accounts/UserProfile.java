package accounts;

public class UserProfile {
    private final String login;
    private final String password;
    private final String email;

    public UserProfile(String login){
        this.login=login;
        this.email=login;
        this.password=login;
    }
    public UserProfile(String login,String pass, String email){
        this.login = login;
        this.password = pass;
        this.email =email;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public String getLogin() {
        return login;
    }
}
