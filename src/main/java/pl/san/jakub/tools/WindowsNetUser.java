package pl.san.jakub.tools;

import com.sun.jna.platform.win32.Netapi32;
import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.san.jakub.tools.exceptions.GeneralServerException;
import pl.san.jakub.tools.exceptions.PasswordIsNotChangedException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Jakub on 05.12.2015.
 *
 */
public class WindowsNetUser {

    private final static Logger LOGGER = LoggerFactory.getLogger(WindowsNetUser.class);

    /**
     * Same as system "ping" command. Checks is given host is reachable.
     *
     * @param ip hostname or ip address
     * @param timeout seconds before connection test fails
     * @return OK, ERROR or UNKNOWN_HOST
     * @throws GeneralServerException
     */
    public static ConnectionStatus checkConnection(String ip, int timeout) throws GeneralServerException {
        try {
            InetAddress inetAddress = InetAddress.getByName(ip);
            LOGGER.info("Sending Ping request to {}", ip);
            if(inetAddress.isReachable(timeout * 1000)) {
                LOGGER.info("HOST {} is reachable!", ip);
                return ConnectionStatus.OK;
            } else {
                LOGGER.info("HOST {} is NOT reachable!", ip);
                return ConnectionStatus.ERROR;
            }
        } catch (UnknownHostException e) {
            LOGGER.debug(e.getMessage());
            return ConnectionStatus.UNKNOWN_HOST;
        } catch (IOException e) {
            throw new GeneralServerException(e.getMessage() ,e.getCause());
        }
    }

    public static OperatingSystem checkOS(String hostname) {
        try {
            String domainName =  Netapi32Util.getDomainName(hostname);
            LOGGER.info("Detected OS type is Windows! Domain name is {}", domainName);
            return OperatingSystem.WINDOWS;
        } catch (Win32Exception e) {
            LOGGER.debug(e.getMessage());
            WinNT.HRESULT hr = e.getHR();
            Integer.toOctalString(hr.intValue());
            /**
             * For Linux OS return code is -2147023151, which means "procedure number beyond border".
             * If winAPI can't access network path it returns code -2147024843 "no network path".
             */
            if (hr.intValue() == -2147023151) {
                LOGGER.info("Detected OS type is Linux!");
                return OperatingSystem.LINUX;
            } else {
                LOGGER.info("Cannot find network path to host {}.", hostname);
                return OperatingSystem.NON_AVAILABLE_PATH;
            }
        }
    }

    //TODO add exception propagation at higher lvl. Controller should display error message. JakubP
    public static void changeWindowsUserPassword(String domainName, String username, String oldPassword, String newPassword) throws PasswordIsNotChangedException {
        int i = Netapi32.INSTANCE.NetUserChangePassword(domainName, username, oldPassword, newPassword);
        if(i != 0) {
            throw new PasswordIsNotChangedException(NetApiError.checkErrorCode(i).getValue());
        }
    }

    public enum ConnectionStatus {
        OK("Host is reachable"),
        ERROR("Host is NOT reachable"),
        UNKNOWN_HOST("Could not find HOST");

        private String value;

        ConnectionStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
