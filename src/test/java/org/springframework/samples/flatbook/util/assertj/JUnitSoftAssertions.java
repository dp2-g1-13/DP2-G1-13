package org.springframework.samples.flatbook.util.assertj;

/**
 * Like {@link SoftAssertions} but as a junit rule that takes care of calling
 * {@link SoftAssertions#assertAll() assertAll()} at the end of each test.
 * <p>
 * Example:
 * <pre><code class='java'> public class SoftlyTest {
 *
 *     &#064;Rule
 *     public final JUnitBDDSoftAssertions softly = new JUnitBDDSoftAssertions();
 *
 *     &#064;Test
 *     public void soft_bdd_assertions() throws Exception {
 *       softly.assertThat(1).isEqualTo(2);
 *       softly.assertThat(Lists.newArrayList(1, 2)).containsOnly(1, 2);
 *       // no need to call assertAll(), this is done automatically.
 *     }
 *  }</code></pre>
 */
@javax.annotation.Generated(value="assertj-assertions-generator")
public class JUnitSoftAssertions extends org.assertj.core.api.JUnitSoftAssertions {

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.AddressAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public AddressAssert assertThat(org.springframework.samples.flatbook.model.Address actual) {
    return proxy(AddressAssert.class, org.springframework.samples.flatbook.model.Address.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.AdvertisementAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public AdvertisementAssert assertThat(org.springframework.samples.flatbook.model.Advertisement actual) {
    return proxy(AdvertisementAssert.class, org.springframework.samples.flatbook.model.Advertisement.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.AuthoritiesAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public AuthoritiesAssert assertThat(org.springframework.samples.flatbook.model.Authorities actual) {
    return proxy(AuthoritiesAssert.class, org.springframework.samples.flatbook.model.Authorities.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.BaseEntityAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public BaseEntityAssert assertThat(org.springframework.samples.flatbook.model.BaseEntity actual) {
    return proxy(BaseEntityAssert.class, org.springframework.samples.flatbook.model.BaseEntity.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.DBImageAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public DBImageAssert assertThat(org.springframework.samples.flatbook.model.DBImage actual) {
    return proxy(DBImageAssert.class, org.springframework.samples.flatbook.model.DBImage.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.FlatAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public FlatAssert assertThat(org.springframework.samples.flatbook.model.Flat actual) {
    return proxy(FlatAssert.class, org.springframework.samples.flatbook.model.Flat.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.FlatReviewAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public FlatReviewAssert assertThat(org.springframework.samples.flatbook.model.FlatReview actual) {
    return proxy(FlatReviewAssert.class, org.springframework.samples.flatbook.model.FlatReview.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.HostAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public HostAssert assertThat(org.springframework.samples.flatbook.model.Host actual) {
    return proxy(HostAssert.class, org.springframework.samples.flatbook.model.Host.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.IndicatorsAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public IndicatorsAssert assertThat(org.springframework.samples.flatbook.model.Indicators actual) {
    return proxy(IndicatorsAssert.class, org.springframework.samples.flatbook.model.Indicators.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.MessageAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public MessageAssert assertThat(org.springframework.samples.flatbook.model.Message actual) {
    return proxy(MessageAssert.class, org.springframework.samples.flatbook.model.Message.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.PersonAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public PersonAssert assertThat(org.springframework.samples.flatbook.model.Person actual) {
    return proxy(PersonAssert.class, org.springframework.samples.flatbook.model.Person.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.ReportAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public ReportAssert assertThat(org.springframework.samples.flatbook.model.Report actual) {
    return proxy(ReportAssert.class, org.springframework.samples.flatbook.model.Report.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.RequestAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public RequestAssert assertThat(org.springframework.samples.flatbook.model.Request actual) {
    return proxy(RequestAssert.class, org.springframework.samples.flatbook.model.Request.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.ReviewAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public ReviewAssert assertThat(org.springframework.samples.flatbook.model.Review actual) {
    return proxy(ReviewAssert.class, org.springframework.samples.flatbook.model.Review.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.StatisticsAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public StatisticsAssert assertThat(org.springframework.samples.flatbook.model.Statistics actual) {
    return proxy(StatisticsAssert.class, org.springframework.samples.flatbook.model.Statistics.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.TaskAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public TaskAssert assertThat(org.springframework.samples.flatbook.model.Task actual) {
    return proxy(TaskAssert.class, org.springframework.samples.flatbook.model.Task.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.TenantAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public TenantAssert assertThat(org.springframework.samples.flatbook.model.Tenant actual) {
    return proxy(TenantAssert.class, org.springframework.samples.flatbook.model.Tenant.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.TenantReviewAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public TenantReviewAssert assertThat(org.springframework.samples.flatbook.model.TenantReview actual) {
    return proxy(TenantReviewAssert.class, org.springframework.samples.flatbook.model.TenantReview.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.UserAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public UserAssert assertThat(org.springframework.samples.flatbook.model.User actual) {
    return proxy(UserAssert.class, org.springframework.samples.flatbook.model.User.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.mappers.AdvertisementFormAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public AdvertisementFormAssert assertThat(org.springframework.samples.flatbook.model.mappers.AdvertisementForm actual) {
    return proxy(AdvertisementFormAssert.class, org.springframework.samples.flatbook.model.mappers.AdvertisementForm.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.mappers.PersonFormAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public PersonFormAssert assertThat(org.springframework.samples.flatbook.model.mappers.PersonForm actual) {
    return proxy(PersonFormAssert.class, org.springframework.samples.flatbook.model.mappers.PersonForm.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.mappers.RequestFormAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public RequestFormAssert assertThat(org.springframework.samples.flatbook.model.mappers.RequestForm actual) {
    return proxy(RequestFormAssert.class, org.springframework.samples.flatbook.model.mappers.RequestForm.class, actual);
  }

  /**
   * Creates a new "soft" instance of <code>{@link org.springframework.samples.flatbook.model.mappers.ReviewFormAssert}</code>.
   *
   * @param actual the actual value.
   * @return the created "soft" assertion object.
   */
  @org.assertj.core.util.CheckReturnValue
  public ReviewFormAssert assertThat(org.springframework.samples.flatbook.model.mappers.ReviewForm actual) {
    return proxy(ReviewFormAssert.class, org.springframework.samples.flatbook.model.mappers.ReviewForm.class, actual);
  }

}
