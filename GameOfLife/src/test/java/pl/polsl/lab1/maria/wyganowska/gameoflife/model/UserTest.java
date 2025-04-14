package pl.polsl.lab1.maria.wyganowska.gameoflife.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Parametrized unit tests for the User class.
 * Covers the validateInput method with valid, invalid, and boundary cases.
 * Each test is parameterized.
 * 
 * @author Maria Wyganowska
 * @version 1.0
 */
@RunWith(Parameterized.class)
public class UserTest {

    private User user;

    private final int input;
    private final int maxChoice;
    private final int minChoice;
    private final boolean shouldThrowException;

    /**
     * Parameterized constructor for test cases.
     *
     * @param input               the user input to test
     * @param maxChoice           the maximum allowed value
     * @param minChoice           the minimum allowed value
     * @param shouldThrowException whether an exception is expected
     */
    public UserTest(int input, int maxChoice, int minChoice, boolean shouldThrowException) {
        this.input = input;
        this.maxChoice = maxChoice;
        this.minChoice = minChoice;
        this.shouldThrowException = shouldThrowException;
    }

    /**
     * Provides parameterized test cases.
     *
     * @return test parameters as a collection
     */
    @Parameterized.Parameters(name = "Input: {0}, MinChoice: {2}, MaxChoice: {1}, ShouldThrowException: {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {3, 5, 1, false}, // Valid input in range
                {1, 5, 1, false}, // Boundary case: minimum value
                {5, 5, 1, false}, // Boundary case: maximum value
                {0, 5, 1, true},  // Invalid input: below minimum
                {6, 5, 1, true}   // Invalid input: above maximum
        });
    }

    /**
     * Sets up the user object before each test.
     */
    @Before
    public void setUp() {
        user = new User();
    }

    /**
     * Cleans up the user object after each test.
     */
    @After
    public void tearDown() {
        user = null;
    }

    /**
     * Tests the validateInput method with parameterized data.
     * Verifies both valid and invalid scenarios.
     */
    @Test
    public void testValidateInput() {
        // GIVEN: User input is set
        user.setInput(input);

        if (shouldThrowException) {
            try {
                // WHEN: validateInput is called
                user.validateInput(maxChoice, minChoice);
                fail("Expected IllegalArgumentException was not thrown.");
            } catch (IllegalArgumentException e) {
                // THEN: Exception is thrown with correct message
                assertEquals("invalid_choice", e.getMessage());
            }
        } else {
            try {
                // WHEN: validateInput is called
                user.validateInput(maxChoice, minChoice);
                // THEN: No exception is thrown
            } catch (IllegalArgumentException e) {
                fail("IllegalArgumentException should not be thrown for valid input.");
            }
        }
    }
}
