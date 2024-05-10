package com.uppmacparser;
import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class DefaultParser {
    
    private File file;
    String NTAName;
    public DefaultParser(File file, String NTAName) {
        this.file = file;
        this.NTAName = NTAName;
    }

    public Nta parse() throws Exception {
        FileInputStream fileIS = new FileInputStream(this.getFile());
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);
        XPath xPath = XPathFactory.newInstance().newXPath();

        NodeList templateList = (NodeList) xPath.compile("/nta/template").evaluate(xmlDocument, XPathConstants.NODESET);
        NodeList tempGlobalDeclarationList = (NodeList) xPath.compile("/nta/declaration/text()").evaluate(xmlDocument, XPathConstants.NODESET);
        //System.out.println(tempGlobalDeclarationList.item(0).toString());
        String globalDeclaration = "";
        if (tempGlobalDeclarationList.getLength() > 0) {
            globalDeclaration = tempGlobalDeclarationList.item(0).toString();
        }
        Nta nta = new Nta(templateList.getLength(), NTAName, globalDeclaration);

        // do for each template
        // - find locations
        // - find transitions
        //
        // indexing starts on 1 because thats how xpath does things
        for (int i = 1; i <= templateList.getLength(); i++) {
            NodeList tempLocationList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/location").evaluate(xmlDocument, XPathConstants.NODESET);
            NodeList tempTransitionList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/transition").evaluate(xmlDocument, XPathConstants.NODESET);
            NodeList tempDeclarationList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/declaration/text()").evaluate(xmlDocument, XPathConstants.NODESET);
            NodeList tempTemplateName = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/name/text()").evaluate(xmlDocument, XPathConstants.NODESET);
            String declaration = "";
            if (tempDeclarationList.getLength() > 0) {
                declaration = tempDeclarationList.item(0).toString();
            }

            Template tempTemplate = new Template(cleanText(tempTemplateName.item(0).toString()), tempLocationList.getLength(), tempTransitionList.getLength(), declaration);
            //System.out.println(tempDeclarationList.item(0).toString());

            NodeList tempInit = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/init/@ref").evaluate(xmlDocument, XPathConstants.NODESET);
            tempTemplate.init = cleanID(tempInit.item(0).toString(), 5);

            for (int j = 1; j <= tempLocationList.getLength(); j++) {
                NodeList tempLocationIDList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/location[" + String.valueOf(j) + "]/@id").evaluate(xmlDocument, XPathConstants.NODESET);
                NodeList tempLocationNameList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/location[" + String.valueOf(j) + "]/name/text()").evaluate(xmlDocument, XPathConstants.NODESET);

                // incase name isn't included
                String name = "";
                if (tempLocationNameList.item(0) != null) {
                    name = cleanText(tempLocationNameList.item(0).toString());
                }
                Location tempLocation = new Location(cleanID(tempLocationIDList.item(0).toString(), 4), name);
                tempTemplate.addLocation(tempLocation, j-1);
                
            }

            for (int j = 1; j <= tempTransitionList.getLength(); j++) {
                NodeList tempTransitionSourceList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/transition[" + String.valueOf(j) + "]/source/@ref").evaluate(xmlDocument, XPathConstants.NODESET);
                NodeList tempTransitionTargetList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/transition[" + String.valueOf(j) + "]/target/@ref").evaluate(xmlDocument, XPathConstants.NODESET);
                //printNodeList(tempTransitionSourceList);
                //printNodeList(tempTransitionTargetList);
                Transition tempTransition = new Transition(cleanID(tempTransitionSourceList.item(0).toString(), 5), cleanID(tempTransitionTargetList.item(0).toString(), 5), "tid" + j);
                tempTemplate.addTransition(tempTransition, j-1);
                tempTemplate.updateTemplateWithTransitions(tempTransition);
            }

            nta.addTemplate(tempTemplate, i-1);
        }
        return nta;
    }

    public File getFile() {
        return file;
    }

    public void printNodeList(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            System.out.println(nodeList.item(i).toString());
        }
    }

    // returns simplified ID from NodeList, helps with readability
    // input: id="id0"
    // output: id0
    public String cleanID(String string, int cutOff) {
        return string.substring(cutOff, string.length()-1);
    }

    // returns simplified text from NodeList, helps with readability
    // input: [#text: L3]
    // output: L3
    public String cleanText(String string) {
        return string.substring(8, string.length()-1);
    }

}