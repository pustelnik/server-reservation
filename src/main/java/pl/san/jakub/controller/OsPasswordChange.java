package pl.san.jakub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import pl.san.jakub.model.CredentialsAccess;
import pl.san.jakub.model.ServersAccess;
import pl.san.jakub.model.UsersAccess;
import pl.san.jakub.model.data.Credentials;
import pl.san.jakub.model.data.Servers;
import pl.san.jakub.model.data.Users;
import pl.san.jakub.tools.Environment;
import pl.san.jakub.tools.OperatingSystem;
import pl.san.jakub.tools.SSHConnector;
import pl.san.jakub.tools.WindowsNetUser;
import pl.san.jakub.tools.exceptions.GeneralServerException;
import pl.san.jakub.tools.exceptions.PasswordIsNotChangedException;

/**
 * Created by Jakub on 04.06.2016.
 */
public class OsPasswordChange {

    private final static Logger LOGGER = LoggerFactory.getLogger(OsPasswordChange.class);
    private static final int DEFAULT_PORT = 2222;
    private static final String NEW_PASSWORD = Environment.readProperty("server.new.password");
    private static final String DEFAULT_PASSWORD = Environment.readProperty("server.default.password");
    private ServersAccess serversAccess;
    private UsersAccess usersAccess;
    private CredentialsAccess credentialsAccess;

    public OsPasswordChange(ServersAccess serversAccess, UsersAccess usersAccess, CredentialsAccess credentialsAccess) {
        this.serversAccess = serversAccess;
        this.usersAccess = usersAccess;
        this.credentialsAccess = credentialsAccess;
    }

    public String changeOsPassword(Model model, Servers servers, String name, Users user, Credentials credentials) throws GeneralServerException {
        OperatingSystem operatingSystem = WindowsNetUser.checkOS(servers.getHost_name());
        switch (operatingSystem) {
            case WINDOWS:
                try {
                    WindowsNetUser.changeWindowsUserPassword(servers.getOs_ip(), credentials.getLogin(), credentials.getPassword(), NEW_PASSWORD);
                    credentials.setPassword(NEW_PASSWORD);
                    LOGGER.info("Ok. Reserving server for user " + name);
                    serversAccess.save(servers);
                    usersAccess.save(user, true);
                    credentialsAccess.save(credentials);
                } catch (PasswordIsNotChangedException | GeneralServerException e) {
                    return passwordNotChanged(model, e);
                }
                break;
            case LINUX:
                SSHConnector sshConnector = new SSHConnector(credentials.getIp(), DEFAULT_PORT, credentials.getLogin(), credentials.getPassword());
                try {
                    sshConnector.changePassword(NEW_PASSWORD);
                    credentials.setPassword(NEW_PASSWORD);
                    LOGGER.info("Ok. Reserving server for user " + name);
                    serversAccess.save(servers);
                    usersAccess.save(user, true);
                    credentialsAccess.save(credentials);
                } catch (PasswordIsNotChangedException | GeneralServerException e ) {
                    return passwordNotChanged(model, e);
                }
                break;
            case NON_AVAILABLE_PATH:
                model.addAttribute("error", "Can't connect to server! Reservation failed.");
                return "redirect:/servers";
        }
        throw new GeneralServerException();
    }

    public boolean restoreDefaultPassword(Servers server) throws PasswordIsNotChangedException {
        Users user = server.getUser();
        user.removeHostname(server);
        server.setUser(null);
        Credentials os = credentialsAccess.findByIp(server.getOs_ip());
        Credentials irmc = credentialsAccess.findByIp(server.getIrmc_ip());
        OperatingSystem operatingSystem = WindowsNetUser.checkOS(server.getHost_name());

        switch (operatingSystem) {

            case WINDOWS:
                try {
                    WindowsNetUser.changeWindowsUserPassword(server.getHost_name(), os.getLogin(), os.getPassword(), DEFAULT_PASSWORD);

                    serversAccess.save(server);
                    usersAccess.save(user, true);
                    os.setPassword(DEFAULT_PASSWORD);
                    credentialsAccess.save(os);
                    return true;
                } catch (PasswordIsNotChangedException | GeneralServerException e) {
                    LOGGER.debug(e.getMessage());
                    throw new PasswordIsNotChangedException(e.getMessage());
                }
            case LINUX:
                SSHConnector sshConnector = new SSHConnector(os.getIp(), DEFAULT_PORT, os.getLogin(), os.getPassword());
                try {
                    sshConnector.changePassword(DEFAULT_PASSWORD);
                    serversAccess.save(server);
                    usersAccess.save(user, true);
                    os.setPassword(DEFAULT_PASSWORD);
                    credentialsAccess.save(os);
                    return true;
                } catch (PasswordIsNotChangedException | GeneralServerException e) {
                    LOGGER.debug(e.getMessage());
                    return false;
                }
            case NON_AVAILABLE_PATH:
                break;

        }
        return false;

    }
    private String passwordNotChanged(Model model, Exception e) {
        LOGGER.debug(e.getMessage());
        model.addAttribute("error", "Error while reserving server. Changes are not saved! "+e.getMessage());
        LOGGER.info("Error while reserving server. Changes are not saved!");
        return "redirect:/servers";
    }
}
