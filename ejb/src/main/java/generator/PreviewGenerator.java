package generator;

import canvas.Canvas;
import model.xml.BlockField;
import model.xml.ImageField;
import model.xml.Project;
import model.xml.TextField;
import org.jboss.logging.Logger;
import processor.ImageProcessor;
import repository.ProjectRepository;
import util.ProjectUtils;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import java.io.File;

public class PreviewGenerator {

    @Inject
    private Canvas canvas;
    private Project project;
    @Inject
    private ImageProcessor imageProcessor;

    private static final Logger LOGGER = Logger.getLogger(PreviewGenerator.class);

    private void prepareProject(ProjectRepository repository) {
        File xmlFile = repository.getXML();
        try {
            this.project = ProjectUtils.getObject(xmlFile, Project.class);
        } catch (JAXBException e) {
            LOGGER.error("Error at the process of build project from xml. " + e.getMessage());
        }
        canvas.prepare(project);
    }

    public void generate(ProjectRepository repository) {
        prepareProject(repository);
        prepareImages(repository);

        if (project.getImageFields() != null)
            for (ImageField image : project.getImageFields()) {
                File outFile = repository.getThumbnail(image.getImageRef(), image.getHeight(), image.getWidth());
                canvas.drawImage(image.getX(), image.getY(), outFile);
            }

        if (project.getBlocks() != null)
            for (BlockField blockField : project.getBlocks()) {
                drawInBlock(blockField, repository);
            }

        if (project.getTextFields() != null)
            for (TextField textField : project.getTextFields()) {
                canvas.drawText(textField.getX(), textField.getY(), textField.getValue());
            }

        canvas.build(repository);

    }

    private void prepareImages(ProjectRepository repository) {
        if (project.getImageFields() != null)
            for (ImageField image : project.getImageFields()) {
                File target = repository.getThumbnail(image.getImageRef(), image.getHeight(), image.getWidth());
                File source = repository.getImage(image.getImageRef());
                imageProcessor.scale(source, target, image.getWidth(), image.getHeight());
            }
        if (project.getBlocks() != null)
            for (BlockField blockField : project.getBlocks()) {
                prepareInBlock(blockField, repository);
            }
    }

    private void prepareInBlock(BlockField block, ProjectRepository repository) {
        if (block.getImageFields() != null)
            for (ImageField image : block.getImageFields()) {
                File target = repository.getThumbnail(image.getImageRef(), image.getHeight(), image.getWidth());
                File source = repository.getImage(image.getImageRef());
                imageProcessor.scale(source, target, image.getWidth(), image.getHeight());
            }
        if (block.getBlocks() != null)
            prepareInBlock(block, repository);
    }

    private void drawInBlock(BlockField block, ProjectRepository repository) {
            if (block.getImageFields() != null)
                for (ImageField image : block.getImageFields()) {
                    File outFile = repository.getThumbnail(image.getImageRef(), image.getHeight(), image.getWidth());
                    canvas.drawImage(image.getX(), image.getY(), outFile);
                }

        if (block.getBlocks() != null)
            drawInBlock(block, repository);
    }
}

