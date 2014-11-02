package kendzi.kendzi3d.service.viewer;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * Facade for providing texture streams.
 * 
 * @author Tomasz Kedziora (Kendzi)
 * 
 */
@Path("/texture")
@Api(value = "/texture", description = "Resource for textures", position = 2)
public interface TextureFacade {

	/**
	 * Gets texture stream from kendzi3d resources or build it using name.
	 * 
	 * @param textureName
	 *            texture key/name
	 * @return texture stream in response
	 */
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces("image/jpeg")
	@ApiOperation(//
	position = 1, //
	value = "Provide texture data.", //
	notes = "Provide texture stream in jpeg format.", //
	produces = "image/jpeg", //
	response = OutputStream.class)
	@ApiResponses(value = { @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "When ok") })
	Response get(@ApiParam(value = "The texture name/key.") @QueryParam("key") String textureName);

}