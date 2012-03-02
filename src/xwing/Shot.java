/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xwing;

/**
 *
 * @author omer
 */
public class Shot extends Entity {
    private double moveSpeed = -1;
	/** The game in which this entity exists */
    private Game game;
	/** True if this shot has been "used", i.e. its hit something */
    private boolean hit = false;
    public Shot(Game game1,String sprite,int x,int y) {
	super(sprite,x,y);
		
	this.game = game1;
		
	dy = moveSpeed;
        
    }
    	public void move(long delta) {
		// proceed with normal move
		super.move(delta);
		
		// if we shot off the screen, remove ourselfs
		if (y < -100) {
			game.removeEntity(this);
		}
	}
        public void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		if (hit) {
			return;
		}
		
		// if we've hit an alien, kill it!
		if (other instanceof Hostile) {
			// remove the affected entities
			game.removeEntity(this);
			game.removeEntity(other);
			
			// notify the game that the alien has been killed
			game.notifyHostileKilled();
			hit = true;
		}
        }
}
