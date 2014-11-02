/*
 * This software is provided "AS IS" without a warranty of any kind. You use it
 * on your own risk and responsibility!!! This file is shared under BSD v3
 * license. See readme.txt and BSD3 file for details.
 */

package kendzi.kendzi3d.service.viewer.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

@Component
public class OverpassService {

    /**
     * Overpass default url.
     */
    private final static String OVERPASS_URL = "http://www.overpass-api.de/api/interpreter";

    /**
     * Overpass url.
     */
    private String overpassUrl = OVERPASS_URL;

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException,
            XPathExpressionException {

        String q = "<osm-script>" //
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
                + " <print limit=\"\" mode=\"meta\" order=\"id\"/>" //
                + " </osm-script>";

        q = q.replaceAll("\\{\\{bbox}}",
                " s=\"52.105892353376625\" w=\"15.670173168182371\" n=\"52.10792193310275\" e=\"15.675639510154724\" ");

        String xml = new OverpassService().findQuery(q);

        System.out.println(xml);

    }

    /**
     * Run given query on overpass server.
     * 
     * @param query
     *            query
     * @return response body
     */
    public String findQuery(String query) {
        return findQuery(query, "UTF-8");
    }

    /**
     * Run given query on overpass server.
     * 
     * @param query
     *            query
     * @param encoding
     *            query encoding
     * @return response body
     */
    public String findQuery(String query, String encoding) {

        HttpClient client = HttpClientBuilder.create().build();

        try {

            HttpPost post = new HttpPost(overpassUrl);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("data", query));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = client.execute(post);

            if (response == null) {
                throw new RuntimeException();
            }

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new IllegalStateException("overpass service returned error status: " + statusCode);
            }
            return EntityUtils.toString(response.getEntity(), "UTF-8");

        } catch (IOException e) {
            throw new IllegalStateException("error findQuery", e);
        }
    }
}
