package tn.soa.emergency.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    
    public static Document parseXML(String xmlString) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlString)));
    }

    public static String getTextContent(Document doc, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }

    public static List<Element> getElementsByTagName(Document doc, String tagName) {
        List<Element> elements = new ArrayList<>();
        NodeList nodeList = doc.getElementsByTagName(tagName);
        
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                elements.add((Element) node);
            }
        }
        
        return elements;
    }

    public static String getElementValue(Element element, String childTagName) {
        NodeList nodeList = element.getElementsByTagName(childTagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }

    public static Long parseLongValue(Element element, String childTagName) {
        String value = getElementValue(element, childTagName);
        return value != null ? Long.parseLong(value) : null;
    }

    public static Integer parseIntValue(Element element, String childTagName) {
        String value = getElementValue(element, childTagName);
        return value != null ? Integer.parseInt(value) : null;
    }

    public static boolean hasElement(Element element, String childTagName) {
        NodeList nodeList = element.getElementsByTagName(childTagName);
        return nodeList.getLength() > 0;
    }

    public static String extractTextFromNode(Node node) {
        if (node == null) return null;
        
        StringBuilder text = new StringBuilder();
        NodeList children = node.getChildNodes();
        
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                text.append(child.getNodeValue());
            }
        }
        
        return text.toString().trim();
    }

    public static String formatXML(String xml) {
        try {
            Document doc = parseXML(xml);
            return formatDocument(doc);
        } catch (Exception e) {
            return xml;
        }
    }

    private static String formatDocument(Document doc) {
        try {
            javax.xml.transform.TransformerFactory tf = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            java.io.StringWriter writer = new java.io.StringWriter();
            transformer.transform(new javax.xml.transform.dom.DOMSource(doc), 
                                new javax.xml.transform.stream.StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            return doc.toString();
        }
    }
}