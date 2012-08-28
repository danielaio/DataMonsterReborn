import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class FeatureExtraction {

	String label;
	LinkedHashMap<String,Double> result;
	BufferedWriter out;
	FileWriter fstream;
	String filename;
	String coll; 
	
	public FeatureExtraction(String filename, String label, String collection) {

		this.filename = filename;
		this.label = label;	
		this.coll = collection;

		result = new LinkedHashMap<String, Double>();
		result.put("N", 0.0);
		result.put("O", 0.0);
		result.put("S", 0.0);
		result.put("properNoun", 0.0);
		result.put("Z", 0.0);
		result.put("L", 0.0);
		result.put("M", 0.0);
		result.put("V", 0.0);
		result.put("A", 0.0);
		result.put("R", 0.0);
		result.put("bang", 0.0);
		result.put("D", 0.0);
		result.put("P", 0.0);
		result.put("cc", 0.0);
		result.put("T", 0.0);
		result.put("X", 0.0);
		result.put("Y", 0.0);
		result.put("hash", 0.0);
		result.put("mention", 0.0);
		result.put("retweet", 0.0);
		result.put("U", 0.0);
		result.put("E", 0.0);
		result.put("number", 0.0);
		result.put("punct", 0.0);
		result.put("G", 0.0);
	}


	private void storeLineForOldFeatureExtraction(String id, String file) throws IOException {

		fstream = new FileWriter(file, true);
		out = new BufferedWriter(fstream);

		StringBuilder line = new StringBuilder();
		line.append(id).append(" ").append(label).append(" ");
		for (String key : result.keySet()) {
			line.append(result.get(key)).append(" ");
		}
		line.append("\n");
		out.write(line.toString());
		out.close();
		fstream.close();
	}

	public void extractFeatures(String file) {


		TweetsStorageUtils utils = new TweetsStorageUtils(coll);
		Collection<String> users = utils.getUsers();

		System.out.println(users.toString());

		for (String user : users) {

			int total = 0;
			DBCursor tweetsForUser = utils.getTweetsForUser(user);

			while (tweetsForUser.hasNext()) {

				DBObject next = tweetsForUser.next();
				
				if (next.get("texttt") == null)
					continue;
				
				BasicDBObject object = (BasicDBObject) next.get("texttt");

				Map<String, ArrayList<String>> tweet = object.toMap();


				for (String key : result.keySet()) {
					if(tweet.containsKey(key)) {
						total += tweet.get(key).size();
						result.put(key, result.get(key) + tweet.get(key).size());
					}
				}
			}

			//normalise (divide by total)
			for (String key : result.keySet()) {
				result.put(key, total == 0 ? 0 : (result.get(key) / total));
			}

			System.out.println(total);

			try {
				storeLineForOldFeatureExtraction(user, file);
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (String key : result.keySet()) {
				result.put(key, 0.0);
			}
		}
	}
	
	private void storeTweetAsLine(String line) throws IOException {
		fstream = new FileWriter(filename, true);
		out = new BufferedWriter(fstream);
		
		out.write(line + "\n");
		
		out.close();
		fstream.close();
	}
	
	public void extractFeaturesNaiveBayesPerTweet() {


		TweetsStorageUtils utils = new TweetsStorageUtils(coll);
		
		DBCursor allTweets = utils.getAllTweets();
		
		while(allTweets.hasNext()) {
			//add tweet to file, tokens separated with a space :)
			DBObject next = allTweets.next();
			String text = (String) next.get("text_tokens");
			
			ObjectId object = (ObjectId) next.get("_id");
			
			StringBuilder b = new StringBuilder();
			b.append(object.toString()).append(" ").append(label).append(" ").append(text);
			
			try {
				storeTweetAsLine(b.toString());
			} catch (IOException e) {
				System.out.println("Couldn't store tweets");
				e.printStackTrace();
			}
		}
	}
	
	HashMap<String, Pattern> features = new HashMap<String, Pattern>();
	
	private void createSocioLinguisticFeatures() {
		features.put("omg", Pattern.compile("omg|OMG|(Oh)?.? (My)?.? Go+d.?|(oh)? my go+d|(oh)? (my)? goo+dness|(oh)? (my)? gosh"));
		features.put("shout", Pattern.compile("[A-Z]+"));
		features.put("exasp", Pattern.compile("ugh|m+|hm+|a+h+|gr+|ahm+"));
		features.put("agreement", Pattern.compile("yeah?|ohya+|su+re|ofc"));
		features.put("honors", Pattern.compile("bros?|dudes?|ma+n|sirs?|bro+"));
		features.put("xoxo", Pattern.compile("(x|o)+"));
		features.put("excitement", Pattern.compile("!+"));
		features.put("puzzled", Pattern.compile("(!|?)+"));
		features.put("possessive", Pattern.compile("my|our"));
		features.put("repeated", Pattern.compile(""));
	}
	
	
	public void extractFeaturesWinnowPerTweet() {
		TweetsStorageUtils utils = new TweetsStorageUtils(coll);
		
		DBCursor allTweets = utils.getAllTweets();
		
		while(allTweets.hasNext()) {
			DBObject next = allTweets.next();
			String text = (String) next.get("text_tokens");
			
			BasicDBObject object = (BasicDBObject) next.get("texttt");
			Map<String, ArrayList<String>> tags = object.toMap();
			ArrayList<String> emoticons = tags.get("E");
			ArrayList<String> punct = tags.get("punct");
			ArrayList<String> bang = tags.get("bang");
			
			String[] split = text.split(" ");
			StringBuilder result = new StringBuilder();
			
			
			for (String word : split) {
				//does word match any of the socio-linguistic features?
				for (String feature : features.keySet()) {
					Pattern p = features.get(feature);
					Matcher m = p.matcher(word);
					boolean fromregex = m.find(); 
					boolean fromtags = emoticons.contains(word) || punct.contains(word) || bang.contains(word);

					result.append(fromregex || fromtags ? 1 : 0);
				}
			}
		}
	}
}
