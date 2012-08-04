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
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

//it's possible that getting a db object every time doesn't work..
public class FollowersStorageUtils {

	private Mongo m;
	private DB db;
	private DBCollection coll;
	
	
	public FollowersStorageUtils(String collection) {
		try {
			m = new Mongo();
			db = m.getDB("mydb");
			coll = db.getCollection(collection);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
	
	public void storeFollowers(ArrayList<Long> followers) {

		for (long follower : followers) {			
			BasicDBObject f = new BasicDBObject("_id", follower);
			coll.save(f);
		}
	}
	
	public long[] getCollectedFollowersFromDB() {
		DBCursor cur = coll.find();
		System.out.println(cur.count());
		
		long[] arr = new long[cur.count()];
		for (int i = 0; i < cur.count(); i++) {
			arr[i] = ((Long) cur.next().get("_id")).longValue();
		}
		
		return arr;
	}
		

}