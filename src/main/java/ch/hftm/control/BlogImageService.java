package ch.hftm.control;
import ch.hftm.config.AppConfig;
import ch.hftm.entity.Blog;
import ch.hftm.entity.Image;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Dependent
public class BlogImageService {

    @Inject
    BlogRepository blogRepository;

    @Inject
    BlogImageRepository imageRepository;

    @Inject
    AppConfig appConfig;

    @Transactional
    public void addImage(long id, InputStream imageStream, String fileName) throws IOException {
        Blog blog = blogRepository.findById(id);
        if (blog != null) {
            String uploadDir = appConfig.getUploadDir() + "/" + id;
            File uploadPath = new File(uploadDir);
            if(!uploadPath.exists()) {
                uploadPath.mkdirs();
            }
            File newFile = new File(uploadDir + "/" + fileName);
            Files.copy(imageStream, newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Image image = new Image();
            image.setPath(uploadDir + "/" + fileName);
            image.setBlog(blog);
            blog.getImages().add(image);
            blog.addImage(image);
            imageRepository.persist(image);
            blogRepository.persist(blog);
        }
    }

    @Transactional
    public void create(Image image){
        imageRepository.persist(image);
    }

    @Transactional
    public void deleteImage(long blogId, String fileName) {
        Blog blog = blogRepository.findById(blogId);
        if (blog != null) {
            for (Image image : blog.getImages()) {
                String imagePath = image.getPath();
                String imageName = new File(imagePath).getName();
                if (imageName.equals(fileName)) {
                    try {
                        Path imageFilePath = Paths.get(imagePath);
                        Files.deleteIfExists(imageFilePath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    blog.getImages().remove(image);
                    break;
                }
            }
        }
    }
}