package pl.san.jakub.controller.forms;

import org.eclipse.jetty.util.StringUtil;
import pl.san.jakub.model.data.Credentials;
import pl.san.jakub.model.data.Servers;
import pl.san.jakub.tools.exceptions.ServerCreationException;

import javax.validation.constraints.Size;

/**
 * Created by Jakub on 08.11.2015.
 */
public class ServerForm {

    private static final String EMPTY_FIELD = "Empty field!";
    @Size(min = 4, max = 15, message = "{host_name.size}")
    private String host_name;
    @Size(min = 7, max = 15, message = "{irmc_ip.size}")
    private String irmc_ip;
    @Size(min = 7, max = 15, message = "{os_ip.size}")
    private String os_ip;
    @Size(min = 4, max = 15, message = "{irmcLogin.size}")
    private String irmcLogin;
    @Size(min = 4, max = 15, message = "{irmcPassword.size}")
    private String irmcPassword;
    @Size(min = 4, max = 15, message = "{osLogin.size}")
    private String osLogin;
    @Size(min = 4, max = 15, message = "{osPassword.size}")
    private String osPassword;

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String  getIrmc_ip() {
        return irmc_ip;
    }

    public void setIrmc_ip(String irmc_ip) {
        this.irmc_ip = irmc_ip;
    }

    public String getOs_ip() {
        return os_ip;
    }

    public void setOs_ip(String os_ip) {
        this.os_ip = os_ip;
    }

    public String getIrmcLogin() {
        return irmcLogin;
    }

    public void setIrmcLogin(String irmcLogin) {
        this.irmcLogin = irmcLogin;
    }

    public String getIrmcPassword() {
        return irmcPassword;
    }

    public void setIrmcPassword(String irmcPassword) {
        this.irmcPassword = irmcPassword;
    }

    public String getOsLogin() {
        return osLogin;
    }

    public void setOsLogin(String osLogin) {
        this.osLogin = osLogin;
    }

    public String getOsPassword() {
        return osPassword;
    }

    public void setOsPassword(String osPassword) {
        this.osPassword = osPassword;
    }

    public Credentials getCredentialsIrmc() throws ServerCreationException {
        String[] temp = {getIrmc_ip(), getIrmcLogin(), getIrmcPassword()};
        for (String s : temp) {
            if(StringUtil.isBlank(s)) {
                throw new ServerCreationException(EMPTY_FIELD);
            }
        }
        return new Credentials(temp[0], temp[1], temp[2]);
    }
    public Credentials getCredentialsOS() throws ServerCreationException {
        String[] temp = {getOs_ip(), getOsLogin(), getOsPassword()};
        for (String s : temp) {
            if(StringUtil.isBlank(s)) {
                throw new ServerCreationException(EMPTY_FIELD);
            }
        }
        return new Credentials(temp[0], temp[1], temp[2]);
    }
    public Servers getServer() throws ServerCreationException {
        String[] temp = {getHost_name(), getOs_ip(), getIrmc_ip()};
        for (String s : temp) {
            if(StringUtil.isBlank(s)) {
                throw new ServerCreationException(EMPTY_FIELD);
            }
        }
        return new Servers(temp[0], temp[1], temp[2]);
    }
}
