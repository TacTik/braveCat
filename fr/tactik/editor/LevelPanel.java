package fr.tactik.editor;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import handlers.SelectTileHelper;
import handlers.TextureLoader;
import handlers.TexturesMapper;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * This is the levelPanel class. This class create a panel which display the level.
 * 
 * @author Juliette Belin, Alice Neichols, Denis Tribouillois 
 * @version 1.0
 */
public class LevelPanel extends JPanel implements MouseListener, MouseMotionListener {

	static String rootdir = System.getProperty("user.dir");
	
	private static final long serialVersionUID = 1L;
	Image backgroundImage;
	static String bg;
	
	static int width;
	static int height;
	int tileDimention = 50;
	static int[][] tiles;
	
	/**
	 * This function create the level panel.
	 * @param width
	 * 				width of the panel.
	 * @param height
	 * 				height of the panel.
	 * @param background
	 * 				the path to the panel background.
	 */
	public LevelPanel(int width, int height, String background){
		LevelPanel.bg = background;
		LevelPanel.width = width;
		LevelPanel.height = height;
		tiles = new int[width][height];
		
		addMouseListener(this);
		addMouseMotionListener(this);
		this.requestFocus();

	}
	
	/**
	 * This function load the background and refresh the panel.
	 * @param background
	 * 				the path for the background.
	 */
	public void setBg(String background){
		LevelPanel.bg = background;
		backgroundImage = TextureLoader.getImageFromPath(LevelPanel.bg);
		repaint();
	}
	
	/**
	 * This function create the background and give the dmension to the panel and capture the movement of the mouse.
	 */
	public int init(){
		if(LevelPanel.bg != null)
			backgroundImage = TextureLoader.getImageFromPath(LevelPanel.bg);
		
		setPreferredSize(new Dimension((int)width * tileDimention, (int)height * tileDimention));
		this.requestFocus();
		if (null == backgroundImage)
			setBackground(Color.white);
		return 0;
	}
	

	
	public void setViewFromFile(String levelPath){
		int i = 0;

		try{
			FileInputStream fstream = new FileInputStream(levelPath);
			DataInputStream in = new DataInputStream(fstream);
    		BufferedReader br = new BufferedReader(new InputStreamReader(in));
    		String strLine;
    		
    		strLine = br.readLine();  
			String[] tokens = strLine.split(" ");

    		
    		while (i != height && (strLine = br.readLine()) != null)   {
    			tokens = strLine.split(" ");
	    	    for (int j = 0; j < width ; j++){
	    		    tiles[j][i] = Integer.parseInt(tokens[j]);
    			} 
	    	    i++;
    		}
    		LevelPanel.bg = br.readLine();
    		in.close();
    	}
    	catch (Exception e){
    	     System.err.println("Error: " + e.getMessage());
    	}

		//repaint();
	}
	

	/**
	 * This function paint all the components.
	 */

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0,0,null);
		
		g.setColor(new Color(51,51,51,40));
		
	
		for(int i = 0; i < LevelPanel.width; i++){
			for(int j = 0; j < LevelPanel.height; j++){
				if(tiles[i][j] == -8){
					g.fillRect(i * 50, j * 50, 50, 50);
				}
				else if(tiles[i][j] != 0){
					g.drawImage(TexturesMapper.getImageById(tiles[i][j]).getImage(), i*50, j*50, 50 , 50, null);
				}
			}
		}

		
		g.setColor(Color.black);
		for(int a = 0; a <= tileDimention; a++){
			g.drawLine(0, a * tileDimention, width* tileDimention, a * tileDimention);
			g.drawLine(a * tileDimention, 0 , a * tileDimention, height* tileDimention);
		}
	}
	
	/**
	 * This function write a file from the editor.
	 */
	public static void writeFileFromEditor(String levelPath){
		final String path = levelPath;

        final File file = new File(path); 
        try {
            // file creation
            file.createNewFile();
            // writer creation
            final FileWriter writer = new FileWriter(file);
            try {
                writer.write(height + " " + width + "\n");
                for (int i = 0; i < height; i++){
                	for (int j=0; j < width; j++){
                		if (tiles[j][i] == -8) tiles[j][i] = 0;
                		writer.write(tiles[j][i] + " ");
                	}
                	writer.write("\n");
                }
                writer.write(bg);
            } finally {
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("can't create file");
        }
	}


	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(e.getX() < 0 || e.getY() > height * 50 || e.getX() > width * 50 || e.getY() < 0)
			return;
			
		if (SwingUtilities.isRightMouseButton(e)){
			tiles[ e.getX() / 50][e.getY() / 50] = 0;
		}
		else 
			tiles[ e.getX() / 50][e.getY() / 50] = SelectTileHelper.getSelectedTile();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getX() < 0 || e.getY() > height * 50 || e.getX() > width * 50 || e.getY() < 0)
			return;
		
		if (SwingUtilities.isRightMouseButton(e)){
			tiles[ e.getX() / 50][e.getY() / 50] = 0;
		}
		else 
			tiles[ e.getX() / 50][e.getY() / 50] = SelectTileHelper.getSelectedTile();
		
		repaint();
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		for(int i = 0; i < LevelPanel.width; i++){
			for(int j = 0; j < LevelPanel.height; j++){
				if(tiles[i][j] == -8 && (i !=  e.getX() && j != e.getY() )){
					tiles[i][j] = 0;
				}
			}
		}
		
		if(tiles[ e.getX() / 50][e.getY() / 50] == 0)
			tiles[ e.getX() / 50][e.getY() / 50] = -8;
		repaint();
	}
}
