package at.htlleonding.training;

import java.util.function.Supplier;

/**
 * The application of this example.
 * It shall be implemented as a singleton.
 */
public class App {
	/** The constant string to use for confirming completion of 'isPositive' */
	public static final String DONE = "DONE";
	/** The constant string to use as message for {@link SomethingWrongException} */
	public static final String SOMETHING_WRONG_MSG = "something's wrong";
	/** The constant string to use as message for {@link NotANumberException} */
	public static final String NOT_A_NUMBER_MSG = "hey, that's not a number!";

	/** Implement the {@link App} instance as a singleton */
	//...

	/** The counter member for trigger invokations */
	private int mTriggerCounter;

	// ...

	/** The {@link Trigger} lambda for incrementing the trigger counter member which is provided via {@link #getTriggerCounter()} */
//	public static final ... INCREASE_COUNTER_TRIGGER =  ...;
	/** The {@link Trigger} lambda for decrementing the trigger counter member which is provided via {@link #getTriggerCounter()} */
//	public static final ... DECREASE_COUNTER_TRIGGER = ...;
	/** The {@link ThrowingTrigger} lambda that unconditionally throws a {@link SomethingWrongException} */
//	public static final ... SOMETHING_WRONG_TRIGGER = ...;
	/** The {@link Producer} lambda that returns the Integer value as a String using a Method Reference! */
//	public static final ... INTEGER2STRING_PRODUCER = ...;
	/** The {@link ThrowingProducer} lambda that converts exactly the first String of the given String array into the corresponding Integer.
	 *  Exceptionally NO safeguards (null-check or index bounds checks) shall be applied but exceptions shall be rethrown:
	 *  - A {@link NotANumberException} with constant message and original exception shall be thrown upon occurrence of a {@link NumberFormatException}
	 *  - A {@link SomethingWrongException} with constant message and original exception shall be thrown for any other {@link Exception}
	 */
//	public static final ... STRING2INTEGER_PRODUCER = ...;
	/** The {@link Aggregator} lambda that returns a String that is concatenated of two input string 'str1' and 'str2' in format 'A, B' */
//	public static final ... STRING_CONCATENATOR = ...;

	/**
	 * Tests the first element of the given array whether or not it is a positive number.
	 * Eventually it replaces the first array item with the constant string {@link #DONE}
	 * in any case (!).
	 * In addition to the declared exception, a {@link NotANumberException} shall be thrown
	 * if the cast fails.
	 *
	 * @implNote The existing code must not be changed, neither method signature nor method existing method body.
	 * Only exception handling code shall be surrounded as appropriate.
	 *
	 * @param value The array holding the value to test.
	 * @return True if the number is larger than zero, false otherwise.
	 * @throws SomethingWrongException if an exception other than a {@link ClassCastException} occurs.
	 * The thrown exception shall carry the appropriate message as well as the original exception.
	 */
	public boolean isPositive(Object[] value) throws SomethingWrongException {
		// ...
		return ((Number) value[0]).doubleValue() > 0;
		// ...
	}

	/**
	 * The method 'calc' calculates a result of two numbers using the given {@link NumberAggregator}.
	 * If no aggregator is provided, the given default value is returned.
	 * @param v1 The first value of type N
	 * @param v2 The second value of type N
	 * @param calculator The calculation {@link NumberAggregator}
	 * @param defaultValue The value to return if {@code calculator} is null.
	 * @param <N> The concrete value type.
	 * @return The calculation result or the default value.
	 */
	// ...

	/**
	 * The method 'calc' calculates a result of two numbers using the given {@link NumberAggregator}.
	 * If no aggregator is provided, the given default value is returned.
	 *
	 * implNote:
	 * This overloaded version of 'calc' accepts a {@link Supplier} that provides the default value instead
	 * of applying the default value directly as argument.
	 * Think of the benefits of both variants. Which variant is beneficial if just a plain value shall be supplied,
	 * and which one if the default value need to be calculated or fetched from a database? Why, what happens behind
	 * the scenes at runtime?
	 *
	 * @param v1 The first value of type N
	 * @param v2 The second value of type N
	 * @param calculator The calculation {@link NumberAggregator}
	 * @param defaultValueSupplier The {@link Supplier} for the value to return if {@code calculator} is null.
	 * @param <N> The concrete value type.
	 * @return The calculation result or the default value. If the default value supplied is null, null is returned.
	 */
	// ...

	/**
	 * Converts a list of numbers into a list of integers.
	 * @param values The list of numbers to convert.
	 * @return The corresponding list of integers.
	 */
	// ...

	/**
	 * Provides the value of the trigger counter.
	 *
	 * implNote: Nothing needs to be done here
	 * @return The number of times a trigger was executed.
	 */
	public int getTriggerCounter() {
		return mTriggerCounter;
	}

	/**
	 * A string concatenation benchmark
	 *
	 * Note:
	 * IntelliJ IDEA suggests replacing StringBuilder().append("A").append("B") by plain string concatenation like "A" + "B".
	 * While the proposed approach shortens the code, it is still significantly less performant.
	 * The performance penalty may be neglectable if that piece of code is rarely executed, but it becomes significant
	 * for code which is executed frequently as the little benchmark below reveals.
	 *
	 * Execute 'main' and be surprised by the result (spoiler: it may take a while)
	 *
	 * @param args irrelevant
	 */
	public static void main(String[] args) {
		int repetitions = 1000*1000;
		long start = System.nanoTime();
		String str1 = createString(repetitions);
		long timeString = System.nanoTime() - start;
		start = System.nanoTime();
		String str2 = createStringBuilder(repetitions);
		long timeStringBuilder = System.nanoTime() - start;
		boolean faster = (timeString > timeStringBuilder);
		long factor = faster ? (timeString / timeStringBuilder) : (timeStringBuilder / timeString);
		System.out.printf("String '+'    (%d x): %12d ns (%8.1f ms)%n", str1.length()-1, timeString, timeString / (1000.0*1000.0));
		System.out.printf("StringBuilder (%d x): %12d ns (%8.1f ms)%n", str2.length()-1, timeStringBuilder,  timeStringBuilder / (1000.0*1000.0));
		System.out.printf("StringBuilder is ~ %d times %s!%n", factor, faster ? "faster" : "slower");
	}

	private static String createString(int repetitions) {
		String str = "_";
		for (int i = 0; i < repetitions; i++) {
			str += "A";
		}
		return str;
	}
	private static String createStringBuilder(int repetitions) {
		StringBuilder sb = new StringBuilder("_");
		for (int i = 0; i < repetitions; i++) {
			sb.append("A");
		}
		return sb.toString();
	}
}
