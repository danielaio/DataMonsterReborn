import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

import cc.mallet.classify.Classification;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.InstanceList;
import cc.mallet.util.Randoms;


public class NaiveBayesClassification {


	public static InstanceList createInstances(String dataFile) {

		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		pipeList.add(new Target2Label());
		pipeList.add(new CharSequence2TokenSequence());
		pipeList.add(new TokenSequence2FeatureSequence());
		pipeList.add(new FeatureSequence2FeatureVector());

		InstanceList instances = new InstanceList(new SerialPipes(pipeList));
		try {
			instances.addThruPipe(new CsvIterator(new FileReader(dataFile), "(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return instances;

	}

	public static void main (String Args []) {                              

//		String file = "allTweetsNaiveBayes.txt";
		String file = "allTweetsWinnow2.txt";

		InstanceList instances = createInstances(file);
		InstanceList[] instanceLists = instances.split(new Randoms(), new double[] {0.9, 0.1, 0.0});

		NaiveBayesTrainer trainer = new NaiveBayesTrainer ();
		NaiveBayes cl = trainer.train(instanceLists[0]);

		Trial trial = new Trial(cl, instanceLists[1]);
		
		System.out.println("Accuracy: " + trial.getAccuracy());
		System.out.println("F1 for class Y: " + trial.getF1("Y"));
		System.out.println("F1 for class O: " + trial.getF1("O"));
		System.out.println("Recall: " + trial.getRecall("Y"));
		System.out.println("Recall: " + trial.getRecall("O"));
		
	}
}
