package ch.hftm.control;
import ch.hftm.entity.Image;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BlogImageRepository implements PanacheRepository<Image> {
}
