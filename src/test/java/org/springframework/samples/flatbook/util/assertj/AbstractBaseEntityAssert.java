package org.springframework.samples.flatbook.util.assertj;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.util.Objects;
import org.springframework.samples.flatbook.model.BaseEntity;

/**
 * Abstract base class for {@link BaseEntity} specific assertions - Generated by CustomAssertionGenerator.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public abstract class AbstractBaseEntityAssert<S extends AbstractBaseEntityAssert<S, A>, A extends BaseEntity> extends AbstractObjectAssert<S, A> {

  /**
   * Creates a new <code>{@link AbstractBaseEntityAssert}</code> to make assertions on actual BaseEntity.
   * @param actual the BaseEntity we want to make assertions on.
   */
  protected AbstractBaseEntityAssert(A actual, Class<S> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the actual BaseEntity's id is equal to the given one.
   * @param id the given id to compare the actual BaseEntity's id to.
   * @return this assertion object.
   * @throws AssertionError - if the actual BaseEntity's id is not equal to the given one.
   */
  public S hasId(Integer id) {
    // check that actual BaseEntity we want to make assertions on is not null.
    isNotNull();

    // overrides the default error message with a more explicit one
    String assertjErrorMessage = "\nExpecting id of:\n  <%s>\nto be:\n  <%s>\nbut was:\n  <%s>";

    // null safe check
    Integer actualId = actual.getId();
    if (!Objects.areEqual(actualId, id)) {
      failWithMessage(assertjErrorMessage, actual, id, actualId);
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual BaseEntity is new.
   * @return this assertion object.
   * @throws AssertionError - if the actual BaseEntity is not new.
   */
  public S isNew() {
    // check that actual BaseEntity we want to make assertions on is not null.
    isNotNull();

    // check that property call/field access is true
    if (!actual.isNew()) {
      failWithMessage("\nExpecting that actual BaseEntity is new but is not.");
    }

    // return the current assertion for method chaining
    return myself;
  }

  /**
   * Verifies that the actual BaseEntity is not new.
   * @return this assertion object.
   * @throws AssertionError - if the actual BaseEntity is new.
   */
  public S isNotNew() {
    // check that actual BaseEntity we want to make assertions on is not null.
    isNotNull();

    // check that property call/field access is false
    if (actual.isNew()) {
      failWithMessage("\nExpecting that actual BaseEntity is not new but is.");
    }

    // return the current assertion for method chaining
    return myself;
  }

}
