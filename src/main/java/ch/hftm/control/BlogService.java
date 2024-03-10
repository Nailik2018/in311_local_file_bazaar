package ch.hftm.control;
import ch.hftm.entity.Blog;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@Dependent
public class BlogService {

    @Inject
    BlogRepository blogRepository;

    @Transactional
    public Blog create(Blog newBlog) {
        blogRepository.persist(newBlog);
        return newBlog;
    }

    public List<Blog> getAll() {
        return this.blogRepository.listAll();
    }

    public Blog getById(Long id) {
        return blogRepository.findById(id);
    }

    public Blog updateBlog(Long id, Blog updatedBlog) {
        Blog updateingBlog = blogRepository.findById(id);
        if (updateingBlog != null) {
            updateingBlog.setTitle(updatedBlog.getTitle());
            updateingBlog.setContent(updatedBlog.getContent());
            blogRepository.persist(updateingBlog);
            return updateingBlog;
        } else {
            return null;
        }
    }

    public void delete(Long id) {
        blogRepository.deleteById(id);
    }
}