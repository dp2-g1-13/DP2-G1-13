package org.springframework.samples.flatbook.utils.assertj;

import org.springframework.samples.flatbook.model.Indicators;

/**
 * {@link Indicators} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractIndicatorsAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class IndicatorsAssert extends AbstractIndicatorsAssert<IndicatorsAssert, Indicators> {

  /**
   * Creates a new <code>{@link IndicatorsAssert}</code> to make assertions on actual Indicators.
   * @param actual the Indicators we want to make assertions on.
   */
  public IndicatorsAssert(Indicators actual) {
    super(actual, IndicatorsAssert.class);
  }

  /**
   * An entry point for IndicatorsAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myIndicators)</code> and get specific assertion with code completion.
   * @param actual the Indicators we want to make assertions on.
   * @return a new <code>{@link IndicatorsAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static IndicatorsAssert assertThat(Indicators actual) {
    return new IndicatorsAssert(actual);
  }
}
