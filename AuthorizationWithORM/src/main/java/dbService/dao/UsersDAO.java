package dbService.dao;

import dbService.dataSets.UsersDataSet;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class UsersDAO {
    private Session session;

    public UsersDAO(Session session) {
        this.session = session;
    }

    public UsersDataSet get(long id) {
        return (UsersDataSet) session.get(UsersDataSet.class, id);
    }

    public UsersDataSet getUserByLogin(String login) {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return ((UsersDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult());
    }

    public long getUserId(String name) {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        return ((UsersDataSet) criteria.add(Restrictions.eq("name", name)).uniqueResult()).getId();
    }

    public long insertUser(String name) {
        if (checkUserFromDB(name)) return (Long) session.save(new UsersDataSet(name));
        else return 0;

    }

    public long insertUser(String login, String pass) {

        if (checkUserFromDB(login)) return (Long) session.save(new UsersDataSet(login, pass));
        else return 0;

    }

    private boolean checkUserFromDB(String login) {
        Criteria criteria = session.createCriteria(UsersDataSet.class);
        UsersDataSet usersDataSet = (UsersDataSet) criteria.add(Restrictions.eq("login", login)).uniqueResult();
        if(usersDataSet==null) return true;
        else return false;
    }
}
