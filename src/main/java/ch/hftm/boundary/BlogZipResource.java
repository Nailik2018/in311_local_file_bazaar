package ch.hftm.boundary;
import ch.hftm.control.BlogService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Tag(name = "Blog Zip REST API")
@Path("Zip Blog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogZipResource {

    @GET
    @Path("/backupAndDownloadZip/{blogId}")
    @Produces("application/zip")
    public Response backupAndDownloadZip(@PathParam("blogId") Long blogId) {
        return null;
    }
}
