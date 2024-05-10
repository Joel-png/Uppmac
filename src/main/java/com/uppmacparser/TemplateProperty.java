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
    
    public TemplateProperty(Template template) {
        this.template = template;
    }
}
