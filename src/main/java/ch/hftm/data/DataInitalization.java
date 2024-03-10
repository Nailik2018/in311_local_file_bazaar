package ch.hftm.data;
import ch.hftm.control.BlogService;
import ch.hftm.entity.Blog;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class DataInitalization {

    @Inject
    BlogService blogService;

    @Transactional
    public void init(@Observes StartupEvent event) {
        createBlogs();
    }

    public void createBlogs(){
        Blog schweiz = new Blog();
        schweiz.setTitle("Schweiz");
        schweiz.setContent("Gleichschirmfliegen auf dem Niesen.");

        Blog deutschland = new Blog();
        deutschland.setTitle("Deutschland");
        deutschland.setContent("Europapark in Rust.");

        Blog spanien = new Blog();
        spanien.setTitle("Spanien");
        spanien.setContent("Teneriffa Vulkane, Lostplaces und mit dem Boot auf dem Meer.");

        Blog polen = new Blog();
        polen.setTitle("Polen");
        polen.setContent("Tischtennisspielen in Danzig. Andrzej Grubba Akademi.");

        Blog frankreich = new Blog();
        frankreich.setTitle("Frankreich");
        frankreich.setContent("Eiffelturm, Metro und erstes Subway.");

        Blog italien = new Blog();
        italien.setTitle("Italien");
        italien.setContent("Stromboli und die Amalfiküste.");

        Blog oesterreich = new Blog();
        oesterreich.setTitle("Österreich");
        oesterreich.setContent("Den Prater besuchen in Wien.");

        Blog lichtenstein = new Blog();
        lichtenstein.setTitle("Lichtenstein");
        lichtenstein.setContent("Zu besuch im Fürstentum Lichtenstein.");

        Blog uae = new Blog();
        uae.setTitle("UAE");
        uae.setContent("Dubai und Abu Dhabi.");

        Blog indien = new Blog();
        indien.setTitle("Indien");
        indien.setContent("Ahmedabad, Mumbay, Taj Mahal und die heilige Stadt Varanasi.");

        Blog bangladesch = new Blog();
        bangladesch.setTitle("Bangladesch");
        bangladesch.setContent("Honey Hunting im Mangrovenwald. Ein Abenteuer in Bangladesch.");

        Blog nepal = new Blog();
        nepal.setTitle("Nepal");
        nepal.setContent("Trekking im Himalaya Kanchenjunga.");

        Blog thailand = new Blog();
        thailand.setTitle("Thailand");
        thailand.setContent("Erholung von den Ferien.");

        Blog myanmar = new Blog();
        myanmar.setTitle("Myanmar");
        myanmar.setContent("Bagan und Mandalay.");

        Blog laos = new Blog();
        laos.setTitle("Laos");
        laos.setContent("Vang Vieng und Luang Prabang.");

        Blog china = new Blog();
        china.setTitle("China");
        china.setContent("Die Chinesische Mauer und die Verbotene Stadt.");

        Blog hongKong = new Blog();
        hongKong.setTitle("Hong Kong");
        hongKong.setContent("Die Stadt der Wolkenkratzer und Märkte.");

        Blog island = new Blog();
        island.setTitle("Island");
        island.setContent("Im Land der Vulkane und Geysire.");

        Blog vatikan = new Blog();
        vatikan.setTitle("Vatikan");
        vatikan.setContent("Der kleinste Staat der Welt.");

        Blog niederlande = new Blog();
        niederlande.setTitle("Niederlande");
        niederlande.setContent("Kurz mal über die Grenze in Sitard.");

        Blog bosnienUndHerzegowina = new Blog();
        bosnienUndHerzegowina.setTitle("Bosnien und Herzegowina");
        bosnienUndHerzegowina.setContent("Sarajevo, Mostar, Ferien mit dem Gleitschirm.");

        blogService.create(schweiz);
        blogService.create(deutschland);
        blogService.create(spanien);
        blogService.create(polen);
        blogService.create(frankreich);
        blogService.create(italien);
        blogService.create(oesterreich);
        blogService.create(lichtenstein);
        blogService.create(uae);
        blogService.create(indien);
        blogService.create(bangladesch);
        blogService.create(china);
        blogService.create(nepal);
        blogService.create(hongKong);
        blogService.create(laos);
        blogService.create(thailand);
        blogService.create(myanmar);
        blogService.create(island);
        blogService.create(vatikan);
        blogService.create(niederlande);
        blogService.create(bosnienUndHerzegowina);
    }
}
