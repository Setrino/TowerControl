/*
 * Sergei Kostevitch
 * Mar 25, 2012
 */

package gui;

import gui.given.PlaneDrawnForGivenGui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

import plane.Plane;

import tower.Tower;

// TODO: Auto-generated Javadoc
/**
 * The Class RadarFrame.
 */
public class RadarFrame extends JFrame implements Runnable, MenuKeyListener,
		ActionListener, WindowListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9180111039407571515L;

	/** The menu bar. */
	private JMenuBar menuBar;
	
	/** The main menu. */
	private JMenu mainMenu;
	
	/** The about. */
	private JMenuItem about;
	
	/** The twitter log. */
	private JMenuItem twitterLog;
	
	/** The messages log. */
	private JMenuItem messagesLog;
	
	/** The close frame. */
	private JMenuItem closeFrame;
	
	/** The messages log frame. */
	private FrameMessageLog messagesLogFrame;
	// Three Intermediates used for window closing
	/** The message log intermediate. */
	private int messageLogIntermediate;
	
	/** The twitter log intermediate. */
	private int twitterLogIntermediate;
	
	/** The line component. */
	private TurningLineComponent lineComponent;
	
	/** The plane drawn. */
	private PlaneDrawnForGivenGui planeDrawn;
	
	/** The layered pane. */
	private JLayeredPane layeredPane;

	/** The twitter log frame. */
	private TwitterTextAreaFrame twitterLogFrame;
	
	private int layerNumber = 0;
	
	private Tower tower = null;

	/**
	 * Instantiates a new radar frame.
	 */

	
	public RadarFrame() {

		String vers = System.getProperty("os.name").toLowerCase();
		tower = Tower.getTowerInstance();
		//this.setBackground(Color.BLACK);
		menuBar = new JMenuBar();
		mainMenu = new JMenu("Main");
		setJMenuBar(menuBar);
		menuBar.add(mainMenu);
		about = new JMenuItem("About");
		closeFrame = new JMenuItem("Close");
		twitterLog = new JMenuItem("Twitter Log",
				new ImageIcon("twit_bird.png"));
		messagesLog = new JMenuItem("Messages Log", new ImageIcon(
				"letter_box.png"));
		mainMenu.setMnemonic(KeyEvent.VK_M);

		if (vers.indexOf("windows") != -1) {

			twitterLog.setAccelerator(KeyStroke.getKeyStroke('T',
					InputEvent.CTRL_MASK));
			about.setAccelerator(KeyStroke.getKeyStroke('A',
					InputEvent.CTRL_MASK));
			messagesLog.setAccelerator(KeyStroke.getKeyStroke('M',
					InputEvent.CTRL_MASK));
			closeFrame.setAccelerator(KeyStroke.getKeyStroke('W',
					InputEvent.CTRL_MASK));
		} else if (vers.indexOf("mac") != -1) {
			twitterLog.setAccelerator(KeyStroke.getKeyStroke('T',
					InputEvent.META_MASK));
			about.setAccelerator(KeyStroke.getKeyStroke('A',
					InputEvent.META_MASK));
			messagesLog.setAccelerator(KeyStroke.getKeyStroke('M',
					InputEvent.META_MASK));
			closeFrame.setAccelerator(KeyStroke.getKeyStroke('W',
					InputEvent.META_MASK));
		}

		this.addWindowListener(this);
		// about.addMenuKeyListener(this);
		about.addActionListener(this);
		// twitterLog.addMenuKeyListener(this);
		twitterLog.addActionListener(this);
		// messagesLog.addMenuKeyListener(this);
		messagesLog.addActionListener(this);

		closeFrame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (messageLogIntermediate == 1) {

					messagesLogFrame.setVisible(false);
				} else if (twitterLogIntermediate == 1) {

					twitterLogFrame.setVisible(false);
				}

			}
		});

		mainMenu.add(about);
		mainMenu.addSeparator();
		mainMenu.add(messagesLog);
		mainMenu.addSeparator();
		mainMenu.add(twitterLog);
		mainMenu.addSeparator();
		mainMenu.add(closeFrame);

		//ImageIcon radar = new ImageIcon("radar_Control540B.png");
		// getContentPane().add(new JLabel(radar), BorderLayout.CENTER);
			//getContentPane().add(new RadarComponent(), BorderLayout.CENTER);

		getContentPane().add(new RadarBottomPanel(this), BorderLayout.SOUTH);
		
		layeredPane = new JLayeredPane();	
		layeredPane.setBounds(0, 0, 1200, 1200);
		lineComponent = new TurningLineComponent();
		lineComponent.setBounds(0, 0, 1200, 1200);
		layeredPane.add(lineComponent, new Integer(layerNumber));
		layerNumber++;
		//addPlaneToTheFrame("LALA");
		getContentPane().add(layeredPane);
		lineComponent.start();
		//addPlaneToTheFrame("LOLO");
		
		this.pack();
	}
	
	// Need to fix this method
	
	/**
	 * Add a plane as a new Thread, as a layer to LayeredPane
	 * 
	 */
	
	public void addPlaneToTheFrame(String planeName, Plane plane){
		
		System.out.println("Added Plane with name " + planeName);
		planeDrawn = new PlaneDrawnForGivenGui(planeName, plane);
		lineComponent.addPlaneDrawingToRadar(planeDrawn);
		planeDrawn.setBounds(0, 0, 1200, 1200);		
		layeredPane.add(planeDrawn, new Integer(layerNumber));
		layerNumber++;
		planeDrawn.start();
	}

	/* (non-Javadoc)
 * @see java.lang.Runnable#run()
 */
@Override
	public void run() {

	}

	/* (non-Javadoc)
	 * @see javax.swing.event.MenuKeyListener#menuKeyPressed(javax.swing.event.MenuKeyEvent)
	 */
	@Override
	public void menuKeyPressed(MenuKeyEvent e) {
		MenuElement[] path = e.getPath();
		JMenuItem item = (JMenuItem) path[path.length - 1];
		System.out.println("Key released: " + e.getKeyChar() + ", "
				+ e.getKeyText(e.getKeyCode()) + " on " + item.getText());

	}

	/* (non-Javadoc)
	 * @see javax.swing.event.MenuKeyListener#menuKeyReleased(javax.swing.event.MenuKeyEvent)
	 */
	@Override
	public void menuKeyReleased(MenuKeyEvent e) {
		MenuElement[] path = e.getPath();
		JMenuItem item = (JMenuItem) path[path.length - 1];
		System.out.println("Key pressed: " + e.getKeyChar() + ", "
				+ e.getKeyText(e.getKeyCode()) + " on " + item.getText());

	}

	/* (non-Javadoc)
	 * @see javax.swing.event.MenuKeyListener#menuKeyTyped(javax.swing.event.MenuKeyEvent)
	 */
	@Override
	public void menuKeyTyped(MenuKeyEvent e) {

		MenuElement[] path = e.getPath();
		JMenuItem item = (JMenuItem) path[path.length - 1];
		System.out.println("Key typed: " + e.getKeyChar() + ", "
				+ e.getKeyText(e.getKeyCode()) + " on " + item.getText());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		System.out.println("Item clicked: " + e.getActionCommand());

		if (e.getActionCommand() == "Twitter Log") {

			twitterLogIntermediate = 1;
			messageLogIntermediate = 0;

			if (twitterLogFrame == null) {
				twitterLogFrame = new TwitterTextAreaFrame(menuBar);
				twitterLogFrame.setVisible(true);
			}

			else {

				twitterLogFrame.setVisible(true);
			}

			if (messagesLogFrame != null) {

				messagesLogFrame.setVisible(false);

			}

		} else if (e.getActionCommand() == "Messages Log") {

			twitterLogIntermediate = 0;
			messageLogIntermediate = 1;

			if (messagesLogFrame == null) {

				messagesLogFrame = new FrameMessageLog(menuBar);
				messagesLogFrame.setVisible(true);
			} else {

				messagesLogFrame.setVisible(true);
			}

			if (twitterLogFrame != null) {

				twitterLogFrame.setVisible(false);

			}

		} else if (e.getActionCommand() == "About") {

		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent e) {

		this.setJMenuBar(menuBar);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowOpened(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
