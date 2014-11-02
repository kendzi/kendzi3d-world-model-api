package kendzi.kendzi3d.service.viewer.dto;

import java.util.List;

import javax.vecmath.Point2d;

import kendzi.kendzi3d.world.quad.layer.Layer;

import org.openstreetmap.josm.data.osm.DataSet;

public class WorldQuadJob {
    private Point2d maxPoint;
    private Point2d minPoint;

    private DataSet dataSet;

    private List<Layer> layers;

    /**
     * @return the maxPoint
     */
    public Point2d getMaxPoint() {
        return maxPoint;
    }

    /**
     * @param maxPoint
     *            the maxPoint to set
     */
    public void setMaxPoint(Point2d maxPoint) {
        this.maxPoint = maxPoint;
    }

    /**
     * @return the minPoint
     */
    public Point2d getMinPoint() {
        return minPoint;
    }

    /**
     * @param minPoint
     *            the minPoint to set
     */
    public void setMinPoint(Point2d minPoint) {
        this.minPoint = minPoint;
    }

    /**
     * @return the dataSet
     */
    public DataSet getDataSet() {
        return dataSet;
    }

    /**
     * @param dataSet
     *            the dataSet to set
     */
    public void setDataSet(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    /**
     * @return the layers
     */
    public List<Layer> getLayers() {
        return layers;
    }

    /**
     * @param layers
     *            the layers to set
     */
    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }

}
