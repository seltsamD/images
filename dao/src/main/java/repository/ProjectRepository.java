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

    private String projectName;
    private Path projectDirectory;
    private Path userDirectory;
    private File previewFile;

    public ProjectRepository(long userId, String rootPath, String projectName) {
        this.projectName = projectName;
        this.userDirectory = Paths.get(rootPath).resolve(String.valueOf(userId)).toAbsolutePath();
        this.projectDirectory = userDirectory.resolve(projectName).toAbsolutePath();
        this.previewFile = projectDirectory.resolve("preview.png").toFile();

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

     File saveProjectZip(InputStream inputStream) {
        File fileName = userDirectory.resolve(projectName + ".zip").toFile();
        try (InputStream is = inputStream;
             OutputStream fos = new FileOutputStream(fileName)) {
            IOUtils.copy(is, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }


    File getFile(String name) {
        return projectDirectory.resolve(name).toFile();
    }

    public void unzipProject(InputStream inputStream) {
        File file = saveProjectZip(inputStream);
        try {

            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));

            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                File fileInArchive = getFile(entry.getName());
                try (BufferedOutputStream outpuStreamZip = new BufferedOutputStream(new FileOutputStream(fileInArchive))) {
                    IOUtils.copy(zipInputStream, outpuStreamZip);
                }
                entry = zipInputStream.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public File getXML() {
        String[] filter = new String[]{"xml"};
        List<File> files = (List<File>) FileUtils.listFiles(projectDirectory.toFile(), filter, true);
        return files.get(0);
    }

    public File getPathForThumbnail(String name, int height, int width) {
        String filename = FilenameUtils.getBaseName(name) + "_" + width + "_" + height + "." + FilenameUtils.getExtension(name);
        return projectDirectory.resolve("thumbnails").resolve(filename).toFile();
    }

    public File getImagePathForThumbnail(String name) {
       return getFile(name);
    }


    public void saveImage(File result, String name) {
        try {
            BufferedImage buf = ImageIO.read(result);
            ImageIO.write(buf, FilenameUtils.getExtension(name), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void savePreview(InputStream is){
        try (OutputStream fos = new FileOutputStream(previewFile)) {
            IOUtils.copy(is, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File callPreview() {
        File file = getFile("preview.png");
        if (file.exists())
            return file;

        return null;
    }

    public void deleteProject() {
        FileUtils.deleteQuietly(projectDirectory.toFile());
        FileUtils.deleteQuietly(userDirectory.resolve(projectName + ".zip").toFile());
    }


}
