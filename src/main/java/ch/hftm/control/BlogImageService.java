package ch.hftm.control;
import ch.hftm.entity.AppConfig;
import ch.hftm.entity.Blog;
import ch.hftm.entity.Image;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
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
}
