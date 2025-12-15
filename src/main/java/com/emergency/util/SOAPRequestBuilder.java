package tn.soa.emergency.util;

import tn.soa.emergency.model.Emergency;

public class SOAPRequestBuilder {
    
    private static final String SOAP_NAMESPACE = "http://service.emergency.soa.tn/";
    
    public static String buildGetAllEmergenciesRequest() {
        return buildSOAPEnvelope(
            "<ns2:getAllEmergencies xmlns:ns2=\"" + SOAP_NAMESPACE + "\"/>"
        );
    }

    public static String buildGetEmergencyByIdRequest(Long id) {
        return buildSOAPEnvelope(
            "<ns2:getEmergencyById xmlns:ns2=\"" + SOAP_NAMESPACE + "\">" +
            "   <id>" + id + "</id>" +
            "</ns2:getEmergencyById>"
        );
    }

    public static String buildCreateEmergencyRequest(Emergency emergency) {
        StringBuilder request = new StringBuilder();
        request.append("<ns2:createEmergency xmlns:ns2=\"").append(SOAP_NAMESPACE).append("\">");
        request.append("   <emergency>");
        
        if (emergency.getId() != null) {
            request.append("      <id>").append(emergency.getId()).append("</id>");
        }
        
        request.append("      <type>").append(escapeXML(emergency.getType())).append("</type>");
        request.append("      <location>").append(escapeXML(emergency.getLocation())).append("</location>");
        request.append("      <priority>").append(escapeXML(emergency.getPriority())).append("</priority>");
        request.append("      <status>").append(escapeXML(emergency.getStatus())).append("</status>");
        request.append("      <date>").append(escapeXML(emergency.getDate())).append("</date>");
        
        if (emergency.getDescription() != null) {
            request.append("      <description>").append(escapeXML(emergency.getDescription())).append("</description>");
        }
        
        if (emergency.getAssignedTo() != null) {
            request.append("      <assignedTo>").append(escapeXML(emergency.getAssignedTo())).append("</assignedTo>");
        }
        
        request.append("   </emergency>");
        request.append("</ns2:createEmergency>");
        
        return buildSOAPEnvelope(request.toString());
    }

    public static String buildUpdateEmergencyRequest(Emergency emergency) {
        StringBuilder request = new StringBuilder();
        request.append("<ns2:updateEmergency xmlns:ns2=\"").append(SOAP_NAMESPACE).append("\">");
        request.append("   <emergency>");
        request.append("      <id>").append(emergency.getId()).append("</id>");
        request.append("      <type>").append(escapeXML(emergency.getType())).append("</type>");
        request.append("      <location>").append(escapeXML(emergency.getLocation())).append("</location>");
        request.append("      <priority>").append(escapeXML(emergency.getPriority())).append("</priority>");
        request.append("      <status>").append(escapeXML(emergency.getStatus())).append("</status>");
        request.append("      <date>").append(escapeXML(emergency.getDate())).append("</date>");
        
        if (emergency.getDescription() != null) {
            request.append("      <description>").append(escapeXML(emergency.getDescription())).append("</description>");
        }
        
        if (emergency.getAssignedTo() != null) {
            request.append("      <assignedTo>").append(escapeXML(emergency.getAssignedTo())).append("</assignedTo>");
        }
        
        request.append("   </emergency>");
        request.append("</ns2:updateEmergency>");
        
        return buildSOAPEnvelope(request.toString());
    }

    public static String buildDeleteEmergencyRequest(Long id) {
        return buildSOAPEnvelope(
            "<ns2:deleteEmergency xmlns:ns2=\"" + SOAP_NAMESPACE + "\">" +
            "   <id>" + id + "</id>" +
            "</ns2:deleteEmergency>"
        );
    }

    public static String buildGetEmergenciesByStatusRequest(String status) {
        return buildSOAPEnvelope(
            "<ns2:getEmergenciesByStatus xmlns:ns2=\"" + SOAP_NAMESPACE + "\">" +
            "   <status>" + escapeXML(status) + "</status>" +
            "</ns2:getEmergenciesByStatus>"
        );
    }

    public static String buildGetEmergenciesByPriorityRequest(String priority) {
        return buildSOAPEnvelope(
            "<ns2:getEmergenciesByPriority xmlns:ns2=\"" + SOAP_NAMESPACE + "\">" +
            "   <priority>" + escapeXML(priority) + "</priority>" +
            "</ns2:getEmergenciesByPriority>"
        );
    }

    public static String buildUpdateStatusRequest(Long id, String status) {
        return buildSOAPEnvelope(
            "<ns2:updateStatus xmlns:ns2=\"" + SOAP_NAMESPACE + "\">" +
            "   <id>" + id + "</id>" +
            "   <status>" + escapeXML(status) + "</status>" +
            "</ns2:updateStatus>"
        );
    }

    public static String buildAssignEmergencyRequest(Long id, String assignedTo) {
        return buildSOAPEnvelope(
            "<ns2:assignEmergency xmlns:ns2=\"" + SOAP_NAMESPACE + "\">" +
            "   <id>" + id + "</id>" +
            "   <assignedTo>" + escapeXML(assignedTo) + "</assignedTo>" +
            "</ns2:assignEmergency>"
        );
    }

    private static String buildSOAPEnvelope(String body) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
               "xmlns:ns2=\"" + SOAP_NAMESPACE + "\">" +
               "   <soap:Body>" +
               body +
               "   </soap:Body>" +
               "</soap:Envelope>";
    }

    private static String escapeXML(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }

    public static String extractSOAPBody(String soapResponse) {
        try {
            int bodyStart = soapResponse.indexOf("<soap:Body>");
            int bodyEnd = soapResponse.indexOf("</soap:Body>");
            
            if (bodyStart != -1 && bodyEnd != -1) {
                return soapResponse.substring(bodyStart + 11, bodyEnd).trim();
            }
            
            bodyStart = soapResponse.indexOf("<Body>");
            bodyEnd = soapResponse.indexOf("</Body>");
            
            if (bodyStart != -1 && bodyEnd != -1) {
                return soapResponse.substring(bodyStart + 6, bodyEnd).trim();
            }
            
            return soapResponse;
        } catch (Exception e) {
            return soapResponse;
        }
    }
}