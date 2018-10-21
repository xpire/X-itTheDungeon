package  main.client.structure;

/**
 * Class representing a request to the server
 */
public class ReqStructure {
    public String name;
    public String mapname;

    /**
     * Generic constructor
     * @param name : username
     * @param mapname : name of map being requested
     */
    public ReqStructure(String name, String mapname) {
        this.name = name;
        this.mapname = mapname;
    }

    @Override
    public String toString() {
        return "Map name: " + mapname + " " + "Author: " + name;
    }
}
