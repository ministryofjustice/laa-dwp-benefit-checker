package uk.gov.justice.laa.bc.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.Item;
import uk.gov.justice.laa.bc.model.BenefitCheckRequestBody;
import uk.gov.justice.laa.bc.model.BenefitCheckResponseBody;

/**
 * BcWebService.
 */
@Service
public class BcWebService {
  private final RestTemplate restTemplate;
  private final String soapUrl;
  private static final String FEATURE = "http://apache.org/xml/features/disallow-doctype-decl";

  public BcWebService(RestTemplate restTemplate, @Value(value = "${dwp.soap.url}") String soapUrl) {
    this.restTemplate = restTemplate;
    this.soapUrl = soapUrl;
  }

  private static final String ENVELOPE = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" "
          + "xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
          + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:m0=\"http://dwp.gov.uk/Common/CIS/GetBenefitStatusExt/Data/2.0\">"
          + "<SOAP-ENV:Body>"
          + "<m:getBenefitStatusExt xmlns:m=\"http://dwp.gov.uk/Common/CIS/GetBenefitStatusExt/Service/2.0\">"
          + "<m0:surname>%s</m0:surname>"
          + "<m0:dateOfBirth>%s</m0:dateOfBirth>"
          + "<m0:nino>%s</m0:nino>"
          + "<m0:dateOfAward>%s</m0:dateOfAward>"
          + "</m:getBenefitStatusExt>"
          + "</SOAP-ENV:Body>"
          + "</SOAP-ENV:Envelope>";
  private static final String PATH_ID = "/"
          + "*[local-name()='Envelope']"
          + "/*[local-name()='Body']"
          + "/*[local-name()='getBenefitStatusExtResponse']"
          + "/*[local-name()='itemList'][1]"
          + "/*[local-name()='id']";
  private static final String PATH_VALUE = "/"
          + "*[local-name()='Envelope']"
          + "/*[local-name()='Body']"
          + "/*[local-name()='getBenefitStatusExtResponse']"
          + "/*[local-name()='itemList'][1]"
          + "/*[local-name()='value']";

  /**
   * perform.
   *
   * @param benefitCheckRequestBody BenefitCheckRequestBody
   * @return BenefitCheckResponseBody
   */
  public BenefitCheckResponseBody performCheck(
          BenefitCheckRequestBody benefitCheckRequestBody) throws Exception {
    Item item = perform(benefitCheckRequestBody);
    BenefitCheckResponseBody response = new BenefitCheckResponseBody();
    response.setConfirmationRef(item.getId());
    response.setBenefitCheckerStatus(item.getValue());
    response.setOriginalClientRef("CLIENT_ID");
    return response;
  }

  protected Item perform(BenefitCheckRequestBody request)
          throws Exception {

    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
    messageConverters.add(new StringHttpMessageConverter());
    restTemplate.setMessageConverters(messageConverters);


    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_XML);
    headers.add("SOAPAction", "");

    String xmlString = String.format(ENVELOPE, request.getSurname(), request.getDateOfBirth(),
            Objects.isNull(request.getNino()) ? "nil" : request.getNino(),
            request.getDateOfAward());
    HttpEntity<String> httpRequest = new HttpEntity<String>(xmlString, headers);

    final ResponseEntity<String>
            response = restTemplate.postForEntity(soapUrl, httpRequest, String.class);
    String body = response.getBody();
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    try {
      builderFactory.setFeature(FEATURE, true);
      builderFactory.setXIncludeAware(false);
    } catch (Exception e) {
      e.printStackTrace();
    }
    DocumentBuilder builder = builderFactory.newDocumentBuilder();
    Document xmlDocument = builder.parse(new InputSource(new StringReader(body)));
    XPath path = XPathFactory.newInstance().newXPath();
    NodeList id = (NodeList) path.compile(PATH_ID).evaluate(xmlDocument, XPathConstants.NODESET);
    Item item = new Item();
    item.setId(id.item(0).getTextContent());
    NodeList value = (NodeList) path.compile(PATH_VALUE)
            .evaluate(xmlDocument, XPathConstants.NODESET);
    item.setValue(value.item(0).getTextContent());
    return item;
  }

}
