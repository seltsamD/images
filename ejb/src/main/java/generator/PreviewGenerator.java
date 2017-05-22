package generator;

import canvas.Canvas;
import canvas.Canvas2D;
import model.xml.BlockField;
import model.xml.ImageField;
import model.xml.Project;
import model.xml.TextField;
import processor.Graphic2DProcessor;
import processor.ImageProcessor;
import repository.ProjectRepository;
import service.ConfigService;
import util.ProjectUtils;
import util.PropertyKeys;

import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.File;

public class PreviewGenerator {
    //TODO: make it private
    Canvas canvas;
    Project project;
    ProjectRepository repository;
    ImageProcessor imageProcessor;


    //TODO: lets rework this bean, make it as cdi bean
    // add @Injects of Canvas and ImageProcessor here
    public PreviewGenerator(ProjectRepository repository, ConfigService configService) {
        this.repository = repository;
        File xmlFile = repository.getXML();
        try {
            this.project = ProjectUtils.getObject(xmlFile, Project.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        if (configService.getBoolean(PropertyKeys.GRAPHIC_2D)) {
            this.imageProcessor = new Graphic2DProcessor();
            BufferedImage imageBI = new BufferedImage(project.getWidth(), project.getHeight(), BufferedImage.TYPE_INT_ARGB);
            this.canvas = new Canvas2D(imageBI);
        }

    }

    //TODO: let generate() will have ProjectRepo as argument and call prepareImages() func on this repo
    public void generate() {
        if (project.getImageFields() != null)
            for (ImageField image : project.getImageFields()) {
                File outFile = repository.getImagePathForThumbnail(image.getImageRef());
                canvas.drawImage(image.getX(), image.getY(), outFile);
            }

        if(project.getBlocks() != null)
        for (BlockField blockField : project.getBlocks()) {
            if (blockField.getImageFields() != null)
                for (ImageField image : blockField.getImageFields()) {
                    File outFile = repository.getPathForThumbnail(image.getImageRef(), image.getHeight(), image.getWidth());
                    canvas.drawImage(image.getX(), image.getY(), outFile);
                }
        }

        if (project.getTextFields() != null)
            for (TextField textField : project.getTextFields()) {
                canvas.drawText(textField.getX(), textField.getY(), textField.getValue());
            }

        repository.savePreview(canvas.build());

    }

    //TODO: let prepareImages() will have ProjectRepo as argument, and make it private
    public void prepareImages() {
        if (project.getImageFields() != null)
            for (ImageField image : project.getImageFields()) {
                File target = repository.getPathForThumbnail(image.getImageRef(), image.getHeight(), image.getWidth());
                File source = repository.getImagePathForThumbnail(image.getImageRef());
                repository.saveImage(imageProcessor.scale(source, target, image.getWidth(), image.getHeight()), target.getName());
            }
        if (project.getBlocks() != null)
            for (BlockField blockField : project.getBlocks()) {
                prepareInBlock(blockField);
            }
    }

    //TODO: make it private
    public void prepareInBlock(BlockField block) {
        if (block.getImageFields() != null)
            for (ImageField image : block.getImageFields()) {
                File target = repository.getPathForThumbnail(image.getImageRef(), image.getHeight(), image.getWidth());
                File source = repository.getImagePathForThumbnail(image.getImageRef());
                imageProcessor.scale(source, target, image.getWidth(), image.getHeight());
            }
        if (block.getBlocks() != null)
            prepareInBlock(block);
    }
}

