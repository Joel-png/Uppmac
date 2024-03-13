import java.util.ArrayList;

public class Nta {
    Declaration globalDeclaration;
    ArrayList<Template> templates = new ArrayList<Template>();

    public Nta() {

    }

    public void addTemplate(Template template) {
        templates.add(template);
    }
}
