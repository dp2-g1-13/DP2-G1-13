package org.springframework.samples.flatbook.utils.assertj;

import org.assertj.core.util.Objects;
import org.springframework.samples.flatbook.model.Person;
import org.springframework.samples.flatbook.model.Report;

/**
 * Abstract base class for {@link Report} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractReportAssert<S extends AbstractReportAssert<S, A>, A extends Report> extends AbstractBaseEntityAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractReportAssert}</code> to make assertions on actual Report.
   * @param actual the Report we want to make assertions on.
   */
  protected AbstractReportAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual Report's creationDate is equal to the given one.
   * @param creationDate the given creationDate to compare the actual Report's creationDate to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Report's creationDate is not equal to the given one.
   */
  public S hasCreationDate(java.time.LocalDate creationDate) {
    // check that actual Report we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting creationDate of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    java.time.LocalDate actualCreationDate = actual.getCreationDate();
    if (!Objects.areEqual(actualCreationDate, creationDate)) {
      failWithMessage(assertjErrorMessage, actual, creationDate, actualCreationDate);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Report's reason is equal to the given one.
   * @param reason the given reason to compare the actual Report's reason to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Report's reason is not equal to the given one.
   */
  public S hasReason(String reason) {
    // check that actual Report we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting reason of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualReason = actual.getReason();
    if (!Objects.areEqual(actualReason, reason)) {
      failWithMessage(assertjErrorMessage, actual, reason, actualReason);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Report's receiver is equal to the given one.
   * @param receiver the given receiver to compare the actual Report's receiver to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Report's receiver is not equal to the given one.
   */
  public S hasReceiver(Person receiver) {
    // check that actual Report we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting receiver of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Person actualReceiver = actual.getReceiver();
    if (!Objects.areEqual(actualReceiver, receiver)) {
      failWithMessage(assertjErrorMessage, actual, receiver, actualReceiver);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Report's sender is equal to the given one.
   * @param sender the given sender to compare the actual Report's sender to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Report's sender is not equal to the given one.
   */
  public S hasSender(Person sender) {
    // check that actual Report we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting sender of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Person actualSender = actual.getSender();
    if (!Objects.areEqual(actualSender, sender)) {
      failWithMessage(assertjErrorMessage, actual, sender, actualSender);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
