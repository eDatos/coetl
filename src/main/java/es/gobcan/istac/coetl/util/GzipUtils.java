package es.gobcan.istac.coetl.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;

public class GzipUtils {
    
    public static String toGzipBase64File(String data) {
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
            throw new RuntimeException("Error has occured while creating GZIP file", e);
        }

        return gzipB64;
    }
    
    public static void compressGZip(Path fileToCompress, Path outputFile) throws IOException {
        try (GZIPOutputStream gzipOutputStream = 
             new GZIPOutputStream(Files.newOutputStream(outputFile))) {
     
            byte[] allBytes = Files.readAllBytes(fileToCompress);
            gzipOutputStream.write(allBytes);
        }
    }
}
