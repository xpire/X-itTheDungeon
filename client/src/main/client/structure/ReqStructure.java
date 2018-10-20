package  main.client.structure;

public class ReqStructure {
    public String name;
    public String mapname;

    public ReqStructure(String name, String mapname) {
        this.name = name;
        this.mapname = mapname;
    }

    @Override
    public String toString() {
        return "Map name: " + mapname + " " + "Author: " + name;
    }
}
