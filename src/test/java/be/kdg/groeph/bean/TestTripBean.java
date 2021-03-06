package be.kdg.groeph.bean;

import be.kdg.groeph.dao.TripDao;
import be.kdg.groeph.dao.UserDao;
import be.kdg.groeph.mockMother.UserMother;
//import be.kdg.groeph.model.Label;
import be.kdg.groeph.model.Trip;
import be.kdg.groeph.model.TripType;
import be.kdg.groeph.model.TripUser;
import be.kdg.groeph.service.TripService;
import org.hibernate.validator.constraints.NotEmpty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.faces.bean.ManagedProperty;
import javax.security.auth.login.LoginException;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:daoContext.xml"})
public class TestTripBean extends AbstractTransactionalJUnit4SpringContextTests {
    public static final String TIMEBOUND = "Timebound";
    public static final String REPEATING = "Repeating";
    public static final String ANYTIME = "AnyTime";
    public static final String TITLE_1 = "title1";
    public static final String TEST = "TestLabel";

    @Qualifier("tripBean")
    @Autowired
    TripBean tripBean;

    @Qualifier("registerBean")
    @Autowired
    RegisterBean registerBean;

    @Qualifier("loginBean")
    @Autowired
    LoginBean loginBean;

    @Autowired
    private TripDao tripDao;

    @Autowired
    private UserDao userDao;

    @ManagedProperty(value = "#{tripService}")
    @Autowired
    TripService tripService;
    private List<Trip> trips;

    private Trip trip;

    private final String validEmail = "greg.deckers@student.kdg.be";

    @Before
    public void init() throws ParseException, LoginException {
        tripDao.addTripType(new TripType(TIMEBOUND));
        tripDao.addTripType(new TripType(REPEATING));
        tripDao.addTripType(new TripType(ANYTIME));

        TripUser tripUser1 = UserMother.validUser2();
        tripUser1.setEnabled(true);
        tripUser1.setAccountNonLocked(true);
        tripUser1.setAccountNonExpired(true);
        tripUser1.setCredentialsNonExpired(true);
        userDao.addUser(tripUser1);

        TripUser user = userDao.getUserByEmail(validEmail);
        makeTrips(user);
        loginBean.setEmail(user.getEmail());
        loginBean.setPassword("password");
        loginBean.loginUser();
    }

    public void makeTrips(TripUser tripUser) {
        Calendar cal;
        cal = Calendar.getInstance();
        cal.set(2013, Calendar.MARCH, 29, 12, 00);
        TripType type = tripDao.getTypeByName(TIMEBOUND);
        /*List<Label> lbls = new ArrayList<Label>();
        lbls.add(new Label("Fun"));
        lbls.add(new Label(TEST)); */
        Trip trip = new Trip(TITLE_1, "desc", cal.getTime(), cal.getTime(), "label", type, true);
        trip.setVisible(false);
        trip.setStarted(false);
        Trip trip2 = new Trip("titel2", "desc", cal.getTime(), cal.getTime(), "label", type, true);
        trip2.setVisible(false);
        trip.setStarted(false);
        type = tripDao.getTypeByName(REPEATING);
        Trip trip3 = new Trip("titel3", "desc", cal.getTime(), cal.getTime(), "label", type, true);
        trip3.setVisible(false);
        trip.setStarted(false);
        Trip trip4 = new Trip("titel4", "desc", cal.getTime(), cal.getTime(), "label", type, false);
        trip4.setVisible(false);
        trip.setStarted(false);

        tripUser.addTrip(trip);
        tripUser.addTrip(trip2);
        tripUser.addTrip(trip3);
        tripUser.addTrip(trip4);
        tripService.addTrip(trip);
        tripService.addTrip(trip2);
        tripService.addTrip(trip3);
        tripService.addTrip(trip4);
        tripBean.setCurrentTrip(trip);

    }

    public void fillTripBean() {
        Calendar cal;
        cal = Calendar.getInstance();
        cal.set(2013, Calendar.MARCH, 29, 12, 00);
        tripBean.setTitle("Lekker int antwaarps");
        tripBean.setDescription("Een publieke test trip");
        tripBean.setStartTime(cal.getTime());
        cal.set(2013, Calendar.MARCH, 29, 12, 00);
        tripBean.setEndTime(cal.getTime());
        tripBean.setLabel("Test");
        tripBean.setTripType(TIMEBOUND);
        tripBean.setPublic(true);
    }

    @Test
    public void testGetAllTripTypes() {
        assertEquals("3 types should be fetched from database", 3, tripBean.getAllTripTypes().size());
    }

    @Test
    public void testAddValidOpenTrip() {
        fillTripBean();

        assertEquals("addTrip result should be SUCCESS for Open Trip", "SUCCESS", tripBean.addTrip());
        assertFalse("Trip should not be visible after adding", tripBean.isVisible());
        assertEquals("List of organised trips should be 5 after adding", 5, loginBean.getUser().getTrips().size());
        assertNull("Field should be null after adding", tripBean.getTitle());
        assertEquals("Dao returns 5 trip", 5, tripDao.getTripByUserId(loginBean.getUser()).size());
    }

    @Test
    public void testAddValidRepetitionTypeTrip() {

        fillTripBeanForRepetitionType();
        tripBean.setTitle("Yearly repetition trip");
        tripBean.setRepetitionType("Yearly");
        assertEquals("addTrip returns success", "SUCCESS", tripBean.addTrip());
        assertEquals("Yearly trip should be 2 times in database", 2, tripDao.getTripsByName("Yearly repetition trip").size());

        fillTripBeanForRepetitionType();
        tripBean.setTitle("Monthly repetition trip");
        tripBean.setRepetitionType("Monthly");
        assertEquals("addTrip returns success", "SUCCESS", tripBean.addTrip());
        assertEquals("Monthly trip should be 2 times in database", 2, tripDao.getTripsByName("Monthly repetition trip").size());

        fillTripBeanForRepetitionType();
        tripBean.setTitle("Weekly repetition trip");
        tripBean.setRepetitionType("Weekly");
        assertEquals("addTrip returns success", "SUCCESS", tripBean.addTrip());
        assertEquals("Weekly trip should be 2 times in database", 2, tripDao.getTripsByName("Weekly repetition trip").size());

        fillTripBeanForRepetitionType();
        tripBean.setTitle("Daily repetition trip");
        tripBean.setRepetitionType("Daily");
        assertEquals("addTrip returns success", "SUCCESS", tripBean.addTrip());
        assertEquals("Daily trip should be 2 times in database", 2, tripDao.getTripsByName("Daily repetition trip").size());
    }

    private void fillTripBeanForRepetitionType() {
        tripBean.setDescription("Test for different repeating types");
        tripBean.setStartTime(new Date());
        tripBean.setEndTime(new Date());
        tripBean.setLabel("repetition");
        tripBean.setTripType(REPEATING);
        tripBean.setNumberOfRepetitions(2);
    }

    @Test
    public void testGetAllPublicTrips() {
        tripBean.setFilter("");
        trips = tripBean.getAllPublicTrips();
        assertEquals("getOpenTrips should give a list of 3  trips", 3, trips.size());
        assertTrue("getOpenTrips should return public trips", trips.get(0).isPublic());
    }

    @Test
    public void testFilteredPublicTripsType() {
        tripBean.setFilter(REPEATING);
        trips = tripBean.getAllPublicTrips();
        assertEquals("getAllPublicTrips should return 1 trip", 1, trips.size());
        assertEquals("getAllPublicTrips should return 1 trip of the type Repeating", REPEATING, trips.get(0).getTripType().getType());
    }

    @Test
    public void testFilteredPublicTripsTitle() {
        tripBean.setFilter(TITLE_1);
        trips = tripBean.getAllPublicTrips();
        assertEquals("getAllPublicTrips should return 1 trip", 1, trips.size());
        assertEquals("getAllPublicTrips should return 1 trip with the title title1", TITLE_1, trips.get(0).getTitle());
    }

    //TODO: Maarten nazien!
    @Test
    public void testFilteredPublicTripsLabels() {
        tripBean.setFilter(TEST);
        trips = tripBean.getAllPublicTrips();
        //assertEquals("getAllPublicTrips should return 1 trip", 1, trips.size());
        //assertTrue("getAllPublicTrips should return 1 trip containing the label TestLabel", trips.get(0).getLabel().contains(TEST));
    }

    @Test
    public void testTripStarted() {
        assertFalse("trip is not started", tripBean.getCurrentTrip().isStarted());
        assertTrue("trip is started", tripBean.startTrip());
        assertTrue("trip is  started", tripBean.getCurrentTrip().isStarted());

        trip = tripDao.getTripById(tripBean.getCurrentTrip().getId());
        assertTrue("trip is started DAO", trip.isStarted());
    }

    @Test
    public void testTripStopped() {
        tripBean.getCurrentTrip().setStarted(true);
        assertTrue("trip is not stopped", tripBean.getCurrentTrip().isStarted());

        trip = tripDao.getTripById(tripBean.getCurrentTrip().getId());
        assertTrue("trip is running DAO", trip.isStarted());

        assertTrue("trip is stopped", tripBean.stopTrip());

        trip = tripDao.getTripById(tripBean.getCurrentTrip().getId());
        assertFalse("Trip is stopped DAO", trip.isStarted());
    }

    @Test
    public void testUpdateTrip() {
        Trip updateTrip = tripDao.getTripsByName("title1").get(0);
        tripBean.setCurrentTrip(updateTrip);

        assertEquals("The trip is not editable", false, tripBean.isEditableTrip());
        assertEquals("The trip must have 'desc' as description", "desc", updateTrip.getDescription());

        tripBean.setEditableTrip(true);

        tripBean.setNewTitle("TitleUpdate");
        tripBean.setNewDescription("Description after update");
        tripBean.setNewStartTime(new Date());
        tripBean.setNewEndTime(new Date());
        tripBean.setNewLabel("Label1");
        tripBean.setNewTripType(TIMEBOUND);
        tripBean.setNewIsPublic(true);

        assertEquals("The trip is editable", true, tripBean.isEditableTrip());

        tripBean.updateTrip();

        updateTrip = tripDao.getTripsByName("TitleUpdate").get(0);

        assertEquals("The trip is not editable anymore", false, tripBean.isEditableTrip());
        assertEquals("The trip must have 'Description after update' as description", "Description after update", updateTrip.getDescription());
    }


    //TODO: Testen uitwerken, trips aan loginbean.user hangen
//    @Test
//    public void testFilteredPrivateTripsType()
//    {
//        tripBean.setFilter(REPEATING);
//        trips= tripBean.getAllPrivateTrips();
//        assertEquals("getAllPrivateTrips should return 1 trip of the type Repeating", 1, trips.size());
//        assertEquals("getAllPrivateTrips should return 1 trip of the type Repeating", REPEATING, trips.get(0).getTitle());
//    }
//    @Test
//    public void testFilteredPrivateTripsTitle()
//    {
//        tripBean.setFilter(REPEATING);
//        trips= tripBean.getAllPrivateTrips();
//        assertEquals("getAllPrivateTrips should return 1 trip of the type Repeating", 1, trips.size());
//        assertEquals("getAllPrivateTrips should return 1 trip of the type Repeating", REPEATING, trips.get(0).getTitle());
//    }
//    @Test
//    public void testFilteredPrivateTripsLabels()
//    {
//        tripBean.setFilter(REPEATING);
//        trips= tripBean.getAllPrivateTrips();
//        assertEquals("getAllPrivateTrips should return 1 trip of the type Repeating", 1, trips.size());
//        assertEquals("getAllPrivateTrips should return 1 trip of the type Repeating", REPEATING, trips.get(0).getTitle());
//    }

     /*
    @Test
    public void testPublishTrip(){
        tripBean.setCurrentTrip(trip1);
        assertEquals("Current trip title must be Title1", TITLE_1, tripBean.getTitle());
        assertFalse("Trip1 is not visible", tripBean.currentTrip.isVisible());
        tripBean.publishTrip();
        assertTrue("Trip1 is visible", tripBean.currentTrip.isVisible());
        //TODO: trip1 gaan opzoeke en checke of die wel visible wordt opgeslagen
        //tripDao.getTripById()
    }

    //TODO: checken of dit werkt
    @Test
    public void testUpdateTrip(){
        Trip trip1 = tripDao.getTripById(1);
        assertEquals("Triptitle must be Title1", TITLE_1, trip1.getTitle());
        trip1.setTitle("ChangedTitle");
        tripDao.updateTrip(trip1);
        trip1 = tripDao.getTripById(1);
        assertEquals("Triptitle after update has to be: ChangedTitle","ChangedTitle", trip1.getTitle());
    }
    */
}