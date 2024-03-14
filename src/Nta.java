

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

    public void printTemplates() {
        for (int i = 0; i < templates.length; i++) {
            System.out.print("[" + i + "] ");
            templates[i].printData();
        }
    }
}
