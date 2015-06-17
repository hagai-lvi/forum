package main.Persistancy;

import main.User.UserForumID;
import main.interfaces.PersistancyAbstractionI;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Created by xkcd on 5/13/2015.
 */
public class HibernatePersistancyAbstractor implements PersistancyAbstractionI{

    private SessionFactory session_Factory = HibernateSessionFactory.getSessionFactory();

    private static HibernatePersistancyAbstractor singleton = null;

    private HibernatePersistancyAbstractor(){
        session_Factory = HibernateSessionFactory.getSessionFactory();
    }

    static public HibernatePersistancyAbstractor getPersistanceAbstractor(){
        if (singleton == null){
            singleton = new HibernatePersistancyAbstractor();
        }
        return singleton;
    }

    @Override
    public void save(Object o) {
        Session session = session_Factory.openSession();
        session.beginTransaction();
        session.save(o);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveOrUpdate(Object o) {
        Session session = session_Factory.openSession();
        session.flush();
        session.beginTransaction();
        session.saveOrUpdate(o);
        session.getTransaction().commit();
        session.clear();
        session.close();

    }

    @Override
    public <T> T load(Class<T> c, int id) {
        Session sess = session_Factory.openSession();
        T result = c.cast(sess.get(c, id));
        sess.close();
        return result;
    }

    @Override
    public <T> T load(Class<T> c, String id) {
        Session sess = session_Factory.openSession();
        T result = c.cast(sess.get(c, id));
        sess.close();
        return result;
    }

    @Override
    public <T> T load(Class<T> c, UserForumID idclass) {
        Session sess = session_Factory.openSession();
        T result = c.cast(sess.get(c, idclass));
        sess.close();
        return result;
    }

    @Override
    public void Update(Object o){
        Session session = session_Factory.openSession();
        session.flush();
        session.beginTransaction();
        session.saveOrUpdate(o);
        session.getTransaction().commit();
        session.clear();
        session.close();
    }


}