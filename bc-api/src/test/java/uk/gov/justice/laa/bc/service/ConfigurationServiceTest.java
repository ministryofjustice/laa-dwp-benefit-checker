package uk.gov.justice.laa.bc.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.justice.laa.bc.model.ClientOrg;
import uk.gov.justice.laa.bc.model.Configuration;


@ExtendWith(MockitoExtension.class)
class ConfigurationServiceTest {

  private ConfigurationService service;

  // We’ll inject this mock into the private field "configuration"
  @Mock
  private Configuration configuration;

  // Mocks for javax.validation ConstraintValidatorContext
  @Mock
  private ConstraintValidatorContext validatorContext;

  @Mock
  private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

  @BeforeEach
  void setUp() throws Exception {
    service = new ConfigurationService();

    // Inject mock Configuration into the private field
    Field field = ConfigurationService.class.getDeclaredField("configuration");
    field.setAccessible(true);
    field.set(service, configuration);

    // By default, return a builder when a violation template is built (even if not used further)
    lenient().when(validatorContext.buildConstraintViolationWithTemplate(anyString()))
        .thenReturn(violationBuilder);
  }

  @Test
  void containsScopedPrincipal_returnsTrue_whenAllMatch() {
    // Given
    String requestServiceName = "svc-A";
    String requestGroupId = "group-1";

    Map<String, ClientOrg> clientOrgs = new HashMap<>();
    ClientOrg clientOrg = ClientOrg.parse("svc-A:user-123,user-456");
    clientOrgs.put(requestGroupId, clientOrg);

    String requestUserId = "user-123";

    // When
    when(configuration.getServiceName()).thenReturn(requestServiceName);
    when(configuration.getClientOrgs()).thenReturn(clientOrgs);
    boolean result = service.containsScopedPrincipal(validatorContext,
        requestServiceName, requestGroupId, requestUserId);

    // Then
    assertTrue(result);
    verify(validatorContext, never()).buildConstraintViolationWithTemplate(anyString());
  }

  @Test
  void containsScopedPrincipal_returnsFalse_andAddsViolation_whenServiceNameMismatch() {
    // Given
    String configuredServiceName = "svc-configured";
    String requestServiceName = "svc-request";
    when(configuration.getServiceName()).thenReturn(configuredServiceName);

    // When
    boolean result = service.containsScopedPrincipal(validatorContext,
        requestServiceName, "any-group", "any-user");

    // Then
    assertFalse(result);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(validatorContext).buildConstraintViolationWithTemplate(messageCaptor.capture());

    String msg = messageCaptor.getValue();
    assertTrue(msg.contains("Did not match request 'Service Context'"));
    assertTrue(msg.contains(requestServiceName));
  }

  @Test
  void containsScopedPrincipal_returnsFalse_andAddsViolation_whenGroupIdNotRegistered() {
    // Given
    String requestServiceName = "svc-A";
    String missingGroupId = "missing-group";
    ClientOrg clientOrg = ClientOrg.parse("missing-group:u1");
    when(configuration.getServiceName()).thenReturn(requestServiceName);
    when(configuration.getClientOrgs())
        .thenReturn(Collections.singletonMap("existing-group", clientOrg));

    // When
    boolean result = service.containsScopedPrincipal(validatorContext,
        requestServiceName, missingGroupId, "any-user");

    // Then
    assertFalse(result);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(validatorContext).buildConstraintViolationWithTemplate(messageCaptor.capture());

    String msg = messageCaptor.getValue();
    assertTrue(msg.contains("Did not match request 'Client Org. Id'"));
    assertTrue(msg.contains(missingGroupId));
    // Also nice to assert it mentions the registered keys
    assertTrue(msg.contains("existing-group"));
  }

  @Test
  void containsScopedPrincipal_returnsFalse_andAddsViolation_whenUserNotInGroup() {
    // Given
    String requestServiceName = "svc-A";
    String groupId = "group-1";
    String missingUserId = "user-missing";
    ClientOrg clientOrg = ClientOrg.parse("group-1:user-1,user-2");

    when(configuration.getServiceName()).thenReturn(requestServiceName);
    when(configuration.getClientOrgs()).thenReturn(
        Collections.singletonMap(groupId, clientOrg)
    );

    // When
    boolean result = service.containsScopedPrincipal(validatorContext,
        requestServiceName, groupId, missingUserId);

    // Then
    assertFalse(result);;

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(validatorContext).buildConstraintViolationWithTemplate(messageCaptor.capture());

    String msg = messageCaptor.getValue();
    assertTrue(msg.contains("Did not match request 'Client User Id'"));
    assertTrue(msg.contains(missingUserId));
    assertTrue(msg.contains(groupId));
    // And it should show some of the allowed user IDs
    assertTrue(msg.contains("user-1"));
    assertTrue(msg.contains("user-2"));
  }
}
