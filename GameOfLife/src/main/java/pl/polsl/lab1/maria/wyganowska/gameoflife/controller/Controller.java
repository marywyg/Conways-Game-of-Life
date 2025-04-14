package pl.polsl.lab1.maria.wyganowska.gameoflife.controller;
import pl.polsl.lab1.maria.wyganowska.gameoflife.model.Grid;
import pl.polsl.lab1.maria.wyganowska.gameoflife.view.GUIView;
import javax.swing.*;
import pl.polsl.lab1.maria.wyganowska.gameoflife.model.Coordinates;
import pl.polsl.lab1.maria.wyganowska.gameoflife.model.CellState;
import lombok.Getter;
import lombok.Setter;
/**
 * The controller class which manages the communication between the model and the view (GUIView).
 * It handles user input, grid state updates, and UI changes.
 * @author Maria Wyganowska
 * @version 2.0
 */
public class Controller {
    private final Grid grid;
    private final GUIView view;
    @Getter
    private boolean editingMode;
    @Getter 
    @Setter
    private int selectedRow;
    @Getter 
    @Setter
    private int selectedCol;
    private Coordinates selectedCell;
/***
 * Constructor that initializes the grid, view, and sets up some initial cells.
 * @param grid The grid for the game.
 * @param view The view to display the game.
 */
    public Controller(Grid grid, GUIView view) {
        this.grid = grid;
        this.view = view;
        grid.setCellStatus(3, 2, CellState.ALIVE);
        grid.setCellStatus(3, 3, CellState.ALIVE);
        grid.setCellStatus(3, 4, CellState.ALIVE);
        this.view.setController(this);
        this.view.getGridPanel().setController(this);
        selectedCell = new Coordinates(0, 0);
        editingMode = false;
        selectedCol = 0;
        selectedRow = 0;
    }
   
    /***
     * Class representing the exception thrown when an invalid grid size is provided.
     */
public class InvalidGridSizeException extends Exception {
    public InvalidGridSizeException(String message) {
        super(message);
    }
}
    /***
     * Starts the game by invoking the GUI creation on the Swing event dispatch thread.
     */
    public void startGame() {
        SwingUtilities.invokeLater(() -> view.createAndShowGUI());
    }
    /***
     * Continues the game by updating the grid state and refreshing the view.
     */
    private void continueGame() {
        grid.updateState();
        view.updateGridDisplay();
    }
    /***
     * Prompts the user to change the grid size, validates the input, and updates the grid accordingly.
     */
    private void changeGridSize() {
    String input = JOptionPane.showInputDialog(null, "Enter new grid size:", "Change Grid Size", JOptionPane.PLAIN_MESSAGE);
    if (input != null) {
        try {
            int newSize = Integer.parseInt(input);
            if (newSize <= 0) {
                throw new InvalidGridSizeException("Size must be positive!");
            }
            grid.setSize(newSize);
            view.updateGridDisplay();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid size entered!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidGridSizeException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    /***
     * Handles key presses for navigation and actions in editing mode.
     * @param keyCode The key code of the pressed key.
     */
    public void handleKeyPress(int keyCode) {
        if (editingMode) {
            handleEditingKeyPress(keyCode);
        }
        view.updateGridDisplay();
    }
    /***
     * Handles key presses specifically for editing mode, including navigation and cell state toggle.
     * @param keyCode The key code of the pressed key.
     */
private void handleEditingKeyPress(int keyCode) {
    int row = selectedCell.row();
    int col = selectedCell.col();
    switch (keyCode) {
        case java.awt.event.KeyEvent.VK_UP -> row = (row > 0) ? row - 1 : grid.getHeight() - 1;
        case java.awt.event.KeyEvent.VK_DOWN -> row = (row + 1) % grid.getHeight();
        case java.awt.event.KeyEvent.VK_LEFT -> col = (col > 0) ? col - 1 : grid.getWidth() - 1;
        case java.awt.event.KeyEvent.VK_RIGHT -> col = (col + 1) % grid.getWidth();
        case java.awt.event.KeyEvent.VK_ENTER -> toggleCellState();
        case java.awt.event.KeyEvent.VK_ESCAPE -> disableEditingMode();
    }
    selectedCell = new Coordinates(row, col);
    selectedRow = row;
    selectedCol = col;
}
    /***
     * Toggles the state (alive or dead) of the currently selected cell.
     */
    private void toggleCellState() {
            grid.getCell(selectedRow, selectedCol).switchAliveStatus();
    }
    /***
     * Disables editing mode and shows an informational message to the user.
     */
public void disableEditingMode() {
    editingMode = false;
    JOptionPane.showMessageDialog(null, "Editing mode exited.", 
                                  "Editing Mode", JOptionPane.INFORMATION_MESSAGE);
    view.updateGridDisplay();
}
    /***
     * Enables editing mode and shows an informational message to the user.
     */
public void enableEditingMode() {
    editingMode = true;
    JOptionPane.showMessageDialog(null, "Editing mode enabled. Use arrow keys to navigate. Press ENTER to toggle cell state or ESC to exit.", 
                                  "Editing Mode", JOptionPane.INFORMATION_MESSAGE);
    view.updateGridDisplay();
}
    /***
     * Handles the selection of menu actions and calls the corresponding method.
     * @param action The selected action from the menu.
     */
    public void handleMenuAction(String action){
        switch (action) {
        case "Continue" -> continueGame();
            case "Grid size" -> changeGridSize();
            case "Cells" -> enableEditingMode();
            case "Show Cell Table" -> view.showCellTable();
            case "About" -> view.showAboutPanel();
            case "Return to game" -> view.showGrid();
    }
    }
}
