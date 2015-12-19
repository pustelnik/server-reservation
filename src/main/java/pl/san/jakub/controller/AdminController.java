package pl.san.jakub.controller;

import org.apache.cxf.common.i18n.Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.san.jakub.model.AuthoritiesAccess;
import pl.san.jakub.model.CredentialsAccess;
import pl.san.jakub.model.ServersAccess;
import pl.san.jakub.model.UsersAccess;
import pl.san.jakub.model.data.Credentials;
import pl.san.jakub.model.data.Servers;
import pl.san.jakub.model.data.Users;
import pl.san.jakub.tools.OperatingSystem;
import pl.san.jakub.tools.WindowsNetUser;
import pl.san.jakub.tools.WindowsNetUser.ConnectionStatus;
import pl.san.jakub.tools.exceptions.GeneralServerException;
import pl.san.jakub.tools.exceptions.ServerCreationException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Jakub on 14.11.2015.
 */

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    public static final int PING_TIMEOUT = 5;
    private ServersAccess serversAccess;
    private CredentialsAccess credentialsAccess;
    private UsersAccess usersAccess;
    private AuthoritiesAccess authoritiesAccess;

    @Autowired
    public AdminController(ServersAccess serversAccess, CredentialsAccess credentialsAccess,
                           UsersAccess usersAccess, AuthoritiesAccess authoritiesAccess) {
        this.serversAccess = serversAccess;
        this.credentialsAccess = credentialsAccess;
        this.usersAccess = usersAccess;
        this.authoritiesAccess = authoritiesAccess;
    }

    @RequestMapping(method = GET)
    public String getAdminHome() {

        return "admin";
    }

    @RequestMapping(value = "/users", method = GET)
    public String usersList(Model model) {

        model.addAttribute("users", usersAccess.findAll());

        return "adm_users";
    }

    @RequestMapping(value = "/users", method = POST)
    public String editUser(RegisterForm registerForm, Model model) {
        Users users = usersAccess.findByUsername(registerForm.getUsername());

        String firstname = registerForm.getFirstName();
        String lastName = registerForm.getLastName();
        String password = registerForm.getPassword();
        boolean isEnabled = registerForm.isEnabled();

        if(isNotBlank(firstname)) {
            users.setFirstName(firstname);
        }
        if(isNotBlank(lastName)) {
            users.setLastName(lastName);
        }
        if(isNotBlank(password)) {
            users.setPassword(password);
        }
        users.setEnabled(isEnabled);

        try {
            usersAccess.save(users, true);
        } catch (GeneralServerException e) {
            LOGGER.debug(e.getMessage());
            model.addAttribute("error", "Operation failed because DB error!");
            return "redirect:/users";
        }
        model.addAttribute("msg", "Successfully changed user " + users.getUsername() + " account details.");

        return "redirect:/admin/users/"+users.getUsername();

    }

    @RequestMapping(value = "/removeUser", method = POST)
    public String removeUser(RegisterForm registerForm, Model model) {
        Users user = usersAccess.findByUsername(registerForm.getUsername());

        if(user == null || user.getHost_names().size() != 0) {
            model.addAttribute("error", "User is not removed!");
            return "redirect:/admin/users";
        }
        usersAccess.delete(user.getUsername());
        authoritiesAccess.remove(user.getUsername());

        model.addAttribute("msg", "Successfully removed user "+user.getUsername() + ".");
        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/removeServer", method = POST)
    public String removeServer(ServerForm form, Model model) {
        Servers server = serversAccess.findOne(form.getHost_name());

        if(null == server) {
            model.addAttribute("error", "Server is not removed!");
            return "adm_servers";
        }
        try {
            removeReservations(server);
        } catch (GeneralServerException e) {
            model.addAttribute("error", "Operation failed because DB error!");
            return "redirect:/admin/servers";
        }
        serversAccess.delete(server.getHost_name());
        credentialsAccess.delete(server.getIrmc_ip());
        credentialsAccess.delete(server.getOs_ip());
        model.addAttribute("msg", "Successfully removed server "+server.getHost_name() + ".");
        return "redirect:/admin/servers";
    }

    @RequestMapping(value = "/editServer", method = POST)
    public String editServer(ServerForm form, Model model) {
        try {
            Servers server = serversAccess.findOne(form.getHost_name());
            Credentials os = credentialsAccess.findByIp(server.getOs_ip());
            Credentials irmc = credentialsAccess.findByIp(server.getIrmc_ip());

            String irmcLogin = form.getIrmcLogin();
            String irmcPassword = form.getIrmcPassword();
            String osLogin = form.getOsLogin();
            String osPassword = form.getOsPassword();

            if(isNotBlank(irmcLogin)) {
                irmc.setLogin(irmcLogin);
            }
            if(isNotBlank(irmcPassword)) {
                irmc.setPassword(irmcPassword);
            }
            if(isNotBlank(osLogin)) {
                os.setLogin(osLogin);
            }
            if(isNotBlank(osPassword)) {
                os.setPassword(osPassword);
            }
            try {
                credentialsAccess.save(os);
                credentialsAccess.save(irmc);
            } catch (GeneralServerException e) {
                LOGGER.debug(e.getMessage());
                model.addAttribute("error", "Operation failed because DB error!");
            }
            model.addAttribute("msg", "Successfully changed " + form.getHost_name() + " details.");
            return "redirect:/admin/servers/"+server.getHost_name();
        } catch (IllegalArgumentException e) {
            LOGGER.debug(e.getMessage());
            model.addAttribute("error", "Applying changes to "+form.getHost_name()+ " FAILED");
            return "redirect:/admin/servers";
        }

    }

    @RequestMapping(value = "/testconnection", method = POST)
    public String testServerConnection(ServerForm form, Model model) {
        Servers server = serversAccess.findOne(form.getHost_name());
        Credentials os = credentialsAccess.findByIp(server.getOs_ip());
        Credentials irmc = credentialsAccess.findByIp(server.getOs_ip());

        try {
            OperatingSystem operatingSystem = WindowsNetUser.checkOS(server.getOs_ip());
            ConnectionStatus pingOS = WindowsNetUser.checkConnection(os.getIp(), PING_TIMEOUT);
            ConnectionStatus pingIRMC = WindowsNetUser.checkConnection(irmc.getIp(),PING_TIMEOUT);

            //TODO add iRMC connection test
            if(operatingSystem != OperatingSystem.NON_AVAILABLE_PATH && pingOS == ConnectionStatus.OK) {
                model.addAttribute("msg", pingOS.getValue());
            } else {
                model.addAttribute("error", pingOS.getValue() +", "+operatingSystem.getValue());
            }
        } catch (GeneralServerException e) {
           LOGGER.debug(e.getMessage());
        }
        return "redirect:/admin/servers";
    }

    private void removeReservations(Servers server) throws GeneralServerException {
        Users user = server.getUser();
        if(user != null){
            server.setUser(null);
            user.removeHostname(server);
            usersAccess.save(user, true);
        }
    }

    @RequestMapping(value = "/servers", method = GET)
    public String serversList(Model model) {
        model.addAttribute("servers", serversAccess.findAll());
        return "adm_servers";
    }

    @RequestMapping(value = "/users/{username}", method = GET)
    public String userEditForm(@PathVariable("username") String username, Model model) {
        model.addAttribute("user",usersAccess.findByUsername(username));
        return "adm_user";
    }

    @RequestMapping(value = "/servers/{ip}/credentials", method = GET)
    public String serversCredentials(@PathVariable("ip") String ip, Model model) {
        model.addAttribute("credentials", credentialsAccess.findByIp(ip));
        return "adm_server_credentials";
    }

    @RequestMapping(value = "/addserver", method = GET)
    public String showServerRegistration() {
        return "adm_server_form";
    }

    @RequestMapping(value = "/addserver", method = POST)
    public String processServerRegistration(ServerForm serverForm, Model model) {
        try {
            Credentials credentialsIRMC = serverForm.getCredentialsIrmc();
            Credentials credentialsOS = serverForm.getCredentialsOS();
            Servers servers = serverForm.getServer();

            serversAccess.save(servers, credentialsOS, credentialsIRMC);
            return "redirect:/admin/servers/"+servers.getHost_name();
        } catch (ServerCreationException e) {
            model.addAttribute("error", "Server creation failed. Attributes can't be empty!");
            return "adm_server_form";
        } catch (GeneralServerException e) {
            model.addAttribute("error", "Operation failed because DB error!");
            return "adm_server_form";
        }
    }

    @RequestMapping(value = "/servers/{hostName}", method = RequestMethod.GET)
    public String server(@PathVariable("hostName") String hostName, Model model) {
        Servers servers = serversAccess.findOne(hostName);
        Credentials credentialsIrmc = credentialsAccess.findByIp(servers.getIrmc_ip());
        Credentials credentialsOS = credentialsAccess.findByIp(servers.getOs_ip());
        model.addAttribute(servers);
        model.addAttribute("credentialsIrmc", credentialsIrmc);
        model.addAttribute("credentialsOS", credentialsOS);

        return "adm_server";
    }

    @RequestMapping(value = "/servers/restoredefaultcredentials", method = POST)
    public String restoreDefaultCredentials(ServerForm serverForm) {

        return "";
    }


}
