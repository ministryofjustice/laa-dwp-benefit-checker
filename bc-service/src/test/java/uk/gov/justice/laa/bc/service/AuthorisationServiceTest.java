package uk.gov.justice.laa.bc.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.bc.exception.ValidationException;
import uk.gov.justice.laa.bc.model.ClientOrg;

@ExtendWith(MockitoExtension.class)
class AuthorisationServiceTest {

  private AuthorisationService service;

  // We’ll inject this mock into the private field "configuration"
  @Mock
  private ConfigurationService configurationService;

  @BeforeEach
  void setUp() throws Exception {
    service = new AuthorisationService(configurationService);
  }

  @Test
  void performSecurityCheck_returnsTrue_whenAllMatch() {
    // Given
    String requestServiceName = "svc-A";
    String requestGroupId = "group-1";

    Map<String, ClientOrg> clientOrgs = new HashMap<>();
    ClientOrg clientOrg = ClientOrg.parse("svc-A:user-123,user-456");
    clientOrgs.put(requestGroupId, clientOrg);

    String requestUserId = "user-123";

    // When
    when(configurationService.getServiceName()).thenReturn(requestServiceName);
    when(configurationService.getClientOrgs()).thenReturn(clientOrgs);
    service.performSecurityCheck(requestServiceName, requestGroupId, requestUserId);
  }

  @Test
  void performSecurityCheck_returnsFalse_andAddsViolation_whenServiceNameMismatch() {
    // Given
    String configuredServiceName = "svc-configured";
    String requestServiceName = "svc-request";
    when(configurationService.getServiceName()).thenReturn(configuredServiceName);

    Exception exception = assertThrows(ValidationException.class, () -> {
      service.performSecurityCheck(
              requestServiceName, "any-group", "any-user");
    });
  }

  @Test
  void performSecurityCheck_returnsFalse_andAddsViolation_whenGroupIdNotRegistered() {
    // Given
    String requestServiceName = "svc-A";
    String missingGroupId = "missing-group";
    ClientOrg clientOrg = ClientOrg.parse("missing-group:u1");
    when(configurationService.getServiceName()).thenReturn(requestServiceName);
    when(configurationService.getClientOrgs())
            .thenReturn(Collections.singletonMap("existing-group", clientOrg));

    // When
    Exception exception = assertThrows(ValidationException.class, () -> {
      service.performSecurityCheck(
              requestServiceName, missingGroupId, "any-user");
    });
  }

  @Test
  void performSecurityCheck_returnsFalse_andAddsViolation_whenUserNotInGroup() {
    // Given
    String requestServiceName = "svc-A";
    String groupId = "group-1";
    String missingUserId = "user-missing";
    ClientOrg clientOrg = ClientOrg.parse("group-1:user-1,user-2");

    when(configurationService.getServiceName()).thenReturn(requestServiceName);
    when(configurationService.getClientOrgs()).thenReturn(
            Collections.singletonMap(groupId, clientOrg)
    );

    Exception exception = assertThrows(ValidationException.class, () -> {

      service.performSecurityCheck(requestServiceName, groupId, missingUserId);
    });
  }

}