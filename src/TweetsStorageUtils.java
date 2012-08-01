import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;


public class TweetsStorageUtils {

	private Mongo m;
	private DB db;
	private DBCollection coll;
	
	
	public TweetsStorageUtils(String collection) {
		
		try {
			m = new Mongo();
			db = m.getDB("mydb");
			this.coll = db.getCollection(collection);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	public void storeTweet(String tweet) {
		coll.save((DBObject) JSON.parse(tweet));
	}

	
	
	//this should be a set rather than an array
	public Collection<String> getUsers() {

		//DBCursor cur = coll.find(new BasicDBObject(), new BasicDBObject("user.id_str", 1));
		Collection<String> set = new HashSet<String>();

		DBCursor cur = coll.find();
		while (cur.hasNext()) {
			String userid = (String) ((DBObject)(cur.next().get("user"))).get("id_str");
			set.add(userid);
		}
		
		return set;

	}

	public DBCursor getTweetsForUser(String user) {

		DBCursor cur = coll.find(new BasicDBObject("user.id_str", user)); 
				//((DBObject)(user.get("user"))).get("id_str")));
		return cur;

	}
	

	
	public void removeTweets() {
		coll.remove(new BasicDBObject("_id", new ObjectId("50107a0d00618068dd15f97d")));
	}
	
	public void getAllTweets() {
		DBCursor cur = coll.find();
		int c = 0;
		while (cur.hasNext()) {
			System.out.println(cur.next().get("texttt"));
			c++;
		}
	}
	

	//method to parse output file 
	public void storeTaggedTweets(String output) {

		try {
			FileReader fstream = new FileReader(output);
			BufferedReader in = new BufferedReader(fstream);
			String line = in.readLine();

			while (line != null) {

				String id = line.substring(0, line.length() - 2);
				line = in.readLine();

				HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
				while (!line.equals("")) {
					//each line is a word, tag pair
					String word = line.substring(0, line.indexOf("\t"));
					String tag = line.substring(line.indexOf("\t") + 1);

					tag = convertTag(tag);
					if (map.get(tag) == null)
						map.put(tag, new ArrayList<String>());
					map.get(tag).add(word);

					line = in.readLine();
				}

				BasicDBObject tweetObj = new BasicDBObject(map);
				BasicDBObject newObj = new BasicDBObject().append("$set", new BasicDBObject().append("texttt", tweetObj));

				coll.update(new BasicDBObject("_id", new ObjectId(id)), newObj);
				line = in.readLine();
			}
			in.close();
			fstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createFileForTagging(String filename){
		DBCursor cur = coll.find();

		while(cur.hasNext()) {

			try {
				FileWriter fstream = new FileWriter(filename, true);
				BufferedWriter out = new BufferedWriter(fstream);
				DBObject next = cur.next();
				String text = (String) next.get("text");
				if (text.contains("\n"))
					text = text.replaceAll("\n", " ");
				out.write(next.get("_id")+ " " + text + "\n");
				out.close();
				fstream.close();
			} catch (MongoException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String convertTag(String tag) {
		if (tag.equals("$"))
			return "number";
		else if (tag.equals(","))
			return "punct";
		else if (tag.equals("&"))
			return "cc";
		else if (tag.equals("^"))
			return "properNoun";
		else if (tag.equals("!"))
			return "bang";
		else if (tag.equals("#"))
			return "hash";
		else if (tag.equals("@"))
			return "mention";
		else if (tag.equals("~"))
			return "retweet";
		else 
			return tag;
	}
}
