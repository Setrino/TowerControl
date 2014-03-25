/*
 * Sergei Kostevitch
 * Mar 21, 2012
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import twitter.TweetATweet;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

// TODO: Auto-generated Javadoc
/**
 * The Class TwitterTextAreaFrame.
 */
public class TwitterTextAreaFrame extends JFrame implements WindowListener{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -179011968001251302L;
	
	/** The twitter. */
	private Twitter twitter = TweetATweet.getTweetATweetInstance().getTwitter();
	
	/** The twitter timeline. */
	private JTextArea twitterTimeline;
	
	/** The twitter scroll pane. */
	private JScrollPane twitterScrollPane;
	
	/** The bottom label. */
	private JLabel bottomLabel;
	
	/** The icon twitter. */
	private Icon iconTwitter;
	
	/** The bar. */
	private JMenuBar bar;

	/**
	 * Instantiates a new twitter text area frame.
	 *
	 * @param bar the bar
	 */
	public TwitterTextAreaFrame(JMenuBar bar) {

		this.bar = bar;
		setJMenuBar(bar);
		this.setPreferredSize(new Dimension(590, 350));
		this.setSize(new Dimension(590, 350));
		this.setMinimumSize(new Dimension(590, 350));
		this.setLocation(100, 100);
		twitterTimeline = new JTextArea(6, 30);
		twitterTimeline.setFont(new Font("Arial", 0, 15));
		twitterTimeline.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		twitterTimeline.setEditable(false);
		twitterTimeline.setLineWrap(true);
		twitterTimeline.setWrapStyleWord(true);
		twitterScrollPane = new JScrollPane(twitterTimeline,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		twitterScrollPane.setBorder(BorderFactory.createMatteBorder(5,5,5,5, new Color(27, 202, 255)));
	    
		
		iconTwitter = new ImageIcon("twitter_logo.png");
		//toyPlane = new ImageIcon("small_plane.png");
		bottomLabel = new JLabel(iconTwitter, JLabel.CENTER);
		
		getContentPane().add(twitterScrollPane, BorderLayout.CENTER);
		getContentPane().add(bottomLabel, BorderLayout.SOUTH);
		
		twitterList();
		
		this.addWindowListener(this);

	}

	/**
	 * Twitter list.
	 */
	private void twitterList() {
		try {
			
				// twitter.updateStatus(txtUpdateStatus.getText());
				// twitterTimeline.setText(null);
				java.util.List<Status> statusList = twitter.getUserTimeline();
				for (int i = 0; i < statusList.size(); i++) {
					twitterTimeline.append(String.valueOf(statusList.get(i)
							.getText()) + "\n");
					twitterTimeline.append("------------------------------------------------\n");
				}
			
		} catch (TwitterException e) {

			JOptionPane.showMessageDialog(null, "A Twitter Error ocurred!");
		}
		// txtUpdateStatus.setText("");
		twitterTimeline.updateUI();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent e) {
		
		this.setJMenuBar(bar);
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
