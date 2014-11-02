package kendzi.kendzi3d.service.viewer.bo.world;

import java.util.List;

public class QuadModel {

    private double x;

    private double y;

    private String format;

    private List<Model> model;

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x
     *            the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y
     *            the y to set
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param format
     *            the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * @return the model
     */
    public List<Model> getModel() {
        return model;
    }

    /**
     * @param model
     *            the model to set
     */
    public void setModel(List<Model> model) {
        this.model = model;
    }
}
