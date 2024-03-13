

public class Nta {
    Declaration globalDeclaration;
    Template[] templates;
    SSystem system;

    public Nta(int templateLength) {
        this.templates = new Template[templateLength];
    }

    public void addTemplate(Template template, int index) {
        templates[index] = template;
    }

}
