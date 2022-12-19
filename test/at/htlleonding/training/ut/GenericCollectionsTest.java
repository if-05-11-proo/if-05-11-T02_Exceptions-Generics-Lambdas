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

public class GenericCollectionsTest {

	private  App mApp;
	@BeforeEach
	void initAll() {
		mApp = App.getMe();
		assertNotNull(mApp);
	}

	@Test
	void test_toIntegerList_WithIntegers_ShallBeCorrect() {
		List<Integer> values = List.of(1, 2, 3);
		List<Integer> results = assertDoesNotThrow(() -> mApp.toIntegerList(values));
		assertNotNull(results);
		assertIterableEquals(values, results);
	}

	@Test
	void test_toIntegerList_WithDoubles_ShallBeCorrect() {
		List<Double> values = List.of(1.0, 2.2, 3.3);
		List<Integer> expected = List.of(1, 2, 3);
		List<Integer> results = assertDoesNotThrow(() -> mApp.toIntegerList(values));
		assertNotNull(results);
		assertIterableEquals(expected, results);
	}

	@Test
	void test_toIntegerList_WithNumbers_ShallBeCorrect() {
		List<Number> values = List.of(1, 2.0, (short) 3);
		List<Integer> expected = List.of(1, 2, 3);
		List<Integer> results = assertDoesNotThrow(() -> mApp.toIntegerList(values));
		assertNotNull(results);
		assertIterableEquals(expected, results);
	}

	@Test
	void test_toIntegerList_WithNull_ShallBeNull() {
		List<Integer> results = assertDoesNotThrow(() -> mApp.toIntegerList(null));
		assertNull(results);
	}
}
