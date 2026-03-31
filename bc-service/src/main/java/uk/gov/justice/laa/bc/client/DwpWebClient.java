package uk.gov.justice.laa.bc.client;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.Item;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;

public class DwpWebClient {

  public Item perform(BenefitCheckRequestBody request)
      throws Exception {
    RestTemplate restTemplate = new RestTemplate();

    String xmlString = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:m0=\"http://dwp.gov.uk/Common/CIS/GetBenefitStatusExt/Data/2.0\">" +
        "<SOAP-ENV:Body>" +
        "<m:getBenefitStatusExt xmlns:m=\"http://dwp.gov.uk/Common/CIS/GetBenefitStatusExt/Service/2.0\">" +
        "<m0:surname>" + request.getSurname() + "</m0:surname>" +
        "<m0:dateOfBirth>" + request.getDateOfBirth() + "</m0:dateOfBirth>" +
        "<m0:nino>" + request.getNino() + "</m0:nino>" +
        "<m0:dateOfAward>" + request.getDateOfAward() + "</m0:dateOfAward>" +
        "</m:getBenefitStatusExt>" +
        "</SOAP-ENV:Body>" +
        "</SOAP-ENV:Envelope>";
    //Create a list for the message converters
    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
    //Add the String Message converter
    messageConverters.add(new StringHttpMessageConverter());
    //Add the message converters to the restTemplate
    restTemplate.setMessageConverters(messageConverters);


    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_XML);
    headers.add("SOAPAction", "");
    HttpEntity<String> httpRequest = new HttpEntity<String>(xmlString, headers);

    final ResponseEntity<String>
        response = restTemplate.postForEntity("http://localhost:8080/axis/services/CorporateCISGetBenefitStatusExtWS03?getBenefitStatusExt", httpRequest, String.class);
    String body = response.getBody();
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = builderFactory.newDocumentBuilder();
    Document xmlDocument = builder.parse(new InputSource(new StringReader(body)));
    XPath xPath = XPathFactory.newInstance().newXPath();
    String idExpression = "/" +
        "*[local-name()='Envelope']" +
        "/*[local-name()='Body']" +
        "/*[local-name()='getBenefitStatusExtResponse']" +
        "/*[local-name()='itemList'][1]" +
        "/*[local-name()='id']";
    NodeList id = (NodeList) xPath.compile(idExpression).evaluate(xmlDocument, XPathConstants.NODESET);
    Item item = new Item();
    item.setId(id.item(0).getTextContent());
    String valueExpression = "/" +
        "*[local-name()='Envelope']" +
        "/*[local-name()='Body']" +
        "/*[local-name()='getBenefitStatusExtResponse']" +
        "/*[local-name()='itemList'][1]" +
        "/*[local-name()='value']";
    NodeList value = (NodeList) xPath.compile(valueExpression).evaluate(xmlDocument, XPathConstants.NODESET);
    item.setValue(value.item(0).getTextContent());
    return item;
  }

}
