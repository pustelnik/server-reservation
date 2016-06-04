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
import pl.san.jakub.controller.forms.ServerForm;
import pl.san.jakub.model.CredentialsAccess;
import pl.san.jakub.model.ServersAccess;
import pl.san.jakub.model.UsersAccess;
import pl.san.jakub.model.data.Credentials;
import pl.san.jakub.model.data.Servers;
import pl.san.jakub.model.data.Users;
import pl.san.jakub.tools.Environment;
import pl.san.jakub.tools.exceptions.GeneralServerException;
import pl.san.jakub.tools.exceptions.PasswordIsNotChangedException;

import java.util.List;

/**
 * Created by Jakub on 08.11.2015.
 */

@Controller
@RequestMapping("/servers")
public class ServerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);
    private static final boolean PASSWORD_CHANGE_ENABLED = Environment.readBooleanProperty("server.password.change");
    private final ServersAccess serversAccess;
    private final UsersAccess usersAccess;
    private final CredentialsAccess credentialsAccess;
    private final OsPasswordChange osPasswordChange;

    @Autowired
    public ServerController(ServersAccess serversAccess, UsersAccess usersAccess, CredentialsAccess credentialsAccess) {
        this.serversAccess = serversAccess;
        this.usersAccess = usersAccess;
        this.credentialsAccess = credentialsAccess;
        this.osPasswordChange = new OsPasswordChange(serversAccess,usersAccess,credentialsAccess);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Servers> servers() {
        return serversAccess.findAll();
    }

    @RequestMapping(value = "/reserve",method = RequestMethod.POST)
    public String reserveServer(ServerForm serverForm, Model model) {

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

            if(PASSWORD_CHANGE_ENABLED) {
                try {
                    String viewName = osPasswordChange.changeOsPassword(model, servers, name, user, credentials);
                    if (viewName != null) return viewName;
                } catch (GeneralServerException e) {
                    LOGGER.warn(e.getMessage());
                }
            }
        }
        return "redirect:/servers/" + servers.getHost_name();
    }

    @RequestMapping(value = "/reserve/resign", method = RequestMethod.POST)
    public String resignServer(ServerForm serverForm, Model model){
        String host_name = serverForm.getHost_name();
        LOGGER.info("SEARCHING FOR SERVER: " + host_name + "!");
        Servers servers = serversAccess.findOne(host_name);

        if(!PASSWORD_CHANGE_ENABLED) {
            return "redirect:/servers";
        }
        boolean done = false;
        try {
            done = osPasswordChange.restoreDefaultPassword(servers);
        } catch (PasswordIsNotChangedException e) {
            LOGGER.debug(e.getMessage());
            model.addAttribute("error",e.getMessage());
        }
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
        try {
            serversAccess.save(new Servers(form.getHost_name(), form.getOs_ip(), form.getIrmc_ip(), form.getModel(),
                            form.getRackPosition(), form.getLan(), form.getOperatingSystem(), form.getComment()),
                    new Credentials(form.getIrmc_ip(), form.getIrmcLogin(), form.getIrmcPassword()),
                    new Credentials(form.getOs_ip(), form.getOsLogin(), form.getOsPassword()));
        } catch (GeneralServerException e) {
            LOGGER.debug(e.getMessage());
        }
        return "redirect:/servers";
    }
}
