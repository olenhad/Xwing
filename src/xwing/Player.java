/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xwing;

/**
 *
 * @author omer
 */
public class Player extends Entity {
    private Game game;
    public Player(Game game1, String ref,int x,int y){
        super(ref,x,y);
        this.game= game1;
    }
    public void move(double delta) {
		// if we're moving left and have reached the left hand side
		// of the screen, don't move
		if ((dx < 0) && (x < 10)) {
			return;
		}
		// if we're moving right and have reached the right hand side
		// of the screen, don't move
		if ((dx > 0) && (x > 750)) {
			return;
		}
                if((dy < 0)&& (y < 10)){
                    return;
                }
                if((dy > 0) && (y > 580)){
                    return;
                }
		
		super.move(delta);
	}
    public void collidedWith(Entity other){
        if(other instanceof Hostile){
            game.notifyDeath();
        }
    }
    
}
