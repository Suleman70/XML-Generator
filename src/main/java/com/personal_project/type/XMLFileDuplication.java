package com.personal_project.type;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//Developed by Suleman70
//This class is responsible for creating an XML file that can duplicate itself and also modify its creation date

public class XMLFileDuplication implements CustomFileDuplication {



    //Executes the duplication mechanism
    @Override
    public synchronized boolean executeDuplication(int noOfTimes, String templateName, String pathway, boolean customize, int fileNumber) {
        try {
                Document document = getDocument();
                if(customize == true) {
                    document = customizeDocument(document, fileNumber);
                }
                removeWhiteSpace(document);

                String file = pathway + '/' + templateName  +  fileNumber + ".xml";
                System.out.println(file);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(new DOMSource(document), new StreamResult(new File(file)));
                System.out.println("XML file saved successfully!");
            return true;
        }
        catch (Exception e) {

                System.out.println(e);
                return false;
        }

    }


    //Changing the mdofiication of the file, given the pathway of the String
    @Override
    public synchronized void changeDuplicationDate(String pathway, Date from, Date to){

        //Generating Random Date

        long randomTime = ThreadLocalRandom.current().nextLong(from.getTime(),to.getTime()); ///Thread not necessary but gives range unlike Random
        System.out.println(new Date(randomTime));

        try {

            File f = new File(pathway);
            System.out.println(f.setLastModified(randomTime));
        }
        catch (Exception e){
            System.out.println(e);
        }

    }



    //Gets the document for XML DOM from the filepath
    private  synchronized Document getDocument(){
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder(); //Static factory method
            InputStream xmlFile = XMLFileDuplication.class.getResourceAsStream("/files/xml_template.xml"); //InputStream to enable JAR finding the file while execting
            return builder.parse(xmlFile);

        }
        catch(Exception e) {
            System.out.println(e);
        }
        return null;
    }

    //Customize the document by adding the attributes
    private Document customizeDocument(Document document, int number){
        Random random = new Random();
        int orderRandom = random.nextInt(999999999) + 100000000;
        int leftPrice = random.nextInt(100);
        int rightPrice = random.nextInt(100);

        Node orderNodeName = document.createElement("orderID");
        Text oderNodeInnerText = document.createTextNode( String.valueOf(orderRandom));

        Node totalCostName = document.createElement("totalCost");
        Text totalCostInnerText = document.createTextNode( "£" + leftPrice + "." + rightPrice);

        Node fileNumberName = document.createElement("fileNumber");
        Text fileNumberText = document.createTextNode(String.valueOf(number));

        fileNumberName.appendChild(fileNumberText);
        orderNodeName.appendChild(oderNodeInnerText);
        totalCostName.appendChild(totalCostInnerText);
        document.getElementsByTagName("root").item(0).appendChild(fileNumberName);
        document.getElementsByTagName("root").item(0).appendChild(orderNodeName);
        document.getElementsByTagName("root").item(0).appendChild(totalCostName);

        return document;
    }



    //Removing whitespace from Nodes.
    private void removeWhiteSpace(Node node)
    {
        NodeList childrenNodes = node.getChildNodes();
        for(int i = 0; i < childrenNodes.getLength(); i++) {
            Node child = childrenNodes.item(i);
            if(child.getNodeType() == Node.TEXT_NODE) {
                child.setTextContent(child.getTextContent().trim());
            }
            removeWhiteSpace(child);
        }
    }





}
