package com.emergency.util;

import org.xml.sax.SAXException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

public class XMLValidator {
    
    private Schema schema;
    
    public XMLValidator(String xsdPath) throws SAXException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        InputStream xsdStream = getClass().getClassLoader().getResourceAsStream(xsdPath);
        if (xsdStream == null) {
            throw new IllegalArgumentException("XSD file not found: " + xsdPath);
        }
        this.schema = factory.newSchema(new StreamSource(xsdStream));
    }
    
    public boolean validate(String xmlContent) {
        try {
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlContent)));
            return true;
        } catch (SAXException | IOException e) {
            System.err.println("XML Validation Error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean validateFile(File xmlFile) {
        try {
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
            return true;
        } catch (SAXException | IOException e) {
            System.err.println("XML File Validation Error: " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args) {
        try {
            XMLValidator validator = new XMLValidator("schema/emergency.xsd");
            
            String testXML = """
                <?xml version="1.0" encoding="UTF-8"?>
                <emergency xmlns="http://emergency.com/schema">
                    <id>1</id>
                    <type>FIRE</type>
                    <description>Building fire on Main Street</description>
                    <location>123 Main St, Downtown</location>
                    <severity>HIGH</severity>
                    <status>PENDING</status>
                    <reporterName>John Doe</reporterName>
                    <reporterPhone>+216-98-123-456</reporterPhone>
                </emergency>
                """;
            
            boolean isValid = validator.validate(testXML);
            System.out.println("XML Validation Result: " + (isValid ? "✓ Valid" : "✗ Invalid"));
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}