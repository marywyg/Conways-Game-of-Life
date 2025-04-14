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
 * Parametrized unit tests for the Grid class.
 * Covers public methods except constructors, accessors, and mutators.
 * Tests include valid, invalid, and boundary cases.
 * Each test is parameterized.
 * 
 * @author Maria Wyganowska
 */
@RunWith(Parameterized.class)
public class GridTest {

    private Grid grid;

    private final int size;
    private final int testX;
    private final int testY;
    private final CellState expectedState;

    /**
     * Parameterized constructor for test cases.
     *
     * @param size          the size of the grid
     * @param testX         the x-coordinate of the tested cell
     * @param testY         the y-coordinate of the tested cell
     * @param expectedState the expected state of the cell
     */
    public GridTest(int size, int testX, int testY, CellState expectedState) {
        this.size = size;
        this.testX = testX;
        this.testY = testY;
        this.expectedState = expectedState;
    }

    /**
     * Provides parameterized test cases.
     *
     * @return test parameters as a collection
     */
    @Parameterized.Parameters(name = "Size: {0}, TestX: {1}, TestY: {2}, ExpectedState: {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {5, 2, 2, CellState.ALIVE}, // Valid middle cell
                {5, 0, 0, CellState.DEAD},  // Valid corner cell
                {5, 4, 4, CellState.DEAD},  // Valid edge cell
                {5, -1, -1, null},          // Invalid negative indices
                {5, 5, 5, null}             // Invalid out-of-bound indices
        });
    }

    /**
     * Sets up the grid and initializes states before each test.
     */
    @Before
    public void setUp() {
        grid = new Grid(size);
        if (testX >= 0 && testY >= 0 && testX < size && testY < size && expectedState != null) {
            grid.setCellStatus(testY, testX, expectedState);
        }
    }

    /**
     * Cleans up the grid after each test.
     */
    @After
    public void tearDown() {
        grid = null;
    }

    /**
     * Test for the getCell method.
     * Verifies correct retrieval of cell state and exception handling.
     */
    @Test
    public void testGetCell() {
        if (testX >= 0 && testY >= 0 && testX < size && testY < size) {
            Cell cell = grid.getCell(testY, testX);
            assertNotNull("Cell should not be null for valid coordinates.", cell);
            assertEquals("Cell state should match expected state.", expectedState, cell.getAliveStatus());
        } else {
            try {
                grid.getCell(testY, testX);
                fail("Expected IndexOutOfBoundsException for invalid coordinates.");
            } catch (IndexOutOfBoundsException e) {
                // Test passed
            }
        }
    }

    /**
     * Test for the setCellStatus method.
     * Verifies state updates and exception handling for invalid coordinates.
     */
    @Test
    public void testSetCellStatus() {
        if (testX >= 0 && testY >= 0 && testX < size && testY < size) {
            grid.setCellStatus(testY, testX, CellState.ALIVE);
            assertEquals("Cell state should be updated to ALIVE.", CellState.ALIVE, grid.getCell(testY, testX).getAliveStatus());
        } else {
            try {
                grid.setCellStatus(testY, testX, CellState.ALIVE);
                fail("Expected IndexOutOfBoundsException for invalid coordinates.");
            } catch (IndexOutOfBoundsException e) {
                // Test passed
            }
        }
    }

    /**
     * Test for the countAliveNeighbours method.
     * Verifies neighbor count logic and exception handling.
     */
    @Test
    public void testCountAliveNeighbours() {
        if (testX >= 0 && testY >= 0 && testX < size && testY < size) {
            int expectedAliveNeighbours = 0;
            try {
            if (testY > 0) {
                grid.setCellStatus(testY - 1, testX, CellState.ALIVE); // Cell above
                expectedAliveNeighbours++;
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore out-of-bounds exceptions for invalid neighbors
        }

        try {
            if (testX > 0) {
                grid.setCellStatus(testY, testX - 1, CellState.ALIVE); // Cell to the left
                expectedAliveNeighbours++;
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore out-of-bounds exceptions for invalid neighbors
        }

        try {
            if (testY < size - 1) {
                grid.setCellStatus(testY + 1, testX, CellState.ALIVE); // Cell below
                expectedAliveNeighbours++;
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore out-of-bounds exceptions for invalid neighbors
        }

        try {
            if (testX < size - 1) {
                grid.setCellStatus(testY, testX + 1, CellState.ALIVE); // Cell to the right
                expectedAliveNeighbours++;
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore out-of-bounds exceptions for invalid neighbors
        }

        try {
            if (testY > 0 && testX > 0) {
                grid.setCellStatus(testY - 1, testX - 1, CellState.ALIVE); // Top-left diagonal
                expectedAliveNeighbours++;
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore out-of-bounds exceptions for invalid neighbors
        }

        try {
            if (testY < size - 1 && testX > 0) {
                grid.setCellStatus(testY + 1, testX - 1, CellState.ALIVE); // Bottom-left diagonal
                expectedAliveNeighbours++;
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore out-of-bounds exceptions for invalid neighbors
        }

        try {
            if (testY > 0 && testX < size - 1) {
                grid.setCellStatus(testY - 1, testX + 1, CellState.ALIVE); // Top-right diagonal
                expectedAliveNeighbours++;
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore out-of-bounds exceptions for invalid neighbors
        }

        try {
            if (testY < size - 1 && testX < size - 1) {
                grid.setCellStatus(testY + 1, testX + 1, CellState.ALIVE); // Bottom-right diagonal
                expectedAliveNeighbours++;
            }
        } catch (IndexOutOfBoundsException e) {
            // Ignore out-of-bounds exceptions for invalid neighbors
        }
            int aliveNeighbours = grid.countAliveNeighbours(testY, testX);
            assertEquals("Alive neighbours count should match expected value.", expectedAliveNeighbours, aliveNeighbours);
        } else {
            try {
                grid.countAliveNeighbours(testY, testX);
                fail("Expected IndexOutOfBoundsException for invalid coordinates.");
            } catch (IndexOutOfBoundsException e) {
                // Test passed
            }
        }
    }

    /**
     * Test for the resetGrid method.
     * Ensures all cells are reset to DEAD.
     */
    @Test
    public void testResetGrid() {
        grid.setCellStatus(0, 0, CellState.ALIVE);
        grid.resetGrid();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                assertEquals("All cells should be DEAD after reset.", CellState.DEAD, grid.getCell(y, x).getAliveStatus());
            }
        }
    }

    /**
     * Test for the setSize method.
     * Verifies grid resizing and state retention where applicable.
     */
    @Test
    public void testSetSize() {
        grid.setCellStatus(0, 0, CellState.ALIVE);
        grid.setSize(size + 2);
        assertEquals("Top-left cell should remain ALIVE.", CellState.ALIVE, grid.getCell(0, 0).getAliveStatus());
        assertEquals("Grid width should match new size.", size + 2, grid.getWidth());
        assertEquals("Grid height should match new size.", size + 2, grid.getHeight());
    }
}
