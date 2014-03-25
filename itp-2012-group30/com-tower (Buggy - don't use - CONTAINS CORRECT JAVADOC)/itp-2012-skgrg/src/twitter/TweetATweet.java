/*
 * Sergei Kostevitch
 * Mar 21, 2012
 */

package twitter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.*;
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: Auto-generated Javadoc
/**
 * The Class TweetATweet.
 */
public class TweetATweet {

	/** The configuration. */
	private Configuration configuration = null;

	/** The date. */
	private Date date;

	/** The simple time format. */
	private SimpleDateFormat simpleTimeFormat;

	/** The simple date format. */
	private SimpleDateFormat simpleDateFormat;

	/** The picture message. */
	private String pictureMessage;

	/** The tweet a tweet. */
	private static TweetATweet tweetATweet = null;

	/** The consumer key. */
	private final String consumerKey = "2DastHEtgYsBAtEyyweXqg";

	/** The consumer secret. */
	private final String consumerSecret = "hC6LC0JSSnroHfWRZNr7yZmpE2AdkWvV2gOXXYpXJYE";

	/** The access token. */
	private final String accessToken = "532207118-qCbdY6RCDg1BhcvUk4WyBsdo7UkY2W2GW9ORAwfw";

	/** The access secret. */
	private final String accessSecret = "xewjQw7Dcxld5P5joXSrbp99nvsDYK5Fu81VeDeAE";

	/** The twitter. */
	private Twitter twitter;

	/*
	 * public static void main(String[] args) {
	 * 
	 * TweetATweet.getTweetATweetInstance(); }
	 */

	// Setting up a Singleton Pattern for TweetATweet

	/**
	 * Gets the tweet a tweet instance.
	 * 
	 * @return the tweet a tweet instance
	 */
	public static synchronized TweetATweet getTweetATweetInstance() {

		if (tweetATweet == null) {

			tweetATweet = new TweetATweet();
		}

		return tweetATweet;
	}

	/**
	 * Instantiates a new tweet a tweet.
	 */
	private TweetATweet() {

		configuration = new ConfigurationBuilder().setDebugEnabled(true)
				.setOAuthConsumerKey(consumerKey)
				.setOAuthConsumerSecret(consumerSecret)
				.setOAuthAccessToken(accessToken)
				.setOAuthAccessTokenSecret(accessSecret).build();

		try {
			TwitterFactory factory = new TwitterFactory();
			twitter = factory.getInstance();
			AccessToken accestoken = new AccessToken(accessToken, accessSecret);

			twitter.setOAuthConsumer(consumerKey, consumerSecret);
			twitter.setOAuthAccessToken(accestoken);

		} catch (Exception e) {
			e.printStackTrace();
		}

		date = new Date();
		simpleTimeFormat = new SimpleDateFormat("HH:mm");
		simpleDateFormat = new SimpleDateFormat("MMM dd");
		// postPicture("Steve.jpg", "ITPSR2304");
		// postTweet("ITPSR2304");

	}

	/**
	 * Post tweet.
	 * 
	 * @param planeID
	 *            the plane id
	 */
	public void postTweet(String planeID) {

		Status status = null;

		try {
			status = twitter.updateStatus("Plane #" + planeID
					+ " has landed at #ITP00 airport on "
					+ simpleDateFormat.format(date) + " at "
					+ simpleTimeFormat.format(date) + ". #ICITP2012");

		} catch (TwitterException e) {

			e.printStackTrace();
		}

		if (status.getId() == 0) {
			System.out.println("Error occured while posting tweets to twitter");
		} else {
			System.out.println(status.getText());
		}
	}

	/**
	 * Post picture.
	 * 
	 * @param picturePath
	 *            the picture path
	 * @param planeID
	 *            the plane id
	 */
	public void postPicture(String picturePath, String planeID,
			String dataFormat) {

			//String pictureName = planeID.substring(0, jpgOccurence);

			pictureMessage = "Plane #" + planeID + " has sent a picture \""
					+ planeID + "." + dataFormat + "\" to #ITP00 airport on "
					+ simpleDateFormat.format(date) + " at "
					+ simpleTimeFormat.format(date) + ". #ICITP2012";

			ImageUpload upload = new ImageUploadFactory(configuration)
					.getInstance(MediaProvider.TWITTER);
			File f = new File(picturePath);
			String postedPicture = null;
			try {
				postedPicture = upload.upload(f, pictureMessage);

			} catch (TwitterException e) {

				e.printStackTrace();
			}

			System.out.println(postedPicture);
	}

	/**
	 * Gets the twitter.
	 * 
	 * @return the twitter
	 */
	public Twitter getTwitter() {

		return twitter;
	}

}