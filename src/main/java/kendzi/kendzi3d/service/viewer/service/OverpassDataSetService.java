package kendzi.kendzi3d.service.viewer.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import kendzi.math.geometry.bbox.Bbox2d;

import org.apache.log4j.Logger;
import org.openstreetmap.josm.data.osm.DataSet;
import org.openstreetmap.josm.gui.progress.NullProgressMonitor;
import org.openstreetmap.josm.io.IllegalDataException;
import org.openstreetmap.josm.io.OsmReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provide dataset using overpass as datasource.
 * 
 * @author Tomasz Kedziora (Kendzi)
 * 
 */
@Component
public class OverpassDataSetService implements DataSetService {

    /** Log. */
    private static final Logger log = Logger.getLogger(OverpassDataSetService.class);

    @Autowired
    private OverpassService overpassService;

    /**
     * Overpass query.
     */
    private final static String bboxQuery2 = "<osm-script>" //
            + "<union>"//
            + "<query type=\"node\"> "//
            + "  <bbox-query {{bbox}}/>"//
            + "</query>"//
            + "<query type=\"way\"> "//
            + "  <bbox-query {{bbox}}/>"//
            + "</query>"//
            + "<query type=\"relation\"> "//
            + "  <bbox-query {{bbox}}/>"//
            + "</query>"//
            + "</union>" //
            + "<union>" //
            + "  <item/>" //
            + "  <recurse type=\"down\"/>" //
            + "</union>" //
            + " <print limit=\"\" mode=\"meta\" order=\"id\"/>" //
            + " </osm-script>";

    /**
     * XXX move to external file. Overpass query.
     */
    private final static String bboxQuery = "<osm-script>"//
            + "<union into=\"_\">"//
            + "<bbox-query {{bbox}} />"//
            + "<recurse into=\"x\" type=\"node-relation\"/>"//
            + "<query type=\"way\">"//
            + "<bbox-query {{bbox}} />"//
            + "</query>"//
            + "<recurse into=\"x\" type=\"way-node\"/>"//
            + "<recurse type=\"way-relation\"/>"//
            + "</union>"//
            + "<print mode=\"meta\"/>"//
            + "</osm-script>";

    @Override
    public DataSet find(Bbox2d bbox) {

        String s = format(bbox.getyMin());
        String w = format(bbox.getxMin());
        String n = format(bbox.getyMax());
        String e = format(bbox.getxMax());

        String bboxString = " s=\"" + s + "\" w=\"" + w + "\" n=\"" + n + "\" e=\"" + e + "\" ";

        String query = bboxQuery.replaceAll("\\{\\{bbox}}", bboxString);

        if (log.isDebugEnabled()) {
            log.debug("input query: " + query);
        }

        String data = overpassService.findQuery(query);

        if (log.isDebugEnabled()) {
            log.debug("output data: " + data);
        }

        return read(data);
    }

    private DataSet read(String data) {

        try {
            InputStream in = new ByteArrayInputStream(data.getBytes("UTF-8"));

            return OsmReader.parseDataSet(in, NullProgressMonitor.INSTANCE);

        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("wrong encoding", e);
        } catch (IllegalDataException e) {
            throw new IllegalStateException("illegal data", e);
        }
    }

    private String format(double number) {
        return ("" + number).replaceAll(",", ".");
    }

    public static void main(String[] args) {
        OverpassDataSetService s = new OverpassDataSetService();
        System.out.println(s.format(1.2200000022d));
    }

}
