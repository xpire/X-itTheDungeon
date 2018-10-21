package  main.client.structure;

/**
 * Class representing the map structure
 */
public class MapStructure {
    public String mapName;
    public String mapContent;

    /**
     * Generic constructor
     * @param mapName : name of the map
     * @param mapContent : map content in a single string form
     */
    public MapStructure(String mapName, String mapContent) {
        this.mapName = mapName;
        this.mapContent = mapContent;
    }
}
