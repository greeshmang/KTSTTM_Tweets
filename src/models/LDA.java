package models;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import utility.FuncUtils;

/**
 * STTM: A Java package for short text topic models
 * 
 * Implementation of the Latent Dirichlet Allocation topic model, using
 * collapsed Gibbs sampling, as described in:
 * 
 * Thomas L. Griffiths and Mark Steyvers. 2004. Finding scientific topics.
 * Proceedings of the National Academy of Sciences of the United States of
 * America, 101(Suppl 1):5228–5235.
 * 
 * @author: Jipeng Qiang */

public class LDA
{
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
											
        public List<String> poliTerms = new ArrayList<String>(); //to hold all terms
        public List<String> filmTerms = new ArrayList<String>();// given an ID
        public List<String> sportsTerms = new ArrayList<String>();// given an ID
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

	public LDA(String pathToCorpus, int inNumTopics,
		double inAlpha, double inBeta, int inNumIterations, int inTopWords)
		throws Exception
	{
		this(pathToCorpus, inNumTopics, inAlpha, inBeta, inNumIterations,
			inTopWords, "LDAmodel");
	}

	public LDA(String pathToCorpus, int inNumTopics,
		double inAlpha, double inBeta, int inNumIterations, int inTopWords,
		String inExpName)
		throws Exception
	{

		alpha = inAlpha;
		beta = inBeta;
		K = inNumTopics;
		ITERATIONS = inNumIterations;
		topWords = inTopWords;

		expName = inExpName;
		orgExpName = expName;
		corpusPath = pathToCorpus;
		folderPath = "/home/cec/STTM/result/";
		File dir = new File(folderPath);
		if (!dir.exists())
			dir.mkdir();

		System.out.println("Reading topic modeling corpus: " + pathToCorpus);

		word2IdVocabulary = new HashMap<String, Integer>();
		id2WordVocabulary = new HashMap<Integer, String>();
		corpus = new ArrayList<List<Integer>>();
		numDocuments = 0;
		numWordsInCorpus = 0;

		BufferedReader br = null;
		try {
			int indexWord = -1;
			br = new BufferedReader(new FileReader(pathToCorpus));
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
				corpus.add(document);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
                System.out.println("Reading external corpus: ");

		word2IdVocabularyE = new HashMap<String, Integer>();
		id2WordVocabularyE = new HashMap<Integer, String>();
		corpusE = new ArrayList<List<Integer>>();
		numDocumentsE = 0;
		numWordsInCorpusE = 0;

		BufferedReader brr = null;
		try {
			int indexWord = -1;
			brr = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newscorpus.txt"));
			for (String doc; (doc = brr.readLine()) != null;) {

				if (doc.trim().length() == 0) {

					System.out.println("number of documents"+numDocumentsE);
					continue;
				}

				String[] words = doc.trim().split("\\s+");
				List<Integer> documentE = new ArrayList<Integer>();

				if(words.length==0)
					System.out.println("here!");
				for (String word : words) {
					if (word2IdVocabularyE.containsKey(word)) {
						documentE.add(word2IdVocabularyE.get(word));
					}
					else {
						indexWord += 1;
						word2IdVocabularyE.put(word, indexWord);
						id2WordVocabularyE.put(indexWord, word);
						documentE.add(indexWord);
					}
				}

				numDocumentsE++;
				numWordsInCorpusE += documentE.size();
				corpusE.add(documentE);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		V = word2IdVocabulary.size(); // vocabularySize = indexWord


		// initialise count variables.
		nw = new int[V][K];
		nd = new int[numDocuments][K];
		nwsum = new int[K];
		ndsum = new int[numDocuments];
               
                
                VE = word2IdVocabularyE.size(); // vocabularySize = indexWord


		// initialise count variables.
		nwE = new int[VE][K];
		ndE = new int[numDocumentsE][K];
		nwsumE = new int[K];
		ndsumE = new int[numDocumentsE];

		multiPros = new double[K];
		for (int i = 0; i < K; i++) {
			multiPros[i] = 1.0 / K;
		}

		z = new int[numDocuments][];
                zE = new int[numDocumentsE][];
                numD=numDocuments+numDocumentsE;
                Z = new int[numD][];
                //newly added portion to read political terms
                BufferedReader in = null;
                in = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/terms_poli.txt"));
             for (String doc; (doc = in.readLine()) != null;) {
               String[] tokenizedTerms = doc.trim().split("\\s+");
                //String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                for (String term : tokenizedTerms) {
                    if (!poliTerms.contains(term)) {  //avoid duplicate entry
                        poliTerms.add(term);
                        //System.out.print(term);
                    }
                }
               
                } 
             BufferedReader inn = null;
                inn = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/terms_film.txt"));
             for (String doc; (doc = inn.readLine()) != null;) {
               String[] tokenizedTerms = doc.trim().split("\\s+");
                //String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                for (String term : tokenizedTerms) {
                    if (!filmTerms.contains(term)) {  //avoid duplicate entry
                        filmTerms.add(term);
                        //System.out.print(term);
                    }
                }
               
                } 
               BufferedReader innn = null;
                innn = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/terms_sports.txt"));
             for (String doc; (doc = innn.readLine()) != null;) {
               String[] tokenizedTerms = doc.trim().split("\\s+");
                //String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                for (String term : tokenizedTerms) {
                    if (!sportsTerms.contains(term)) {  //avoid duplicate entry
                        sportsTerms.add(term);
                        //System.out.print(term);
                    }
                }
               
                } 


		System.out.println("Corpus size: " + numDocuments + " docs, "
			+ numWordsInCorpus + " words");
		System.out.println("Vocabuary size: " + V);
                System.out.println("External Corpus size: " + numDocumentsE + " docs, "
			+ numWordsInCorpusE + " words");
		System.out.println("External Vocabuary size: " + VE);
		System.out.println("Number of topics: " + K);
		System.out.println("alpha: " + alpha);
		System.out.println("beta: " + beta);
		System.out.println("Number of sampling iterations: " + ITERATIONS);
		System.out.println("Number of top topical words: " + topWords);

		initialize();
	}

	/**
	 * Randomly initialize topic assignments
	 */
        
	public void initialize()
		throws IOException
	{
		System.out.println("Randomly initializing topic assignments for the corpus ...");

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < numDocuments; i++) {
			List<Integer> topics = new ArrayList<Integer>();
			int docSize = corpus.get(i).size();
			z[i] = new int[docSize];
                        Z[i]= new int[docSize];
			for (int j = 0; j < docSize; j++) {
				int topic = FuncUtils.nextDiscrete(multiPros); // Sample a topic
				// Increase counts
				nd[i][topic] += 1;
				nw[corpus.get(i).get(j)][topic] += 1;
				ndsum[i] += 1;
				nwsum[topic] += 1;
				z[i][j] = topic;
                                Z[i][j]=topic;
			}

		}
                
                System.out.println("Randomly initializing topic assignments for the external corpus ...");
                for (int i = 0; i < numDocumentsE; i++) {
			List<Integer> topicsE = new ArrayList<Integer>();
			int docSizeE = corpusE.get(i).size();
			zE[i] = new int[docSizeE];
                        Z[i+numDocuments]= new int[docSizeE];
			for (int j = 0; j < docSizeE; j++) {
				int topicE = FuncUtils.nextDiscrete(multiPros); // Sample a topic
				// Increase counts
				ndE[i][topicE] += 1;
				nwE[corpusE.get(i).get(j)][topicE] += 1;
				ndsumE[i] += 1;
				nwsumE[topicE] += 1;
				zE[i][j] = topicE;
                                Z[i+numDocuments][j]=topicE;
			}

		}
                /*
                for( int i=0;i<numDocuments+numDocumentsE;i++)
                {System.out.print("at"+ i+"- ");
                     for( int j=0;j<Z[i].length;j++)
                       System.out.print(Z[i][j]);
                  System.out.println();   
                }
                */
                    
                
		initTime =System.currentTimeMillis()-startTime;
	}

	public void inference()
		throws IOException
	{

		writeDictionary();

		System.out.println("Running Gibbs sampling inference: ");

		// init sampler statistics
		if (SAMPLE_LAG > 0) {
			thetasum = new double[numDocuments][K];
			phisum = new double[K][V];
			numstats = 0;
		}



		System.out.println("Sampling " + ITERATIONS
				+ " iterations with burn-in of " + BURN_IN + " (B/S="
				+ THIN_INTERVAL + ").");

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < ITERATIONS; i++) {

			// for all z_i
			for (int m = 0; m < z.length; m++) {
				for (int n = 0; n < z[m].length; n++) {

					// (z_i = z[m][n])
					// sample from p(z_i|z_-i, w)
					int topic = sampleFullConditional(m, n);
					z[m][n] = topic;
				}
			}
                        /*
                        for (int m = 0; m < zE.length; m++) {
				for (int n = 0; n < zE[m].length; n++) {

					// (z_i = z[m][n])
					// sample from p(z_i|z_-i, w)
					int topic = sampleFullConditionalE(m, n);
					zE[m][n] = topic;
				}
			}*/
                       

			if ((i < BURN_IN) && (i % THIN_INTERVAL == 0)) {
				System.out.print("B");
				dispcol++;
			}
			// display progress
			if ((i > BURN_IN) && (i % THIN_INTERVAL == 0)) {
				System.out.print("S");
				dispcol++;
			}
			// get statistics after burn-in
			if ((i > BURN_IN) && (SAMPLE_LAG > 0) && (i % SAMPLE_LAG == 0)) {
				updateParams();
				System.out.print("|");
				if (i % THIN_INTERVAL != 0)
					dispcol++;
			}
			if (dispcol >= 100) {
				System.out.println();
				dispcol = 0;
			}
		}

		iterTime =System.currentTimeMillis()-startTime;

		expName = orgExpName;

		System.out.println("Writing output from the last sample ...");
		write();

		System.out.println("Sampling completed!");

	}


	/**
	 * Add to the statistics the values of theta and phi for the current state.
	 */
	private void updateParams() {
		for (int m = 0; m < numDocuments; m++) {
			for (int k = 0; k < K; k++) {
				thetasum[m][k] += (nd[m][k] + alpha) / (ndsum[m] + K * alpha);
			}
		}
                for (int m = 0; m < numDocumentsE; m++) {
			for (int k = 0; k < K; k++) {
				thetasum[m][k+numDocuments] += (ndE[m][k] + alpha) / (ndsumE[m] + K * alpha);
			}
		}
		for (int k = 0; k < K; k++) {
			for (int w = 0; w < V; w++) {
				phisum[k][w] += (nw[w][k] + beta) / (nwsum[k] + V * beta);
			}
		}
                for (int k = 0; k < K; k++) {
			for (int w = 0; w < VE; w++) {
				phisum[k][w+V] += (nwE[w][k] + beta) / (nwsumE[k] + V * beta);
			}
		}
		numstats++;
	}

	/**
	 * Sample a topic z_i from the full conditional distribution: p(z_i = j |
	 * z_-i, w) = (n_-i,j(w_i) + beta)/(n_-i,j(.) + W * beta) * (n_-i,j(d_i) +
	 * alpha)/(n_-i,.(d_i) + K * alpha)
	 *
	 * @param m
	 *            document
	 * @param n
	 *            word
	 */
	private int sampleFullConditional(int m, int n) {

		// remove z_i from the count variables
            //    System.out.println("index are "+m+" and "+n);

		int topic = z[m][n];
		nw[corpus.get(m).get(n)][topic]--;
		nd[m][topic]--;
		nwsum[topic]--;
		//ndsum[m]--;

		// do multinomial sampling via cumulative method:
		double[] p = new double[K];
                int f=0;
		//int maxK = -1;
		// double value = 0;
                
		for (int k = 0; k < K; k++) 
                { 
                    int wiE;
                    String word=id2WordVocabulary.get(corpus.get(m).get(n));
                    if(poliTerms.contains(word))
                    { f=1;
                       topic=0;
                       nw[corpus.get(m).get(n)][topic]++;
		       nd[m][topic]++;
		       nwsum[topic]++;
                    }
                    else if(filmTerms.contains(word))
                    {
                        f=1;
                        topic=1;
                       nw[corpus.get(m).get(n)][topic]++;
		       nd[m][topic]++;
		       nwsum[topic]++;
                    }
                    
                    else if(sportsTerms.contains(word))
                    {
                        f=1;
                        topic=2;
                       nw[corpus.get(m).get(n)][topic]++;
		       nd[m][topic]++;
		       nwsum[topic]++;
                    }
                }
                if(f==0)
                 {
                       for (int k = 0; k < K; k++) 
                       {
			p[k] = (nw[corpus.get(m).get(n)][k] + beta) / (nwsum[k] + V * beta)
					* (nd[m][k] + alpha);// / (ndsum[m] + K * alpha);

            
                        }
                    
                        for (int k = 1; k < p.length; k++) 
                         {
                            p[k] += p[k - 1];
                         }
                        // scaled sample because of unnormalised p[]
                        double u = Math.random() * p[K - 1];
                        for (topic = 0; topic < p.length; topic++) 
                        {
			if (u < p[topic])
				break;
                        }
                        // topic = maxK;
                        // add newly estimated z_i to count variables
                       // System.out.println(topic+ "is the topic");
                        nw[corpus.get(m).get(n)][topic]++;
                        nd[m][topic]++;
                        nwsum[topic]++;
                        // ndsum[m]++;
                 }

                return topic;
	}

        private int sampleFullConditionalE(int m, int n) {

		// remove z_i from the count variables
                //System.out.println("index are "+(m+numDocuments)+" and "+n);
		int topic = Z[m+numDocuments][n];
		nwE[corpusE.get(m).get(n)][topic]--;
		ndE[m][topic]--;
		nwsumE[topic]--;
		//ndsum[m]--;

		// do multinomial sampling via cumulative method:
		double[] p = new double[K];
		//int maxK = -1;
		// double value = 0;
		for (int k = 0; k < K; k++) {
		p[k] = ((nwE[corpusE.get(m).get(n)][k] + beta) / (nwsumE[k] + VE * beta)* (ndE[m][k] + alpha) / (ndsumE[m] + K * alpha));
            /*if(p[k]>value)
            {
            	value = p[k];
            	maxK = k;
            }*/
		}
		// cumulate multinomial parameters
		for (int k = 1; k < p.length; k++) {
			p[k] += p[k - 1];
		}
		// scaled sample because of unnormalised p[]
		double u = Math.random() * p[K - 1];
		for (topic = 0; topic < p.length; topic++) {
			if (u < p[topic])
				break;
		}
		// topic = maxK;
		// add newly estimated z_i to count variables
		nwE[corpusE.get(m).get(n)][topic]++;
		ndE[m][topic]++;
		nwsumE[topic]++;
		// ndsum[m]++;

		return topic;
	}



	public void writeParameters()
		throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath
			+ expName + ".paras"));
		writer.write("-model" + "\t" + "LDA");
		writer.write("\n-corpus" + "\t" + corpusPath);
		writer.write("\n-ntopics" + "\t" + K);
		writer.write("\n-alpha" + "\t" + alpha);
		writer.write("\n-beta" + "\t" + beta);
		writer.write("\n-niters" + "\t" + ITERATIONS);
		writer.write("\n-twords" + "\t" + topWords);
		writer.write("\n-name" + "\t" + expName);

		writer.write("\n-initiation time" + "\t" + initTime);
		writer.write("\n-one iteration time" + "\t" + iterTime/ITERATIONS);
		writer.write("\n-total time" + "\t" + (initTime+iterTime));

		writer.close();
	}

	public void writeDictionary()
		throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath
			+ expName + ".vocabulary"));
		for (int id = 0; id < V; id++)
			writer.write(id2WordVocabulary.get(id) + " " + id + "\n");
                //for (int id = 0; id < VE; id++)
		//	writer.write(id2WordVocabularyE.get(id) + " " + id + "\n");
                
		writer.close();
	}



	public void writeTopicAssignments()
		throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath
			+ expName + ".topicAssignments"));
		for (int dIndex = 0; dIndex < numDocuments; dIndex++) {
			int docSize = corpus.get(dIndex).size();
			for (int wIndex = 0; wIndex < docSize; wIndex++) {
				writer.write(z[dIndex][wIndex] + " ");
			}
			writer.write("\n");
		}
                /*for (int dIndex = 0; dIndex < numDocumentsE; dIndex++) {
			int docSizeE = corpusE.get(dIndex).size();
			for (int wIndex = 0; wIndex < docSizeE; wIndex++) {
				writer.write(zE[dIndex][wIndex] + " ");
			}
			writer.write("\n");
		}*/
		writer.close();
	}

	public void writeTopTopicalWords()
		throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath
			+ expName + ".topWords"));

		for (int tIndex = 0; tIndex < K; tIndex++) {
			//writer.write("Topic" + new Integer(tIndex) + ":");

			Map<Integer, Integer> wordCount = new TreeMap<Integer, Integer>();
			for (int wIndex = 0; wIndex < V; wIndex++) {
				wordCount.put(wIndex, nw[wIndex][tIndex]);
			}
                        
			wordCount = FuncUtils.sortByValueDescending(wordCount);

			Set<Integer> mostLikelyWords = wordCount.keySet();
			int count = 0;
			for (Integer index : mostLikelyWords) {
				if (count < topWords) {
					double pro = (nw[index][tIndex] + beta)
						/ (nwsum[tIndex] + beta*V);
					pro = Math.round(pro * 1000000.0) / 1000000.0;
                                        if(id2WordVocabulary.get(index)!=null)
                                            writer.write( id2WordVocabulary.get(index) + " ");
                                       
					count += 1;
				}
				else {
					writer.write("\n");
					break;
				}
			}
		}
                /*for (int tIndex = 0; tIndex < K; tIndex++) {
			//writer.write("Topic" + new Integer(tIndex) + ":");

			Map<Integer, Integer> wordCount = new TreeMap<Integer, Integer>();
			for (int wIndex = 0; wIndex < VE; wIndex++) {
				wordCount.put(wIndex, nwE[wIndex][tIndex]);
			}
                       
			wordCount = FuncUtils.sortByValueDescending(wordCount);

			Set<Integer> mostLikelyWords = wordCount.keySet();
			int count = 0;
			for (Integer index : mostLikelyWords) {
				if (count < topWords) {
					double pro = (nwE[index][tIndex] + beta)
						/ (nwsumE[tIndex] + beta*VE);
					pro = Math.round(pro * 1000000.0) / 1000000.0;
                                        if(id2WordVocabularyE.get(index)!=null)
                                            writer.write( id2WordVocabulary.get(index) + " ");
                                      
					count += 1;
				}
				else {
					writer.write("\n");
					break;
				}
			}
                     }*/
		
		writer.close();
        }
	

	/**
	 * Retrieve estimated topic--word associations. If sample lag > 0 then the
	 * mean value of all sampled statistics for phi[][] is taken.
	 *

	 */
	public void writeTopicWordPros()
			throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath
				+ expName + ".phi"));
		if (SAMPLE_LAG > 0) {
			for (int k = 0; k < K; k++) {
				for (int w = 0; w < V; w++) {
					double pro = phisum[k][w] / numstats;
					writer.write(pro + " ");
				}
				writer.write("\n");
			}
		} else {
			for (int k = 0; k < K; k++) {
				for (int w = 0; w < V; w++) {
					double pro = (nw[w][k] + beta) / (nwsum[k] + V * beta);
					writer.write(pro + " ");
				}
				writer.write("\n");
			}
		}
		writer.close();

	}

	/**
	 * Retrieve estimated document--topic associations. If sample lag > 0 then
	 * the mean value of all sampled statistics for theta[][] is taken.
	 *

	 */
	public void writeDocTopicPros()
			throws IOException{

		BufferedWriter writer = new BufferedWriter(new FileWriter(folderPath
				+ expName + ".theta"));


		if (SAMPLE_LAG > 0) {
			for (int m = 0; m < numDocuments; m++) {
				for (int k = 0; k < K; k++) {
					double pro = thetasum[m][k] / numstats;
					writer.write(pro + " ");
				}
				writer.write("\n");
			}
		}
		else{
			for (int m = 0; m < numDocuments; m++) {
				for (int k = 0; k < K; k++) {
					double pro = (nd[m][k] + alpha) / (ndsum[m] + K * alpha);
					writer.write(pro + " ");
				}
				writer.write("\n");
			}
		}

		writer.close();

	}

	public void write()
		throws IOException
	{
		writeTopTopicalWords();
		writeDocTopicPros();
		writeTopicAssignments();
		writeTopicWordPros();

		writeParameters();
	}

	public static void main(String args[])
		throws Exception
	{
		LDA lda = new LDA("/home/cec/STTM/src/dataset/tweetnews.txt",3, 0.1,0.1, 1000, 10, "tweet");
		lda.inference();
	}
}
