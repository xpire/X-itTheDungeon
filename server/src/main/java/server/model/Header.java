package server.model;

/**
 * Header object for comparison and map return
 * @author Peiyu Tang
 */
public class Header {
    private String name;
    private String mapname;

    public Header(String name, String mapname) {
        this.name = name;
        this.mapname = mapname;
    }

    public String getName() { return name; }

    public String getMapname() { return mapname; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Header)) return false;
        Header header = (Header) o;
        return header.getName().equals(this.name) && header.getMapname().equals(this.mapname);
    }
}
