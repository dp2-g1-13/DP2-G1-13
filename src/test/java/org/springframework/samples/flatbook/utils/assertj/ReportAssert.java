package org.springframework.samples.flatbook.utils.assertj;

import org.springframework.samples.flatbook.model.Report;

/**
 * {@link Report} specific assertions - Generated by CustomAssertionGenerator.
 *
 * Although this class is not final to allow Soft assertions proxy, if you wish to extend it,
 * extend {@link AbstractReportAssert} instead.
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class ReportAssert extends AbstractReportAssert<ReportAssert, Report> {

  /**
   * Creates a new <code>{@link ReportAssert}</code> to make assertions on actual Report.
   * @param actual the Report we want to make assertions on.
   */
  public ReportAssert(Report actual) {
    super(actual, ReportAssert.class);
  }

  /**
   * An entry point for ReportAssert to follow AssertJ standard <code>assertThat()</code> statements.<br>
   * With a static import, one can write directly: <code>assertThat(myReport)</code> and get specific assertion with code completion.
   * @param actual the Report we want to make assertions on.
   * @return a new <code>{@link ReportAssert}</code>
   */
  @org.assertj.core.util.CheckReturnValue
  public static ReportAssert assertThat(Report actual) {
    return new ReportAssert(actual);
  }
}