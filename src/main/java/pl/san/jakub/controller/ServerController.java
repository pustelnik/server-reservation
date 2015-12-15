package pl.san.jakub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.san.jakub.model.CredentialsAccess;
import pl.san.jakub.model.ServersAccess;
import pl.san.jakub.model.UsersAccess;
import pl.san.jakub.model.data.Credentials;
import pl.san.jakub.model.data.Servers;
import pl.san.jakub.model.data.Users;
import pl.san.jakub.tools.OperatingSystem;
import pl.san.jakub.tools.WindowsNetUser;
import pl.san.jakub.tools.exceptions.Environment;
import pl.san.jakub.tools.exceptions.PasswordDoesNotMatchException;
import pl.san.jakub.tools.exceptions.PasswordIsNotChangedException;
import pl.san.jakub.tools.SSHConnector;

import java.util.List;

/**
 * Created by Jakub on 08.11.2015.
 */

@Controller
@RequestMapping("/servers")
public class ServerController {

    private static final int DEFAULT_PORT = 2222;
    private static final String NEW_PASSWORD = Environment.readProperty("server.new.password");
    private static final String DEFAULT_PASSWORD = Environment.readProperty("server.default.password");
    private ServersAccess serversAccess;
    private UsersAccess usersAccess;
    private CredentialsAccess credentialsAccess;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);

    @Autowired
    public ServerController(ServersAccess serversAccess, UsersAccess usersAccess, CredentialsAccess credentialsAccess) {
        this.serversAccess = serversAccess;
        this.usersAccess = usersAccess;
        this.credentialsAccess = credentialsAccess;
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<Servers> servers() {
        return serversAccess.findAll();
    }

    @RequestMapping(value = "/reserve",method = RequestMethod.POST)
    public String reserveServer(ServerForm serverForm,  Model model) {

        String host_name = serverForm.getHost_name();
        LOGGER.info("SEARCHING FOR SERVER: " + host_name + "!");
        Servers servers = serversAccess.findOne(host_name);

        if(servers.getUser() != null) {
            model.addAttribute("error", "Server is already reserved by user " + servers.getUser().getUsername());
            LOGGER.info("Server is already reserved.");
            return "servers";

        } else {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            LOGGER.info("SEARCHING FOR USER: " + name + "!");

            Users user = usersAccess.findByUsername(name);
            servers.setUser(user);
            user.addHostname(servers);
            Credentials credentials = credentialsAccess.findByIp(servers.getOs_ip());

            String viewName = changeOsPassword(model, servers, name, user, credentials);
            if (viewName != null) return viewName;
        }
        return "redirect:/servers/" + servers.getHost_name();
    }

    private String changeOsPassword(Model model, Servers servers, String name, Users user, Credentials credentials) {
        OperatingSystem operatingSystem = WindowsNetUser.checkOS(servers.getHost_name());
        switch (operatingSystem) {

            case WINDOWS:
                try {
                    WindowsNetUser.changeWindowsUserPassword(servers.getHost_name(), credentials.getLogin(), credentials.getPassword(), NEW_PASSWORD);
                    credentials.setPassword(NEW_PASSWORD);
                    LOGGER.info("Ok. Reserving server for user " + name);
                    serversAccess.save(servers);
                    usersAccess.save(user);
                    credentialsAccess.save(credentials);
                } catch (PasswordIsNotChangedException e) {
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
                    usersAccess.save(user);
                    credentialsAccess.save(credentials);
                } catch (PasswordIsNotChangedException e ) {
                    return passwordNotChanged(model, e);
                }
                break;
            case NON_AVAILABLE_PATH:
                model.addAttribute("error", "Can't connect to server! Reservation failed.");
                return "redirect:/servers";

        }
        return null;
    }

    @RequestMapping(value = "/reserve/resign", method = RequestMethod.POST)
    public String resignServer(ServerForm serverForm, Model model){
        String host_name = serverForm.getHost_name();
        LOGGER.info("SEARCHING FOR SERVER: " + host_name + "!");
        Servers servers = serversAccess.findOne(host_name);

        boolean done = restoreDefaultPassword(servers);
        if(done) {
            model.addAttribute("msg", servers.getHost_name() +" reservation canceled successfully.");
        }
        if(!done) {
            model.addAttribute("error", "Error while reserving server. Wrong username or password. Changes are not saved!");
        }

        return "redirect:/servers";
    }

    @RequestMapping(value = "/{hostName}", method = RequestMethod.GET)
    public String server(@PathVariable("hostName") String hostName, Model model) {
        Servers servers = serversAccess.findOne(hostName);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        if(servers.getUser() != null && servers.getUser().getUsername().equals(name)) {
            Credentials credentialsIrmc = credentialsAccess.findByIp(servers.getIrmc_ip());
            Credentials credentialsOS = credentialsAccess.findByIp(servers.getOs_ip());
            model.addAttribute(servers);
            model.addAttribute("credentialsIrmc", credentialsIrmc);
            model.addAttribute("credentialsOS", credentialsOS);

            return "server";
        } else {
            model.addAttribute("error", "You can't view details of servers you aren't assigned to!");
            return "redirect:/servers";
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveServer(ServerForm form, Model model) {
        serversAccess.save(new Servers(form.getHost_name(), form.getOs_ip(), form.getIrmc_ip()),
                new Credentials(form.getIrmc_ip(), form.getIrmcLogin(), form.getIrmcPassword()),
                new Credentials(form.getOs_ip(), form.getOsLogin(), form.getOsPassword()));
        return "redirect:/servers";
    }

    private boolean restoreDefaultPassword(Servers server) {
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
                    usersAccess.save(user);
                    os.setPassword(DEFAULT_PASSWORD);
                    credentialsAccess.save(os);
                    return true;
                } catch (PasswordIsNotChangedException e) {
                    LOGGER.debug(e.getMessage());
                    return false;
                }
            case LINUX:
                SSHConnector sshConnector = new SSHConnector(os.getIp(), DEFAULT_PORT, os.getLogin(), os.getPassword());
                try {
                    sshConnector.changePassword(DEFAULT_PASSWORD);
                    serversAccess.save(server);
                    usersAccess.save(user);
                    os.setPassword(DEFAULT_PASSWORD);
                    credentialsAccess.save(os);
                    return true;
                } catch (PasswordIsNotChangedException e) {
                    LOGGER.debug(e.getMessage());
                    return false;
                }
            case NON_AVAILABLE_PATH:
                break;

        }
        return false;

    }
    private String passwordNotChanged(Model model, PasswordIsNotChangedException e) {
        LOGGER.debug(e.getMessage());
        model.addAttribute("error", "Error while reserving server. Changes are not saved!");
        LOGGER.info("Error while reserving server. Changes are not saved!");
        return "redirect:/servers";
    }


}
