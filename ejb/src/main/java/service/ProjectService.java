package service;

import dao.ProjectDao;
import dao.UserDao;
import model.Project;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static constants.ProjectConstants.LOCATION_FOLDER;

@Stateless
public class ProjectService {

    @Inject
    ProjectDao projectDao;


    @Inject
    UserDao userDao;

    public List<Project> findAll() {
        return projectDao.findAll();
    }

    public List<Project> getByUserName(String username) {
        return projectDao.getByUserName(username);
    }

    public void delete(long id) {
        Project project = projectDao.findById(id);


        File folder = new File(LOCATION_FOLDER + "/" + project.getUser().getId() + "/" + project.getProjectName());
        deleteFolder(folder);
        File file = new File(LOCATION_FOLDER + "/" + project.getUser().getId() + "/" + project.getProjectName() + ".zip");
        file.delete();
        projectDao.delete(project);
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) { //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    public void save(long userId, String projectName, InputStream inputStream) {
        String fileName = null;

        try {

            File directory = new File(LOCATION_FOLDER + "/" + userId);
            if (!directory.exists()) {
                directory.mkdir();
            }
            String name = projectName.substring(0, projectName.indexOf("."));
            long projectId = projectDao.save(new Project(name, userDao.findById(userId)));
            fileName = LOCATION_FOLDER + "/" + userId + "/" + projectName;

            //save zip
            OutputStream outpuStream = new FileOutputStream(new File(fileName));
            int read = 0;
            byte[] bytes = new byte[4096];
            while ((read = inputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            outpuStream.flush();
            outpuStream.close();

            //create dir and unzip


            File directoryZip = new File(LOCATION_FOLDER + "/" + userId + "/" + name);
            if (!directoryZip.exists()) {
                directoryZip.mkdir();
            }

            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileName));
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                BufferedOutputStream outpuStreamZip = new BufferedOutputStream(new FileOutputStream(LOCATION_FOLDER + "/" + userId + "/" + name + "/" + entry.getName()));
                int readInZip = 0;
                while ((readInZip = zipInputStream.read(bytes)) != -1) {
                    outpuStreamZip.write(bytes, 0, readInZip);
                }
                outpuStreamZip.close();

                entry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void savePreview(String userId, String projectName) {
        File preview = new File(LOCATION_FOLDER + "/preview.jpg");
        String name = projectName.substring(0, projectName.indexOf("."));
        File copyFile = new File(LOCATION_FOLDER + "/" + userId + "/" + name + "/preview.jpg");

        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(preview);
            FileOutputStream outStream = new FileOutputStream(copyFile);
            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0) {
                outStream.write(buffer, 0, length);
            }
            inStream.close();
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public File callPreview(String username, String projectName){
        long userId = userDao.getIdByUsername(username);
        File file = new File(LOCATION_FOLDER + "/" + userId + "/" + projectName + "/preview.jpg");
        if(file.exists())
            return file;

        return null;
    }
}
