/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xwing;

/**
 *
 * @author omer
 */
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
public class SpriteStore {
    private static SpriteStore single= new SpriteStore();
    public static SpriteStore get(){
        return single;
    }
    private HashMap sprites = new HashMap();
    public Sprite getSprite(String ref){
        if(sprites.get(ref)!=null){
            return (Sprite)sprites.get(ref);
        }
        BufferedImage sourceImage = null;
        try{
            URL url=this.getClass().getClassLoader().getResource(ref);
            if(url==null){
                fail(ref+" not found");
            }
            sourceImage = ImageIO.read(url);
            
        }catch(IOException e){
            fail("Failed to load"+ref);
        }
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        Image image1 = gc.createCompatibleImage(sourceImage.getWidth(), sourceImage.getHeight(), Transparency.BITMASK);
        image1.getGraphics().drawImage(sourceImage,0,0,null);
        Sprite sprite1 = new Sprite(image1);
        sprites.put(ref,sprite1);
        return sprite1;
    }
    
    private void fail(String message) {
		
		System.err.println(message);
		System.exit(0);
	}
    
}
