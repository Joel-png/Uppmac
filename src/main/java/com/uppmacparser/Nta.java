package com.uppmacparser;

import java.io.IOException;

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
    int globalDecLength = 0;
    int totalTempDecLength = 0;

    public Nta(int templateLength, String name, String declaration) {
        this.templates = new Template[templateLength];
        this.name = name;
        this.globalDeclaration = new Declaration(declaration);
        this.functionCount = globalDeclaration.countFunctions();
        this.variableCount = globalDeclaration.countVariables();
        this.globalComplexity = this.functionCount + this.variableCount;
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
            case 7:
                return String.valueOf(globalDecLength);
            case 8:
                return String.valueOf(totalTempDecLength);
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

    public int totalTempDeclarationLength() throws IOException {
        int counter = 0;
        for (int i = 0; i < templates.length; i++) {
            counter += getLengthOfDeclaration(templates[i].declaration);
        }
        return counter;
    }

    public int getLengthOfDeclaration(Declaration declaration) throws IOException {
        int count = 0;
        String input = declaration.content;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ';') {
                count++;
            }
        }
        return count;
    }
}
