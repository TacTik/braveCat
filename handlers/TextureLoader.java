package handlers;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 * This is the texture loader Class.
 * 
 * @author Juliette Belin, Alice Neichols, Denis Tribouillois 
 * @version 1.0
 */
public class TextureLoader {

	private TextureLoader() {}
	
	/**
	 * This function get images from folder.
	 * @param path
	 * 				The path of the folder.
	 * @param {number} [number]
	 */
	public static Vector<Image> getImagesFromFolder(String path, int... number ){
		Vector<Image> texturesImages = new Vector<Image>();
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);
		
		int max = (number.length == 0) ? listOfFiles.length : number[0] + 1;
		
		for (int i = 0; i < max; i++) {
			if (listOfFiles[i].isFile()) {
				try {
					texturesImages.add (ImageIO.read(new File(path + listOfFiles[i].getName())));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} 
		return texturesImages;
	}
	
	/**
	 * This function get images from path.
	 * @param path
	 * 			the path of the image.
	 */
	public static Image getImageFromPath(String path){
		Image image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "can't read your file !");
		}
		return image;
	}
	
	/**
	 * This function get image icon from path.
	 * @param path
	 * 			the path of the image icon.
	 * @param {number} [number]
	 */
	public static Vector<ImageIcon> getImageIconFromPath(String path, int... number){
		Vector<ImageIcon> texturesImages = new Vector<ImageIcon>();
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		Arrays.sort(listOfFiles);
		
		int max = (number.length == 0) ? listOfFiles.length : number[0];
		
		for (int i = 0; i < max; i++) {
			if (listOfFiles[i].isFile()) {
				texturesImages.add (new ImageIcon(path + listOfFiles[i].getName()));
			}
		} 
		return texturesImages;
	}

}
