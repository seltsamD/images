package canvas;

import model.xml.Project;
import repository.ProjectRepository;

import java.io.File;

public interface Canvas {

    void prepare(Project project);

    void drawText(int x, int y, String value);

    void drawImage(int x, int y, File file);

    void build(ProjectRepository repository);

}
