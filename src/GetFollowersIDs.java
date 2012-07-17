

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import twitter4j.FilterQuery;
import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;


public final class GetFollowersIDs {
	/**
	 * Usage: java twitter4j.examples.friendsandfollowers.GetFollowersIDs [screen name]
	 *
	 * @param args message
	 */
	public static void main(String[] args) {

		try {
			Twitter twitter = new TwitterFactory().getInstance();
			long cursor = -1;
			IDs ids;
			System.out.println("Listing followers's ids.");

			ArrayList<Long> followers = new ArrayList<Long>();

			int count = 1;
			do {
				if (0 < args.length) {
					ids = twitter.getFollowersIDs(args[0], cursor);
				} else {
					ids = twitter.getFollowersIDs(cursor);
				}
				for (long id : ids.getIDs()) {
					count++;
					followers.add(id);
					System.out.println(id);
				}
			} while ((cursor = ids.getNextCursor()) != 0 && count <= 1000);


			long[] followArray = new long[followers.size()];
			for(int i = 0; i < followers.size(); i++) {
				followArray[i] = followers.get(i);
			}
			GetFollowersIDs.getStream(followArray);


			System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get followers' ids: " + te.getMessage());
			System.exit(-1);
		}
	}

	private static void getStream(long[] followArray) {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("EFvoJ3uiVR9oSYjrcBPb6g")
		.setOAuthConsumerSecret("jTwzY9t5lQRQ7YemhVxfCIBfpfgfloTVGRgvBq9w")
		.setOAuthAccessToken("216501896-LuyoBsr6bfLDP73O1LkbtvW5WJPxwNpoOi8Se8cD")
		.setOAuthAccessTokenSecret("LuftunWNHrhBn05xzkeOlxBGc2PlCFRo6bHe3e49lY");
		
		
		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {

				try{
					FileWriter fstream = new FileWriter("Beliebers.txt", true);
					BufferedWriter out = new BufferedWriter(fstream);
					out.write(status.getText() + "\n");
					out.close();
				}catch (Exception e){
					System.err.println("Error: " + e.getMessage());
				}

				System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
			}

			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
			}

			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			public void onScrubGeo(long userId, long upToStatusId) {
				System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
			}

			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		};
		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		twitterStream.addListener(listener);

		System.out.println("Printing beliebers' tweets :):)");
		twitterStream.filter(new FilterQuery(0, followArray, new String[1]));

		try {
			Thread.sleep(300000);
			twitterStream.cleanUp();
			twitterStream.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
