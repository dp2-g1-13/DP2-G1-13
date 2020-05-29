package org.springframework.samples.flatbook.utils.assertj;

import org.assertj.core.internal.Iterables;
import org.assertj.core.util.Objects;
import org.springframework.samples.flatbook.model.*;

/**
 * Abstract base class for {@link Flat} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractFlatAssert<S extends AbstractFlatAssert<S, A>, A extends Flat> extends AbstractBaseEntityAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractFlatAssert}</code> to make assertions on actual Flat.
   * @param actual the Flat we want to make assertions on.
   */
  protected AbstractFlatAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual Flat's address is equal to the given one.
   * @param address the given address to compare the actual Flat's address to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Flat's address is not equal to the given one.
   */
  public S hasAddress(Address address) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting address of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Address actualAddress = actual.getAddress();
    if (!Objects.areEqual(actualAddress, address)) {
      failWithMessage(assertjErrorMessage, actual, address, actualAddress);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's availableServices is equal to the given one.
   * @param availableServices the given availableServices to compare the actual Flat's availableServices to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Flat's availableServices is not equal to the given one.
   */
  public S hasAvailableServices(String availableServices) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting availableServices of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    String actualAvailableServices = actual.getAvailableServices();
    if (!Objects.areEqual(actualAvailableServices, availableServices)) {
      failWithMessage(assertjErrorMessage, actual, availableServices, actualAvailableServices);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's description is equal to the given one.
   * @param description the given description to compare the actual Flat's description to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Flat's description is not equal to the given one.
   */
  public S hasDescription(String description) {
    // check that actual Flat we want to make assertions on is not null.
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
   * Verifies that the actual Flat's flatReviews contains the given FlatReview elements.
   * @param flatReviews the given elements that should be contained in actual Flat's flatReviews.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's flatReviews does not contain all given FlatReview elements.
   */
  public S hasFlatReviews(FlatReview... flatReviews) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given FlatReview varargs is not null.
    if (flatReviews == null) failWithMessage("Expecting flatReviews parameter not to be null.");

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContains(info, actual.getFlatReviews(), flatReviews);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's flatReviews contains the given FlatReview elements in Collection.
   * @param flatReviews the given elements that should be contained in actual Flat's flatReviews.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's flatReviews does not contain all given FlatReview elements.
   */
  public S hasFlatReviews(java.util.Collection<? extends FlatReview> flatReviews) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given FlatReview collection is not null.
    if (flatReviews == null) {
      failWithMessage("Expecting flatReviews parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContains(info, actual.getFlatReviews(), flatReviews.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's flatReviews contains <b>only</b> the given FlatReview elements and nothing else in whatever order.
   * @param flatReviews the given elements that should be contained in actual Flat's flatReviews.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's flatReviews does not contain all given FlatReview elements.
   */
  public S hasOnlyFlatReviews(FlatReview... flatReviews) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given FlatReview varargs is not null.
    if (flatReviews == null) failWithMessage("Expecting flatReviews parameter not to be null.");

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContainsOnly(info, actual.getFlatReviews(), flatReviews);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's flatReviews contains <b>only</b> the given FlatReview elements in Collection and nothing else in whatever order.
   * @param flatReviews the given elements that should be contained in actual Flat's flatReviews.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's flatReviews does not contain all given FlatReview elements.
   */
  public S hasOnlyFlatReviews(java.util.Collection<? extends FlatReview> flatReviews) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given FlatReview collection is not null.
    if (flatReviews == null) {
      failWithMessage("Expecting flatReviews parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContainsOnly(info, actual.getFlatReviews(), flatReviews.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's flatReviews does not contain the given FlatReview elements.
   *
   * @param flatReviews the given elements that should not be in actual Flat's flatReviews.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's flatReviews contains any given FlatReview elements.
   */
  public S doesNotHaveFlatReviews(FlatReview... flatReviews) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given FlatReview varargs is not null.
    if (flatReviews == null) failWithMessage("Expecting flatReviews parameter not to be null.");

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Iterables.instance().assertDoesNotContain(info, actual.getFlatReviews(), flatReviews);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's flatReviews does not contain the given FlatReview elements in Collection.
   *
   * @param flatReviews the given elements that should not be in actual Flat's flatReviews.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's flatReviews contains any given FlatReview elements.
   */
  public S doesNotHaveFlatReviews(java.util.Collection<? extends FlatReview> flatReviews) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given FlatReview collection is not null.
    if (flatReviews == null) {
      failWithMessage("Expecting flatReviews parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Iterables.instance().assertDoesNotContain(info, actual.getFlatReviews(), flatReviews.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat has no flatReviews.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's flatReviews is not empty.
   */
  public S hasNoFlatReviews() {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // we override the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting :\n  <%s>\nnot to have flatReviews but had :\n  <%s>";

    // check
    if (actual.getFlatReviews().iterator().hasNext()) {
      failWithMessage(assertjErrorMessage, actual, actual.getFlatReviews());
    }

    // return the current assertion for method chaining
    return myself;
  }


  /**
   * Verifies that the actual Flat's images contains the given DBImage elements.
   * @param images the given elements that should be contained in actual Flat's images.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's images does not contain all given DBImage elements.
   */
  public S hasImages(DBImage... images) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given DBImage varargs is not null.
    if (images == null) failWithMessage("Expecting images parameter not to be null.");

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContains(info, actual.getImages(), images);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's images contains the given DBImage elements in Collection.
   * @param images the given elements that should be contained in actual Flat's images.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's images does not contain all given DBImage elements.
   */
  public S hasImages(java.util.Collection<? extends DBImage> images) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given DBImage collection is not null.
    if (images == null) {
      failWithMessage("Expecting images parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContains(info, actual.getImages(), images.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's images contains <b>only</b> the given DBImage elements and nothing else in whatever order.
   * @param images the given elements that should be contained in actual Flat's images.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's images does not contain all given DBImage elements.
   */
  public S hasOnlyImages(DBImage... images) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given DBImage varargs is not null.
    if (images == null) failWithMessage("Expecting images parameter not to be null.");

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContainsOnly(info, actual.getImages(), images);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's images contains <b>only</b> the given DBImage elements in Collection and nothing else in whatever order.
   * @param images the given elements that should be contained in actual Flat's images.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's images does not contain all given DBImage elements.
   */
  public S hasOnlyImages(java.util.Collection<? extends DBImage> images) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given DBImage collection is not null.
    if (images == null) {
      failWithMessage("Expecting images parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContainsOnly(info, actual.getImages(), images.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's images does not contain the given DBImage elements.
   *
   * @param images the given elements that should not be in actual Flat's images.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's images contains any given DBImage elements.
   */
  public S doesNotHaveImages(DBImage... images) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given DBImage varargs is not null.
    if (images == null) failWithMessage("Expecting images parameter not to be null.");

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Iterables.instance().assertDoesNotContain(info, actual.getImages(), images);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's images does not contain the given DBImage elements in Collection.
   *
   * @param images the given elements that should not be in actual Flat's images.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's images contains any given DBImage elements.
   */
  public S doesNotHaveImages(java.util.Collection<? extends DBImage> images) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given DBImage collection is not null.
    if (images == null) {
      failWithMessage("Expecting images parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Iterables.instance().assertDoesNotContain(info, actual.getImages(), images.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat has no images.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's images is not empty.
   */
  public S hasNoImages() {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // we override the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting :\n  <%s>\nnot to have images but had :\n  <%s>";

    // check
    if (actual.getImages().iterator().hasNext()) {
      failWithMessage(assertjErrorMessage, actual, actual.getImages());
    }

    // return the current assertion for method chaining
    return myself;
  }


  /**
   * Verifies that the actual Flat's numberBaths is equal to the given one.
   * @param numberBaths the given numberBaths to compare the actual Flat's numberBaths to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Flat's numberBaths is not equal to the given one.
   */
  public S hasNumberBaths(Integer numberBaths) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting numberBaths of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Integer actualNumberBaths = actual.getNumberBaths();
    if (!Objects.areEqual(actualNumberBaths, numberBaths)) {
      failWithMessage(assertjErrorMessage, actual, numberBaths, actualNumberBaths);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's numberRooms is equal to the given one.
   * @param numberRooms the given numberRooms to compare the actual Flat's numberRooms to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Flat's numberRooms is not equal to the given one.
   */
  public S hasNumberRooms(Integer numberRooms) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting numberRooms of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Integer actualNumberRooms = actual.getNumberRooms();
    if (!Objects.areEqual(actualNumberRooms, numberRooms)) {
      failWithMessage(assertjErrorMessage, actual, numberRooms, actualNumberRooms);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's requests contains the given Request elements.
   * @param requests the given elements that should be contained in actual Flat's requests.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's requests does not contain all given Request elements.
   */
  public S hasRequests(Request... requests) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Request varargs is not null.
    if (requests == null) failWithMessage("Expecting requests parameter not to be null.");

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContains(info, actual.getRequests(), requests);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's requests contains the given Request elements in Collection.
   * @param requests the given elements that should be contained in actual Flat's requests.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's requests does not contain all given Request elements.
   */
  public S hasRequests(java.util.Collection<? extends Request> requests) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Request collection is not null.
    if (requests == null) {
      failWithMessage("Expecting requests parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContains(info, actual.getRequests(), requests.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's requests contains <b>only</b> the given Request elements and nothing else in whatever order.
   * @param requests the given elements that should be contained in actual Flat's requests.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's requests does not contain all given Request elements.
   */
  public S hasOnlyRequests(Request... requests) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Request varargs is not null.
    if (requests == null) failWithMessage("Expecting requests parameter not to be null.");

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContainsOnly(info, actual.getRequests(), requests);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's requests contains <b>only</b> the given Request elements in Collection and nothing else in whatever order.
   * @param requests the given elements that should be contained in actual Flat's requests.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's requests does not contain all given Request elements.
   */
  public S hasOnlyRequests(java.util.Collection<? extends Request> requests) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Request collection is not null.
    if (requests == null) {
      failWithMessage("Expecting requests parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContainsOnly(info, actual.getRequests(), requests.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's requests does not contain the given Request elements.
   *
   * @param requests the given elements that should not be in actual Flat's requests.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's requests contains any given Request elements.
   */
  public S doesNotHaveRequests(Request... requests) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Request varargs is not null.
    if (requests == null) failWithMessage("Expecting requests parameter not to be null.");

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Iterables.instance().assertDoesNotContain(info, actual.getRequests(), requests);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's requests does not contain the given Request elements in Collection.
   *
   * @param requests the given elements that should not be in actual Flat's requests.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's requests contains any given Request elements.
   */
  public S doesNotHaveRequests(java.util.Collection<? extends Request> requests) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Request collection is not null.
    if (requests == null) {
      failWithMessage("Expecting requests parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Iterables.instance().assertDoesNotContain(info, actual.getRequests(), requests.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat has no requests.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's requests is not empty.
   */
  public S hasNoRequests() {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // we override the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting :\n  <%s>\nnot to have requests but had :\n  <%s>";

    // check
    if (actual.getRequests().iterator().hasNext()) {
      failWithMessage(assertjErrorMessage, actual, actual.getRequests());
    }

    // return the current assertion for method chaining
    return myself;
  }


  /**
   * Verifies that the actual Flat's squareMeters is equal to the given one.
   * @param squareMeters the given squareMeters to compare the actual Flat's squareMeters to.
   * @return this assertion object.
   * @throws AssertionError - if the actual Flat's squareMeters is not equal to the given one.
   */
  public S hasSquareMeters(Integer squareMeters) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting squareMeters of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Integer actualSquareMeters = actual.getSquareMeters();
    if (!Objects.areEqual(actualSquareMeters, squareMeters)) {
      failWithMessage(assertjErrorMessage, actual, squareMeters, actualSquareMeters);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's tenants contains the given Tenant elements.
   * @param tenants the given elements that should be contained in actual Flat's tenants.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's tenants does not contain all given Tenant elements.
   */
  public S hasTenants(Tenant... tenants) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Tenant varargs is not null.
    if (tenants == null) failWithMessage("Expecting tenants parameter not to be null.");

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContains(info, actual.getTenants(), tenants);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's tenants contains the given Tenant elements in Collection.
   * @param tenants the given elements that should be contained in actual Flat's tenants.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's tenants does not contain all given Tenant elements.
   */
  public S hasTenants(java.util.Collection<? extends Tenant> tenants) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Tenant collection is not null.
    if (tenants == null) {
      failWithMessage("Expecting tenants parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContains(info, actual.getTenants(), tenants.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's tenants contains <b>only</b> the given Tenant elements and nothing else in whatever order.
   * @param tenants the given elements that should be contained in actual Flat's tenants.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's tenants does not contain all given Tenant elements.
   */
  public S hasOnlyTenants(Tenant... tenants) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Tenant varargs is not null.
    if (tenants == null) failWithMessage("Expecting tenants parameter not to be null.");

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContainsOnly(info, actual.getTenants(), tenants);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's tenants contains <b>only</b> the given Tenant elements in Collection and nothing else in whatever order.
   * @param tenants the given elements that should be contained in actual Flat's tenants.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's tenants does not contain all given Tenant elements.
   */
  public S hasOnlyTenants(java.util.Collection<? extends Tenant> tenants) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Tenant collection is not null.
    if (tenants == null) {
      failWithMessage("Expecting tenants parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message, to set another message call: info.overridingErrorMessage("my error message");
    Iterables.instance().assertContainsOnly(info, actual.getTenants(), tenants.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's tenants does not contain the given Tenant elements.
   *
   * @param tenants the given elements that should not be in actual Flat's tenants.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's tenants contains any given Tenant elements.
   */
  public S doesNotHaveTenants(Tenant... tenants) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Tenant varargs is not null.
    if (tenants == null) failWithMessage("Expecting tenants parameter not to be null.");

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Iterables.instance().assertDoesNotContain(info, actual.getTenants(), tenants);

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat's tenants does not contain the given Tenant elements in Collection.
   *
   * @param tenants the given elements that should not be in actual Flat's tenants.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's tenants contains any given Tenant elements.
   */
  public S doesNotHaveTenants(java.util.Collection<? extends Tenant> tenants) {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // check that given Tenant collection is not null.
    if (tenants == null) {
      failWithMessage("Expecting tenants parameter not to be null.");
      return myself; // to fool Eclipse "Null pointer access" warning on toArray.
    }

    // check with standard error message (use overridingErrorMessage before contains to set your own message).
    Iterables.instance().assertDoesNotContain(info, actual.getTenants(), tenants.toArray());

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual Flat has no tenants.
   * @return this assertion object.
   * @throws AssertionError if the actual Flat's tenants is not empty.
   */
  public S hasNoTenants() {
    // check that actual Flat we want to make assertions on is not null.
    isNotNull();

    // we override the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting :\n  <%s>\nnot to have tenants but had :\n  <%s>";

    // check
    if (actual.getTenants().iterator().hasNext()) {
      failWithMessage(assertjErrorMessage, actual, actual.getTenants());
    }

    // return the current assertion for method chaining
    return myself;
  }


}
