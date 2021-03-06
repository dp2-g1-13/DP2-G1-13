package org.springframework.samples.flatbook.utils.assertj;

import org.springframework.samples.flatbook.model.DBImage;

/**
 * {@link DBImage} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractDBImageAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class DBImageAssert extends AbstractDBImageAssert<DBImageAssert, DBImage> {

  /**
   * Creates a new <code>{@link DBImageAssert}</code> to make assertions on actual DBImage.
   * @param actual the DBImage we want to make assertions on.
   */
  public DBImageAssert(DBImage actual) {
    super(actual, DBImageAssert.class);
  }

  /**
   * An entry point for DBImageAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myDBImage)</code> and get specific assertion with code completion.
   * @param actual the DBImage we want to make assertions on.
   * @return a new <code>{@link DBImageAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static DBImageAssert assertThat(DBImage actual) {
    return new DBImageAssert(actual);
  }
}
