package ch.hftm.boundary;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.io.InputStream;

@Tag(name="Blog Image REST API")
@Path("blogImage")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogImageResource {

    @POST
    @Path("{id}/images")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response addImageToBlog(@PathParam("id") Long id, @QueryParam("fileName") String fileName, InputStream imageStream) {
        return Response.status(Response.Status.CREATED).build();
    }
}
