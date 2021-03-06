package org.springframework.samples.flatbook.utils.assertj;

import org.springframework.samples.flatbook.model.TenantReview;

/**
 * {@link TenantReview} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractTenantReviewAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class TenantReviewAssert extends AbstractTenantReviewAssert<TenantReviewAssert, TenantReview> {

  /**
   * Creates a new <code>{@link TenantReviewAssert}</code> to make assertions on actual TenantReview.
   * @param actual the TenantReview we want to make assertions on.
   */
  public TenantReviewAssert(TenantReview actual) {
    super(actual, TenantReviewAssert.class);
  }

  /**
   * An entry point for TenantReviewAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myTenantReview)</code> and get specific assertion with code completion.
   * @param actual the TenantReview we want to make assertions on.
   * @return a new <code>{@link TenantReviewAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static TenantReviewAssert assertThat(TenantReview actual) {
    return new TenantReviewAssert(actual);
  }
}
