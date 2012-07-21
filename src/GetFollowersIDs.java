
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import edu.cmu.cs.lti.ark.tweetnlp.RunPOSTagger;

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
import twitter4j.json.DataObjectFactory;


public final class GetFollowersIDs {
	/**
	 * Usage: java twitter4j.examples.friendsandfollowers.GetFollowersIDs [screen name]
	 *
	 * @param args message
	 */
	public static void main(String[] args) {

		//		GetFollowersIDs.getStream(getFollowers(args), 10000);

		//		long[] me = {216501896};
		//		GetFollowersIDs.getStream(me, 120000);

		String in = "forTagging.txt";
		String out = "output.txt";

		//DatabaseUtils utils = new DatabaseUtils();
		//utils.createFileForTagging(in)

		runPOSTagger(in, out);
		
	}

	private static void runPOSTagger(String input, String output) {
		try {
			String[] array = {"-input", input, "-output", output};
			RunPOSTagger.main(array );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//only takes the first arg, need to change to enable lists of people
	private static long[] getFollowers(String[] args) {

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

			return followArray;

		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get followers' ids: " + te.getMessage());

			return null;
		}
	}


	private static void getStream(long[] followArray, long milliseconds) {

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("ghlKux5yYJD0hlu0kQITsg")
		.setOAuthConsumerSecret("Kl9LPhiaPThgg5ia1RMWJgZHW1tC9rDEQ8xBPMfp8Y")
		.setOAuthAccessToken("216501896-LuyoBsr6bfLDP73O1LkbtvW5WJPxwNpoOi8Se8cD")
		.setOAuthAccessTokenSecret("LuftunWNHrhBn05xzkeOlxBGc2PlCFRo6bHe3e49lY")
		.setJSONStoreEnabled(true);


		StatusListener listener = new StatusListener() {
			public void onStatus(Status status) {

				String rawJSON = DataObjectFactory.getRawJSON(status);
				DatabaseUtils utils = new DatabaseUtils();
				utils.storeTweet(rawJSON);
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
		twitterStream.filter(new FilterQuery(0, followArray, null));

		try {
			Thread.sleep(milliseconds);
			twitterStream.cleanUp();
			twitterStream.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
