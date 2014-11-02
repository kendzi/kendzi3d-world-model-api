package kendzi.kendzi3d.service.viewer.bo.world;

import java.util.List;

public class Quad {
    private double lat;
    private double lon;
    private double size;

    private List<QuadModel> quadModel;

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat
     *            the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the lon
     */
    public double getLon() {
        return lon;
    }

    /**
     * @param lon
     *            the lon to set
     */
    public void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * @return the size
     */
    public double getSize() {
        return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(double size) {
        this.size = size;
    }

    /**
     * @return the quadModel
     */
    public List<QuadModel> getQuadModel() {
        return quadModel;
    }

    /**
     * @param quadModel
     *            the quadModel to set
     */
    public void setQuadModel(List<QuadModel> quadModel) {
        this.quadModel = quadModel;
    }

}
