import java.util.HashMap;
import java.util.List;

import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class FeatureExtraction {

	/**
	 * @param args
	 */

	
	
	public static void main(String[] args) {
	}
	
	public static void extractFeatures() {

		
		DatabaseUtils utils = new DatabaseUtils();

		List<DBObject> users = utils.getUsers();
		
		for (DBObject user : users) {
			DBCursor tweetsForUser = utils.getTweetsForUser(user);
			
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			
			while (tweetsForUser.hasNext()) {
				//tweetsForUser.next().get(key)
			}
			
		}
		
	}
}
