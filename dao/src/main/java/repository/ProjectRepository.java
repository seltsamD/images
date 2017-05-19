package repository;


import JAXB.*;
import JAXB.TextField;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.awt.Color.BLACK;


public class ProjectRepository {

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
        File archiveDirectory = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).toFile();

        if (!archiveDirectory.exists()) {
            archiveDirectory.mkdir();
        }

        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileName));
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                File fileInArchive = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve(entry.getName()).toFile();
                try (BufferedOutputStream outpuStreamZip = new BufferedOutputStream(new FileOutputStream(fileInArchive))) {
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


        generateThumbnail(archiveDirectory);


    }

    public void generateThumbnail(File dir) {


        String[] filter = new String[]{"xml"};
        List<File> files = (List<File>) FileUtils.listFiles(dir, filter, true);
        File xmlFile = files.get(0);

        ProjectParser projectParser = new ProjectParser();
        try {
            UserProject project = (UserProject) projectParser.getObject(xmlFile, UserProject.class);

            BufferedImage imageBI = new BufferedImage(project.getWidth(), project.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = imageBI.createGraphics();
            if (project.getTextFields() != null)
                for (TextField textField : project.getTextFields()) {
                    graphics.setPaint(BLACK);
                    graphics.drawString(textField.getValue(), textField.getX(), textField.getY());
                }


            if (project.getImageFields() != null)
                for (ImageField image : project.getImageFields()) {
                    File file = saveThumbnail(image.getImageRef(), image.getHeight(), image.getWidth());
                    BufferedImage buf = ImageIO.read(file);
                    graphics.drawImage(buf, image.getX(), image.getY(), image.getWidth(), image.getHeight(), null);
                }
            for (Block block : project.getBlocks()) {
                if (block.getImageFields() != null)
                    for (ImageField image : block.getImageFields()) {
                        File file = saveThumbnail(image.getImageRef(), image.getHeight(), image.getWidth());
                        BufferedImage buf = ImageIO.read(file);
                        graphics.drawImage(buf, image.getX(), image.getY(), image.getWidth(), image.getHeight(), null);
                    }
            }


            File previewToProject = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve("preview.png").toFile();

            try (OutputStream fis = new FileOutputStream(previewToProject)) {
                ImageIO.write(imageBI, "png", previewToProject);
            } catch (IOException e) {
                e.printStackTrace();
            }
//
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder = null;
//        try {
//            dBuilder = dbFactory.newDocumentBuilder();
//            Document doc = dBuilder.parse(file.get(0));
//            doc.getDocumentElement().normalize();
//            Node project = doc.getElementsByTagName("project").item(0);
//            Element projectElement = (Element) project;
//            String name = projectElement.getAttribute("name");
//            int height = Integer.parseInt(projectElement.getAttribute("height"));
//            int width = Integer.parseInt(projectElement.getAttribute("width"));
//
//            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//            Graphics2D graphics = image.createGraphics();
//            graphics.setPaint(new Color(0, 255, 10));
//            graphics.fillRect(0,0, width, height);
//            RenderedImage rendImage = image;
//            File previewToProject = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve("preview.png").toFile();
//
//            try (OutputStream fis = new FileOutputStream(previewToProject)) {
//                ImageIO.write(rendImage, "png", previewToProject);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//        } catch (ParserConfigurationException | IOException | SAXException e) {
//            e.printStackTrace();
//        }


    }

    public File saveThumbnail(String name, int height, int width) {

        File result = null;
        File folder = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve("thumbnails").toFile();
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve(name).toFile();
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
            BufferedImage outputImage = new BufferedImage(width, height, image.getType());
            Graphics2D graphics2D = outputImage.createGraphics();
            graphics2D.drawImage(image, 0, 0, width, height, null);
            graphics2D.dispose();
            String filename = FilenameUtils.getBaseName(name) + "_" + width + "_" + height + "." + FilenameUtils.getExtension(name);
            result = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve("thumbnails").resolve(filename).toFile();
            ImageIO.write(outputImage, FilenameUtils.getExtension(name), result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }


    public File callPreview() {
        File file = Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).resolve("preview.png").toFile();
        if (file.exists())
            return file;

        return null;
    }

    public void deleteProject() {
        try {
            FileUtils.deleteDirectory(Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName).toFile());
            Paths.get(rootPath).resolve(String.valueOf(userId)).resolve(projectName + ".zip").toFile().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
