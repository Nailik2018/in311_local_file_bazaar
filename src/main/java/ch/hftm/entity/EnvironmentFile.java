package ch.hftm.entity;
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
        String currentFilePath = EnvironmentFile.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String currentDir = new File(currentFilePath).getParent();
        System.out.println("Aktuelles Verzeichnis: " + currentDir);

        currentDir = currentDir.replace("%20", " ");
        System.out.println("Dekodiertes Verzeichnis: " + currentDir);

        // Ein Verzeichnis zurück zum root Verzeichnis des Projektes
        String parentDir = new File(currentDir).getParent();
        parentDir = new File(parentDir).getParent(); // ausserhalb src
        System.out.println("Oberverzeichnis: " + parentDir);

        File envDir = new File(parentDir);
        if (!envDir.exists()) {
            System.err.println("Verzeichnis existiert nicht: " + parentDir);
            return;
        }

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

        // .env Setzen
        String envSettings = sectionNameImageDir + imageDir + "\n";
        envSettings += sectionNameZipDir + zipDir + "\n";
        envSettings += sectionNameZipUploadDir + zipUploadDir + "\n";
        envSettings += sectionNameZipExtractDir + zipExtractDir + "\n";

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(envFile))) {
            bufferedWriter.write(envSettings);
            System.out.println("env Einstellungen geschrieben.");
        } catch (IOException e) {
            System.err.println("env Einstellungen schreiben fehlgeschlagen: " + e.getMessage());
        }
    }
}