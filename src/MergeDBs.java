import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;


public class MergeDBs {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			Mongo m = new Mongo();
			DB db = m.getDB("mydb");
			DB db2 = m.getDB("mydb2");
			
			DBCollection coll = db.getCollection("youngstersTweets");
			DBCollection coll2 = db2.getCollection("youngstersTweets");
			
			DBCursor cur = coll.find();
			while (cur.hasNext()) {
				DBObject obj = cur.next();
				coll2.save(obj);
			}
			
			DBCollection coll3 = db.getCollection("oldstersTweets");
			DBCollection coll4 = db2.getCollection("oldstersTweets");
			
			DBCursor c = coll3.find();
			while(c.hasNext()) {
				DBObject o = c.next();
				coll4.save(o);
			}
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		
		
	}

}
