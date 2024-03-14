package ch.hftm.boundary;
import ch.hftm.entity.AppConfig;
import ch.hftm.entity.FileHelper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

@Tag(name = "Blog Zip REST API")
@Path("Zip Blog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BlogZipResource {

    @Inject
    AppConfig appConfig;

    @Inject
    FileHelper fileHelper;

    @POST
    @Path("/uploadBlogZip")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadBlogZip(InputStream fileInputStream) {
        try {
            java.nio.file.Path uploadDirPath = Paths.get(appConfig.getZipUploadDir());
            // Falls das Verzeichnis nicht existiert, wird es erstellt
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }
            String filename = "uploadBlog.zip";
            String uploadedFilePath = uploadDirPath.resolve(filename).toString();
            Files.copy(fileInputStream, Paths.get(uploadedFilePath), StandardCopyOption.REPLACE_EXISTING);
            Files.deleteIfExists(Paths.get(uploadedFilePath));
            return Response.ok("Blog ZIP erfolgreich").build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/backupAndDownloadZip/{blogId}")
    @Produces("application/zip")
    public Response backupAndDownloadZip(@PathParam("blogId") Long blogId) {
        String uploadDir = appConfig.getUploadDir() + blogId;
        String zipFilePath = appConfig.getZipDir() + "blog_" + blogId + ".zip";
        String jsonFileName = "blog_" + blogId + ".json";
        try {
            java.nio.file.Path zipDirPath = Paths.get(appConfig.getZipDir());
            // Falls das Verzeichnis nicht existiert, wird es erstellt
            if (!Files.exists(zipDirPath)) {
                Files.createDirectories(zipDirPath);
            }
            // Erstellen eines Zip-Streams / Zip-Datei
            FileOutputStream fileOutputStream = new FileOutputStream(zipFilePath);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
            // Image => Zip
            fileHelper.addFilesToZip(uploadDir, zipOutputStream);
            // JSON data
            String jsonData = fileHelper.generateJsonData(blogId);
            // JSON file => zip
            fileHelper.addJsonFileToZip(jsonFileName, jsonData, zipOutputStream);
            zipOutputStream.close();
            fileOutputStream.close();
            // Return zip file für download
            File zipFile = new File(zipFilePath);
            Response.ResponseBuilder response = Response.ok((Object) zipFile);
            response.header("Content-Disposition", "attachment; filename=" + zipFile.getName());
            return response.build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
