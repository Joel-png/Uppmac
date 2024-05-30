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
    int flowers = 0;
    
    public TemplateProperty(Template template) {
        this.template = template;
    }

    public String getData(int i) {
        switch (i) {
            case 1:
                return template.name;
            case 2:
                if (lonelyInit) {
                    return "1";
                } else {
                    return "0";
                }
            case 3:
                return String.valueOf(numLocations);
            case 4:
                return String.valueOf(numTransitions);
            case 5:
                return String.valueOf(declarationLength);
            case 6:
                return String.valueOf(functions);
            case 7:
                return String.valueOf(variables);
            case 8:
                return String.valueOf(numOfPoplarLocations);
            case 9:
                if (dag) {
                    return "1";
                } else {
                    return "0";
                }
            case 10:
                if (singleLocation) {
                    return "1";
                } else {
                    return "0";
                }
            case 11:
                return String.valueOf(deadEnds);
            case 12:
                return String.valueOf(flowers);
            default:
                return "ERROR";
        }
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
            String.valueOf(variables),
            String.valueOf(numOfPoplarLocations), 
            dagString, 
            isSingleLocation, 
            String.valueOf(deadEnds),
            String.valueOf(flowers)
        };

        return data;
    }
}
