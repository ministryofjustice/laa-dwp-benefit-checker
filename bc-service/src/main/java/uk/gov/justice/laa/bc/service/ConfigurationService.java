package uk.gov.justice.laa.bc.service;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.bc.model.ClientOrg;
import uk.gov.justice.laa.bc.model.Configuration;

/**
 * Listener responsible for parsing the environment variables, and stopping
 * the application from starting if it isn't configured properly.
 */
@Service
public class ConfigurationService {

  private Configuration configuration;
  @Value("${service.name}")
  private String serviceName;
  @Value("${service.client-ids}")
  private String clientDefinitions;

  @PostConstruct
  public void initialize() {
    configuration = Configuration.parse(serviceName, clientDefinitions);
  }

  public String getServiceName() {
    return configuration.getServiceName();
  }

  public Map<String, ClientOrg> getClientOrgs() {
    return configuration.getClientOrgs();
  }
}
