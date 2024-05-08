/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import models.LDA;

/**
 *
 * @author cec
 */
public class cluster {
    public double alpha; // Hyper-parameter alpha
	public double beta; // Hyper-parameter alpha
	public int K; // Number of topics

	public int topWords; // Number of most probable words for each topic



	public List<List<Integer>> corpus; // Word ID-based corpus
	public List<List<Integer>> corpusE; // Word ID-based corpus

													// in the corpus
	public int numDocuments; // Number of documents in the corpus
	public int numWordsInCorpus; // Number of words in the corpus
        public int numDocumentsE; // Number of documents in the corpus
	public int numWordsInCorpusE;
        public int numD;// total number of documents

	public HashMap<String, Integer> word2IdVocabulary; // Vocabulary to get ID
														// given a word
	public HashMap<Integer, String> id2WordVocabulary; // Vocabulary to get word
        public HashMap<String, Integer> word2IdVocabularyE; // Vocabulary to get ID
														// given a word
	public HashMap<Integer, String> id2WordVocabularyE; // Vocabulary to get word
														// given an ID
	public int V; // The number of word types in the corpus
        public int VE; // The number of word types in the corpus

	/**
	 * topic assignments for each word.
	 */
	int z[][];
        int zE[][];
        int Z[][];

	/**
	 * cwt[i][j] number of instances of word i (term?) assigned to topic j.
	 */
	int[][] nw;


	/**
	 * na[i][j] number of words in document i assigned to topic j.
	 */
	int[][] nd;
	/**
	 * nwsum[j] total number of words assigned to topic j.
	 */
	int[] nwsum;

	/**
	 * nasum[i] total number of words in document i.
	 */
	int[] ndsum;
        int[][] nwE;
        


	/**
	 * na[i][j] number of words in document i assigned to topic j.
	 */
	int[][] ndE;
	/**
	 * nwsum[j] total number of words assigned to topic j.
	 */
	int[] nwsumE;

	/**
	 * nasum[i] total number of words in document i.
	 */
	int[] ndsumE;

	/**
	 * cumulative statistics of theta
	 */
	double[][] thetasum;


	/**
	 * cumulative statistics of phi
	 */
	double[][] phisum;

	/**
	 * size of statistics
	 */
	int numstats;
	/**
	 * sampling lag (?)
	 */
	private static int THIN_INTERVAL = 20;

	/**
	 * burn-in period
	 */
	private static int BURN_IN = 100;

	/**
	 * max iterations
	 */
	private static int ITERATIONS = 1000;

	private static int dispcol = 0;
	/**
	 * sample lag (if -1 only one sample taken)
	 */
	private static int SAMPLE_LAG;
	// Double array used to sample a topic
	public double[] multiPros;

	// Path to the directory containing the corpus
	public String folderPath;
	// Path to the topic modeling corpus
	public String corpusPath;

	public String expName = "LDAmodel";
	public String orgExpName = "LDAmodel";


	public double initTime = 0;
	public double iterTime = 0;
    public cluster()throws Exception
	
    {

		//alpha = inAlpha;
		//beta = inBeta;
		//K = inNumTopics;
		//ITERATIONS = inNumIterations;
		//topWords = inTopWords;

		//expName = inExpName;
		//orgExpName = expName;
//		corpusPath = pathToCorpus;
		folderPath = "/home/cec/STTM/results/";
		File dir = new File(folderPath);
		if (!dir.exists())
			dir.mkdir();

		//System.out.println("Reading topic modeling corpus: " + pathToCorpus);

		word2IdVocabulary = new HashMap<String, Integer>();
		id2WordVocabulary = new HashMap<Integer, String>();
		//corpus = new ArrayList<List<Integer>>();
		numDocuments = 0;
		numWordsInCorpus = 0;

		BufferedReader br = null;
		try {
			int indexWord = -1;
			br = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newscorpus.txt"));
			for (String doc; (doc = br.readLine()) != null;) {

				if (doc.trim().length() == 0) {

					System.out.println(numDocuments);
					continue;
				}

				String[] words = doc.trim().split("\\s+");
				List<Integer> document = new ArrayList<Integer>();

				if(words.length==0)
					System.out.println("here!");
				for (String word : words) {
					if (word2IdVocabulary.containsKey(word)) {
						document.add(word2IdVocabulary.get(word));
					}
					else {
						indexWord += 1;
						word2IdVocabulary.put(word, indexWord);
						id2WordVocabulary.put(indexWord, word);
						document.add(indexWord);
					}
				}

				numDocuments++;
				numWordsInCorpus += document.size();
				//corpus.add(document);
			}
		}
                
                
		catch (Exception e) {
			e.printStackTrace();
		}
                

		//System.out.println("Corpus size: " + numDocuments + " docs, "
		//	+ numWordsInCorpus + " words");
		//System.out.println("Vocabuary size: " + V);
               // System.out.println("External Corpus size: " + numDocumentsE + " docs, "
		//	+ numWordsInCorpusE + " words");
		//System.out.println("External Vocabuary size: " + VE);
		//System.out.println("Number of topics: " + K);
		//System.out.println("alpha: " + alpha);
		//System.out.println("beta: " + beta);
		//System.out.println("Number of sampling iterations: " + ITERATIONS);
		//System.out.println("Number of top topical words: " + topWords);

		//initialize();
    }
    public void writevalues()
    {
        for(int k=0;k<id2WordVocabulary.size();k++)
                {System.out.println("word id "+ id2WordVocabulary);
                
                }
    }
   
    
public static void main(String args[]) throws Exception
	{
             DocumentParser dp = new DocumentParser();
             //dp.cNN();
             //dp.parseFiles("/home/cec/STTM/src/dataset/newscorpusNN.txt");
            // dp.terms();
            // dp.countterms("/home/cec/STTM/src/dataset/newscorpusNN.txt");
             
		//cluster cl = new cluster();
               // cl.writevalues();
              // dp.clusterterms();
              dp.createanno();
		
	}
	
    
}
