package dbService;

import accounts.UserProfile;
import dbService.dao.UsersDAO;
import dbService.dataSets.UsersDataSet;
import org.h2.jdbcx.JdbcDataSource;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.SQLException;


public class DBService {
    private static final String hibernate_show_sql = "true";
    private static final String hibernate_hbm2ddl_auto = "update";
    private final SessionFactory sessionFactory;
    public DBService(){
        Configuration configuration = getH2Configuration();
        sessionFactory=createSessionFactory(configuration);

    }
    public long addUser(String name) throws DBException {
        try{ Session session =sessionFactory.openSession();
            Transaction transaction=session.beginTransaction();
            UsersDAO dao = new UsersDAO(session);
            long id=dao.insertUser(name);
            transaction.commit();
            session.close();
            return id;
        }catch (HibernateException e){

            throw new DBException(e);
        }
    }
    public long addUser(UserProfile userProfile){
        Session session =sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        UsersDAO dao = new UsersDAO(session);
        long id=dao.insertUser(userProfile.getLogin(),userProfile.getPassword());
        transaction.commit();
        session.close();
        return id;
    }
    public long addUser(String login,String pass) throws DBException {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction =session.beginTransaction();
            UsersDAO dao = new UsersDAO(session);
            long id =dao.insertUser(login,pass);
            transaction.commit();
            session.close();
            return id;

        }catch (HibernateException e){
            throw  new DBException(e);
        }
    }
    public UsersDataSet getUser(long id) throws DBException {
        try{
            Session session=sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UsersDataSet dataSet =dao.get(id);
            session.close();
            return dataSet;
        }catch (HibernateException e){
            throw  new DBException(e);
        }
    }
    public UsersDataSet getUser(String login) throws DBException {
        try{
            Session session=sessionFactory.openSession();
            UsersDAO dao = new UsersDAO(session);
            UsersDataSet dataSet =dao.getUserByLogin(login);
            session.close();
            return dataSet;
        }catch (HibernateException e){
            throw  new DBException(e);
        }
    }
    public void printConnectionInfo(){
        try {
            SessionFactoryImpl sessionFactoryImpl=(SessionFactoryImpl) sessionFactory;
            Connection connection =sessionFactoryImpl.getConnectionProvider().getConnection();
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    private Configuration getH2Configuration(){
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UsersDataSet.class);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./h2db");
        configuration.setProperty("hibernate.connection.username", "tully");
        configuration.setProperty("hibernate.connection.password", "tully");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", hibernate_hbm2ddl_auto);
        return configuration;
    }
    private Connection getH2Connection() throws SQLException {
        String url = "jdbc:h2:./h2db";
        String name = "test";
        String pass = "test";
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL(url);
        ds.setUser(name);
        ds.setPassword(pass);
        Connection connection=ds.getConnection();
        return connection;
    }
    private static SessionFactory createSessionFactory(Configuration configuration){
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry= builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }
}
