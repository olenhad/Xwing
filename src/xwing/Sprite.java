/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xwing;

/**
 *
 * @author omer
 */
import java.awt.Graphics;
import java.awt.Image;
public class Sprite {
    private Image image;
    public Sprite(Image i1){
        this.image= i1;
    }
    public int getWidth() {
		return image.getWidth(null);
	}
    public int getHeight() {
		return image.getHeight(null);
	}
    public void draw(Graphics g,int x,int y) {
		g.drawImage(image,x,y,null);
	}
    
}
