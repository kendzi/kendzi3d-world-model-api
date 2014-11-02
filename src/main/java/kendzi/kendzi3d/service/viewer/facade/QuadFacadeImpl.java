package kendzi.kendzi3d.service.viewer.facade;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import kendzi.kendzi3d.service.viewer.QuadFacade;
import kendzi.kendzi3d.service.viewer.bo.world.Quad;
import kendzi.kendzi3d.service.viewer.service.CenterQuadModelService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Facade implementation for providing quad world models.
 * 
 * @author Tomasz Kedziora (Kendzi)
 * 
 */
public class QuadFacadeImpl implements QuadFacade {
	static final Logger logger = LoggerFactory.getLogger(QuadFacadeImpl.class);

	@Autowired
	CenterQuadModelService centerQuadModelService;

	@Override
	public Response get(double lat, double lon, double size) {

		Quad quad = centerQuadModelService.build(lat, lon, size);

		ResponseBuilder ok = Response.ok(quad);
		ok.header("Access-Control-Allow-Origin", "*");

		return ok.build();
	}
}
