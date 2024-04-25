package com.uppmacparser;


public class Nta {
    Declaration globalDeclaration;
    Template[] templates;
    SSystem system;
    String name;

    public Nta(int templateLength, String name) {
        this.templates = new Template[templateLength];
        this.name = name;
    }

    public void addTemplate(Template template, int index) {
        templates[index] = template;
    }

    public void printTemplates(int indent) {
        for (int i = 0; i < templates.length; i++) {
            Navigator.indent(indent);
            System.out.print("[" + i + "] ");
            templates[i].printData(indent);
        }
    }
}
