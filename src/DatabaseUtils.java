import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

//it's possible that getting a db object every time doesn't work..
public class DatabaseUtils {

	private Mongo m;

	public DatabaseUtils() {
		try {
			m = new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}


	public void storeTweet(String tweet) {
		DB db = m.getDB( "mydb" );

		DBCollection coll = db.getCollection("youngstersTweets");
		coll.save((DBObject) JSON.parse(tweet));
	}

	public List<DBObject> getUsers() {
		DB db = m.getDB("mydb");
		DBCollection coll = db.getCollection("youngstersTweets");

		DBCursor cur = coll.find(new BasicDBObject(), new BasicDBObject("user.id_str", 1));

		return cur.toArray();

	}

	public DBCursor getTweetsForUser(DBObject user) {

		DB db = m.getDB("mydb");
		DBCollection coll = db.getCollection("youngstersTweets");

		DBCursor cur = coll.find(new BasicDBObject("user.id_str", ((DBObject)(user.get("user"))).get("id_str")));
		return cur;

	}

	public void getAllTweets() {
		DB db = m.getDB("mydb");
		DBCollection coll = db.getCollection("youngstersTweets");
		DBCursor cur = coll.find();
		while (cur.hasNext()) {
			System.out.println(cur.next());
		}
	}

	public void createFileForTagging(String filename){

		DB db = m.getDB("mydb");
		DBCollection coll = db.getCollection("youngstersTweets");

		DBCursor cur = coll.find();


		while(cur.hasNext()) {

			try {
				FileWriter fstream = new FileWriter(filename, true);
				BufferedWriter out = new BufferedWriter(fstream);
				DBObject next = cur.next();
				out.write(next.get("_id")+ " " + next.get("text").toString() + "\n");
				out.close();
				fstream.close();
			} catch (MongoException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	//method to parse output file 
	public void storeTaggedTweets(String output) {

		DB db = m.getDB("mydb");
		DBCollection coll = db.getCollection("youngstersTweets");

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
				BasicDBObject newObj = new BasicDBObject().append("$set", new BasicDBObject().append("text", tweetObj));

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
