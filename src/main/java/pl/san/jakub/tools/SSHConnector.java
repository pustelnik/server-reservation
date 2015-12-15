package pl.san.jakub.tools;

import com.jcraft.jsch.*;
import org.slf4j.*;
import org.slf4j.Logger;
import pl.san.jakub.tools.exceptions.PasswordDoesNotMatchException;
import pl.san.jakub.tools.exceptions.PasswordIsNotChangedException;

import java.io.*;

/**
 * Created by Jakub on 24.11.2015.
 */
public class SSHConnector{

    public class SSHResponse {

        private String errors;
        private String output;

        public SSHResponse(String errors, String output) {
            this.errors = errors;
            this.output = output;
        }

        public String getErrors() {
            return errors;
        }

        public String getOutput() {
            return output;
        }
    }


    private final static Logger LOGGER = LoggerFactory.getLogger(SSHConnector.class);

    public void changePassword(String newPassword) throws PasswordIsNotChangedException {
        LOGGER.info("Changing password at {} for user {}.", hostname, login);
        SSHResponse response = sendCommand("echo -e \"" +  newPassword + "\\n" +
                newPassword + "\" | passwd root\n");
    }

    private final String hostname;
    private final int port;
    private final String login;
    private final String password;


    public SSHConnector(String hostname, int port, String login, String password) {
        this.hostname = hostname;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public SSHResponse sendCommand(String command) throws PasswordIsNotChangedException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorSteam = new ByteArrayOutputStream();
        StringBuilder result = new StringBuilder();
        StringBuilder errors = new StringBuilder();
        JSch jSch = new JSch();
        Channel channel = null;
        Session session = null;
        try {

            session = jSch.getSession(login, hostname, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            ((ChannelExec) channel).setErrStream(errorSteam);
            ((ChannelExec) channel).setOutputStream(outputStream);
            channel.connect();

            InputStream in = channel.getInputStream();
            byte[] tmp = new byte[1024];
            readStream(result, channel, in, tmp);
            if(errorSteam.size() > 0) {
                LOGGER.debug(errorSteam.toString("UTF-8"));
            }
            InputStream errorIn = ((ChannelExec) channel).getErrStream();
            byte[] temp = new byte[1024];
            readStream(errors, channel, errorIn, temp);

        } catch (InterruptedException | IOException | JSchException e) {
            LOGGER.debug(e.getMessage());
        }
        finally {
            if(null != channel) {
                channel.disconnect();
            }
            if(null != session) {
                session.disconnect();
            }
        }
        return new SSHResponse(errors.toString(), result.toString());
    }

    private void readStream(StringBuilder errors, Channel channel, InputStream errorIn, byte[] temp) throws IOException, PasswordIsNotChangedException, InterruptedException {
        while (true) {
            while (errorIn.available() > 0) {
                int i = errorIn.read(temp, 0, 1024);
                if (i < 0) break;
                errors.append(new String(temp, 0, i)).append("\n");
            }
            if (channel.isClosed()) {
                if (errorIn.available() > 0) continue;
                LOGGER.info("exit-status: " + channel.getExitStatus());
                if(channel.getExitStatus() != 0) {
                    throw new PasswordIsNotChangedException("Wrong exit status");
                }
                break;
            }
            Thread.sleep(1000);
        }
    }
}
