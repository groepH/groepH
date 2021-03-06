package be.kdg.groeph.bean;

import be.kdg.groeph.dao.TripDao;
import be.kdg.groeph.dao.UserDao;
import be.kdg.groeph.mockMother.UserMother;
//import be.kdg.groeph.model.Label;
import be.kdg.groeph.model.Trip;
import be.kdg.groeph.model.TripType;
import be.kdg.groeph.model.TripUser;
import be.kdg.groeph.service.TripService;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:daoContext.xml"})
public class TestParticipantsBean extends AbstractTransactionalJUnit4SpringContextTests {
    public static final String TIMEBOUND = "Timebound";
    public static final String REPEATING = "Repeating";
    public static final String ANYTIME = "AnyTime";

    private final String invalidEmail = "jajajaInvalid";
    private final String validEmail = "guntherlaurijssens@gmail.com";
    private final String validEmail2 = "greg.deckers@student.kdg.be";

    private final String validEmails = "greg.deckers@student.kdg.be; gunther.laurijssens@student.kdg.be";
    private final String inValidEmails = "greg.deckersstudent.kdg.be; gunther.laurijssens@student";

    @Qualifier("participantsBean")
    @Autowired
    ParticipantsBean participantsBean;

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

    @Before
    public void init() throws ParseException, LoginException {
        tripDao.addTripType(new TripType(TIMEBOUND));
        tripDao.addTripType(new TripType(REPEATING));
        tripDao.addTripType(new TripType(ANYTIME));

        //First user
        makeUser(validEmail);
        System.out.println("user 1 made");

        //Second user
        makeUser(validEmail2);
        System.out.println("user 2 made");

        TripUser user = userDao.getUserByEmail(validEmail);

        loginBean.setEmail(user.getEmail());
        loginBean.setPassword("password");
        loginBean.loginUser();
    }

    private void makeUser(String email) {
        TripUser tripUser = UserMother.validUser1();
        tripUser.setEmail(email);
        tripUser.setEnabled(true);
        tripUser.setAccountNonLocked(true);
        tripUser.setAccountNonExpired(true);
        tripUser.setCredentialsNonExpired(true);
        userDao.addUser(tripUser);
    }

    @Test
    public void testInvalidEmail() {
        assertFalse("invalidEmail test. Excpected false", participantsBean.isValidMail(invalidEmail));
    }

    @Test
    public void testValidEmail() {
        assertTrue("validEmail test. Excpected true", participantsBean.isValidMail(validEmail));
    }

    @Test
    public void testValidEmails() {
        assertEquals("validEmails test. Expected true", true, participantsBean.validMails(validEmails));
    }

    @Test
    public void testInValidEmails() {
        assertFalse("validEmails test. Expected false", participantsBean.validMails(inValidEmails));
    }

    @Test
    public void testSendValidEmail() {
        participantsBean.setEmails(validEmail);
        Calendar cal;
        cal = Calendar.getInstance();
        cal.set(2013, Calendar.MARCH, 29, 12, 00);
        tripBean.setTitle("Lekker int antwaarps");
        tripBean.setDescription("Een publieke test trip");
        tripBean.setStartTime(cal.getTime());
        cal.set(2013, Calendar.MARCH, 29, 12, 00);
        tripBean.setEndTime(cal.getTime());
        //ArrayList<Label> lbls = new ArrayList<Label>();
        tripBean.setLabel("Test");
        /*lbls.add(new Label("Test"));
        lbls.add(new Label("Test1"));
        lbls.add(new Label("Test2"));
        lbls.add(new Label("Test3"));
        lbls.add(new Label("Test4"));
        tripBean.setLabels(lbls);   */
        tripBean.setTripType("Timebound");
        tripBean.setPublic(true);
        tripBean.addTrip();

        assertTrue("sendValidEmail test. Expected true", participantsBean.sendInvitations());
    }

    @Test
    public void testSendInvalidEmail() {
        participantsBean.setEmails(invalidEmail);
        assertFalse("sendInvalidEmail test. Expected false", participantsBean.sendInvitations());
    }

    @Test
    public void testSendValidEmails() throws LoginException {
        participantsBean.setEmails(validEmails);
        Calendar cal;
        cal = Calendar.getInstance();
        cal.set(2013, Calendar.MARCH, 29, 12, 00);
        tripBean.setTitle("Lekker int antwaarps");
        tripBean.setDescription("Een publieke test trip");
        tripBean.setStartTime(cal.getTime());
        cal.set(2013, Calendar.MARCH, 29, 12, 00);
        tripBean.setEndTime(cal.getTime());
        //ArrayList<Label> lbls = new ArrayList<Label>();
        tripBean.setLabel("Test");
        /*lbls.add(new Label("Test"));
        lbls.add(new Label("Test1"));
        lbls.add(new Label("Test2"));
        lbls.add(new Label("Test3"));
        lbls.add(new Label("Test4"));
        tripBean.setLabels(lbls);         */
        tripBean.setTripType("Timebound");
        tripBean.setPublic(true);
        tripBean.addTrip();

        assertTrue("sendValidEmails test. Expected true", participantsBean.sendInvitations());
        loginBean.logOut();
        loginBean.setEmail(validEmail2);
        loginBean.setPassword("password");
        loginBean.loginUser();
        assertEquals("User is invited for 1 trip",1, tripBean.getAllInvitedTrips().size());
        tripBean.setCurrentTrip(tripBean.getAllInvitedTrips().get(0));
        tripBean.confirmParticipation();
        assertEquals("User had confirmed 1 trip",1,tripBean.getAllParticipatedTrips().size());
    }


}
