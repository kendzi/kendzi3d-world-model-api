package kendzi.kendzi3d.service.viewer.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import kendzi.jogl.texture.TextureCacheService;
import kendzi.kendzi3d.service.viewer.module.ModelViewerModule;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Provides textures streams.
 * 
 * @author Tomasz Kedziora (Kendzi)
 * 
 */
public class TextureService {
    private static Logger log = Logger.getLogger(TextureService.class);

    /**
     * Kendzi3d texture cache and texture builder.
     */
    private TextureCacheService textureCacheService;

    /**
     * Constructor.
     */
    public TextureService() {
        // inject kendzi3d dependences
        Injector injector = Guice.createInjector(new ModelViewerModule(""));

        textureCacheService = injector.getInstance(TextureCacheService.class);
    }

    /**
     * Gets texture jpeg stream for given key.
     * 
     * @param key
     *            texture name/key
     * @return texture stream
     */
    public InputStream getTexture(String key) {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");
        while (readers.hasNext()) {
            System.out.println("reader: " + readers.next());
        }
        BufferedImage image = textureCacheService.getImage(key);
        //
        // javax.swing.JLabel l = new javax.swing.JLabel();
        // l.setIcon(new ImageIcon(image));
        //
        // JFrame f = new JFrame();
        // f.add(l);
        // f.pack();
        // f.setVisible(true);
        //
        // // Create a new RGB image
        // BufferedImage bi = new BufferedImage(image.getWidth(),
        // image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        //
        // // Fill the new image with the old raster
        // bi.getRaster().setRect(image.getRaster());

        return imageToStream(image);
    }

    private InputStream imageToStream(BufferedImage bufferedImage) {
        if (bufferedImage == null) {
            return null;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", os);
        } catch (IOException e) {
            log.error("can't convert image to stream", e);
            return null;
        }
        return new ByteArrayInputStream(os.toByteArray());
    }
}
