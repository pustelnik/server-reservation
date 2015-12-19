package pl.san.jakub;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import pl.san.jakub.config.HibernateConfig;
import pl.san.jakub.model.CredentialsAccess;
import pl.san.jakub.model.UsersAccess;
import pl.san.jakub.model.ServersAccess;
import pl.san.jakub.model.data.Credentials;
import pl.san.jakub.model.data.Users;
import pl.san.jakub.model.data.Servers;
import pl.san.jakub.tools.exceptions.GeneralServerException;

import java.util.List;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.util.AssertionErrors.assertEquals;

/**
 * Created by Jakub on 07.11.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = HibernateConfig.class)
public class DataSourceConnectionTest {

    @Autowired
    ServersAccess serversAccess;

    @Autowired
    UsersAccess usersAccess;

    @Autowired
    CredentialsAccess credentialsAccess;

    @Test
    @Transactional
    public void createNewServer() {
        try {
            serversAccess.save(
                    new Servers("FRONTSIDE", "172.17.72.156", "172.17.72.56"),
                    new Credentials("172.17.72.56","root", "Password1"),
                    new Credentials("172.17.72.156","admin", "admin"));

            assertEquals("Can't find given server!","FRONTSIDE",
                    serversAccess.findOne("FRONTSIDE").getHost_name());
        } catch (GeneralServerException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    public void hostnameShouldNotBeEmpty() {
        Users jank = usersAccess.findByUsername("jakub");
        if (jank.getHost_names().size() == 0) {
            throw new AssertionError("Hostname is empty!");
        }
    }

    @Test
    @Transactional
    public void removeServer() {
        serversAccess.delete("FRONTSIDE");
    }

    @Test
    @Transactional
    public void findVader() {

        Servers server = serversAccess.findOne("VADER");
        Assert.assertNotNull(server);
    }

    @Test
    @Transactional
    public void reserveSererForUser(){
        Servers server = serversAccess.findOne("VADER");
        Assert.assertNotNull(server);
        Users user = usersAccess.findByUsername("jakub");
        Assert.assertNotNull(user);
        server.setUser(user);
        serversAccess.save(server);
        assertEquals("Can't find user", serversAccess.findOne("VADER").getUser().getUsername(), "jakub");
    }

    @Test
    @Transactional
    public void createNewProfile() {
        try {
            serversAccess.save(new Servers("RAMMSTEIN", "172.17.72.179", "172.17.72.79"),
                    new Credentials("172.17.72.79","root", "Password1"),
                    new Credentials("172.17.72.179","admin", "admin"));
            assertEquals("Can't find given server!","RAMMSTEIN", serversAccess.findOne("RAMMSTEIN").getHost_name());

            List<Servers> all = (List<Servers>) serversAccess.findAll();
            Assert.assertThat(all.size(), is(1));
            usersAccess.save(new Users("Jakub", "P.","zenek", "password", "jakub@email.com", true), false);
            Assert.assertNotNull(usersAccess.findAll());
            assertEquals("Can't find given profile", "zenek", usersAccess.findByUsername("zenek").getUsername());
        } catch (GeneralServerException e) {
            System.out.println(e.getMessage());
        }

    }

    @Test
    @Transactional
    public void createNewCredentials() {
        try {
            credentialsAccess.save(new Credentials("172.17.72.184", "admin", "admin"));
            Credentials credentials = credentialsAccess.findByIp("172.17.72.184");
            Assert.assertNotNull(credentials);
        } catch (GeneralServerException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Transactional
    public void findHostNames() {
        Users user = usersAccess.findByUsername("jank");
        if(user.getHost_names().size()==0) {
            throw new AssertionError();
        }
    }


}
