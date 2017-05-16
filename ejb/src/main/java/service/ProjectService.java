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

@Stateless
public class ProjectService {

    @Inject
    ProjectDao projectDao;

    private static final String LOCATION_FOLDER = "C:/Users/dnakhod/Documents";

    @Inject
    UserDao userDao;

    public List<Object> findAllWithUser() {
        return projectDao.findAllWithUser();
    }

    public List<Project> getByUser(long userId) {
        return projectDao.getByUser(userId);
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
                BufferedOutputStream  outpuStreamZip = new BufferedOutputStream (new FileOutputStream(LOCATION_FOLDER + "/" + userId + "/" + name + "/" + entry.getName()));
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
}
