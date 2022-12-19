package at.htlleonding.training.ut;

import at.htlleonding.training.App;
import at.htlleonding.training.exceptions.NotANumberException;
import at.htlleonding.training.exceptions.SomethingWrongException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class LambdasTest {

	private  App mApp;

	private static Stream<Arguments> provideValuesIntegerToStringConversion() {
		return Stream.of(
				Arguments.of(42, "42"),
				Arguments.of(-42, "-42")
		);
	}

	private static Stream<Arguments> provideStrings() {
		return Stream.of(
				Arguments.of("A", "B", "A, B"),
				Arguments.of("C", null, "C, null"),
				Arguments.of(null, "D", "null, D"),
				Arguments.of(null, null, "null, null")
		);
	}

	private boolean isLambda(Object testee) {
		return testee.getClass().getSimpleName().contains("$Lambda$");
	}

	@BeforeEach
	void initAll() {
		mApp = App.getMe();
		assertNotNull(mApp);
	}

	@Test
	void test_incTriggerCounter_ShallBeLambda() {
		assertTrue(isLambda(App.INCREASE_COUNTER_TRIGGER));
	}

	@RepeatedTest(6)
	void test_incTriggerCounter_ShallBeCorrect() {
		int triggerCnt = mApp.getTriggerCounter();
		App.INCREASE_COUNTER_TRIGGER.go();
		assertEquals(triggerCnt + 1, mApp.getTriggerCounter());
	}

	@Test
	void test_decTriggerCounter_ShallBeLambda() {
		assertTrue(isLambda(App.DECREASE_COUNTER_TRIGGER));
	}

	@RepeatedTest(6)
	void test_decTriggerCounter_ShallBeCorrect() {
		int triggerCnt = mApp.getTriggerCounter();
		App.DECREASE_COUNTER_TRIGGER.go();
		assertEquals(triggerCnt - 1, mApp.getTriggerCounter());
	}

	@Test
	void test_throwingTrigger_ShallBeLambda() {
		assertTrue(isLambda(App.SOMETHING_WRONG_TRIGGER));
	}

	@Test
	void test_throwingTrigger__ShallThrow_SomethingWrongException() {
		Throwable exception = assertThrowsExactly(SomethingWrongException.class, App.SOMETHING_WRONG_TRIGGER::go);
		assertEquals(App.SOMETHING_WRONG_MSG, exception.getMessage());
		assertNull(exception.getCause());
	}

	@Test
	void test_integer2StringProducer_ShallBeLambda() {
		assertTrue(isLambda(App.INTEGER2STRING_PRODUCER));
	}

	@ParameterizedTest
	@MethodSource("provideValuesIntegerToStringConversion")
	void test_integer2StringProducer__ShallBeCorrect(Integer val, String exp) {
		assertEquals(exp, App.INTEGER2STRING_PRODUCER.produce(val));
	}

	@Test
	void test_string2IntegerProducer_ShallBeLambda() {
		assertTrue(isLambda(App.STRING2INTEGER_PRODUCER));
	}

	@ParameterizedTest
	@MethodSource("provideValuesIntegerToStringConversion")
	void test_string2IntegerProducer__ShallBeCorrect(Integer exp, String str) {
		Integer res = assertDoesNotThrow(() -> App.STRING2INTEGER_PRODUCER.produce(new String[] {str}));
		assertEquals(exp, res);
	}

	@Test
	void test_string2IntegerProducer__ShallThrowNotANumberException() {
		String[] values = new String[] {"ZERO"};
		Exception exception = assertThrowsExactly(NotANumberException.class, () -> App.STRING2INTEGER_PRODUCER.produce(values));
		assertEquals(App.NOT_A_NUMBER_MSG, exception.getMessage());
		assertTrue(exception.getCause() instanceof NumberFormatException);
		assertEquals(values[0], ((NotANumberException)exception).getInvalidValue());
	}

	@Test
	void test_string2IntegerProducer__ShallThrowSomethingWrongException() {
		String[] values = new String[0];
		Exception exception = assertThrowsExactly(SomethingWrongException.class, () -> App.STRING2INTEGER_PRODUCER.produce(values));
		assertEquals(App.SOMETHING_WRONG_MSG, exception.getMessage());
		assertTrue(exception.getCause() instanceof ArrayIndexOutOfBoundsException);
	}

	@Test
	void test_stringConcatenationAggregator_ShallBeLambda() {
		assertTrue(isLambda(App.STRING_CONCATENATOR));
	}

	@ParameterizedTest
	@MethodSource("provideStrings")
	void test_stringConcatenationAggregator__ShallBeCorrect(String str1, String str2, String exp) {
		String res = assertDoesNotThrow(() -> App.STRING_CONCATENATOR.aggregate(str1, str2));
		assertEquals(exp, res);
	}

	@Test
	void test_calcInteger__ShallBeCorrect() {
		Integer val1 = 21;
		Integer val2 = 21;
		Integer res = assertDoesNotThrow(() -> mApp.calc(val1, val2, Integer::sum, 0));
		assertEquals(val1 + val2, res);
	}

	@Test
	void test_calcDouble__ShallBeCorrect() {
		Double val1 = 21.21;
		Double val2 = 21.21;
		Supplier<Double> defaultValueSupplier = () -> 0.0;
		Double res = assertDoesNotThrow(() -> mApp.calc(val1, val2, Double::sum, defaultValueSupplier));
		assertEquals(val1 + val2, res);
	}

	@Test
	void test_calcWithoutAggregator__ShallReturnDefaultValue() {
		Double defaultValue = 1.0;
		Double res = assertDoesNotThrow(() -> mApp.calc(33.0, 44.5, null, defaultValue));
		assertEquals(defaultValue, res);
	}
	@Test
	void test_calcWithoutAggregatorButDefaultValueSupplier__ShallReturnDefaultValue() {
		Supplier<Double> defaultValueSupplier = () -> 19.0;
		Double res = assertDoesNotThrow(() -> mApp.calc(33.0, 44.5, null, defaultValueSupplier));
		assertEquals(defaultValueSupplier.get(), res);
	}

	@Test
	void test_calcWithoutAggregatorAndDefaultSupplier__ShallReturnNull() {
		Double res = assertDoesNotThrow(() -> mApp.calc(21.21, 22.22, null, (Supplier<Double>)null));
		assertNull(res);
	}
}
