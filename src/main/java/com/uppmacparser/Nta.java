package com.uppmacparser;


public class Nta {
    Declaration globalDeclaration;
    Template[] templates;
    SSystem system;
    String name;
    int functionCount = 0;
    int variableCount = 0;
    int globalComplexity = 0;
    int templateComplexity = 0;
    int templateFunctions = 0;
    int templateVariables = 0;

    public Nta(int templateLength, String name, String declaration) {
        this.templates = new Template[templateLength];
        this.name = name;
        this.globalDeclaration = new Declaration(declaration);
        this.functionCount = globalDeclaration.countFunctions();
        this.variableCount = globalDeclaration.countVariables();
        this.globalComplexity = functionCount + variableCount;
    }

    public String getData(int i) {
        switch (i) {
            case 0:
                return name;
            case 1:
                return String.valueOf(functionCount);
            case 2:
                return String.valueOf(variableCount);
            case 3:
                return String.valueOf(globalComplexity);
            case 4:
                return String.valueOf(templateFunctions);
            case 5:
                return String.valueOf(templateVariables);
            case 6:
                return String.valueOf(templateComplexity);
            default:
                return "ERROR";
        }
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
