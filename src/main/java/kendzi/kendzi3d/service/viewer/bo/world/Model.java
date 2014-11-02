package kendzi.kendzi3d.service.viewer.bo.world;

import java.util.List;

public class Model {
    private String type = "k3dm";
    private String version = "0.0.1";
    private String id;
    private List<Double> vertex;
    private List<Double> normals;
    private List<List<Double>> textureCoords;
    private List<Face> faces;

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the vertex
     */
    public List<Double> getVertex() {
        return vertex;
    }

    /**
     * @param vertex
     *            the vertex to set
     */
    public void setVertex(List<Double> vertex) {
        this.vertex = vertex;
    }

    /**
     * @return the normals
     */
    public List<Double> getNormals() {
        return normals;
    }

    /**
     * @param normals
     *            the normals to set
     */
    public void setNormals(List<Double> normals) {
        this.normals = normals;
    }

    /**
     * @return the textureCoords
     */
    public List<List<Double>> getTextureCoords() {
        return textureCoords;
    }

    /**
     * @param textureCoords
     *            the textureCoords to set
     */
    public void setTextureCoords(List<List<Double>> textureCoords) {
        this.textureCoords = textureCoords;
    }

    /**
     * @return the faces
     */
    public List<Face> getFaces() {
        return faces;
    }

    /**
     * @param faces
     *            the faces to set
     */
    public void setFaces(List<Face> faces) {
        this.faces = faces;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
