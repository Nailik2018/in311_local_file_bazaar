package ch.hftm.entity;
import ch.hftm.control.BlogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileHelper {

    @Inject
    BlogService blogService;

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
}
