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
			db = m.getDB(RunExperiment.DB);
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
		
		long[] arr = new long[4000];
		for (int i = 0; i < arr.length ; i++) {
			arr[i] = ((Long) cur.next().get("_id")).longValue();
		}
		
		return arr;
	}
	
	public DBCursor getCollectedFollowersFromDBInPortions(int max) {
		
		DBCursor cur = coll.find();
		return cur;
	}
		
	public long[] getRandomSampleOfFollowersFromDB(int number) {
	
		
		long[] res = new long[number];
		for (int i = 0; i < number; i++) {
			double random = Math.random();
			double cmp = Math.random();
			
			res[i] = (Long) coll.findOne().get("_id");
		}
		
		return res;
	}

}
