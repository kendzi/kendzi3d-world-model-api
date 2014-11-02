package kendzi.kendzi3d.service.viewer.service;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;

import kendzi.geo.util.LatLonUtil;
import kendzi.josm.kendzi3d.jogl.model.Perspective3D;
import kendzi.kendzi3d.josm.model.perspective.Perspective;
import kendzi.kendzi3d.service.viewer.bo.world.Model;
import kendzi.kendzi3d.service.viewer.bo.world.Quad;
import kendzi.kendzi3d.service.viewer.bo.world.QuadModel;
import kendzi.kendzi3d.service.viewer.dto.WorldQuadJob;
import kendzi.kendzi3d.service.viewer.exporter.K3dExporter;
import kendzi.kendzi3d.service.viewer.module.LayerConfiguration;
import kendzi.kendzi3d.service.viewer.module.ModelViewerModule;
import kendzi.kendzi3d.world.BuildableWorldObject;
import kendzi.kendzi3d.world.StaticModelWorldObject;
import kendzi.kendzi3d.world.WorldObject;
import kendzi.kendzi3d.world.quad.ModelLayerBuilder;
import kendzi.math.geometry.bbox.Bbox2d;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Preferences;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.data.projection.Projection;
import org.openstreetmap.josm.gui.preferences.projection.ProjectionPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.inject.Guice;
import com.google.inject.Injector;

@Component
public class CenterQuadModelService {

    {
        initJOSMMinimal();
    }

    private LayerConfiguration layerConfiguration;

    @Autowired
    private OverpassDataSetService datasetService;

    public CenterQuadModelService() {

        Injector injector = Guice.createInjector(new ModelViewerModule(""));

        layerConfiguration = injector.getInstance(LayerConfiguration.class);
    }

    public Quad build(double lat, double lon, double edgeSize) {

        Bbox2d bbox = createBBox(lat, lon, edgeSize);

        // add datasource to quad

        DataSet dataSet = createDataSet(bbox);

        // create quad min max

        WorldQuadJob quad = createQuad(bbox, dataSet);

        // build static model for quad
        List<? extends WorldObject> models = buildModels(quad);

        // build not builded models
        for (WorldObject worldObject : models) {
            if (worldObject instanceof BuildableWorldObject) {
                ((BuildableWorldObject) worldObject).buildWorldObject();
            }
        }

        // convert model to world
        return convert(models);
    }

    private Quad convert(List<? extends WorldObject> models) {
        Map<kendzi.jogl.model.geometry.Model, List<Model>> modelInstances = new IdentityHashMap<kendzi.jogl.model.geometry.Model, List<Model>>();
        List<QuadModel> quadModels = new ArrayList<QuadModel>();
        for (WorldObject worldObject : models) {
            Point3d point = worldObject.getPoint();

            if (worldObject instanceof StaticModelWorldObject) {
                StaticModelWorldObject sm = (StaticModelWorldObject) worldObject;

                kendzi.jogl.model.geometry.Model model = sm.getModel();

                List<Model> out = modelInstances.get(model);

                if (out == null) {
                    K3dExporter exporter = new K3dExporter();
                    try {
                        exporter.addModel(model);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }

                    out = exporter.getModels();

                    modelInstances.put(model, out);
                }

                QuadModel qm = new QuadModel();
                qm.setModel(out);
                qm.setX(sm.getPoint().x);
                qm.setY(sm.getPoint().z);

                quadModels.add(qm);
            }

        }

        Quad quad = new Quad();
        quad.setQuadModel(quadModels);

        return quad;
    }

    /**
     * <br>
     * <b>Latitude</b> specifies the north-south position in degrees where valid
     * values are in the [-90,90] and positive values specify positions north of
     * the equator. <br>
     * <b>Longitude</b> specifies the east-west position in degrees <br>
     * <img alt="lat/lon" src=
     * "https://upload.wikimedia.org/wikipedia/commons/thumb/6/62/Latitude_and_Longitude_of_the_Earth.svg/500px-Latitude_and_Longitude_of_the_Earth.svg.png"
     * > <br>
     */
    private Bbox2d createBBox(double lat, double lon, double edgeSize) {
        double size = edgeSize / 2d;

        double deltaLat = LatLonUtil.deltaLat(size);
        double deltaLon = LatLonUtil.deltaLon(lat, size);

        Bbox2d bbox = new Bbox2d();
        bbox.addPoint(lon - deltaLon, lat - deltaLat);
        bbox.addPoint(lon + deltaLon, lat + deltaLat);
        return bbox;
    }

    private List<? extends WorldObject> buildModels(WorldQuadJob quad) {

        Point2d center = new Point2d(quad.getMaxPoint());
        center.add(quad.getMinPoint());
        center.scale(0.5d);

        Perspective perspective = setupPerspective3D(center.x, center.y);

        return ModelLayerBuilder.bulid(quad.getLayers(), quad.getDataSet(), perspective);
    }

    private static void initJOSMMinimal() {

        Main.pref = new Preferences();
        ProjectionPreference.setProjection("core:mercator", null);

    }

    public static void main(String[] args) {
        CenterQuadModelService c = new CenterQuadModelService();
        System.out.println(c);
    }

    private Perspective setupPerspective3D(double x, double y) {

        Projection proj = Main.getProjection();

        LatLon latLon = new LatLon(y, x);

        EastNorth center = proj.latlon2eastNorth(latLon);

        LatLon l1 = proj.eastNorth2latlon(center.add(1, 0));
        LatLon l2 = proj.eastNorth2latlon(center.add(-1, 0));

        double dist = l1.greatCircleDistance(l2);

        double scale = dist / 2d;
        // XXX
        return new Perspective3D(scale, center.getX(), center.getY());
    }

    private DataSet createDataSet(Bbox2d bbox) {

        return datasetService.find(bbox);
    }

    private WorldQuadJob createQuad(Bbox2d bbox, DataSet dataSet) {
        WorldQuadJob worldQuadJob = new WorldQuadJob();
        worldQuadJob.setMinPoint(bbox.getMinPoint());
        worldQuadJob.setMaxPoint(bbox.getMaxPoint());
        worldQuadJob.setDataSet(dataSet);

        worldQuadJob.setLayers(layerConfiguration.getLayers());

        return worldQuadJob;
    }
}
