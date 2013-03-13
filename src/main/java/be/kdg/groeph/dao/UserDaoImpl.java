package be.kdg.groeph.dao;

import be.kdg.groeph.model.Trip;
import be.kdg.groeph.model.TripUser;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
public class UserDaoImpl implements UserDao {
    static Logger logger = Logger.getLogger(UserDaoImpl.class);

    @Qualifier("sessionFactory")
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public boolean addUser(TripUser user) {
        try {
        sessionFactory.getCurrentSession().saveOrUpdate(user);
        return true;
        } catch (Exception e){
            logger.error(e);
            return false;
        }
    }

    @Override
    public TripUser getUserByEmail(String email) {
        try {
        Query query = sessionFactory.getCurrentSession().createQuery("from TripUser where email=:email");
        query.setParameter("email", email);
        if (query.uniqueResult() == null) {
            return TripUser.INVALID_USER();
        }
        return (TripUser) query.uniqueResult();
        } catch (Exception e){
            logger.error(e);
            return null;
        }
    }

    @Override
    public void updateUser(TripUser user) {
        try {
        sessionFactory.getCurrentSession().update(user);
        } catch (Exception e){
            logger.error(e);
        }
    }


    @Override
    public void addInvitedUser(TripUser tripUser) {
        try {
        sessionFactory.getCurrentSession().saveOrUpdate(tripUser);
        } catch (Exception e){
            logger.error(e);
        }
    }


}
