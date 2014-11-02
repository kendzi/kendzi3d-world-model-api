package kendzi.kendzi3d.service.viewer.facade;

import java.io.InputStream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import kendzi.kendzi3d.service.viewer.TextureFacade;
import kendzi.kendzi3d.service.viewer.service.TextureService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resource provides textures. Textures are taken from kendzi3d resources or
 * they are generated on fly.
 * 
 * @author Tomasz Kedziora (Kendzi)
 * 
 */
public class TextureFacadeImpl implements TextureFacade {

	static final Logger logger = LoggerFactory
			.getLogger(TextureFacadeImpl.class);

	private TextureService textureService = new TextureService();
 
	@Override
	public Response get(String textureName) {

		InputStream texture = textureService.getTexture(textureName);
		if (texture == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		ResponseBuilder ok = Response.ok(texture);
		ok.header("Access-Control-Allow-Origin", "*");
		ok.header("Access-Control-Allow-Methods", "POST, GET, OPTIONS");

		return ok.build();
	}
}
