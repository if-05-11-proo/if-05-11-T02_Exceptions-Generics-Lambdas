package at.htlleonding.training.ut;

import at.htlleonding.training.App;
import at.htlleonding.training.exceptions.NotANumberException;
import at.htlleonding.training.exceptions.SomethingWrongException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionsTest {

	private  App mApp;
	private final Object[] mIsPositiveTestData = new Object[1];

	private static Stream<Arguments> provideValuesForIsPositiveDone() {
		return Stream.of(
				Arguments.of(42, true),
				Arguments.of(-4.2, false),
				Arguments.of(null, false),
				Arguments.of("zero", false)
		);
	}

	private static Stream<Arguments> provideValuesForIsPositiveNumbers() {
		return provideValuesForIsPositiveDone().filter(o -> o.get()[0] instanceof Number);
	}

	@BeforeEach
	void initAll() {
		mApp = App.getMe();
		assertNotNull(mApp);
	}

	@ParameterizedTest
	@MethodSource("provideValuesForIsPositiveNumbers")
	void test_isPositive_WithNumbers__ShallBeCorrect(Object val, boolean expRes) {
			mIsPositiveTestData[0] = val;
			boolean res = assertDoesNotThrow(() -> mApp.isPositive(mIsPositiveTestData));
			assertEquals(expRes, res);
	}

	@Test
	void test_isPositive_With_Null__ShallThrow_SomethingWrongException() {
		mIsPositiveTestData[0] = null;
		Throwable exception = assertThrows(SomethingWrongException.class, () -> mApp.isPositive(mIsPositiveTestData));
		assertEquals(App.SOMETHING_WRONG_MSG, exception.getMessage());
		assertTrue(exception.getCause() instanceof NullPointerException);
	}

	@Test
	void test_isPositive_With_String__ShallThrow_NotANumberException() {
		mIsPositiveTestData[0] = "zero";
		Throwable exception = assertThrows(NotANumberException.class, () -> mApp.isPositive(mIsPositiveTestData));
		assertEquals(App.NOT_A_NUMBER_MSG, exception.getMessage());
		assertTrue(exception.getCause() instanceof ClassCastException);
		assertEquals("zero", ((NotANumberException)exception).getInvalidValue());
	}

	@ParameterizedTest
	@MethodSource("provideValuesForIsPositiveDone")
	void test_isPositive_WithAny__ShallBeDone(Object val) {
		mIsPositiveTestData[0] = val;
		if (val instanceof Number) {
			assertDoesNotThrow(() -> mApp.isPositive(mIsPositiveTestData));
		} else {
			assertThrows(Exception.class, () -> mApp.isPositive(mIsPositiveTestData));
		}
		assertEquals(App.DONE, mIsPositiveTestData[0]);
	}
}
