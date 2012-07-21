import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;

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
	
	public void createFileForTagging(String filename){
		
		DB db = m.getDB("mydb");
		DBCollection coll = db.getCollection("youngstersTweets");
		
		DBCursor cur = coll.find();

		
        while(cur.hasNext()) {
        	
        	try {
        		FileWriter fstream = new FileWriter(filename, true);
        		BufferedWriter out = new BufferedWriter(fstream);
				out.write(cur.next().get("text").toString() + "\n");
				out.close();
				fstream.close();
			} catch (MongoException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	
        }
        
	}
}
