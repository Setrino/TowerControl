/*
 * Sergei Kostevitch
 * Feb 28, 2012
 */

package gui;

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import tower.Tower;

//import com.apple.*;
import file.FileWriter;
import file.XMLFileCreator;

// TODO: Auto-generated Javadoc
/**
 * The Class MainWindow.
 */
public class MainWindow {

	  //static FrameMessageLog messageFrame;
		/** The message frame. */
  	static RadarFrame messageFrame;

	/** The main. */
	static MainWindow main = new MainWindow();

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "TowerControl");
		
		try {
			/*new FileWriter();
			XMLFileCreator file = XMLFileCreator.getXMLFileCreatorInstance();
			file.checkFilePresenceAndWriteMessageToFile("HELLO", "ALYOUPO",
					"Tower", "4");*/

			
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//messageFrame = new FrameMessageLog();
			messageFrame = new RadarFrame();
			Tower.getTowerInstance().setRadarFrame(messageFrame);
			messageFrame.setTitle("TowerControl");
			messageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			messageFrame.setPreferredSize(new Dimension(800, 600));
			messageFrame.setSize(new Dimension(590, 350));
			//1109 x 751
			messageFrame.setMinimumSize(new Dimension(1109, 791));
			messageFrame.setLocation(100, 100);
			messageFrame.setVisible(true);
			
			

			
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (UnsupportedLookAndFeelException e) {
		}
	}
}
