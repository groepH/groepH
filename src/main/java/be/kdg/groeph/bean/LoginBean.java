package be.kdg.groeph.bean;

import be.kdg.groeph.model.TripUser;
import be.kdg.groeph.service.LoginService;
import be.kdg.groeph.service.UserService;
import be.kdg.groeph.util.SHAEncryption;
import be.kdg.groeph.util.Tools;
import org.apache.log4j.Logger;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import javax.security.auth.login.LoginException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

@Component
@Named
@SessionScoped
public class LoginBean implements Serializable {
    static Logger logger = Logger.getLogger(LoginBean.class);
    public static final String RESET = "RESET";;

    @ManagedProperty(value = "#{loginService}")
    @Autowired
    LoginService loginService;

    @Autowired
    UserService userService;

    @Qualifier("socialBean")
    @ManagedProperty(value = "#{socialBean}")
    @Autowired
    SocialBean socialBean;

    @Qualifier("tripBean")
    @ManagedProperty(value = "#{tripBean}")
    @Autowired
    TripBean tripBean;

    @NotEmpty(message = "{email} {notempty}")
    @Email(message = "{email} {validEmail}")
    private String email;
    @NotEmpty(message = "{password} {notempty}")
    private String password;
    @NotEmpty(message = "{password} {notempty}")
    private String secondPassword;

    private boolean isLoggedIn;

    TripUser user;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecondPassword() {
        return secondPassword;
    }

    public void setSecondPassword(String password2) {
        this.secondPassword = password2;
    }

    public TripUser getUser() {
        return user;
    }

    public void setUser(TripUser user) {
        this.user = user;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String loginUser() throws LoginException {
        try {
            user = loginService.loginUser(email, SHAEncryption.encrypt(password));
            if (user.isNull()) {
                logger.info("User with: " + email + " is null when trying to log in.");
                return Tools.FAILURE;
            } else if (user.getTempPassword() != null && user.getTempPassword().equals(SHAEncryption.encrypt(password))) {
                userService.changePassword(user, user.getTempPassword());
                isLoggedIn = true;
                logger.info("User: " + user.getEmail() + " logged in with temporary password.");
                return RESET;
            } else {
                isLoggedIn = true;
                logger.info("User: " + user.getEmail() + " logged in");
                return Tools.SUCCESS;
            }
        } catch (NullPointerException e) {
            logger.error(e.toString());
            return Tools.FAILURE;
        }
    }


    public String logOut() {
        try {
            isLoggedIn = false;
            SecurityContextHolder.getContext().setAuthentication(null);
            if (socialBean.isLoggedIn()) {
                socialBean.logout();
            }
            tripBean.currentTrip = null;
            logger.info("User: " + user.getEmail() + " logged out");
            setUser(null);
            return Tools.SUCCESS;
        } catch (NullPointerException e) {
            logger.error(e.toString());
            return Tools.FAILURE;
        }
    }

    public String tempPasswordLogin() {
        try {
            if (password.equals(secondPassword)) {
                userService.changePassword(user, SHAEncryption.encrypt(password));
            }
            logger.info("user: " + user.getEmail() + " his temporary password set to current password and temporary password has been removed.");
            return Tools.SUCCESS;
        } catch (NullPointerException e) {
            logger.error(e.toString());
            return Tools.FAILURE;
        }
    }
}

