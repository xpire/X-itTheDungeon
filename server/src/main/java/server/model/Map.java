package server.model;

/**
 * This classhandles json format files
 * @author Peiyu Tang 2018 Oct.
 */
public class Map {
    Header header;
    String content;

    public Map(Header header, String content) {
        this.header = header;
        this.content = content;
    }

    public Header getHeader() { return header; }
    public String getContent() { return content; }

    public boolean allowCo(Map map) {
        return this.header.getMapname().equals(map.getHeader().getMapname()) && this.header.getName().equals(map.getHeader().getName());
    }
}
