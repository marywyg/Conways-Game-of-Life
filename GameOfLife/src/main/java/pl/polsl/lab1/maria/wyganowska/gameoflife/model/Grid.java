package pl.polsl.lab1.maria.wyganowska.gameoflife.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a grid of alive and dead cells which is drawn on the screen
 * @author Maria Wyganowska
 * @version 2.1
 */
@Getter
@Setter
public class Grid {
    private int width;
    private int height;
    private List<List<Cell>> gridOfCells;

    /**
     * The constructor of the Grid class which initializes the grid with default size parameter.
     * @param size The default size of the grid.
     * @throws IllegalArgumentException if the size is less than or equal to 0.
     */
    public Grid(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Grid size must be greater than 0.");
        }
        this.width = size;
        this.height = size;
        this.gridOfCells = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            List<Cell> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new Cell(CellState.DEAD, new Coordinates(i, j)));
            }
            gridOfCells.add(row);
        }
    }

    /**
     * Method which allows getting the coordinates of a specific single cell.
     * @param y The row index in the grid.
     * @param x The column index in the grid.
     * @return The specific cell.
     * @throws IndexOutOfBoundsException if the coordinates are out of bounds.
     */
    public Cell getCell(int y, int x) {
        validateCoordinates(y, x);
        return gridOfCells.get(y).get(x);
    }

    /**
     * Method which allows setting the status of a specific cell.
     * @param y The row index in the grid.
     * @param x The column index in the grid.
     * @param aliveStatus The current status of a single cell (alive/dead).
     * @throws IndexOutOfBoundsException if the coordinates are out of bounds.
     * @throws IllegalArgumentException if the aliveStatus is null.
     */
    public void setCellStatus(int y, int x, CellState aliveStatus) {
        if (aliveStatus == null) {
            throw new IllegalArgumentException("Cell status cannot be null.");
        }
        validateCoordinates(y, x);
        gridOfCells.get(y).get(x).setAliveStatus(aliveStatus);
    }

    /**
     * Method which counts the number of specific cell's neighbours' "alive" statuses
     * using stream processing and lambda expressions.
     * @param y The row index in the grid.
     * @param x The column index in the grid.
     * @return The number of "alive" statuses in neighbouring cells.
     * @throws IndexOutOfBoundsException if the coordinates are out of bounds.
     */
    public int countAliveNeighbours(int y, int x) {
    if (y < 0 || y >= height || x < 0 || x >= width) {
        throw new IndexOutOfBoundsException(
            String.format("Coordinates (%d, %d) are out of bounds for grid of size %dx%d", y, x, height, width)
        );
    }
    Coordinates cellCoordinates = new Coordinates(y, x);
    return (int) IntStream.rangeClosed(-1, 1)
        .boxed()
        .flatMap(dy -> IntStream.rangeClosed(-1, 1)
            .mapToObj(dx -> new Coordinates(cellCoordinates.row() + dy, cellCoordinates.col() + dx)))
        .filter(coords -> !coords.equals(cellCoordinates))
        .filter(coords -> coords.row() >= 0 && coords.row() < height 
                       && coords.col() >= 0 && coords.col() < width)
        .map(coords -> getCell(coords.row(), coords.col()).getAliveStatus())
        .filter(CellState.ALIVE::equals)
        .count();
}

    /**
     * Method which updates cells in a grid according to the rules of Conway's Game of Life.
     */
    public void updateState() {
        List<List<Cell>> nextState = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            List<Cell> newRow = new ArrayList<>();
            for (int x = 0; x < width; x++) {
                Cell currentCell = getCell(y, x);
                int aliveNeighbors = countAliveNeighbours(y, x);
                CellState nextStatus = currentCell.getAliveStatus();

                if (currentCell.getAliveStatus() == CellState.ALIVE && (aliveNeighbors < 2 || aliveNeighbors > 3)) {
                    nextStatus = CellState.DEAD;
                } else if (currentCell.getAliveStatus() == CellState.DEAD && aliveNeighbors == 3) {
                    nextStatus = CellState.ALIVE;
                }

                newRow.add(new Cell(nextStatus, currentCell.getCellCoordinates()));
            }
            nextState.add(newRow);
        }
        this.gridOfCells = nextState;
    }

    /**
     * Method which resets the grid into a blank one (all of cells' statuses are set to "dead").
     */
    public void resetGrid() {
        for (List<Cell> row : gridOfCells) {
            for (Cell cell : row) {
                cell.setAliveStatus(CellState.DEAD);
            }
        }
    }

    /**
     * Method which changes the grid's size and transfers the data from the previously sized grid.
     * @param newSize The desired new size of the grid.
     * @throws IllegalArgumentException if the newSize is less than or equal to 0.
     */
    public void setSize(int newSize) {
        if (newSize <= 0) {
            throw new IllegalArgumentException("Grid size must be greater than 0.");
        }
        List<List<Cell>> newGrid = new ArrayList<>();
        for (int y = 0; y < newSize; y++) {
            List<Cell> row = new ArrayList<>();
            for (int x = 0; x < newSize; x++) {
                if (y < height && x < width) {
                    row.add(gridOfCells.get(y).get(x));
                } else {
                    row.add(new Cell(CellState.DEAD, new Coordinates(y, x)));
                }
            }
            newGrid.add(row);
        }
        this.gridOfCells = newGrid;
        this.height = newSize;
        this.width = newSize;
    }

    /**
     * Validates if the given coordinates are within the bounds of the grid.
     * @param y The row index.
     * @param x The column index.
     * @throws IndexOutOfBoundsException if the coordinates are out of bounds.
     */
    private void validateCoordinates(int y, int x) {
        if (y < 0 || y >= height || x < 0 || x >= width) {
            throw new IndexOutOfBoundsException("Coordinates (" + y + ", " + x + ") are out of bounds for grid size " + width + "x" + height + ".");
        }
    }
}
