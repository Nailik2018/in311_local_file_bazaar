package ch.hftm.boundary;
import ch.hftm.config.AppConfig;
import ch.hftm.entity.FileHelper;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadBlogZip(InputStream fileInputStream) {
        try {
            java.nio.file.Path uploadDirPath = Paths.get(appConfig.getZipUploadDir());
            java.nio.file.Path extractDirPath = Paths.get(appConfig.getZipExtractDir());
            // Falls das Verzeichnis nicht existiert, wird es erstellt
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }
            if (!Files.exists(extractDirPath)) {
                Files.createDirectories(extractDirPath);
            }

            // Aktuell nur 1 Bild möglich (muss noch angepasst werden)
            String filename = "uploadBlog.zip";
            String uploadedFilePath = uploadDirPath.resolve(filename).toString();
            String extractFilePath = extractDirPath.resolve(filename).toString();
            System.out.println("uploadedFilePath: " + uploadedFilePath);
            Files.copy(fileInputStream, Paths.get(uploadedFilePath), StandardCopyOption.REPLACE_EXISTING);
            fileHelper.unzipFile(uploadedFilePath, extractDirPath);
//            fileHelper.unzipFile(extractFilePath, extractDirPath);

            long blogId = fileHelper.saveBlog(extractDirPath.resolve("blog.json"));
            if(blogId > 0){
                java.nio.file.Path imageBlogPath = Paths.get(appConfig.getUploadDir());
                fileHelper.saveImage(extractDirPath, blogId);
            }
            Files.walk(extractDirPath)
                    .filter(path -> !path.equals(extractDirPath))
                    .map(java.nio.file.Path::toFile)
                    .forEach(File::delete);
            return Response.ok("Blog ZIP erfolgreich hochgeladen").build();
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
