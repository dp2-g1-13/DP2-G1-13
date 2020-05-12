package org.springframework.samples.flatbook.util.assertj;

import org.springframework.samples.flatbook.model.Statistics;

/**
 * {@link Statistics} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractStatisticsAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class StatisticsAssert extends AbstractStatisticsAssert<StatisticsAssert, Statistics> {

  /**
   * Creates a new <code>{@link StatisticsAssert}</code> to make assertions on actual Statistics.
   * @param actual the Statistics we want to make assertions on.
   */
  public StatisticsAssert(Statistics actual) {
    super(actual, StatisticsAssert.class);
  }

  /**
   * An entry point for StatisticsAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myStatistics)</code> and get specific assertion with code completion.
   * @param actual the Statistics we want to make assertions on.
   * @return a new <code>{@link StatisticsAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static StatisticsAssert assertThat(Statistics actual) {
    return new StatisticsAssert(actual);
  }
}
