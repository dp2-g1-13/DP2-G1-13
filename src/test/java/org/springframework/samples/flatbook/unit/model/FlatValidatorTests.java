
package org.springframework.samples.flatbook.unit.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.Request;
import org.springframework.samples.flatbook.model.enums.RequestStatus;
import org.springframework.samples.flatbook.web.validators.FlatValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class FlatValidatorTests {

	public static Address	address;
	public static DBImage	image1;
	public static DBImage	image2;
	public static DBImage	image3;
	public static DBImage	image4;
	public static DBImage	image5;
	public static DBImage	image6;


	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@BeforeAll
	static void instantiateAddressAndImages() {
		FlatValidatorTests.address = new Address();
		FlatValidatorTests.address.setLocation("Av. de la República Argentina 72");
		FlatValidatorTests.address.setCity("Sevilla");
		FlatValidatorTests.address.setCountry("Spain");
		FlatValidatorTests.address.setPostalCode("41011");

		FlatValidatorTests.image1 = new DBImage();
		FlatValidatorTests.image1.setFilename("abc");
		FlatValidatorTests.image1.setFileType("image/png");
		FlatValidatorTests.image1.setData(new byte[] {
			12, 45, 8, 103
		});

		FlatValidatorTests.image2 = new DBImage();
		FlatValidatorTests.image2.setFilename("def");
		FlatValidatorTests.image2.setFileType("image/png");
		FlatValidatorTests.image2.setData(new byte[] {
			122, 124, 21, 4
		});

		FlatValidatorTests.image3 = new DBImage();
		FlatValidatorTests.image3.setFilename("ghi");
		FlatValidatorTests.image3.setFileType("image/png");
		FlatValidatorTests.image3.setData(new byte[] {
			69, 81, 0, 22
		});

		FlatValidatorTests.image4 = new DBImage();
		FlatValidatorTests.image4.setFilename("jkl");
		FlatValidatorTests.image4.setFileType("image/png");
		FlatValidatorTests.image4.setData(new byte[] {
			127, 5, 72, 5
		});

		FlatValidatorTests.image5 = new DBImage();
		FlatValidatorTests.image5.setFilename("mno");
		FlatValidatorTests.image5.setFileType("image/png");
		FlatValidatorTests.image5.setData(new byte[] {
			8, 9, 10, 11
		});

		FlatValidatorTests.image6 = new DBImage();
		FlatValidatorTests.image6.setFilename("pqr");
		FlatValidatorTests.image6.setFileType("image/png");
		FlatValidatorTests.image6.setData(new byte[] {
			12, 13, 14, 15
		});

		Request request = new Request();
		request.setStatus(RequestStatus.PENDING);
		request.setDescription("This is a sample description");
		request.setCreationDate(LocalDateTime.now());
		request.setStartDate(LocalDate.MAX);
		request.setFinishDate(LocalDate.MAX);
	}

	@Test
	void shouldNotValidateWhenDescriptionEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
		Assert.assertTrue("Expecting propertyPath to be equal to description but was not.",
			constraintViolations.stream().allMatch(x -> x.getPropertyPath().toString().equals("description")));
		Assert.assertTrue("Expecting one of the constraint violation messages to be equal to 'must not be blank', but was not.",
			constraintViolations.stream().anyMatch(x -> x.getMessage().equals("must not be blank")));
	}

	@Test
	void shouldNotValidateWhenDescriptionSizeLessThan30() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("sample description w 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
		Assertions.assertThat(violation.getMessage()).isEqualTo("size must be between 30 and 10000");
	}

	@Test
	void shouldNotValidateWhenSquareMetersNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(null);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("squareMeters");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");
	}

	@ParameterizedTest
	@ValueSource(ints = {
		-1, 0
	})
	void shouldNotValidateWhenSquareMetersNegativeOrZero(final int squareMeters) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(squareMeters);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("squareMeters");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be greater than 0");
	}

	@Test
	void shouldNotValidateWhenNumberRoomsNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(null);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("numberRooms");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");
	}

	@ParameterizedTest
	@ValueSource(ints = {
		-1, 0
	})
	void shouldNotValidateWhenNumberRoomsNegativeOrZero(final int rooms) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(rooms);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("numberRooms");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be greater than 0");
	}

	@Test
	void shouldNotValidateWhenNumberBathsNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(null);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("numberBaths");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");
	}

	@ParameterizedTest
	@ValueSource(ints = {
		-1, 0
	})
	void shouldNotValidateWhenNumberBathsNegativeOrZero(final int baths) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(baths);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("numberBaths");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be greater than 0");
	}

	@Test
	void shouldNotValidateWhenAvailableServicesEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("availableServices");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be blank");
	}

	@Test
	void shouldNotValidateWhenAddressNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(null);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");
	}

	@Test
	void shouldNotValidateWhenImagesNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		flat.setImages(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Flat> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("images");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");
	}

	@Test
	void shouldNotValidateWhenAddingImageWithEmptyFilename() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);

		DBImage badImage = new DBImage();
		badImage.setFilename("");
		badImage.setFileType("image/jpg");
		badImage.setData("image".getBytes());

		images.add(badImage);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);
		Assertions.assertThat(constraintViolations).isEmpty();

		org.springframework.validation.Validator customValidator = new FlatValidator();
		Errors errors = new BeanPropertyBindingResult(flat, flat.getClass().toString());
		customValidator.validate(flat, errors);

		Assertions.assertThat(errors.getAllErrors().size()).isEqualTo(1);
		FieldError violation = errors.getFieldErrors().iterator().next();
		Assertions.assertThat(violation.getField()).isEqualTo("images");
		Assertions.assertThat(violation.getDefaultMessage()).isEqualTo("The filename of the image must not be empty");
	}

	@Test
	void shouldNotValidateWhenAddingImageWithEmptyFileType() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);

		DBImage badImage = new DBImage();
		badImage.setFilename("image.jpg");
		badImage.setFileType("");
		badImage.setData("image".getBytes());

		images.add(badImage);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);
		Assertions.assertThat(constraintViolations).isEmpty();

		org.springframework.validation.Validator customValidator = new FlatValidator();
		Errors errors = new BeanPropertyBindingResult(flat, flat.getClass().toString());
		customValidator.validate(flat, errors);

		Assertions.assertThat(errors.getAllErrors().size()).isEqualTo(1);
		FieldError violation = errors.getFieldErrors().iterator().next();
		Assertions.assertThat(violation.getField()).isEqualTo("images");
		Assertions.assertThat(violation.getDefaultMessage()).isEqualTo("The file type of the image must not be empty");
	}

	@Test
	void shouldNotValidateWhenAddingImageWhenFileTypeIsNotImage() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);

		DBImage badImage = new DBImage();
		badImage.setFilename("image.jpg");
		badImage.setFileType("application/json");
		badImage.setData("image".getBytes());

		images.add(badImage);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);
		Assertions.assertThat(constraintViolations).isEmpty();

		org.springframework.validation.Validator customValidator = new FlatValidator();
		Errors errors = new BeanPropertyBindingResult(flat, flat.getClass().toString());
		customValidator.validate(flat, errors);

		Assertions.assertThat(errors.getAllErrors().size()).isEqualTo(1);
		FieldError violation = errors.getFieldErrors().iterator().next();
		Assertions.assertThat(violation.getField()).isEqualTo("images");
		Assertions.assertThat(violation.getDefaultMessage()).isEqualTo("The file type must be an image");
	}

	@Test
	void shouldNotValidateWhenAddingImageWithEmptyData() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription("this is a sample description with more than 29 chars");
		flat.setSquareMeters(90);
		flat.setNumberRooms(2);
		flat.setNumberBaths(2);
		flat.setAvailableServices("Wifi and cable TV");
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);

		DBImage badImage = new DBImage();
		badImage.setFilename("image.jpg");
		badImage.setFileType("image/jpg");
		badImage.setData(new byte[] {});

		images.add(badImage);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);
		Assertions.assertThat(constraintViolations).isEmpty();

		org.springframework.validation.Validator customValidator = new FlatValidator();
		Errors errors = new BeanPropertyBindingResult(flat, flat.getClass().toString());
		customValidator.validate(flat, errors);

		Assertions.assertThat(errors.getAllErrors().size()).isEqualTo(1);
		FieldError violation = errors.getFieldErrors().iterator().next();
		Assertions.assertThat(violation.getField()).isEqualTo("images");
		Assertions.assertThat(violation.getDefaultMessage()).isEqualTo("The data of the image must not be empty");
	}

	@ParameterizedTest
	@CsvSource({
		"sample description w 30 charss, 1, 1, 1, a", "this is another sample description with more than 30 chars, 90, 2, 2, Wifi and cable TV"
	})
	void shouldValidate(final String description, final Integer squareMeters, final Integer rooms, final Integer baths,
		final String availableServices) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Flat flat = new Flat();
		flat.setDescription(description);
		flat.setSquareMeters(squareMeters);
		flat.setNumberRooms(rooms);
		flat.setNumberBaths(baths);
		flat.setAvailableServices(availableServices);
		flat.setAddress(FlatValidatorTests.address);
		Set<DBImage> images = new HashSet<>();
		images.add(FlatValidatorTests.image1);
		images.add(FlatValidatorTests.image2);
		images.add(FlatValidatorTests.image3);
		images.add(FlatValidatorTests.image4);
		images.add(FlatValidatorTests.image5);
		images.add(FlatValidatorTests.image6);
		flat.setImages(images);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Flat>> constraintViolations = validator.validate(flat);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}
}
