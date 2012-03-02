/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xwing;

/**
 *
 * @author omer
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
  
public class Game extends Canvas {
    private BufferStrategy strategy;
    /** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	/** The list of all the entities that exist in our game */
	private ArrayList entities = new ArrayList();
	/** The list of entities that need to be removed from the game this loop */
	private ArrayList removeList = new ArrayList();
	/** The entity representing the player */
	private Entity ship;
	/** The speed at which the player's ship should move (pixels/sec) */
	private double moveSpeed = 1;
	/** The time at which last fired a shot */
	private long lastFire = 0;
	/** The interval between our players shot (ms) */
	private long firingInterval = 300;
	/** The number of hostiles left on the screen */
	private int hostileCount;
	
	/** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = false;
	/** True if the left cursor key is currently pressed */
	private boolean leftPressed = false;
	/** True if the right cursor key is currently pressed */
	private boolean rightPressed = false;
	/** True if we are firing */
	private boolean firePressed = false;
	/** True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean logicRequiredThisLoop = false;
	
	/**
	 * Construct our game and set it running.
	 */
        
    public Game(){
        JFrame Container = new JFrame("XWing");
        JPanel panel = (JPanel)Container.getContentPane();
        panel.setPreferredSize(new Dimension(800,600));
        panel.setLayout(null);
        setBounds(0,0,800,600);
        panel.add(this);
        setIgnoreRepaint(true);
        Container.pack();
        Container.setVisible(true);
        Container.setResizable(false);
        Container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
        addKeyListener(new KeyInputHandler());
        requestFocus();
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        initEntities();
    }
        
    private void startGame() {
		// clear out any existing entities and intialise a new set
		entities.clear();
		initEntities();
		
		// blank out any keyboard settings we might currently have
		leftPressed = false;
		rightPressed = false;
		firePressed = false;
	}
    private void initEntities(){
        ship = new Player(this, "images/XWing.png", 400,500);
        entities.add(ship);
        hostileCount=0;
        for(int a=0;a<3;a++){
            Entity droid = new Hostile(this, "images/droid.png",2*a/4*800,50);
            entities.add(droid);
            hostileCount++;
            
        }
                
    }
    /**
	 * Notification from a game entity that the logic of the game
	 * should be run at the next opportunity (normally as a result of some
	 * game event)
	 */
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
     public void removeEntity(Entity entity) {
		removeList.add(entity);
	}
    public void notifyDeath(){
        message = "the Empire won this sortie";
        waitingForKeyPress=true;
    } 
    public void notifyWin(){
        message = "The Rebellion lives for another day";
        waitingForKeyPress=true;
    }
    public void notifyHostileKilled(){
        hostileCount--;
        if(hostileCount==0){
            notifyWin();
        }
        /*for(int i=0;i<entities.size();i++){
            Entity entity1 = (Entity)entities.get(i);
            if(entity1 instanceof Hostile){
                entity1.setHorizontalMovement(entity1.getHorizontalMovement()*1);
                
            }
        }*/
    }
    public void tryToFire(){
        if(System.currentTimeMillis()-lastFire < firingInterval){
            return;
        }
        lastFire= System.currentTimeMillis();
        Shot fire1= new Shot(this,"images/Shot.png",ship.getX()+10, ship.getY()-30 );
        entities.add(fire1);
    }
    public void GameLoop(){
        long lastLoopTime = System.currentTimeMillis();
        while(gameRunning){
            long delta = System.currentTimeMillis() - lastLoopTime;
            Graphics2D g = (Graphics2D)strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0,0,800,600);
            
            if(!waitingForKeyPress){
                for(int i=0;i<entities.size();i++){
                    Entity entity = (Entity)entities.get(i);
                    entity.move(delta);
                    
                }
            }
                for(int i=0;i<entities.size();i++){
                    Entity entity= (Entity)entities.get(i);
                    entity.draw(g);
                }
                for(int n=0;n<entities.size();n++){
                    for(int r=n+1;r<entities.size();r++){
                        Entity me =(Entity) entities.get(n);
                        Entity it = (Entity) entities.get(r);
                        if(me.collidesWith(it)){
                            me.collidedWith(it);
                            it.collidedWith(me);
                        }
                    }
                }
                entities.removeAll(removeList);
                removeList.clear();
                if(logicRequiredThisLoop){
                    for(int i=0;i<entities.size();i++){
                        Entity entity =(Entity) entities.get(i);
                        entity.doLogic();
                        
                    }
                    logicRequiredThisLoop = false;
                }
                if(waitingForKeyPress){
                    g.setColor(Color.WHITE);
                    g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,200);
                    g.drawString("Press any key to continue",(800-g.getFontMetrics().stringWidth("Press any key to continue"))/2,300);
                    
                }
                g.dispose();
                strategy.show();
                ship.setHorizontalMovement(0);
                if(leftPressed && !rightPressed){
                    ship.setHorizontalMovement(-moveSpeed);
                    
                }
                else if(rightPressed && !leftPressed){
                    ship.setHorizontalMovement(moveSpeed);
                }
                if(firePressed){
                    tryToFire();
                }
                try{
                    Thread.sleep(40);
                }catch(Exception e){}
                
                
            
        }
    }
    private class KeyInputHandler extends KeyAdapter{
        private int keycount =1;
       
        public void keyPressed(KeyEvent e){
            if(waitingForKeyPress){
                return;
            }
            if(e.getKeyCode()==KeyEvent.VK_LEFT){
                leftPressed = true;
                
            }
            if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                rightPressed = true;
            }
            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                firePressed =true;
            }
            
        }
        public void keyReleased(KeyEvent e){
            if(waitingForKeyPress){
                return;
            }
            if(e.getKeyCode()==KeyEvent.VK_LEFT){
                leftPressed = false;
            }
            if(e.getKeyCode()==KeyEvent.VK_RIGHT){
                rightPressed = false;
            }
            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                firePressed = false;
            }
        }
       
        public void keyTyped(KeyEvent e){
            if(waitingForKeyPress){
                if(keycount ==1){
                    waitingForKeyPress=false;
                    startGame();
                    keycount=0;
                } else{
                    keycount++;
                }
            }
            if(e.getKeyChar()==27){
                System.exit(0);
            }
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Game g = new Game();
        g.GameLoop();
    }
}
