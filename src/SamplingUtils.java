import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class SamplingUtils {

	public static long[] pickSample(long[] population, int nSamplesNeeded, Random r) {

		long[] ret = (long[]) Array.newInstance(population.getClass().getComponentType(), nSamplesNeeded);

		int nPicked = 0, i = 0, nLeft = population.length;
		while (nSamplesNeeded > 0) {
			int rand = r.nextInt(nLeft);
			if (rand < nSamplesNeeded) {
				ret[nPicked++] = population[i];
				nSamplesNeeded--;
			}
			nLeft--;
			i++;
		}
		return ret;
	}
	
	


//	public static <long> Set<long> randomSample4(List<long> items, int m){   
//		HashSet<long> res = new HashSet<long>(m); 
//		int n = items.size();
//		for(int i=n-m;i<n;i++){
//			int pos = rnd.nextInt(i+1);
//			long item = items.get(pos);
//			if (res.contains(item))
//				res.add(items.get(i));
//			else
//				res.add(item);      
//		}
//		return res;
//	}

}
