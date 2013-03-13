package be.kdg.groeph.service;

import be.kdg.groeph.dao.CostDao;
import be.kdg.groeph.model.Cost;
import be.kdg.groeph.model.Trip;
import be.kdg.groeph.model.TripUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * Date: 28/02/13
 * Time: 14:14
 */

@Transactional
@Service("costService")
public class CostServiceImpl implements CostService {
    static Logger logger = Logger.getLogger(CostServiceImpl.class);
    @Qualifier("costDaoImpl")
    @Autowired
    CostDao costDao;

    @Override
    public boolean addCost(Cost cost) {
        try {
        return costDao.addCost(cost);
        } catch (Exception e){
            logger.error(e);
            return false;
        }
    }

    @Override
    public boolean updateCost(Cost cost) {
        try {
        return costDao.updateCost(cost);
        } catch (Exception e){
            logger.error(e);
            return false;
        }

    }

    @Override
    public boolean deleteCost(Cost cost) {
        try {
        return costDao.deleteCost(cost);
        } catch (Exception e){
            logger.error(e);
            return false;
        }
    }

    @Override
    public List<Cost> getCostsByTrip(Trip trip) {
        try {
        return costDao.getCostByTrip(trip);
        } catch (Exception e){
            logger.error(e);
            return null;
        }
    }
    @Override
    public Cost getCostByCostId(int id) {
        try {
        return costDao.getCostByCostId(id);
        } catch (Exception e ){
            logger.error(e);
            return null;
        }
    }

    @Override
    public Double getTotalCostByTrip(Trip trip) {
        try {
        return costDao.getTotalCostByTrip(trip);
        } catch (Exception e){
            logger.error(e);
            return null;
        }
    }

    @Override
    public Double getTotalCostByUser(Trip trip, TripUser tripUser) {
        try {
        return costDao.getTotalCostByUser(trip, tripUser);
        } catch (Exception e){
            logger.error(e);
            return null;
        }
    }
}
