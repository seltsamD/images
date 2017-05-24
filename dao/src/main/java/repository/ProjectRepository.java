package repository;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import util.ProjectUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.apache.commons.io.FilenameUtils.getBaseName;
import static org.apache.commons.io.FilenameUtils.getExtension;

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
    private Path thumbnailDirectory;
    private static final Logger LOGGER = Logger.getLogger(ProjectRepository.class);

    public ProjectRepository(long userId, String rootPath, String projectName) {
        this.projectName = projectName;
        this.userDirectory = Paths.get(rootPath).resolve(String.valueOf(userId));
        this.projectDirectory = userDirectory.resolve(projectName);
        this.thumbnailDirectory = getFile("thumbnails").toPath();
        thumbnailDirectory.toFile().mkdirs();
    }

    private File saveProjectZip(InputStream inputStream) {
        File fileName = userDirectory.resolve(projectName + ".zip").toFile();
        try (InputStream is = inputStream;
             OutputStream fos = new FileOutputStream(fileName)) {
            IOUtils.copy(is, fos);
        } catch (IOException e) {
            LOGGER.error("Error at the process of save project.zip " + e.getMessage());
        }
        return fileName;
    }


    private File getFile(String name) {
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
            LOGGER.error("Error at the process of unzip project " + e.getMessage());
        }

    }

    public File getXML() {
        String[] filter = new String[]{"xml"};
        List<File> files = (List<File>) FileUtils.listFiles(projectDirectory.toFile(), filter, true);
        return files.get(0);
    }

    public File getThumbnail(String name, int height, int width) {
        String filename = getBaseName(name) + "_" + width + "_" + height + "." + getExtension(name);
        return projectDirectory.resolve("thumbnails").resolve(filename).toFile();
    }

    public File getImage(String name) {
        return getFile(name);
    }

    public File getProjectDirectory() {
        return projectDirectory.toFile();
    }

    public File getPreview(ProjectUtils.PREVIEW_TYPES type) {
        return projectDirectory.resolve("preview." + type).toFile();
    }

    public void deleteProject() {
        FileUtils.deleteQuietly(projectDirectory.toFile());
        FileUtils.deleteQuietly(userDirectory.resolve(projectName + ".zip").toFile());
    }


}
