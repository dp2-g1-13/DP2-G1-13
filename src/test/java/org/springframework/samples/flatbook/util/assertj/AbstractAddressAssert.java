package org.springframework.samples.flatbook.util.assertj;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Objects;
import org.springframework.samples.flatbook.model.Address;

/**
 * Abstract base class for {@link Address} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractAddressAssert<S extends AbstractAddressAssert<S, A>, A extends Address> extends AbstractBaseEntityAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractAddressAssert}</code> to make assertions on actual Address.
   * @param actual the Address we want to make assertions on.
   */
  protected AbstractAddressAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual Address's address is equal to the given one.
   * @param address the given address to compare the actual Address's address to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Address's address is not equal to the given one.
   */
  public S hasAddress(String address) {
    // check that actual Address we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting address of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualAddress = actual.getLocation();
    if (!Objects.areEqual(actualAddress, address)) {
      failWithMessage(assertjErrorMessage, actual, address, actualAddress);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Address's city is equal to the given one.
   * @param city the given city to compare the actual Address's city to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Address's city is not equal to the given one.
   */
  public S hasCity(String city) {
    // check that actual Address we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting city of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualCity = actual.getCity();
    if (!Objects.areEqual(actualCity, city)) {
      failWithMessage(assertjErrorMessage, actual, city, actualCity);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Address's country is equal to the given one.
   * @param country the given country to compare the actual Address's country to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Address's country is not equal to the given one.
   */
  public S hasCountry(String country) {
    // check that actual Address we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting country of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualCountry = actual.getCountry();
    if (!Objects.areEqual(actualCountry, country)) {
      failWithMessage(assertjErrorMessage, actual, country, actualCountry);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Address's latitude is equal to the given one.
   * @param latitude the given latitude to compare the actual Address's latitude to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Address's latitude is not equal to the given one.
   */
  public S hasLatitude(Double latitude) {
    // check that actual Address we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting latitude of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Double actualLatitude = actual.getLatitude();
    if (!Objects.areEqual(actualLatitude, latitude)) {
      failWithMessage(assertjErrorMessage, actual, latitude, actualLatitude);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Address's latitude is close to the given value by less than the given offset.
   * <p>
   * If difference is equal to the offset value, assertion is considered successful.
   * @param latitude the value to compare the actual Address's latitude to.
   * @param assertjOffset the given offset.
   * @return this assertion object.
   * @throws AssertionError - if the actual Address's latitude is not close enough to the given value.
   */
  public S hasLatitudeCloseTo(Double latitude, Double assertjOffset) {
    // check that actual Address we want to make assertions on is not null.
    isNotNull();

    Double actualLatitude = actual.getLatitude();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = String.format("\nExpecting latitude:\n  <%s>\nto be close to:\n  <%s>\nby less than <%s> but difference was <%s>",
                                               actualLatitude, latitude, assertjOffset, Math.abs(latitude - actualLatitude));

    // check
    Assertions.assertThat(actualLatitude).overridingErrorMessage(assertjErrorMessage).isCloseTo(latitude, Assertions.within(assertjOffset));

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Address's longitude is equal to the given one.
   * @param longitude the given longitude to compare the actual Address's longitude to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Address's longitude is not equal to the given one.
   */
  public S hasLongitude(Double longitude) {
    // check that actual Address we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting longitude of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Double actualLongitude = actual.getLongitude();
    if (!Objects.areEqual(actualLongitude, longitude)) {
      failWithMessage(assertjErrorMessage, actual, longitude, actualLongitude);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Address's longitude is close to the given value by less than the given offset.
   * <p>
   * If difference is equal to the offset value, assertion is considered successful.
   * @param longitude the value to compare the actual Address's longitude to.
   * @param assertjOffset the given offset.
   * @return this assertion object.
   * @throws AssertionError - if the actual Address's longitude is not close enough to the given value.
   */
  public S hasLongitudeCloseTo(Double longitude, Double assertjOffset) {
    // check that actual Address we want to make assertions on is not null.
    isNotNull();

    Double actualLongitude = actual.getLongitude();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = String.format("\nExpecting longitude:\n  <%s>\nto be close to:\n  <%s>\nby less than <%s> but difference was <%s>",
                                               actualLongitude, longitude, assertjOffset, Math.abs(longitude - actualLongitude));

    // check
    Assertions.assertThat(actualLongitude).overridingErrorMessage(assertjErrorMessage).isCloseTo(longitude, Assertions.within(assertjOffset));

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Address's postalCode is equal to the given one.
   * @param postalCode the given postalCode to compare the actual Address's postalCode to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Address's postalCode is not equal to the given one.
   */
  public S hasPostalCode(String postalCode) {
    // check that actual Address we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting postalCode of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualPostalCode = actual.getPostalCode();
    if (!Objects.areEqual(actualPostalCode, postalCode)) {
      failWithMessage(assertjErrorMessage, actual, postalCode, actualPostalCode);
    }

    // return the current assertion for method chaining
    return myself;
  }

}
