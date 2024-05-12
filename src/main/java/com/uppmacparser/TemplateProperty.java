package com.uppmacparser;
public class TemplateProperty {
    Template template;
    Boolean isLinear = false;
    Boolean lonelyInit = false;
    int numOfPoplarLocations = 0;
    Boolean singleLocation = false;
    int numLocations = 0;
    int numTransitions = 0;
    int deadEnds = 0;
    boolean dag = false;
    int declarationLength = 0;
    int functions = 0;
    int variables = 0;
    
    public TemplateProperty(Template template) {
        this.template = template;
    }

    public String[] returnStringData() {
        String isLinearString = "no";
        String hasLonelyInit = "no";
        String isSingleLocation = "no";
        String dagString = "no";

        if (isLinear) {
            isLinearString = "yes";
        }

        if (lonelyInit) {
            hasLonelyInit = "yes";
        }

        if (singleLocation) {
            isSingleLocation = "yes";
        }

        if (dag) {
            dagString = "yes";
        }

        String[] data = new String[] {"", 
            template.name, 
            hasLonelyInit, 
            String.valueOf(numLocations), 
            String.valueOf(numTransitions), 
            String.valueOf(declarationLength), 
            String.valueOf(functions), 
            String.valueOf(numOfPoplarLocations), 
            dagString, 
            isSingleLocation, 
            String.valueOf(deadEnds)};

        return data;
    }
}
