package pl.polsl.lab1.maria.wyganowska.gameoflife.model;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

/**
 * Class representing a single cell in a grid
 * @author Maria Wyganowska
 * @version 2.0
 */

@Setter
@Getter
@AllArgsConstructor
public class Cell {
   private CellState aliveStatus;
   private Coordinates cellCoordinates;

/**
 * Method which switches the status of a single cell to an opposite one (alive/dead)
 */
public void switchAliveStatus(){
    if (this.aliveStatus == null) {
        throw new IllegalArgumentException("The aliveStatus cannot be null when switching status.");
    }
    else if (this.cellCoordinates.col() < 0 || this.cellCoordinates.row() < 0){
        throw new IllegalArgumentException("The cell is out of bounds.");
    }
    this.aliveStatus = (this.aliveStatus == CellState.ALIVE) ? CellState.DEAD : CellState.ALIVE;
   }
}