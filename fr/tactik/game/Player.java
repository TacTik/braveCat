/**
 * 
 */
package fr.tactik.game;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * This is the player Class.
 * 
 * @author Juliette Belin, Alice Neichols, Denis Tribouillois 
 * @version 1.0
 */
public class Player extends Mobile{

	/**
	 * This function is the player constructor.
	 * @param posX
	 * 				this is the x position of the player.
	 * @param posY 
	 * 				this is the y position of the player.
	 * @param sizeX
	 * 				this is the x size of the player.
	 * @param sizeY 
	 * 				this is the y size of the player.
	 * @param isWalkable 
	 * 				this parameter indicate if the mobile is walkable or not.
	 * @param path 
	 * 				this the path of the player.
	 * @param current 
	 * 				It indicate what is the current texture if the mobile have several textures.
	 * @param id 
	 * 				This is the id of the player.
	 */
	public Player(float posX, float posY, int sizeX, int sizeY, boolean isWalkable, String path,
		int current, int id) {
		super(posX, posY, sizeX, sizeY, isWalkable, path, current, id);
		// TODO Auto-generated constructor stub
		
		
	}

	
	static String rootdir = System.getProperty("user.dir");
	float jump = 0;
	int jumpsAvailable = 0;
	boolean running = false;
	
	int lifePoints = 7;
	int state;
	Vector<String> inventory = new Vector<String>();
	boolean triggerNextLevel = false;
	
	/**
	 * This function make the player jumping.
	 */
	public void jump(){
		this.moveUp(jump);
		if (this.jump > 0)
			this.jump -= 0.4;
		else this.jump =0;
	}
	
	/**
	 * This the gravity function for the player.
	 */
	public void gravity(){ 
		this.yspeed += 4;
	}
	
	/**
	 * This function manage the collision with the player and the other items in the level.
	 * @param level
	 * 			this is the level.
	 * @param nbLines
	 * 			this is the number of lines of the level.
	 * @param nbColumns
	 * 			this is the number of columns of the level.
	 * @param stills
	 * 			this is all still element in the level.
	 * @param controlPlayer
	 * 			this is the control player.
	 */
	public void collision(int[][] level, int nbLines, int nbColumns, Vector<Still> stills, ControlerPlayer controlPlayer){	
		
		// Bottom
		if ((int)(posY + this.yspeed - 2) + 50 >= nbLines * 50){
			this.yspeed = 0;
			jumpsAvailable = 2;
		}
		else if (level[((int)(posY + this.yspeed - 2) / 50) + 1][(int)(posX + 8) / 50] != 0 ||
				level[((int)(posY + this.yspeed - 2) / 50) + 1][((int)(posX) / 50) + 1] != 0 ||
				level[((int)(posY + this.yspeed - 2) / 50) + 1][((int)(posX - 8) / 50) + 2] != 0){
			
			// Life bonus
			if (level[((int)(posY + this.yspeed - 2) / 50) + 1][((int)(posX) / 50) + 1] == 9){
				removeStillFromLevel(level,nbColumns,((int)(posY + this.yspeed - 2) / 50) + 1,((int)(posX) / 50) + 1,stills);
				this.lifePoints += 3;
				level[((int)(posY + this.yspeed - 2) / 50) + 1][((int)(posX) / 50) + 1] = 0;
			}
			
			// Foe
			if (level[((int)(posY + this.yspeed - 2) / 50) + 1][((int)(posX) / 50) + 1] == 10 ||
				level[((int)(posY + this.yspeed - 2) / 50) + 1][((int)(posX) / 50) + 1] == 11){
				removeStillFromLevel(level,nbColumns,((int)(posY + this.yspeed - 2) / 50) + 1,((int)(posX) / 50) + 1,stills);
				level[((int)(posY + this.yspeed - 2) / 50) + 1][((int)(posX) / 50) + 1] = 0;
				
				readSound(rootdir + "/sounds/foe_dying.wav");
			}
			
			// Key
			if (level[((int)(posY + this.yspeed - 2) / 50) + 1][((int)(posX) / 50) + 1] == 12){
				removeStillFromLevel(level,nbColumns,((int)(posY + this.yspeed - 2) / 50) + 1,((int)(posX) / 50) + 1,stills);
				level[((int)(posY + this.yspeed - 2) / 50) + 1][((int)(posX) / 50) + 1] = 0;
				this.inventory.add("key");
			}
			
			this.yspeed = 0;
			jumpsAvailable = 2;
		}
		else if (jumpsAvailable == 2) jumpsAvailable = 1;
		
		// Top
		if ((int)(posY + this.yspeed) < 0)
			this.yspeed = 0;
		else if (level[(int)(posY + this.yspeed - 1) / 50][(int)(posX + 8) / 50] != 0 ||
				level[(int)(posY + this.yspeed - 1) / 50][((int)(posX) / 50) + 1] != 0 ||	
				level[(int)(posY + this.yspeed - 1) / 50][((int)(posX - 8) / 50) + 2] != 0)
			this.yspeed = 0;
		

		/// Right
		if ((int)(posX + this.xspeed) + 100 >= nbColumns * 50)
			this.xspeed = 0;
		else if (level[(int)(posY + this.yspeed + 3) / 50][((int)(posX + this.xspeed-8) / 50) + 2] != 0 ||
				level[((int)(posY + this.yspeed - 3) / 50) + 1][((int)(posX + this.xspeed-8) / 50) + 2] != 0){
			
			// Life bonus
			if (level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] == 9){
				this.lifePoints += 3;
				removeStillFromLevel(level,nbColumns,(int)(posY + this.yspeed +25) / 50,((int)(posX + this.xspeed-8) / 50) + 2,stills);
				level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] = 0;
			}
			
			// Foe
			if (level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] == 10 ||
					level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] == 11){
				this.lifePoints --;
				this.posX -= 75;
				
				readSound(rootdir + "/sounds/cat_hurt.wav");
			}
			
			// Key
			if (level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] == 12){
				removeStillFromLevel(level,nbColumns,(int)(posY + this.yspeed +25) / 50,((int)(posX + this.xspeed-8) / 50) + 2,stills);
				level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] = 0;
				this.inventory.add("key");
			}
			
			// Door
			if (level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] == 13){
				if(this.inventory.contains("key")){
					removeStillFromLevel(level,nbColumns,(int)(posY + this.yspeed +25) / 50,((int)(posX + this.xspeed-8) / 50) + 2,stills);
					level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] = 0;
					this.triggerNextLevel = true;
					readSound(rootdir + "/sounds/door.wav");
				}
			}
			
			// QuestionGuy
			if (level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] == 14){
				readSound(rootdir + "/sounds/fouras.wav");
				if (questionGuy(controlPlayer)==true){
					removeStillFromLevel(level,nbColumns,(int)(posY + this.yspeed +25) / 50,((int)(posX + this.xspeed-8) / 50) + 2,stills);
					level[(int)(posY + this.yspeed +25) / 50][((int)(posX + this.xspeed-8) / 50) + 2] = 0;
				}
				else this.posX -= 10;
			}
			
			
			this.xspeed = 0;
			if (jumpsAvailable != 2) jumpsAvailable = 1;
		}
		
			
		
		// Left
		if ((int)(posX + this.xspeed) < 0)
			this.xspeed = 0;
		else if (level[(int)(posY + this.yspeed + 3) / 50][(int)(posX + this.xspeed+8) / 50] != 0 ||
				level[((int)(posY + this.yspeed - 3) / 50) + 1][(int)(posX + this.xspeed+8) / 50] != 0){
			
			// Life bonus
			if (level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] == 9){
				removeStillFromLevel(level,nbColumns,(int)(posY + this.yspeed +25) / 50,(int)(posX + this.xspeed+8) / 50,stills);
				this.lifePoints += 3;
				level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] = 0;
			}
			
			// Foe
			if (level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] == 10 ||
				level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] == 11){
				this.lifePoints --;
				this.posX += 75;
				
				readSound(rootdir + "/sounds/cat_hurt.wav");
			}
			
			// Key
			if (level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] == 12){
				removeStillFromLevel(level,nbColumns,(int)(posY + this.yspeed +25) / 50,(int)(posX + this.xspeed+8) / 50,stills);
				level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] = 0;
				this.inventory.add("key");
			}
			
			// Door
			if (level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] == 13){
				if(this.inventory.contains("key")){
					removeStillFromLevel(level,nbColumns,(int)(posY + this.yspeed +25) / 50,(int)(posX + this.xspeed+8) / 50,stills);
					level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] = 0;
					this.triggerNextLevel = true;
					readSound(rootdir + "/sounds/door.wav");
				}
			}
			
			// QuestionGuy
			if (level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] == 14){
				readSound(rootdir + "/sounds/fouras.wav");
				if (questionGuy(controlPlayer)==true){
					removeStillFromLevel(level,nbColumns,(int)(posY + this.yspeed +25) / 50,(int)(posX + this.xspeed+8) / 50,stills);
					level[(int)(posY + this.yspeed +25) / 50][(int)(posX + this.xspeed+8) / 50] = 0;
				}
				else this.posX += 10;
			}
			
			
			this.xspeed = 0;
			if (jumpsAvailable != 2) jumpsAvailable = 1;
		}
			
	}
	
	/**
	 * This function remove the still which is breakable if the player go on them.
	 */
	public void removeStillFromLevel(int[][] level, int nbColumns, int x, int y, Vector<Still> stills){
		int index = 0;
		
		for (int j = 0; j < x; j++){
			for (int i = 0; i < nbColumns; i++){
				if (level[j][i] >= 4) index++;
			}
		}
		for (int i = 0; i < y; i++){
			if (level[x][i] >= 4) index++;
		}
		stills.remove(index);
		return;
	}
	
	public void cleanInventory(){
		inventory.clear();
	}
	
	public boolean questionGuy(ControlerPlayer controlPlayer){
		String question = "";
		String answer = "";
		Random random = new Random();
		int foo = random.nextInt(10);
		
		switch (foo){
		case 0: question = "Quelle est la couleur du cheval blanc d'Henry IV ?";
				answer = "blanc";
				break;
		case 1: question = "Quel mot courant a une seule consonne, et cinq voyelles toutes différentes ?";
				answer = "oiseau";
				break;
		case 2: question = "Quelle est le meilleur langage de programmation ?";
				answer = "java";
				break;
		case 3: question = "C'est en sciant que Léonard devint...";
				answer = "scie";
				break;
		case 4: question = "Qu'est-ce qu'un mouton sans pattes ?";
				answer = "un nuage";
				break;
		case 5: question = "Quel est le synonyme de synonyme ?";
				answer = "synonyme";
				break;
		case 6: question = "Monsieur et Madame Ouquoi ont deux fils, comment s'appellent-t-ils ? (... et ...) ";
				answer = "ted et bill";
				break;
		case 7: question = "Logarithme et exponentielle sont au restaurant, qui paie ?";
				answer = "logarithme";
				break;
		case 8: question = "Quel animal a six pattes et marche sur la tête?";
				answer = "le pou";
				break;
		case 9: question = "Quelle est la manière orientée objet de devenir riche ?";
				answer = "héritage";
				break;
		default: break;
		}
		
		String test = JOptionPane.showInputDialog(null, question,"Le vieux sage",JOptionPane.QUESTION_MESSAGE);
		controlPlayer.right = false;
		controlPlayer.running = false;
		controlPlayer.left = false;
		this.running = false;
		if (test == null)
			return false;
		if (test.equals(answer)){
			return true;
		}
		else{
			return false;
		}
	}

	
	public void readSound(String path){
	    InputStream in = null;
		try {
			in = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    AudioStream audioStream = null;
		try {
			audioStream = new AudioStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    AudioPlayer.player.start(audioStream);
	}
}
