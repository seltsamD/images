package cdi;


import dao.ProjectDao;
import dao.UserDao;
import model.Project;
import org.apache.commons.io.IOUtils;

import javax.inject.Inject;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static constants.ProjectConstants.LOCATION_FOLDER;

public class ProjectRepository {

    @Inject
    ProjectDao projectDao;

    private long userId;
    private String rootPath;
    private String projectName;

    public ProjectRepository(long userId, String rootPath, String projectName) {
        this.userId = userId;
        this.rootPath = rootPath;
        this.projectName = projectName;
    }

    public void saveProjectArchive(InputStream inputStream) {
        File directory = Paths.get(rootPath).resolve(String.valueOf(userId)).toFile();

            if (!directory.exists()) {
                directory.mkdir();
            }
            File fileName = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName + ".zip").toFile();

            //save archive
            try (InputStream is = inputStream;
                 OutputStream fos = new FileOutputStream(fileName)) {
                 IOUtils.copy(is, fos);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //create dir and unzip
            File archiveDirectory = fileName = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).toFile();

            if (!archiveDirectory.exists()) {
                archiveDirectory.mkdir();
            }

            try(ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileName))){
                ZipEntry entry = zipInputStream.getNextEntry();
                while (entry != null) {
                    File fileInArchive = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve(entry.getName()).toFile();
                    try(BufferedOutputStream outpuStreamZip = new BufferedOutputStream(new FileOutputStream(fileInArchive))) {
                        int readInZip = 0;
                        byte[] bytes = new byte[4096];
                        while ((readInZip = zipInputStream.read(bytes)) != -1) {
                            outpuStreamZip.write(bytes, 0, readInZip);
                        }
                    }
                    entry = zipInputStream.getNextEntry();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    public void savePreview() {
        File preview = Paths.get(rootPath).resolve("preview.png").toFile();
        File previewToProject = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve("preview.png").toFile();

        try (InputStream is = new FileInputStream(preview);
             OutputStream fis = new FileOutputStream(previewToProject)) {
            IOUtils.copy(is, fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public File callPreview(){
        File file = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve("preview.png").toFile();
        if(file.exists())
            return file;

        return null;
    }
}
