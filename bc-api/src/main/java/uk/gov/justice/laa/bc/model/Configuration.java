package uk.gov.justice.laa.bc.model;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration is an immutable class for handling client org definitions.
 * We have a grammar defining how we parse client org definitions:
 * client-definitions = client-definition [ { ";" client-definition } ] ;
 * client-definition  = org-id [ ":" user-ids ] ;
 * org-id             = 13 * character ;
 * user-ids           = user-id [ { "," user-id } ] ;
 * user-id            = 15 * character ;
 * character          = ( number | alpha | allowed-char ) ;
 * number             = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" ;
 * alpha              = ( uppper | lower ) ;
 * upper              = "A" ... "Z" ;
 * lower              = "a" ... "z" ;
 * allowed-char       = "_" ;
 * This is marginally better than the existing thing and means I can deploy
 * a thing to production as part of this cutover weekend which won't break
 * existing clients, and allow me to define the deployment in code for all
 * environments.
 */
public class Configuration {

  public static final String KEY = Configuration.class.getName();

  private final String serviceName;

  private final Map<String, ClientOrg> clientOrgs;

  private Configuration(String serviceName, Map<String, ClientOrg> clientOrgs) {
    this.serviceName = serviceName;
    this.clientOrgs = clientOrgs;
  }

  /**
   * parse string to Configuration.
   *
   * @param serviceName String
   * @param clientDefinitions String
   * @return Configuration
   */
  public static Configuration parse(String serviceName, String clientDefinitions) {
    if (serviceName == null) {
      throw new IllegalArgumentException("Service Name cannot be null");
    }

    // Lazy : using a LinkedHashMap for predictable ordering in the tests.
    // We don't need that guarantee otherwise.
    Map<String, ClientOrg> clientOrgs = new LinkedHashMap<>();

    if (clientDefinitions != null) {
      String[] clientDefns = clientDefinitions.split(";");
      for (String clientDefn : getNonNullStream(clientDefns).filter(Objects::nonNull)
          .collect(Collectors.toList())) {
        ClientOrg org = ClientOrg.parse(clientDefn);
        clientOrgs.put(org.getOrgId(), org);
      }
    }

    return new Configuration(serviceName, clientOrgs);
  }

  private static Stream<String> getNonNullStream(String[] clientDefinitions) {
    if (clientDefinitions == null) {
      return Stream.empty();
    }
    return Arrays.stream(clientDefinitions);
  }

  public String getServiceName() {
    return this.serviceName;
  }

  public Map<String, ClientOrg> getClientOrgs() {
    return this.clientOrgs;
  }


}
