package es.gobcan.istac.coetl.pentaho.service.impl;

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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.xebialabs.overthere.CmdLine;
import com.xebialabs.overthere.ConnectionOptions;
import com.xebialabs.overthere.OperatingSystemFamily;
import com.xebialabs.overthere.Overthere;
import com.xebialabs.overthere.OverthereConnection;
import com.xebialabs.overthere.OverthereFile;

import es.gobcan.istac.coetl.config.PentahoProperties;
import es.gobcan.istac.coetl.config.PentahoProperties.Host;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.File;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.pentaho.service.PentahoSftpService;
import es.gobcan.istac.coetl.pentaho.service.util.PentahoUtil;

@Service
public class PentahoSftpServiceImpl implements PentahoSftpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PentahoSftpService.class);

    private static ConnectionOptions getSourceOptions() {
        ConnectionOptions options = new ConnectionOptions();
        options.set(CONNECTION_TYPE, SFTP);
        options.set(OPERATING_SYSTEM, getSourceOperatingSystem());
        options.set(ADDRESS, "localhost");
        options.set(USERNAME, "root");
        options.set(PASSWORD, "root");
        return options;
    }

    private static ConnectionOptions getDestinationOptions(Host host) {
        ConnectionOptions options = new ConnectionOptions();
        options.set(CONNECTION_TYPE, SFTP);
        options.set(OPERATING_SYSTEM, getDestinationOperatingSistem(host.getOs()));
        options.set(ADDRESS, host.getAddress());
        options.set(USERNAME, host.getUsername());
        options.set(PASSWORD, host.getPassword());
        return options;
    }

    private static ConnectionOptions getSudoDestinationOptions(Host host) {
        ConnectionOptions options = new ConnectionOptions(getDestinationOptions(host));
        options.set(CONNECTION_TYPE, INTERACTIVE_SUDO);
        options.set(SUDO_USERNAME, host.getSudoUsername());
        options.set(PASSWORD, host.getSudoPassword());
        return options;
    }

    private static OperatingSystemFamily getSourceOperatingSystem() {
        return SystemUtils.IS_OS_WINDOWS ? WINDOWS : UNIX;
    }

    private static OperatingSystemFamily getDestinationOperatingSistem(String os) {
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

    private static class SftpException extends NestedRuntimeException {

        private static final long serialVersionUID = 5796382941963409525L;
        private static final String FILE_NOT_FOUND_MESSAGE = "File not found on destination server: (host=%s)%s";
        private static final String COMMAND_EXECUTION_MESSAGE = "An error ocurrs while exceuting the command: %s";

        public SftpException(String msg) {
            super(msg);
        }

        public SftpException(String msg, Throwable cause) {
            super(msg, cause);
        }

    }

    @Autowired
    PentahoProperties pentahoProperties;

    @Override
    public String uploadAttachedFiles(Etl etl) {
        if (CollectionUtils.isEmpty(etl.getAttachedFiles())) {
            LOGGER.debug("There are no attached file to upload of ETL : {}", etl.getCode());
            return null;
        }

        LOGGER.info("Upload attached files of ETL : {}", etl.getCode());
        OverthereConnection sourceConnection = Overthere.getConnection("local", getSourceOptions());
        OverthereConnection destinationConnection = Overthere.getConnection("ssh", getDestinationOptions(pentahoProperties.getHost()));
        OverthereConnection sudoDestinationConnection = Overthere.getConnection("ssh", getSudoDestinationOptions(pentahoProperties.getHost()));
        try {
            String sourceZipPath = createZipAttachedFile(etl);
            uploadZipAttachedFiles(sourceConnection, destinationConnection, sourceZipPath);
            OverthereFile resourcesDirectory = moveZipFile(etl, sudoDestinationConnection);
            String unzippedFilesPath = unzipFile(etl, sudoDestinationConnection, resourcesDirectory);
            changeOwnerUnzippedFiles(sudoDestinationConnection, resourcesDirectory);
            return unzippedFilesPath;
        } catch (Exception e) {
            throw new CustomParameterizedExceptionBuilder().cause(e).message(String.format("An error ocurrs while uploading attached files from ETL : %s. Cause: %s", etl.getCode(), e.getMessage()))
                    .code(ErrorConstants.ETL_ATTACHED_FILES_UPLOAD).build();
        } finally {
            sourceConnection.close();
            destinationConnection.close();
            sudoDestinationConnection.close();
        }
    }

    private String createZipAttachedFile(Etl etl) throws IOException, SQLException {
        LOGGER.debug("Bundle zip with attached files of ETL : {}", etl.getCode());
        Path rootTempDirectory = Files.createTempDirectory(StringUtils.EMPTY);
        Path etlAttachedFilesDirectoryPath = Paths.get(rootTempDirectory.toString(), PentahoUtil.normalizeEtlCode(etl.getCode()));
        Path etlAttachedFilesDirectory = Files.createDirectory(etlAttachedFilesDirectoryPath);
        for (File attachedFile : etl.getAttachedFiles()) {
            createFileStruct(etlAttachedFilesDirectory, attachedFile);
        }
        return buildZipFile(etlAttachedFilesDirectory);
    }

    private void createFileStruct(Path parentDirectory, File attachedFile) throws IOException, SQLException {
        Path filePath = Paths.get(parentDirectory.toString(), attachedFile.getName());
        Path file = Files.createFile(filePath);
        long filesize = attachedFile.getLength();
        byte[] contentFile = attachedFile.getData().getBytes(1, (int) filesize);
        Files.write(file, contentFile, StandardOpenOption.WRITE);
    }

    private String buildZipFile(Path rootTempDirectory) throws IOException {
        String rootTempDirectoryPath = rootTempDirectory.toString();
        String zipFilePath = rootTempDirectoryPath.concat(".zip");
        FileOutputStream fos = new FileOutputStream(zipFilePath);
        ZipOutputStream zos = new ZipOutputStream(fos);
        java.io.File directoryToZip = new java.io.File(rootTempDirectoryPath);

        zip(directoryToZip, directoryToZip.getName(), zos);

        zos.close();
        fos.close();

        java.io.File zipFile = new java.io.File(zipFilePath);
        return zipFile.getAbsolutePath();
    }

    private void zip(java.io.File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            java.io.File[] children = fileToZip.listFiles();
            for (java.io.File childFile : children) {
                zip(childFile, fileName.concat("/").concat(childFile.getName()), zipOut);
            }
            return;
        }
        try (FileInputStream fis = new FileInputStream(fileToZip)) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
        }
    }

    private void uploadZipAttachedFiles(OverthereConnection sourceConnection, OverthereConnection destinationConnection, String sourceZipPath) {
        LOGGER.debug("Upload zip file ({}) to destination host : {}", sourceZipPath, destinationConnection.getOptions().get(ADDRESS));
        OverthereFile sourceZipFile = sourceConnection.getFile(sourceZipPath);
        OverthereFile destinationZipFile = destinationConnection.getFile(pentahoProperties.getHost().getSftpPath());
        sourceZipFile.copyTo(destinationZipFile);
    }

    private OverthereFile moveZipFile(Etl etl, OverthereConnection sudoDestinationConnection) {
        OverthereFile uploadedZipFile = sudoDestinationConnection.getFile(pentahoProperties.getHost().getSftpPath().concat("/").concat(PentahoUtil.normalizeEtlCode(etl.getCode())).concat(".zip"));
        if (!uploadedZipFile.exists()) {
            throw new SftpException(String.format(SftpException.FILE_NOT_FOUND_MESSAGE, pentahoProperties.getHost().getAddress(), uploadedZipFile.getPath()));
        }
        OverthereFile resourcesDirectory = sudoDestinationConnection.getFile(pentahoProperties.getHost().getResourcesPath());
        executeCommand(sudoDestinationConnection, "mv", uploadedZipFile.getPath(), resourcesDirectory.getPath());
        return resourcesDirectory;
    }

    private String unzipFile(Etl etl, OverthereConnection sudoDestinationConnection, OverthereFile resourcesDirectory) {
        OverthereFile movedZipFile = resourcesDirectory.getFile(PentahoUtil.normalizeEtlCode(etl.getCode()).concat(".").concat("zip"));
        OverthereFile unzippedFolder = resourcesDirectory.getFile(PentahoUtil.normalizeEtlCode(etl.getCode()));
        // Delete old unzipped folder if exists
        unzippedFolder.deleteRecursively();
        executeCommand(sudoDestinationConnection, "unzip", movedZipFile.getPath(), "-d", resourcesDirectory.getPath());
        movedZipFile.delete();
        return unzippedFolder.getPath();
    }

    private void changeOwnerUnzippedFiles(OverthereConnection sudoConnection, OverthereFile resourcesDirectory) {
        executeCommand(sudoConnection, "chown", "pentaho:pentaho", "-R", resourcesDirectory.getPath());
    }

    private void executeCommand(OverthereConnection connection, String... args) {
        int exitCode = connection.execute(CmdLine.build(args));
        if (exitCode != 0) {
            throw new SftpException(String.format(SftpException.COMMAND_EXECUTION_MESSAGE, (Object[]) args));
        }
    }
}
