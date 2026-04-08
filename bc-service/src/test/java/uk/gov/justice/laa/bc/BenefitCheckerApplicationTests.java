package uk.gov.justice.laa.bc;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BenefitCheckerApplicationTests {

  @Test
  void main_whenInvoked_runsSpringApplication() {

    // Arrange
    String[] args = {"testArg1", "testArg2"};

    try (MockedStatic<SpringApplication> mockedSpringApplication
                 = Mockito.mockStatic(SpringApplication.class)) {
      mockedSpringApplication.when(() -> SpringApplication.run(
                      any(Class.class), any(String[].class)))
              .thenReturn(null); // Or a mock ApplicationContext if needed for further verification

      // Act
      BenefitCheckerApplication.main(args);

      // Assert
      mockedSpringApplication.verify(() -> SpringApplication.run(
              eq(BenefitCheckerApplication.class), eq(args)), times(1));
    }
  }

  @Test
  void application_constructor_doesNotThrowException() {

    // Arrange & Act
    BenefitCheckerApplication application = new BenefitCheckerApplication();

    // Assert
    org.assertj.core.api.Assertions.assertThat(application).isNotNull();
  }
}
