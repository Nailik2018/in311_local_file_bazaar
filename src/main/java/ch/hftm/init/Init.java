package ch.hftm.init;
import ch.hftm.data.Datainitialization;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class Init {

    @Inject
    Datainitialization datainitialization;

    @Transactional
    public void init(@Observes StartupEvent event) {
        datainitialization.createBlogs();
    }
}