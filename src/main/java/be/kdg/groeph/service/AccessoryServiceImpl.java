package be.kdg.groeph.service;

import be.kdg.groeph.dao.AccessoryDao;
import be.kdg.groeph.model.Accessory;
import be.kdg.groeph.model.Trip;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Transactional
@Service("accessoryService")
public class AccessoryServiceImpl implements AccessoryService {
    static Logger logger = Logger.getLogger(AccessoryServiceImpl.class);
    @Qualifier("accessoryDaoImpl")
    @Autowired
    AccessoryDao accessoryDao;

    @Override
    public boolean addAccessory(Accessory accessory) {
        try {
            logger.info("Accessory: " + accessory.getDescription() + " created");
            return accessoryDao.addAccessory(accessory);
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateAccessory(Accessory accessory) {
        try {
            return accessoryDao.updateAccessory(accessory);
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteAccessory(Accessory accessory) {
        try {
            return accessoryDao.deleteAccessory(accessory);
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Accessory getAccessoryById(int id) {
        try {
            return accessoryDao.getAccessoryById(id);
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Accessory> getAccessoriesByTrip(Trip trip) {
        try {
            return accessoryDao.getAccessoriesByTrip(trip);
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
