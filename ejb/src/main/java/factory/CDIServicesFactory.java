package factory;


import canvas.Canvas;
import canvas.Canvas2D;
import canvas.CanvasBFO;
import generator.PreviewGenerator;
import processor.Graphic2DProcessor;
import processor.ImageMagicProcessor;
import processor.ImageProcessor;
import repository.ProjectRepositoryFactory;
import service.ConfigService;
import util.PropertyKeys;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;


public class CDIServicesFactory {

    @Inject
    private ConfigService configService;

    @Produces
    public ProjectRepositoryFactory createFactory() {
        return new ProjectRepositoryFactory(configService.get(PropertyKeys.ROOT_PATH));
    }


    @Produces
    public ImageProcessor provideImageProcessor() {
        if (configService.getBoolean(PropertyKeys.IMAGE_MAGICK)) {
            return new ImageMagicProcessor(configService.get(PropertyKeys.IMAGE_MAGICK_PATH));
        }
        return new Graphic2DProcessor();
    }

    @Produces
    public Canvas provideCanvas() {
        if (configService.getBoolean(PropertyKeys.GRAPHIC_2D)) {
            return new Canvas2D();
        }
        return new CanvasBFO();
    }


}
