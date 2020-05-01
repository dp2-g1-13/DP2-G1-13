package org.springframework.samples.flatbook.util.assertj;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Objects;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.Flat;

/**
 * Abstract base class for {@link Advertisement} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractAdvertisementAssert<S extends AbstractAdvertisementAssert<S, A>, A extends Advertisement> extends AbstractBaseEntityAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractAdvertisementAssert}</code> to make assertions on actual Advertisement.
   * @param actual the Advertisement we want to make assertions on.
   */
  protected AbstractAdvertisementAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual Advertisement's creationDate is equal to the given one.
   * @param creationDate the given creationDate to compare the actual Advertisement's creationDate to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Advertisement's creationDate is not equal to the given one.
   */
  public S hasCreationDate(java.time.LocalDate creationDate) {
    // check that actual Advertisement we want to make assertions on is not null.
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
   * Verifies that the actual Advertisement's description is equal to the given one.
   * @param description the given description to compare the actual Advertisement's description to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Advertisement's description is not equal to the given one.
   */
  public S hasDescription(String description) {
    // check that actual Advertisement we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting description of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualDescription = actual.getDescription();
    if (!Objects.areEqual(actualDescription, description)) {
      failWithMessage(assertjErrorMessage, actual, description, actualDescription);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Advertisement's flat is equal to the given one.
   * @param flat the given flat to compare the actual Advertisement's flat to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Advertisement's flat is not equal to the given one.
   */
  public S hasFlat(Flat flat) {
    // check that actual Advertisement we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting flat of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Flat actualFlat = actual.getFlat();
    if (!Objects.areEqual(actualFlat, flat)) {
      failWithMessage(assertjErrorMessage, actual, flat, actualFlat);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Advertisement's pricePerMonth is equal to the given one.
   * @param pricePerMonth the given pricePerMonth to compare the actual Advertisement's pricePerMonth to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Advertisement's pricePerMonth is not equal to the given one.
   */
  public S hasPricePerMonth(Double pricePerMonth) {
    // check that actual Advertisement we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting pricePerMonth of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Double actualPricePerMonth = actual.getPricePerMonth();
    if (!Objects.areEqual(actualPricePerMonth, pricePerMonth)) {
      failWithMessage(assertjErrorMessage, actual, pricePerMonth, actualPricePerMonth);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Advertisement's pricePerMonth is close to the given value by less than the given offset.
   * <p>
   * If difference is equal to the offset value, assertion is considered successful.
   * @param pricePerMonth the value to compare the actual Advertisement's pricePerMonth to.
   * @param assertjOffset the given offset.
   * @return this assertion object.
   * @throws AssertionError - if the actual Advertisement's pricePerMonth is not close enough to the given value.
   */
  public S hasPricePerMonthCloseTo(Double pricePerMonth, Double assertjOffset) {
    // check that actual Advertisement we want to make assertions on is not null.
    isNotNull();

    Double actualPricePerMonth = actual.getPricePerMonth();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = String.format("\nExpecting pricePerMonth:\n  <%s>\nto be close to:\n  <%s>\nby less than <%s> but difference was <%s>",
                                               actualPricePerMonth, pricePerMonth, assertjOffset, Math.abs(pricePerMonth - actualPricePerMonth));

    // check
    Assertions.assertThat(actualPricePerMonth).overridingErrorMessage(assertjErrorMessage).isCloseTo(pricePerMonth, Assertions.within(assertjOffset));

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Advertisement's requirements is equal to the given one.
   * @param requirements the given requirements to compare the actual Advertisement's requirements to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Advertisement's requirements is not equal to the given one.
   */
  public S hasRequirements(String requirements) {
    // check that actual Advertisement we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting requirements of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualRequirements = actual.getRequirements();
    if (!Objects.areEqual(actualRequirements, requirements)) {
      failWithMessage(assertjErrorMessage, actual, requirements, actualRequirements);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Advertisement's title is equal to the given one.
   * @param title the given title to compare the actual Advertisement's title to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Advertisement's title is not equal to the given one.
   */
  public S hasTitle(String title) {
    // check that actual Advertisement we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting title of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualTitle = actual.getTitle();
    if (!Objects.areEqual(actualTitle, title)) {
      failWithMessage(assertjErrorMessage, actual, title, actualTitle);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
