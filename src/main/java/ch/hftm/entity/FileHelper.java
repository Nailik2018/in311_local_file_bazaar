package ch.hftm.entity;
import ch.hftm.control.BlogImageService;
import ch.hftm.control.BlogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.json.JSONObject;

@ApplicationScoped
public class FileHelper {

    @Inject
    BlogService blogService;

    @Inject
    AppConfig appConfig;

    @Inject
    BlogImageService blogImageService;

    public String generateJsonData(Long blogId) {
        if (blogService == null) {
            throw new IllegalStateException("blogService is not initialized");
        }
        Blog blog = blogService.getById(blogId);
        String title = blog.getTitle();
        String content = blog.getContent();
        List<Image> images = blog.getImages();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("title", title);
        jsonMap.put("content", content);
        List<Map<String, String>> imagesList = new ArrayList<>();
        for (Image image : images) {
            Map<String, String> imageMap = new HashMap<>();
            imageMap.put("path", image.getPath());
            imagesList.add(imageMap);
        }
        jsonMap.put("images", imagesList);
        try {
            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "{\"error\": \"Error generating JSON data\"}";
        }
    }

    public void addJsonFileToZip(String fileName, String jsonData, ZipOutputStream zipOutputStream) throws IOException {
        zipOutputStream.putNextEntry(new ZipEntry(fileName));
        byte[] jsonDataBytes = jsonData.getBytes();
        zipOutputStream.write(jsonDataBytes, 0, jsonDataBytes.length);
        zipOutputStream.closeEntry();
    }

    public void addFilesToZip(String uploadDir, ZipOutputStream zipOutputStream) throws IOException {
        File uploadFile = new File(uploadDir);
        if (uploadFile.exists() && uploadFile.isDirectory()) {
            for (File file : uploadFile.listFiles()) {
                if (file.isFile()) {
                    zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }
                    fileInputStream.close();
                    zipOutputStream.closeEntry();
                }
            }
        }
    }

    public void unzipFile(String zipFilePath, java.nio.file.Path destDir) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(java.nio.file.Paths.get(zipFilePath)))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                java.nio.file.Path entryPath = destDir.resolve(entry.getName());
                if (!entry.isDirectory()) {
                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zipInputStream, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipInputStream.closeEntry();
            }
        }
    }

    @Transactional
    public long saveBlog(java.nio.file.Path jsonFilePath) throws IOException {
        if (Files.exists(jsonFilePath)) {
            System.out.println("Blog.json:");
            for (String line : Files.readAllLines(jsonFilePath)) {
                JSONObject jsonObject = new JSONObject(line);
                String title = jsonObject.get("title").toString();
                String content = jsonObject.get("content").toString();
                Blog blog = new Blog(title, content);
                blogService.create(blog);
                System.out.println(line);
                System.out.println("blogId: " + blog.getId());
                return blog.getId();
            }
        } else {
            System.out.println("blog.json konnte nicht gefunden werden.");
        }
        return 0;
    }

    public void saveImage(Path extractDirPath, Long blogId) throws IOException {
        // Verzeichnis für Bilder des Blogs erstellen
        Path blogImagesDir = Paths.get(appConfig.getUploadDir() + blogId.toString());
        if (!Files.exists(blogImagesDir)) {
            Files.createDirectories(blogImagesDir);
        }

        // Bilder aus Blogs zip in das Images Blog-Verzeichnis verschieben
        Files.list(extractDirPath)
                .filter(imagePath -> Files.isRegularFile(imagePath) && isImageFile(imagePath)) // Nur Bilder
                .forEach(imagePath -> {
                    try {
                        Files.move(imagePath, blogImagesDir.resolve(imagePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Image moved: " + imagePath.getFileName());
                        Image image = new Image();
                        image.setPath(blogImagesDir.resolve(imagePath.getFileName()).toString());
                        image.setBlog(blogService.getById(blogId));
                        blogImageService.create(image);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private boolean isImageFile(Path filePath) {
        String fileName = filePath.getFileName().toString().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg");
    }
}
