package be.kdg.groeph.service;

import be.kdg.groeph.dao.UserDao;
import be.kdg.groeph.model.TripUser;
import be.kdg.groeph.util.SHAEncryption;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.UUID;

@Transactional
@Service("mailService")
public class MailServiceImpl implements MailService {
    static Logger logger = Logger.getLogger(MailServiceImpl.class);

    @Autowired
    @Resource(name = "mailSender")
    private MailSender mailSender;

    @Qualifier("userDaoImpl")
    @Autowired
    UserDao userDao;

    @Override
    public void uponSuccessfulRegistration(String email) {
        String text="The user '" + email + "' is successfully registered";
        String subject="User Registration successful";
        sendMail(email,subject,text);
    }

    private void sendMail(String email, String subject, String text) {
        SimpleMailMessage[] mailMessageArray = new SimpleMailMessage[1];

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        mailMessageArray[0] = message;

        System.out.println("Sending email ....");
        mailSender.send(mailMessageArray);
    }

    @Override
    public boolean recoverPassword(String email) {
        TripUser userByEmail = userDao.getUserByEmail(email);
        if (userByEmail.isNull()) {
            return false;
        }
        String pw = randomString()   ;
        userByEmail.setTempPassword(SHAEncryption.encrypt(pw) );

        try {
            userDao.updateUser(userByEmail);
            logger.info("User updated| TempPassword is set for user: " + userByEmail.getEmail());
        } catch (SQLException e) {
            logger.warn("User update of TempPassword for user: " + userByEmail.getEmail() + " failed");
            return false;
        }

        sendMail(email,"Password recovery","** This is an automated message -- please do not reply as you will not receive a response. **" + "\n \n This message is in response to your request to reset your account password. \n \n Username: " + userByEmail.getEmail() + "\n New password: " + pw);

        return true;
    }

    public String randomString() {
        String random = UUID.randomUUID().toString().substring(0, 8);
        return random;
    }


}
