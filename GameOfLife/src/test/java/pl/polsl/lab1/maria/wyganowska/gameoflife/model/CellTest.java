package pl.polsl.lab1.maria.wyganowska.gameoflife.model;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import java.util.Arrays;
import java.util.Collection;

/**
 * Unit tests for the Cell class.
 * Tests include valid cases, edge cases, and invalid cases.
 * Constructor, accessors, and mutators are excluded.
 * Tests are parameterized for efficiency and clarity.
 * 
 * @author Maria Wyganowska
 */
@RunWith(Parameterized.class)
public class CellTest {
    private static Grid grid;
    private Cell cell;
    private CellState initialStatus;
    private CellState expectedStatusAfterSwitch;
    private Coordinates coordinates;
    private boolean shouldThrowException;

    /**
     * Constructor for parameterized test data.
     * 
     * @param initialStatus Initial alive status of the cell.
     * @param expectedStatusAfterSwitch Expected status after one switch.
     * @param coordinates Coordinates of the cell.
     * @param shouldThrowException Whether an exception is expected for the test case.
     */
    public CellTest(CellState initialStatus, CellState expectedStatusAfterSwitch, Coordinates coordinates, boolean shouldThrowException) {
        this.initialStatus = initialStatus;
        this.expectedStatusAfterSwitch = expectedStatusAfterSwitch;
        this.coordinates = coordinates;
        this.shouldThrowException = shouldThrowException;
    }

    @Before
    public void setUp() {
        grid = new Grid(10);
    }

    @After
    public void tearDown() {
        cell = null;
        grid = null;
    }

    /**
     * Parameterized data provider for test cases.
     * Provides various initial statuses, expected results, and coordinates.
     * 
     * @return Collection of test parameters.
     */
    @Parameters(name = "Initial: {0}, Expected After Switch: {1}, Coordinates: {2}, ShouldThrow: {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {CellState.ALIVE, CellState.DEAD, new Coordinates(2, 2), false},  // Valid case: alive to dead
                {CellState.DEAD, CellState.ALIVE, new Coordinates(5, 5), false},  // Valid case: dead to alive
                {CellState.ALIVE, CellState.DEAD, new Coordinates(-1, -1), true}, // Invalid case: negative indices
                {null, null, new Coordinates(3, 3), true}  // Invalid case: null state
        });
    }

    /**
     * Test for the switchAliveStatus method.
     * Validates the correct toggling of alive status and handles null cases.
     */
    @Test
    public void testSwitchAliveStatus() {
        if (shouldThrowException) {
            // Expect IllegalArgumentException for invalid cases
            assertThrows("Expected an exception for invalid cell state or out-of-bounds coordinates.",
                         IllegalArgumentException.class, 
                         () -> {
                             cell = new Cell(initialStatus, coordinates);
                             cell.switchAliveStatus();
                         });
        } else {
            // WHEN: Creating a cell within valid bounds
            cell = new Cell(initialStatus, coordinates);
            cell.switchAliveStatus();
            // THEN: Check expected status after switch
            assertEquals("The alive status did not toggle as expected.", 
                         expectedStatusAfterSwitch, cell.getAliveStatus());
        }
    }
}
