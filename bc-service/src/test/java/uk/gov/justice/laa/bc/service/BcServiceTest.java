package uk.gov.justice.laa.bc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.GetBenefitStatusExtResponse;
import uk.gov.dwp.common.cis.getbenefitstatusext.service._3.Item;
import uk.gov.justice.laa.bc.client.DwpClient;
import uk.gov.justice.laa.bc.utils.LogMonitoring;
import uk.gov.lsc.benefitchecker.service._1_0.api_1.BenefitCheckerRequest;

@ExtendWith(MockitoExtension.class)
class BcServiceTest {

  @InjectMocks
  private BcService bcService;

  @Mock
  private DwpClient dwpClient;

  @Test
  void perform() {
    BenefitCheckerRequest request = new BenefitCheckerRequest();
    request.setNino("nino");
    request.setSurname("surname");
    request.setDateOfAward("dateOfAward");
    request.setDateOfBirth("dateOfBirth");
    GetBenefitStatusExtResponse response = mock(GetBenefitStatusExtResponse.class);
    when(dwpClient.getBenefitStatusExtResponse(any()))
        .thenReturn(response);
    Item item = new Item();
    item.setId("id");
    item.setValue("value");
    List<Item> itemList = List.of(item);
    when(response.getItemList()).thenReturn(itemList);
    ListAppender<ILoggingEvent> listAppender
        = LogMonitoring.addListAppenderToLogger(BcService.class);
    assertNotNull(bcService.perform(request));
    List<ILoggingEvent> warningLogs = LogMonitoring.getLogsByLevel(listAppender, Level.INFO);
    assertEquals(1, warningLogs.size());
  }
}