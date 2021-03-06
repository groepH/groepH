package be.kdg.groeph.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:daoContext.xml"})
public class TestRandomPassword extends AbstractJUnit4SpringContextTests {
    @Test
    public void testRandomPassword(){
        Assert.assertNotEquals("Generated password is random", RandomPassword.generatePassword(), RandomPassword.generatePassword());
    }
}
