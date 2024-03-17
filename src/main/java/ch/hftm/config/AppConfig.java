package ch.hftm.config;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AppConfig {
    @ConfigProperty(name = "IMAGE_DIR")
    String uploadDir;

    @ConfigProperty(name = "ZIP_DIR")
    String zipDir;

    @ConfigProperty(name = "ZIP_UPLOAD_DIR")
    String zipUploadDir;

    @ConfigProperty(name = "ZIP_EXTRACT_DIR")
    String zipExtractDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public String getZipDir() {
        return zipDir;
    }

    public String getZipUploadDir() {
        return zipUploadDir;
    }

    public String getZipExtractDir() {
        return zipExtractDir;
    }
}