package ch.hftm.boundary;
import ch.hftm.control.BlogImageService;
import jakarta.inject.Inject;
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

    @Inject
    BlogImageService blogImageService;

    @POST
    @Path("{id}/images")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response addImageToBlog(@PathParam("id") Long id, @QueryParam("fileName") String fileName, InputStream imageStream) {
        try {
            blogImageService.addImage(id, imageStream, fileName);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("{blogId}/images/{fileName}")
    public Response deleteImageFromBlog(@PathParam("blogId") Long blogId, @PathParam("fileName") String fileName) {
        try {
            blogImageService.deleteImage(blogId, fileName);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
