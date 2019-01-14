package accounts;

import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AccountService {
    private final DBService dbService;
    private final Map<String, UserProfile> loginToProfile;
    private final Map<String, Long> sessionIdToProfile;

    public AccountService() {
        this.dbService = new DBService();
        this.loginToProfile = new HashMap<>();
        this.sessionIdToProfile = new HashMap<>();
    }

    public void addNewUser(UserProfile userProfile) {
        dbService.addUser(userProfile);
        loginToProfile.put(userProfile.getLogin(), userProfile);
    }

    public UserProfile getUserByLogin(String login) throws DBException {
        UsersDataSet usersDataSet = dbService.getUser(login);
        if (usersDataSet == null) {
            return null;
        }
        String password = dbService.getUser(login).getPassword();
        String email = dbService.getUser(login).getEmail();

        return new UserProfile(login, password, email);
    }

    public UserProfile getUserBySessionId(String sessionId) {
        UsersDataSet usersDataSet = null;
        try {
            usersDataSet = dbService.getUser(sessionIdToProfile.get(sessionId));
        } catch (DBException e) {
            e.printStackTrace();
        }
        return new UserProfile(usersDataSet.getLogin(), usersDataSet.getPassword(), usersDataSet.getEmail());
    }

    public void addSession(String sessionId, UserProfile userProfile) throws DBException {
        ;

        sessionIdToProfile.put(sessionId, dbService.getUser(userProfile.getLogin()).getId());
    }

    public void deleteSession(String sessionId) {
        sessionIdToProfile.remove(sessionId);
    }
}
