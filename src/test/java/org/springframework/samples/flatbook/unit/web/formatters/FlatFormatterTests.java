
package org.springframework.samples.flatbook.unit.web.formatters;

import java.text.ParseException;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.web.formatters.FlatFormatter;

@ExtendWith(MockitoExtension.class)
class FlatFormatterTests {

	private static final Integer	FLAT	= 1;

	@Mock
	private FlatService				flatService;

	private FlatFormatter			flatFormatter;

	private Flat					flat;


	@BeforeEach
	void setup() {
		this.flatFormatter = new FlatFormatter(this.flatService);
		this.flat = new Flat();
		this.flat.setId(FlatFormatterTests.FLAT);
	}

	@Test
	void testPrint() {
		String id = this.flatFormatter.print(this.flat, Locale.ENGLISH);
		Assertions.assertEquals(FlatFormatterTests.FLAT.toString(), id);
	}

	@Test
	void shouldParse() throws ParseException {
		Mockito.lenient().when(this.flatService.findFlatById(FlatFormatterTests.FLAT)).thenReturn(this.flat);
		Flat flat = this.flatFormatter.parse(FlatFormatterTests.FLAT.toString(), Locale.ENGLISH);
		Assertions.assertEquals(flat, this.flat);
	}

	@Test
	void shouldThrowException() throws ParseException {
		Assertions.assertThrows(ParseException.class, () -> {
			this.flatFormatter.parse("-2", Locale.ENGLISH);
		});
	}
}
