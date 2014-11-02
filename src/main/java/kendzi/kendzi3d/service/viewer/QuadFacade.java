package kendzi.kendzi3d.service.viewer;

import java.net.HttpURLConnection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import kendzi.kendzi3d.service.viewer.bo.world.Quad;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/** Facade for quad world model.
 * @author Tomasz Kedziora (Kendzi)
 *
 */
@Path("/quad")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/quad", description = "Resource for quad model", position = 1)
public interface QuadFacade {

    /** Gets world models in form of quad.
     * @param lat lat coordinate of quad center
     * @param lon lon coordinate of quad center
     * @param size size of quad
     * @return world models
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(//
    position = 1, //
    value = "Provide data for requested quad.", //
    notes = "Provide data for requested quad.", //
    produces = MediaType.APPLICATION_JSON, //
    response = Quad.class //
    )
    @ApiResponses(value = { @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "When ok") })
    Response get(@ApiParam(defaultValue = "52.14021") @QueryParam("lat") double lat,
            @ApiParam(defaultValue = "15.76308") @QueryParam("lon") double lon,
            @ApiParam(defaultValue = "44") @QueryParam("size") double size);

}