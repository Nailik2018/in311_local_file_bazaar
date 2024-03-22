package ch.hftm.environments;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EnvironmentFile {

    public EnvironmentFile() {
    }

    public static void main(String[] args) {
        EnvironmentFile environmentFile = new EnvironmentFile();
        try {
            environmentFile.createEnvFile();
        } catch (IOException e) {
            System.err.println("Fehler beim Erstellen der Umgebungsdatei: " + e.getMessage());
        }
    }

    public void createEnvFile() throws IOException {
        String parentDir = System.getProperty("user.dir");
        System.out.println("Root-Verzeichnis des Projekts: " + parentDir);
        File envFile = new File(parentDir + "/.env");
        if (!envFile.exists()) {
            boolean created = envFile.createNewFile();
            if (created) {
                System.out.println(".env Datei erstellt.");
            } else {
                System.err.println("Erstellen der Datei fehlgeschlagen.");
                return;
            }
        } else {
            System.out.println("Datei existiert bereits.");
            return;
        }
        parentDir = parentDir.replace("\\", "/");
        String envSettings = setEnvironmentVariables(parentDir);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(envFile))) {
            bufferedWriter.write(envSettings);
            System.out.println("env Einstellungen geschrieben.");
        } catch (IOException e) {
            System.err.println("env Einstellungen schreiben fehlgeschlagen: " + e.getMessage());
        }
    }

    private String setEnvironmentVariables(String parentDir){
        // Image Verzeichnis
        String sectionNameImageDir = "IMAGE_DIR=";
        String imageDir = parentDir + "/dev/images/blogs/";

        // Zip download Verzeichnis
        String sectionNameZipDir = "ZIP_DIR=";
        String zipDir = parentDir + "/dev/zips/blogs/";

        // Zip Upload Verzeichnis
        String sectionNameZipUploadDir = "ZIP_UPLOAD_DIR=";
        String zipUploadDir = parentDir + "/dev/zips/uploads/";

        // Zip Extract Verzeichnis
        String sectionNameZipExtractDir = "ZIP_EXTRACT_DIR=";
        String zipExtractDir = parentDir + "/dev/zips/extract/";

        String envSettings = sectionNameImageDir + imageDir + "\n";
        envSettings += sectionNameZipDir + zipDir + "\n";
        envSettings += sectionNameZipUploadDir + zipUploadDir + "\n";
        envSettings += sectionNameZipExtractDir + zipExtractDir + "\n";
        return envSettings;
    }
}