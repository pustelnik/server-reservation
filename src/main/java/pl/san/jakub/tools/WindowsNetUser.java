package pl.san.jakub.tools;

import com.sun.jna.platform.win32.Netapi32;
import com.sun.jna.platform.win32.Netapi32Util;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.WinNT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.san.jakub.tools.exceptions.PasswordIsNotChangedException;

/**
 * Created by Jakub on 05.12.2015.
 */
public class WindowsNetUser {

    private final static Logger LOGGER = LoggerFactory.getLogger(WindowsNetUser.class);

    public static OperatingSystem checkOS(String hostname) {
        try {
            String domainName =  Netapi32Util.getDomainName(hostname);
            LOGGER.info("Detected OS type is Windows! Domain name is {}", domainName);
            return OperatingSystem.WINDOWS;
        } catch (Win32Exception e) {
            LOGGER.info("Detected OS type is Linux!");
            LOGGER.debug(e.getMessage());
            WinNT.HRESULT hr = e.getHR();
            Integer.toOctalString(hr.intValue());
            /**
             * For Linux OS return code is -2147023151, which means "procedure number beyond border".
             * If winAPI can't access network path it returns code -2147024843 "no network path".
             */
            if (hr.intValue() == -2147023151) {
                return OperatingSystem.LINUX;
            } else {
                return OperatingSystem.NON_AVAILABLE_PATH;
            }
        }
    }

    public static void changeWindowsUserPassword(String domainName, String username, String oldPassword, String newPassword) throws PasswordIsNotChangedException {
        int i = Netapi32.INSTANCE.NetUserChangePassword(domainName, username, oldPassword, newPassword);
        if(i != 0) throw new PasswordIsNotChangedException("Password is not changed!");
    }

}
