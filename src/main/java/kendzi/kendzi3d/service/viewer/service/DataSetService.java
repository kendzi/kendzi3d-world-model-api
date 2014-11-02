package kendzi.kendzi3d.service.viewer.service;

import kendzi.math.geometry.bbox.Bbox2d;

import org.openstreetmap.josm.data.osm.DataSet;

/**
 * Provide data set.
 * 
 * @author Tomasz Kedziora (Kendzi)
 * 
 */
public interface DataSetService {

    /**
     * Provide data set for given bbox.
     * 
     * @param bbox
     *            requested bbox in lat/lon
     * @return data set for bbox
     */
    DataSet find(Bbox2d bbox);
}
