package pl.polsl.lab1.maria.wyganowska.gameoflife.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

/**
 * Class which manages the information given by a user
 * @author Maria Wyganowska
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
public class User {
   /**
    * The integer storing the user's input (all menus use integers to navigate/set parameters)
    */
    public int input;
    private int desiredSize;
    /**
     * Method which validates whether the input entered by the user is an actual option in a menu
     * @param maxChoice The maximum number that can be selected in a menu
     * @param minChoice The minimum number that can be selected in a menu
     */
    public void validateInput(int maxChoice, int minChoice){
        if(input < minChoice || input > maxChoice){
                        throw new IllegalArgumentException("invalid_choice");
                    }
    }
}