package kendzi.kendzi3d.service.viewer.exporter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import kendzi.jogl.model.factory.FaceFactory.FaceType;
import kendzi.jogl.model.geometry.Face;
import kendzi.jogl.model.geometry.Mesh;
import kendzi.jogl.model.geometry.Model;
import kendzi.jogl.model.geometry.TextCoord;
import kendzi.kendzi3d.collada.TextExport;
import kendzi.kendzi3d.service.viewer.bo.world.Diffuse;
import kendzi.kendzi3d.service.viewer.bo.world.DiffuseColor;
import kendzi.kendzi3d.service.viewer.bo.world.DiffuseMultiLayer;
import kendzi.kendzi3d.service.viewer.bo.world.DiffuseTexture;
import kendzi.kendzi3d.service.viewer.bo.world.DiffuseType;
import kendzi.kendzi3d.service.viewer.bo.world.Material;

public class K3dExporter extends TextExport {

    private static class ModelVertex {
        private Point3d vertex;
        private Vector3d normal;
        private List<TextCoord> tcs;

        public ModelVertex(Point3d vertex, Vector3d normal, List<TextCoord> tcs) {
            super();
            this.vertex = vertex;
            this.normal = normal;
            this.tcs = tcs;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ModelVertex other = (ModelVertex) obj;
            if (normal == null) {
                if (other.normal != null) {
                    return false;
                }
            } else if (!normal.equals(other.normal)) {
                return false;
            }
            if (tcs == null) {
                if (other.tcs != null) {
                    return false;
                }
            } else if (!tcs.equals(other.tcs)) {
                return false;
            }
            if (vertex == null) {
                if (other.vertex != null) {
                    return false;
                }
            } else if (!vertex.equals(other.vertex)) {
                return false;
            }
            return true;
        }

        /*
         * (non-Javadoc)
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (normal == null ? 0 : normal.hashCode());
            result = prime * result + (tcs == null ? 0 : tcs.hashCode());
            result = prime * result + (vertex == null ? 0 : vertex.hashCode());
            return result;
        }

    }

    private static List<Integer> convertToTriangles(int[] vertIndex, int type) {
        List<Integer> ret = new ArrayList<Integer>();

        if (type == FaceType.QUADS.getType()) {

            for (int offset = 0; offset < vertIndex.length / 4; offset++) {
                int i = offset * 4;

                int i0 = vertIndex[i];
                int i1 = vertIndex[i + 1];
                int i2 = vertIndex[i + 2];
                int i3 = vertIndex[i + 3];

                ret.add(i0);
                ret.add(i1);
                ret.add(i2);

                ret.add(i0);
                ret.add(i2);
                ret.add(i3);
            }

        } else if (type == FaceType.TRIANGLES.getType()) {

            for (int offset = 0; offset < vertIndex.length; offset++) {

                int i0 = vertIndex[offset];

                ret.add(i0);
            }
        } else if (type == FaceType.TRIANGLE_FAN.getType()) {

            for (int offset = 2; offset < vertIndex.length; offset++) {

                int i0 = vertIndex[0];
                int i1 = vertIndex[offset - 1];
                int i2 = vertIndex[offset];

                ret.add(i0);
                ret.add(i1);
                ret.add(i2);
            }
        } else if (type == FaceType.QUAD_STRIP.getType()) {

            for (int offset = 1; offset < vertIndex.length / 2; offset++) {
                int i = offset * 2;

                int i0 = vertIndex[i - 2];
                int i1 = vertIndex[i - 1];
                int i2 = vertIndex[i];
                int i3 = vertIndex[i + 1];

                ret.add(i0);
                ret.add(i1);
                ret.add(i2);

                ret.add(i1);
                ret.add(i2);
                ret.add(i3);
            }

        } else {
            throw new RuntimeException("Face type : " + type + " not supported");
        }

        return ret;
    }

    private Map<DiffuseType, kendzi.kendzi3d.service.viewer.bo.world.Model> grouped = new EnumMap<DiffuseType, kendzi.kendzi3d.service.viewer.bo.world.Model>(
            DiffuseType.class);

    Map<ModelVertex, Integer> vertexCache = new HashMap<K3dExporter.ModelVertex, Integer>();

    @Override
    public void addModel(Model modelToAdd) throws Exception {
        for (Mesh meshToAdd : modelToAdd.mesh) {
            kendzi.kendzi3d.service.viewer.bo.world.Material material = getMaterial(meshToAdd, modelToAdd);

            DiffuseType diffuseType = material.getDiffuse().getType();

            for (Face face : meshToAdd.face) {

                validateFaceMaterial(face, diffuseType);

                addToGrupedMap(material, diffuseType, face, meshToAdd, modelToAdd.getSource());
            }
        }

    }

    public List<kendzi.kendzi3d.service.viewer.bo.world.Model> getModels() {
        List<kendzi.kendzi3d.service.viewer.bo.world.Model> ret = new ArrayList<kendzi.kendzi3d.service.viewer.bo.world.Model>();
        for (DiffuseType diffuseType : grouped.keySet()) {
            ret.add(grouped.get(diffuseType));

        }
        return ret;
    }

    @Override
    public Map<String, String> getTextureKeys() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void save(String fileName) throws Throwable {
        // TODO Auto-generated method stub
    }

    private int addModelVertex(kendzi.kendzi3d.service.viewer.bo.world.Model model, Point3d vertex, Vector3d normal,
            List<TextCoord> tcs) {

        ModelVertex mv = new ModelVertex(vertex, normal, tcs);

        if (vertexCache.containsKey(mv)) {
            return vertexCache.get(mv);
        }

        model.getVertex().add(vertex.x);
        model.getVertex().add(vertex.y);
        model.getVertex().add(vertex.z);

        model.getNormals().add(normal.x);
        model.getNormals().add(normal.y);
        model.getNormals().add(normal.z);

        for (int i = 0; i < tcs.size(); i++) {
            model.getTextureCoords().get(i).add(tcs.get(i).u);
            model.getTextureCoords().get(i).add(tcs.get(i).v);
        }
        int index = model.getVertex().size() / 3 - 1;

        vertexCache.put(mv, index);

        return index;
    }

    private void addToGrupedMap(kendzi.kendzi3d.service.viewer.bo.world.Material material, DiffuseType diffuseType, Face face,
            Mesh mesh, String id) {

        kendzi.kendzi3d.service.viewer.bo.world.Model model = getOrCreateModel(diffuseType);

        kendzi.kendzi3d.service.viewer.bo.world.Face faceToAdd = getOrCreateFace(model, material);

        List<Integer> vertexIndexes = convertToTriangles(face.vertIndex, face.type);
        List<Integer> normalIndexes = convertToTriangles(face.normalIndex, face.type);

        List<List<Integer>> textureInexes = new ArrayList<List<Integer>>();
        for (int i = 0; i < face.coordIndexLayers.length; i++) {
            textureInexes.add(convertToTriangles(face.coordIndexLayers[i], face.type));
        }

        if (id != null) {
            model.setId(model.getId() + "," + id);
        }

        for (int i = 0; i < vertexIndexes.size(); i++) {

            Point3d vertex = mesh.vertices[vertexIndexes.get(i)];
            Vector3d normal = mesh.normals[normalIndexes.get(i)];
            List<TextCoord> textureCoordinates = new ArrayList<TextCoord>();
            for (List<Integer> tc : textureInexes) {
                textureCoordinates.add(mesh.texCoords[tc.get(i)]);
            }

            int index = addModelVertex(model, vertex, normal, textureCoordinates);

            faceToAdd.getIndexes().add(index);
        }
    }

    private Material convert(kendzi.jogl.model.geometry.material.Material m) {
        Material ret = new Material();
        ret.setAmbient(m.getAmbientDiffuse().getAmbientColor());

        Diffuse diffuse = getDiffuse(m, ret);

        ret.setDiffuse(diffuse);
        ret.setShininess((int) m.getOther().getShininess());
        ret.setSpecular(m.getOther().getSpecularColor());

        return ret;
    }

    private Diffuse getDiffuse(kendzi.jogl.model.geometry.material.Material model, Material material) {
        material.setAmbient(model.getAmbientDiffuse().getAmbientColor());
        Diffuse diffuse = null;
        if (model.getTexturesComponent().isEmpty()) {
            diffuse = new DiffuseColor(model.getAmbientDiffuse().getDiffuseColor());
        } else if (model.getTexturesComponent().size() == 1 && model.getTexture0Color() == null) {
            diffuse = new DiffuseTexture(model.getTexture0());
            // } else if (model.getTexturesComponent().size() == 1 &&
            // model.getTexture0Color() != null) {
            // throw new IllegalStateException("TODO");
        } else {
            String texture1 = null;
            if (model.getTexturesComponent().size() > 1) {
                texture1 = model.getTexturesComponent().get(1);
            }
            diffuse = new DiffuseMultiLayer(model.getTexture0Color(), model.getTexture0(), texture1);
        }
        return diffuse;
    }

    private kendzi.kendzi3d.service.viewer.bo.world.Material getMaterial(Mesh mesh, Model model) {
        return convert(model.materials.get(mesh.materialID));
    }

    private kendzi.kendzi3d.service.viewer.bo.world.Face getOrCreateFace(kendzi.kendzi3d.service.viewer.bo.world.Model model,
            Material material) {

        List<kendzi.kendzi3d.service.viewer.bo.world.Face> faces = model.getFaces();
        for (kendzi.kendzi3d.service.viewer.bo.world.Face face : faces) {
            if (material.equals(face.getMaterial())) {
                return face;
            }
        }

        kendzi.kendzi3d.service.viewer.bo.world.Face face = new kendzi.kendzi3d.service.viewer.bo.world.Face();
        face.setMaterial(material);
        face.setIndexes(new ArrayList<Integer>());

        faces.add(face);

        return face;
    }

    private kendzi.kendzi3d.service.viewer.bo.world.Model getOrCreateModel(DiffuseType diffuseType) {
        kendzi.kendzi3d.service.viewer.bo.world.Model model = grouped.get(diffuseType);

        if (model == null) {
            model = new kendzi.kendzi3d.service.viewer.bo.world.Model();
            model.setFaces(new ArrayList<kendzi.kendzi3d.service.viewer.bo.world.Face>());
            model.setVertex(new ArrayList<Double>());
            model.setNormals(new ArrayList<Double>());

            switch (diffuseType) {
            case color:
                break;
            case texture: {
                ArrayList<List<Double>> textureCoords = new ArrayList<List<Double>>();
                textureCoords.add(new ArrayList<Double>());
                model.setTextureCoords(textureCoords);
                break;
            }
            case multilayer: {
                ArrayList<List<Double>> textureCoords = new ArrayList<List<Double>>();
                textureCoords.add(new ArrayList<Double>());
                textureCoords.add(new ArrayList<Double>());
                model.setTextureCoords(textureCoords);
                break;
            }
            default:
                throw new IllegalArgumentException("not supported diffuse type");
            }

            grouped.put(diffuseType, model);
        }
        return model;
    }

    private void validateFaceMaterial(Face face, DiffuseType diffuseType) {
        // TODO Auto-generated method stub

    }

}
