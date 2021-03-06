package be.kdg.groeph.service;

import be.kdg.groeph.model.Cost;
import be.kdg.groeph.model.Trip;
import be.kdg.groeph.model.TripUser;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * <p/>
 * Date: 28/02/13
 * Time: 14:14
 */
public interface CostService {

    public boolean addCost(Cost cost);
    public boolean updateCost(Cost cost);
    public boolean deleteCost(Cost cost);
    public List<Cost> getCostsByTrip(Trip trip);
    public Cost getCostByCostId(int id);
    public  Double getTotalCostByTrip(Trip trip);
    public  Double getTotalCostByUser(Trip trip, TripUser tripUser);
    public List<Cost> getCostByTripByUser(Trip trip, TripUser tripUser);
    public Double getCostForAUserByTripAndUser(Trip trip, TripUser tripUser);
    public Double getAverageCostByTrip(Trip trip);
}
