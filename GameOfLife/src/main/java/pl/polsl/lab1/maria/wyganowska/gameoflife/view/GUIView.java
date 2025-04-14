package pl.polsl.lab1.maria.wyganowska.gameoflife.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.AbstractTableModel;
import pl.polsl.lab1.maria.wyganowska.gameoflife.model.Grid;
import pl.polsl.lab1.maria.wyganowska.gameoflife.controller.Controller;
import pl.polsl.lab1.maria.wyganowska.gameoflife.model.CellState;
import lombok.Getter;
import lombok.Setter;
/**
 * The class representing the view of graphic user interface of the game.
 * @author Maria Wyganowska
 * @version 1.0
 */
@Getter
@Setter
public class GUIView implements ActionListener {
    private JTextArea output;
    private JScrollPane scrollPane;
    private Grid grid;
    private GridPanel gridPanel;
    private JPanel contentPane;
    @Setter
    private Controller controller;
/***
 * The constructor of the view.
 * Sets the grid which is later shown on the screen.
 * @param grid The grid of the game.
 */
    public GUIView(Grid grid) {
        this.grid = grid;
        this.gridPanel = new GridPanel(grid);
        
    }
/***
 * Creates and shows the frame of the graphic user interface.
 */
    public void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Conway's Game of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.setContentPane(createContentPane());
        frame.setSize(450, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
/***
 * Creates the menu bar at the top of the window.
 * Sets each accessibility context for every options available in the menu.
 * @return Returns the JMenuBar (menu bar) created.
 */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menu.setMnemonic(KeyEvent.VK_G);
        menu.getAccessibleContext().setAccessibleDescription("This menu has items connected to running the game.");
        menu.setToolTipText("This menu has items connected to running the game.");
        JMenuItem continueItem = new JMenuItem("Continue", KeyEvent.VK_SPACE);
        continueItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, ActionEvent.CTRL_MASK));
        continueItem.addActionListener(this);
        continueItem.getAccessibleContext().setAccessibleDescription("Continue to the next tick of the game.");
        continueItem.setToolTipText("Continue to the next tick of the game.");
        menu.add(continueItem);
        
        menu.addSeparator();
        
        JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic(KeyEvent.VK_O);
        optionsMenu.getAccessibleContext().setAccessibleDescription("Show options.");
        optionsMenu.setToolTipText("Show options.");
        JMenuItem changeGridSize = new JMenuItem("Grid size");
        changeGridSize.setMnemonic(KeyEvent.VK_G);
        changeGridSize.getAccessibleContext().setAccessibleDescription("Change the size of the grid.");
        changeGridSize.setToolTipText("Change the size of the grid.");
        changeGridSize.addActionListener(this);
        optionsMenu.add(changeGridSize);

        JMenuItem changeCells = new JMenuItem("Cells");
        changeCells.setMnemonic(KeyEvent.VK_C);
        changeCells.getAccessibleContext().setAccessibleDescription("Edit the statuses of cells on the grid.");
        changeCells.setToolTipText("Edit the statuses of cells on the grid.");
        changeCells.addActionListener(this);
        optionsMenu.add(changeCells);

        menu.add(optionsMenu);
        menuBar.add(menu);

        JMenu infoMenu = new JMenu("Info");
        infoMenu.setMnemonic(KeyEvent.VK_I);
        infoMenu.getAccessibleContext().setAccessibleDescription("This menu allows access to information about the game.");
        infoMenu.setToolTipText("This menu allows access to information about the game.");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic(KeyEvent.VK_A);
        aboutItem.getAccessibleContext().setAccessibleDescription("Learn about the game.");
        aboutItem.setToolTipText("Learn about the game.");
        aboutItem.addActionListener(this);
        infoMenu.add(aboutItem);
        JMenuItem returnItem = new JMenuItem("Return to game", KeyEvent.VK_A);
        returnItem.setMnemonic(KeyEvent.VK_R);
        returnItem.getAccessibleContext().setAccessibleDescription("Go back to the game grid.");
        returnItem.setToolTipText("Go back to the game grid.");
        returnItem.addActionListener(this);
        infoMenu.add(returnItem);
        menuBar.add(infoMenu);
        JMenuItem showCellTable = new JMenuItem("Show Cell Table");
        showCellTable.setMnemonic(KeyEvent.VK_S);
        showCellTable.getAccessibleContext().setAccessibleDescription("Show a table listing the cell's statuses.");
        showCellTable.setToolTipText("Show a table listing the cell's statuses.");
        showCellTable.addActionListener(this);
        menu.add(showCellTable);
        return menuBar;
    }
/***
 * Creates the content pane, on which different contents are displayed at a specific moment.
 * @return Returns the created content pane.
 */
    private JPanel createContentPane() {
        contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);
        output = new JTextArea(2, 30);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);
        contentPane.add(scrollPane, BorderLayout.SOUTH);
        contentPane.add(gridPanel, BorderLayout.CENTER);
        return contentPane;
    }
/***
 * Changes colors of the grid shown in the content pane.
 */
    public void updateGridDisplay() {
        gridPanel.repaint();
    }
/***
 * Shows the grid in the content pane.
 */
public void showGrid(){
                contentPane.removeAll();
                gridPanel = new GridPanel(grid);
                contentPane.add(gridPanel, BorderLayout.CENTER);
                contentPane.revalidate();
                contentPane.repaint();
}
/***
 * Overrides the actionPerformed method, which now gives the controller parameters to handle the action performed in the menu bar.
 * @param e Action event made in the menu bar.
 */
    @Override
public void actionPerformed(ActionEvent e) {
    String action = ((JMenuItem) e.getSource()).getText();
    controller.handleMenuAction(action);
}
/***
 * Changes the size of the grid shown on the content pane.
 */
    private void changeGridSize() {
    String input = JOptionPane.showInputDialog(null, "Enter new grid size:", "Change Grid Size", JOptionPane.PLAIN_MESSAGE);
    if (input != null) {
        try {
            int newSize = Integer.parseInt(input);
            if (newSize > 0) {
                contentPane.removeAll();
                grid = new Grid(newSize);
                gridPanel = new GridPanel(grid);
                contentPane.add(gridPanel, BorderLayout.CENTER);
                contentPane.revalidate();
                contentPane.repaint();
            } else {
                JOptionPane.showMessageDialog(null, "Size must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid size entered!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
/***
 * Shows the window which pops out when user chooses the "Cell" option in the options menu, leading to editing the cells on the grid.
 */
    private void changeCells() {
    if (JOptionPane.showConfirmDialog(null, "Enter cell editing mode?", "Change Cells", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        gridPanel.getController().enableEditingMode();
        gridPanel.requestFocusInWindow();
    }
}
/***
 * Class which represents the grid panel used in the game's window.
 */
@Getter
@Setter
    public class GridPanel extends JPanel {
        private final Grid grid;
        private Controller controller;
/***
 * The constructor of the grid panel class, sets the grid used by it.
 * @param grid The grid used to set the grid on the screen.
 */
        public GridPanel(Grid grid) {
            this.grid = grid;
            setPreferredSize(new Dimension(300, 300));
            setFocusable(true);
            addKeyListener(new KeyAdapter(){
                @Override
                public void keyPressed(KeyEvent e){
                    if(controller!=null){
                        controller.handleKeyPress(e.getKeyCode());
                    }
                }
            });
}
/***
 * Overrides the paintComponent function, letting it paint the cells on a grid accordingly.
 * This method is called whenever the panel needs to be redrawn.
 * It will render each cell in the grid and also highlight the selected cell when in editing mode.
 * 
 * @param g The Graphics object used for drawing on the panel.
 */
           @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int cellSize = Math.min(getWidth() / grid.getWidth(), getHeight() / grid.getHeight());
        for (int row = 0; row < grid.getHeight(); row++) {
            for (int col = 0; col < grid.getWidth(); col++) {
                g.setColor(grid.getCell(row, col).getAliveStatus() == CellState.ALIVE ? Color.PINK : Color.WHITE);
                g.fillRect(col * cellSize, row * cellSize, cellSize, cellSize);
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(col * cellSize, row * cellSize, cellSize, cellSize);
            }
        }
       if (controller != null && controller.isEditingMode()) {
    int selectedRow = controller.getSelectedRow();
    int selectedCol = controller.getSelectedCol();
    g.setColor(Color.RED);
    g.drawRect(selectedCol * cellSize, selectedRow * cellSize, cellSize, cellSize);
}
    }
}
/***
 * Method which shows the text on a content pane in the "About" section in the Info menu.
 */
    public void showAboutPanel() {
        contentPane.removeAll();
        JTextArea aboutText = new JTextArea("""
The Game of Life, also known as Conway's Game of Life or simply Life, is a cellular automaton devised by the British mathematician John Horton Conway in 1970.
It is a zero-player game, meaning that its evolution is determined by its initial state, requiring no further input. One interacts with the Game of Life by creating an initial configuration and observing how it evolves.
Every cell interacts with its eight neighbors, which are the cells that are horizontally, vertically, or diagonally adjacent. At each step in time, the following transitions occur:
                                      
1. Any live cell with fewer than two live neighbours dies, as if by underpopulation.
2. Any live cell with two or three live neighbours lives on to the next generation.
3. Any live cell with more than three live neighbours dies, as if by overpopulation.
4. Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
        """);
        aboutText.setEditable(false);
        aboutText.setWrapStyleWord(true);
        aboutText.setLineWrap(true);
        contentPane.add(new JScrollPane(aboutText), BorderLayout.CENTER);
        contentPane.revalidate();
        contentPane.repaint();
    }
    /***
     * The CellTableModel class is a custom table model that provides data for displaying the 
     * status (Alive or Dead) of cells in a grid, used in a JTable for visualization.
     */
    public class CellTableModel extends AbstractTableModel {
    private final Grid grid;
    /***
     * Constructor to initialize the grid.
     * @param grid The grid that holds the cell data.
     */
    public CellTableModel(Grid grid) {
        this.grid = grid;
    }
    /***
     * Returns the number of rows in the table, which corresponds to the height of the grid.
     * @return The height of the grid.
     */
    @Override
    public int getRowCount() {
        return grid.getHeight();
    }
    /***
     * Returns the number of columns in the table, which corresponds to the width of the grid.
     * @return The width of the grid.
     */
    @Override
    public int getColumnCount() {
        return grid.getWidth();
    }
    /***
     * Returns the value of a specific cell in the table. Displays "Alive" or "Dead" based on the cell's state.
     * @param rowIndex The row index of the cell.
     * @param columnIndex The column index of the cell.
     * @return "Alive" if the cell is alive, otherwise "Dead".
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return grid.getCell(rowIndex, columnIndex).getAliveStatus();
    }
    /***
     * Returns the column name for the table (e.g., "Col 0", "Col 1", etc.)
     * @param column The column index.
     * @return The name of the column, formatted as "Col" followed by the column index.
     */
    @Override
    public String getColumnName(int column) {
        return "Col " + column;
    }
}
    /***
    * Method to display a table of cell statuses in a JFrame.
    */
    public void showCellTable() {
    JFrame tableFrame = new JFrame("Cell Status Table");
    tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JTable table = new JTable(new CellTableModel(grid));
    table.setPreferredScrollableViewportSize(new Dimension(500, 300));
    table.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(table);
    tableFrame.add(scrollPane);
    tableFrame.pack();
    tableFrame.setLocationRelativeTo(null);
    tableFrame.setVisible(true);
}
    /***
    * Clears all components from the content pane and repaints it.
    */
    public void clearAndPaintContentPane(){
                contentPane.removeAll();
                contentPane.revalidate();
                contentPane.repaint();
    }
}
