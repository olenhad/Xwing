/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xwing;

/**
 *
 * @author omer
 */
public class Hostile extends Entity {
    private double moveSpeed = 0.5;
    private Game game;
    public Hostile(Game game1,String ref,int x,int y) {
		super(ref,x,y);
		
		this.game = game1;
		dx = moveSpeed;
	}
    @Override
    public void move(double delta) {
		// if we have reached the left hand side of the screen and
		// are moving left then request a logic update 
		if ((dx < 0) && (x < 10)) {
                    
			game.updateLogic();
                        dx=-dx;
                        
                        //doLogic();
		}
		// and vice vesa, if we have reached the right hand side of 
		// the screen and are moving right, request a logic update
                else if ((dx > 0) && (x > 750)) {
                    
			game.updateLogic();
                        dx=-dx;
                        
                        //doLogic();
		}
		
		// proceed with normal move
		super.move(delta);
	}
   
    public void doLogic() {
		// swap over horizontal movement and move down the
		// screen a bit
		//dx = -dx;
		y += 10;
		
		// if we've reached the bottom of the screen then the player
		// dies
		if (y > 570) {
			game.notifyDeath();
		}
	}
	public void collidedWith(Entity other) {
		// collisions with aliens are handled elsewhere
	}
    
}
