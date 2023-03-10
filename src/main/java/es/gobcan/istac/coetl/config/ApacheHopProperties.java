package es.gobcan.istac.coetl.config;

import es.gobcan.istac.coetl.config.common.PlatformAuth;
import es.gobcan.istac.coetl.config.common.PlatformHost;
import es.gobcan.istac.coetl.config.common.PlatformProperties;
import es.gobcan.istac.coetl.service.DataConfigurationService;
import es.gobcan.istac.coetl.service.MetadataConfigurationService;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

@Component
public class ApacheHopProperties implements PlatformProperties {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String endpoint = StringUtils.EMPTY;
    private String mainResourcePrefix = StringUtils.EMPTY;
    private String jsonMetadata = StringUtils.EMPTY;
    private final PlatformAuth auth = new PlatformAuth();
    private final PlatformHost host = new PlatformHost();

    @Autowired
    private MetadataConfigurationService configurationService;

    @Autowired
    private DataConfigurationService instanceService;

    @PostConstruct
    public void setValues() {
        try {
            setEndpoint(configurationService.findProperty(instanceService.getMetamacKeyApacheHopEndpoint()));
            setMainResourcePrefix(configurationService.findProperty(instanceService.getMetamacKeyApacheHopMainResourcePrefix()));
            setJsonMetadata(configurationService.findProperty(instanceService.getMetamacKeyApacheHopJsonMetadata()));
            auth.setUser(configurationService.findProperty(instanceService.getMetamacKeyApacheHopAuthUser()));
            auth.setPassword(configurationService.findProperty(instanceService.getMetamacKeyApacheHopAuthPassword()));
            host.setOs(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostOs()));
            host.setAddress(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostAddress()));
            host.setUsername(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostUsername()));
            host.setPassword(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostPassword()));
            host.setSudoUsername(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostSudoUsername()));
            host.setSudoPassword(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostSudopassword()));
            host.setSudoPasswordPromptRegex(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostSudoPasswordProptRegex()));
            host.setSftpPath(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostSftpPath()));
            host.setResourcesPath(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostResourcesPath()));
            host.setOwnerUserResourcesPath(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostOwnerUserResourcesPath()));
            host.setOwnerGroupResourcesPath(configurationService.findProperty(instanceService.getMetamacKeyApacheHopHostOwnerGroupResourcesPath()));
        } catch (Exception e) {
            log.error("Error getting the value of a metadata {}", e);
        }
    }

    public String getJsonMetadata() {
        return jsonMetadata;
    }

    public void setJsonMetadata(String jsonMetadata) {
        this.jsonMetadata = toGzipBase64File(jsonMetadata);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMainResourcePrefix() {
        return mainResourcePrefix;
    }

    public void setMainResourcePrefix(String mainResourcePrefix) {
        this.mainResourcePrefix = mainResourcePrefix;
    }

    public PlatformAuth getAuth() {
        return auth;
    }

    public PlatformHost getHost() {
        return host;
    }

    
    private String toGzipBase64File(String data) {
        String gzipB64 = null;
        try {
            Path tempFile = Files.createTempFile(null, null);
            Path tempGzipFile = Files.createTempFile("application", ".gz");

            
            FileWriter fw = new FileWriter(tempFile.toFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.flush();
            bw.close();
            fw.close();

            compressGZip(tempFile, tempGzipFile);
            
            byte[] encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(tempGzipFile.toFile()));
            gzipB64 = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error has occured while creating GZIP json_metadata for Apache Hop properties", e);
        }

        return gzipB64;
    }
    
    private void compressGZip(Path fileToCompress, Path outputFile) throws IOException {
        try (GZIPOutputStream gzipOutputStream = 
             new GZIPOutputStream(Files.newOutputStream(outputFile))) {
     
            byte[] allBytes = Files.readAllBytes(fileToCompress);
            gzipOutputStream.write(allBytes);
        }
    }
}
