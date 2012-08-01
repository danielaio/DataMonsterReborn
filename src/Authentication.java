import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class Authentication {

	public static Configuration authenticate () {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("ghlKux5yYJD0hlu0kQITsg")
		.setOAuthConsumerSecret("Kl9LPhiaPThgg5ia1RMWJgZHW1tC9rDEQ8xBPMfp8Y")
		.setOAuthAccessToken("216501896-LuyoBsr6bfLDP73O1LkbtvW5WJPxwNpoOi8Se8cD")
		.setOAuthAccessTokenSecret("LuftunWNHrhBn05xzkeOlxBGc2PlCFRo6bHe3e49lY")
		.setJSONStoreEnabled(true);
		
		return cb.build();		
	}
}
