package repository;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class ProjectRepository {
    //NOTE
    //|root
    //  |-user
    //    |-project.zip
    //    |-project
    //    |  |-thumbnails
    //    |  |  |-image1_10_10.png
    //    |  |  |-image2_20_20.png
    //    |  |-project.xml
    //    |  |-image1.png
    //    |  |-image2.png

    private String projectName;
    private Path projectDirectory;
    private Path userDirectory;
    private File previewFile;

    public ProjectRepository(long userId, String rootPath, String projectName) {
        this.projectName = projectName;
        //TODO: remove .toAbsolutePath(),
        // in our case we have root paths to correctly relativize path
        //TODO: set 3 base directories: root/user, root/user/project, root/user/project/thumbnails
        // userDirectory - you already has
        // projectDirectory - also
        // thumbnailsDirectory - need to create
        // we will use them across project repo
        this.userDirectory = Paths.get(rootPath).resolve(String.valueOf(userId)).toAbsolutePath();
        this.projectDirectory = userDirectory.resolve(projectName).toAbsolutePath();
        //TODO: remove this,
        // its better to resolve this path inside getPreview() func
        this.previewFile = projectDirectory.resolve("preview.png").toFile();

        //TODO; replace last 10 lines with single call thumbnailsDirectory.toFile().mkdirs()
        // mkdirs in opposite to mkdir will create all chain of directories
        // so there are no need to create userDirectory or projectDirectory separately
        if (!userDirectory.toFile().exists()) {
            userDirectory.toFile().mkdir();
        }
        if (!projectDirectory.toFile().exists()) {
            projectDirectory.toFile().mkdir();
        }
        File folder = getFile("thumbnails");
        if (!folder.exists()) {
            folder.mkdir();
        }

    }

    private File saveProjectZip(InputStream inputStream) {
        //TODO: replace with getFile(projectName + ".zip")
        //violation of DRY
        File fileName = userDirectory.resolve(projectName + ".zip").toFile();
        try (InputStream is = inputStream;
             OutputStream fos = new FileOutputStream(fileName)) {
            IOUtils.copy(is, fos);
        } catch (IOException e) {
            //TODO: log error
            e.printStackTrace();
        }
        return fileName;
    }


    private File getFile(String name) {
        return projectDirectory.resolve(name).toFile();
    }

    public void unzipProject(InputStream inputStream) {
        File file = saveProjectZip(inputStream);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            for (ZipEntry entry = zis.getNextEntry(); entry != null; )
                try (FileOutputStream fis = new FileOutputStream(getFile(entry.getName()))) {
                    IOUtils.copy(zis, fis);
                }
        } catch (IOException e) {
            //TODO: log error
            e.printStackTrace();
        }
    }

    public File getXML() {
        String[] filter = new String[]{"xml"};
        List<File> files = (List<File>) FileUtils.listFiles(projectDirectory.toFile(), filter, true);
        return files.get(0);
    }

    //TODO: rename to getThumbnail
    // we assume that thumbnail is a file and its exist
    public File getPathForThumbnail(String name, int height, int width) {
        String filename = FilenameUtils.getBaseName(name) + "_" + width + "_" + height + "." + FilenameUtils.getExtension(name);
        return projectDirectory.resolve("thumbnails").resolve(filename).toFile();
    }

    //TODO: rename to getImage
    // we assume that image is a file and its exist
    public File getImagePathForThumbnail(String name) {
        return getFile(name);
    }

    //TODO: replace with File saveImage(InputStream is, String name) or File saveImage(File source)
    // it should copy/store image into repository
    public void saveImage(File result, String name) {
        try {
            BufferedImage buf = ImageIO.read(result);
            ImageIO.write(buf, FilenameUtils.getExtension(name), result);
        } catch (IOException e) {
            //TODO: log error
            e.printStackTrace();
        }
    }


    public void savePreview(InputStream is) {
        try (OutputStream fos = new FileOutputStream(previewFile)) {
            IOUtils.copy(is, fos);
        } catch (IOException e) {
            //TODO: log error
            e.printStackTrace();
        }
    }

    //TODO: rename to getPreview(), return built File, check later inside business logic
    public File callPreview() {
        File file = getFile("preview.png");
        if (file.exists())
            return file;

        return null;
    }

    public void deleteProject() {
        FileUtils.deleteQuietly(projectDirectory.toFile());
        //TODO: replace with FileUtils.deleteQuietly(getProjectZip());
        FileUtils.deleteQuietly(userDirectory.resolve(projectName + ".zip").toFile());
    }


}
