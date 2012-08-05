import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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


	private void storeLine(String id) throws IOException {

		fstream = new FileWriter(filename, true);
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

	public void extractFeatures() {


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
				storeLine(user);
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
			
			
			
			
		}
	}
}
