import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;

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

				BasicDBObject tweetObj = new BasicDBObject();
				while (!line.equals("")) {
					//each line is a word, tag pair
					String word = line.substring(0, line.indexOf("\t"));
					String tag = line.substring(line.indexOf("\t") + 1);
					tweetObj.put(word.contains(".") ? "ellipsis" : word, tag);
					line = in.readLine();
				}

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

}
