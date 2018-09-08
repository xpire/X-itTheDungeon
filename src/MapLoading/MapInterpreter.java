package MapLoading;

import java.util.ArrayList;

public class MapInterpreter {

    public ArrayList<Entity> getTileEntities(String tileInput) {
        ArrayList<Entity> tileEntities = new ArrayList<>();

        char[] eachEntity = tileInput.toCharArray();
        for (char ch : eachEntity) {

        }

        return tileEntities;
    }

}

/*

    Entity Symbols:

    P : Player
    * : Wall
    X : Exit
    / : Switch
    O : Boulder
    | : Door            must link with a key
    K : Key
    $ : Treasure
    + : Sword
    - : Arrow
    ! : Bomb
    # : Pit
    1 : Hunter
    2 : Strategist
    3 : Hound
    4 : Coward
    > : Invincibility Potion
    ^ : Hover Potion

    Objectives:
    A : Exit the Dungeon
    B : Collect all Treasure
    C : Kill all Enemies
    D : Activate all Switches

*/
