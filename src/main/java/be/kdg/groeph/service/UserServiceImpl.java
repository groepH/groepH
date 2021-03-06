package be.kdg.groeph.service;

import be.kdg.groeph.dao.UserDao;
import be.kdg.groeph.model.TripUser;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Transactional
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {
    static Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Qualifier("userDaoImpl")
    @Autowired
    UserDao userDao;

    public boolean addUser(TripUser user) {
        try {
            TripUser userByEmail = userDao.getUserByEmail(user.getEmail());
            if (userByEmail.isNull()) {
                userDao.addUser(user);
                return true;
            }
            return false;
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean changePassword(TripUser user, String newPassword) {
        try {
            if (!user.isNull()) {
                user.setPassword(newPassword);
                user.setTempPassword("");
                try {
                    userDao.updateUser(user);
                    logger.info("Password changed for user: " + user.getEmail());
                } catch (SQLException e) {
                    logger.warn(e.getMessage());
                    return false;
                }
                return true;
            }
            return false;
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException, DataAccessException {
        try {
            boolean accountNonExpired = true;
            boolean accountNonLocked = true;
            boolean credentialsNonExpired = true;
            boolean enabled = true;
            User user = new User("test", "test", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, getDefaultAuthority());

            TripUser userDB = userDao.getUserByEmail(email);

            if (userDB.getRole().equals("ROLE_ADMIN")) {
                user = new User(userDB.getEmail(), userDB.getPassword(),
                        userDB.isEnabled(), userDB.isAccountNonExpired(), userDB.isCredentialsNonExpired(), userDB.isAccountNonLocked(), getAdminAuthority());
            } else if (userDB.getRole().equals("ROLE_USER")) {
                user = new User(userDB.getEmail(), userDB.getPassword(),
                        userDB.isEnabled(), userDB.isAccountNonExpired(), userDB.isCredentialsNonExpired(), userDB.isAccountNonLocked(), getDefaultAuthority());
            }
            return user;
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private Collection<GrantedAuthority> getDefaultAuthority() {
        try {
            Collection<GrantedAuthority> userAuthorities = new ArrayList<GrantedAuthority>();
            userAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return userAuthorities;
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private Collection<GrantedAuthority> getAdminAuthority() {
        try {
            Collection<GrantedAuthority> userAuthorities = new ArrayList<GrantedAuthority>();
            userAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            userAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return userAuthorities;
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void updateUser(TripUser user) {
        try {

            userDao.updateUser(user);

        } catch (SQLException|HibernateException  e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public TripUser getUserByEmail(String s) {
        try {
            TripUser tripUser = userDao.getUserByEmail(s);
            if (tripUser.isNull()) {
                return TripUser.INVALID_USER();
            } else {
                return userDao.getUserByEmail(s);
            }
        } catch (NullPointerException|HibernateException e) {
            logger.error(e.getMessage());
            return TripUser.INVALID_USER();
        }
    }

}
