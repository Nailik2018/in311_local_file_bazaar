package ch.hftm.entity;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AppConfig {
    @ConfigProperty(name = "IMAGE_DIR")
    String uploadDir;

    @ConfigProperty(name = "ZIP_DIR")
    String zipDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public String getZipDir() {
        return zipDir;
    }
}