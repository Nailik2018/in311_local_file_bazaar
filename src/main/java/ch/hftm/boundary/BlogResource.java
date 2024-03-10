package ch.hftm.boundary;
import ch.hftm.control.BlogService;
import ch.hftm.entity.Blog;
import ch.hftm.entity.BlogDto;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.util.List;

@Tag(name="Blog REST API")
@Path("blog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogResource {

    @Inject
    BlogService blogService;

    @POST
    @APIResponse(responseCode = "201", description = "Blog created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogDto.class)))
    public Response createBlog(Blog blog) {
        Blog createdBlog = blogService.create(blog);
        if (createdBlog != null) {
            BlogDto createdBlogDto = new BlogDto(blog.getTitle(), blog.getContent());
            return Response.status(Response.Status.CREATED).entity(createdBlogDto).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    public List<Blog> getAllBlogs() {
        return blogService.getAll();
    }

    @GET
    @Path("{id}")
    public Blog getById(@PathParam("id") Long id) {
        return blogService.getById(id);
    }

    @PUT
    @Path("{id}")
    public Response updateBlog(@PathParam("id") Long id, Blog updatedBlog) {
        Blog updated = blogService.updateBlog(id, updatedBlog);
        if (updated != null) {
            return Response.status(Response.Status.OK).entity(updated).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteBlog(@PathParam("id") Long id) {
        blogService.delete(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
