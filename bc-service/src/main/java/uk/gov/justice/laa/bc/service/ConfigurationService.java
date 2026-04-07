package uk.gov.justice.laa.bc.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.justice.laa.bc.model.Configuration;

/**
 * Listener responsible for parsing the environment variables, and stopping
 * the application from starting if it isn't configured properly.
 */
@Service
public class ConfigurationService {

  private Configuration configuration;
  @Value("${credentials.service.context}")
  private String serviceName;
  @Value("${client.ids}")
  private String clientDefinitions;

  @PostConstruct
  public void initialize() {
    configuration = Configuration.parse(serviceName, clientDefinitions);
  }

  public Configuration getConfiguration() {
    return configuration;
  }
}
