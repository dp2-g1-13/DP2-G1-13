package org.springframework.samples.flatbook.utils.assertj;

import org.springframework.samples.flatbook.model.Request;

/**
 * {@link Request} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractRequestAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class RequestAssert extends AbstractRequestAssert<RequestAssert, Request> {

  /**
   * Creates a new <code>{@link RequestAssert}</code> to make assertions on actual Request.
   * @param actual the Request we want to make assertions on.
   */
  public RequestAssert(Request actual) {
    super(actual, RequestAssert.class);
  }

  /**
   * An entry point for RequestAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myRequest)</code> and get specific assertion with code completion.
   * @param actual the Request we want to make assertions on.
   * @return a new <code>{@link RequestAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static RequestAssert assertThat(Request actual) {
    return new RequestAssert(actual);
  }
}
