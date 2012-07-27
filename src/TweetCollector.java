import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;


public class TweetCollector {

	static Configuration authenticate () {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("ghlKux5yYJD0hlu0kQITsg")
		.setOAuthConsumerSecret("Kl9LPhiaPThgg5ia1RMWJgZHW1tC9rDEQ8xBPMfp8Y")
		.setOAuthAccessToken("216501896-LuyoBsr6bfLDP73O1LkbtvW5WJPxwNpoOi8Se8cD")
		.setOAuthAccessTokenSecret("LuftunWNHrhBn05xzkeOlxBGc2PlCFRo6bHe3e49lY")
		.setJSONStoreEnabled(true);
		
		return cb.build();		
	}
	
	static void getStream(long[] followArray, long milliseconds) {

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
		TwitterStream twitterStream = new TwitterStreamFactory(authenticate()).getInstance();
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
