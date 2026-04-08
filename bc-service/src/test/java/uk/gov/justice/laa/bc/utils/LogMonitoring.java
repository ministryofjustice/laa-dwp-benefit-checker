package uk.gov.justice.laa.bc.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Some utility functions to capture and verify logs when writing unit tests.
 */
public class LogMonitoring {

  /**
   * addListAppenderToLogger.
   *
   * @param testedClass testedClass
   * @return ListAppender ListAppender
   */
  public static ListAppender<ILoggingEvent> addListAppenderToLogger(Class<?> testedClass) {
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    ((Logger) LoggerFactory.getLogger(testedClass)).addAppender(listAppender);
    return listAppender;
  }

  /**
   * getLogsByCriteria.
   *
   * @param listAppender ListAppender
   * @param criteria     List
   * @return List
   */
  public static List<ILoggingEvent> getLogsByCriteria(ListAppender<ILoggingEvent> listAppender,
                                                      Predicate<ILoggingEvent> criteria) {
    return listAppender.list.stream()
            .filter(criteria)
            .collect(Collectors.toList());
  }

  public static List<ILoggingEvent> getLogsByLevel(ListAppender<ILoggingEvent> listAppender,
                                                   Level level) {
    return getLogsByCriteria(listAppender,
            logEvent -> logEvent.getLevel().equals(level));
  }
}
