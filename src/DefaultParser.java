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
    public DefaultParser(File file) {
        this.file = file;
    }

    public void parse() throws Exception {
        FileInputStream fileIS = new FileInputStream(this.getFile());
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setNamespaceAware(true);
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document xmlDocument = builder.parse(fileIS);
        XPath xPath = XPathFactory.newInstance().newXPath();

        NodeList templateList = (NodeList) xPath.compile("/nta/template").evaluate(xmlDocument, XPathConstants.NODESET);
        
        Nta nta = new Nta(templateList.getLength());

        // do for each template
        // - find locations
        // - find transitions
        //
        // indexing starts on 1 because thats how xpath does things
        for (int i = 1; i <= templateList.getLength(); i++) {
            NodeList tempLocationList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/location").evaluate(xmlDocument, XPathConstants.NODESET);
            NodeList tempTransitionList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/transition").evaluate(xmlDocument, XPathConstants.NODESET);
            NodeList tempTemplateName = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/name/text()").evaluate(xmlDocument, XPathConstants.NODESET);
            Template tempTemplate = new Template(tempTemplateName.item(0).toString(), tempLocationList.getLength(), tempTransitionList.getLength());

            NodeList tempLocationIDList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/location/@id").evaluate(xmlDocument, XPathConstants.NODESET);
            NodeList tempLocationNameList = (NodeList) xPath.compile("/nta/template[" + String.valueOf(i) + "]/location/name/text()").evaluate(xmlDocument, XPathConstants.NODESET);
            printNodeList(tempLocationIDList);
            printNodeList(tempLocationNameList);
            
            for (int j = 0; j < tempLocationList.getLength(); j++) {
                // incase name isn't included
                String name = "";
                if (tempLocationNameList.item(j) != null) {
                    name = tempLocationNameList.item(j).toString();
                }
                Location tempLocation = new Location(cleanID(tempLocationIDList.item(j).toString()), name);
                tempTemplate.addLocation(tempLocation, j);
                
            }
            tempTemplate.printData();
        }

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
    public String cleanID(String string) {
        return string.substring(4, string.length()-1);
    }
}