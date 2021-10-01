package es.gobcan.istac.coetl.pentaho.service.util;

import static com.xebialabs.overthere.ConnectionOptions.ADDRESS;
import static com.xebialabs.overthere.ConnectionOptions.OPERATING_SYSTEM;
import static com.xebialabs.overthere.ConnectionOptions.PASSWORD;
import static com.xebialabs.overthere.ConnectionOptions.USERNAME;
import static com.xebialabs.overthere.OperatingSystemFamily.UNIX;
import static com.xebialabs.overthere.OperatingSystemFamily.WINDOWS;
import static com.xebialabs.overthere.ssh.SshConnectionBuilder.CONNECTION_TYPE;
import static com.xebialabs.overthere.ssh.SshConnectionBuilder.SUDO_USERNAME;
import static com.xebialabs.overthere.ssh.SshConnectionType.INTERACTIVE_SUDO;
import static com.xebialabs.overthere.ssh.SshConnectionType.SFTP;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.core.NestedRuntimeException;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereExecutionOutputHandler;

import es.gobcan.istac.coetl.config.PentahoProperties.Host;

public class RemoteConnectionUtils {

    public static ConnectionOptions getSourceOptions() {
        ConnectionOptions options = new ConnectionOptions();
        options.set(CONNECTION_TYPE, SFTP);
        options.set(OPERATING_SYSTEM, getSourceOperatingSystem());
        options.set(ADDRESS, "localhost");
        options.set(USERNAME, "root");
        options.set(PASSWORD, "root");
        return options;
    }

    public static ConnectionOptions getDestinationOptions(Host host) {
        ConnectionOptions options = new ConnectionOptions();
        options.set(CONNECTION_TYPE, SFTP);
        options.set(OPERATING_SYSTEM, getDestinationOperatingSystem(host.getOs()));
        options.set(ADDRESS, host.getAddress());
        options.set(USERNAME, host.getUsername());
        options.set(PASSWORD, host.getPassword());
        return options;
    }

    public static ConnectionOptions getSudoDestinationOptions(Host host) {
        ConnectionOptions options = new ConnectionOptions(getDestinationOptions(host));
        options.set(CONNECTION_TYPE, INTERACTIVE_SUDO);
        options.set(SUDO_USERNAME, host.getSudoUsername());
        options.set(PASSWORD, host.getSudoPassword());
        return options;
    }

    private static OperatingSystemFamily getSourceOperatingSystem() {
        return SystemUtils.IS_OS_WINDOWS ? WINDOWS : UNIX;
    }

    private static OperatingSystemFamily getDestinationOperatingSystem(String os) {
        switch (os.toUpperCase()) {
            case "UNIX":
                return OperatingSystemFamily.UNIX;
            case "WINDOWS":
                return OperatingSystemFamily.WINDOWS;
            case "ZOS":
                return OperatingSystemFamily.ZOS;
            default:
                return null;
        }
    }

    public static class SftpException extends NestedRuntimeException {

        private static final long serialVersionUID = 5796382941963409525L;
        public static final String FILE_NOT_FOUND_MESSAGE = "File not found on destination server: (host=%s)%s";
        public static final String COMMAND_EXECUTION_MESSAGE = "An error ocurrs while exceuting the command: %s";

        public SftpException(String msg) {
            super(msg);
        }

        public SftpException(String msg, Throwable cause) {
            super(msg, cause);
        }

    }
    
    public static void executeCommand(OverthereConnection connection, String... args) {
        int exitCode = connection.execute(CmdLine.build(args));
        if (exitCode != 0) {
            throw new SftpException(String.format(SftpException.COMMAND_EXECUTION_MESSAGE, (Object[]) args));
        }
    }
    
    public static void executeCommand(OverthereConnection connection, OverthereExecutionOutputHandler outHandler, String... args) {
        CmdLine cmd = new CmdLine();
        for (int i = 0; i < args.length; i++) {
            cmd.addRaw(args[i]);
        }
        int exitCode = connection.execute(outHandler, outHandler, cmd);
        if (exitCode != 0) {
            throw new SftpException(String.format(SftpException.COMMAND_EXECUTION_MESSAGE, (Object[]) args));
        }
    }
}
