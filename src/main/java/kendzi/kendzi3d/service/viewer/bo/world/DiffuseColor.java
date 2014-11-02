package kendzi.kendzi3d.service.viewer.bo.world;

import java.awt.Color;

public class DiffuseColor extends Diffuse {

    private Color color;

    public DiffuseColor() {
        super(DiffuseType.color);
    }

    public DiffuseColor(Color color) {
        super(DiffuseType.color);
        this.color = color;
    }

    /**
     * @return the color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(Color color) {
        this.color = color;
    }

}
