/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

/**
 *
 * @author cec
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream; 
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;  

import opennlp.tools.namefind.NameFinderME; 
import opennlp.tools.namefind.TokenNameFinderModel; 
import opennlp.tools.util.Span;  
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.Tokenizer;



public class ner{ 
   public static void main(String args[]) throws Exception{ 
     //Loading the NER - Person model       
              InputStream inputStream = new FileInputStream("/home/cec/en-ner-person.bin"); 
      TokenNameFinderModel model = new TokenNameFinderModel(inputStream);
      
      //Instantiating the NameFinder class 
      NameFinderME nameFinder = new NameFinderME(model); 
      BufferedReader inn = null;
            inn = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newscorpusNN.txt"));
                //StringBuilder sb = new StringBuilder();
                String s = null;
                int t=0;
                InputStream inputStreamNameFinder = new FileInputStream("/home/cec/en-ner-location.bin");       
                        TokenNameFinderModel modell = new TokenNameFinderModel(inputStreamNameFinder);
                        //Instantiating the NameFinderME class 
                        NameFinderME nameFinderl = new NameFinderME(modell); 
                        
                        
                        InputStream modelInToken = null;
		InputStream modelIn = null;
                        modelInToken = new FileInputStream("/home/cec/en-token.bin");
		    	TokenizerModel modelToken = new TokenizerModel(modelInToken); 
		    	Tokenizer tokenizer = new TokenizerME(modelToken); 
                        
                        
                        
              BufferedWriter writer = new BufferedWriter(new FileWriter("/home/cec/STTM/src/dataset/newscorpusNNner.txt"));
                while ((s = inn.readLine()) != null) {
    
      //Getting the sentence in the form of String array  
     /* String [] sentence = new String[]{ 
         "Modi", 
         "went", 
         "with", 
         "Thomas", 
         "to", 
         "a" ,
         "famous",
         "university",
         "in",
         "India"
      }; */
       
      //Finding the names in the sentence 
              //String[] sentence = tokenize(s);
              /*
                String[] sentence = s.trim().split("\\s+");
               String [] sent=new String[sentence.length];
               int v=0;
                for (String term : sentence) {
                    
                        System.out.print(" "+term);
                        sent[v]=term;
                        v++;
                        
                    }
*/
               
		    	String tokens[] = tokenizer.tokenize(s);

                //System.out.println();
                //System.out.println("Finding nouns");
                
                //sentence="john meera hello";
              //sentence = new String[] {"john", "meera", "run", "DDD", "EEE"};
                Span nameSpans[] = nameFinder.find(tokens); 
                //System.out.println(nameSpans.length);
               
                for(Span sn: nameSpans) 
                {
                    System.out.println(sn.toString());
                    writer.write(sn.toString());
                }
                //find probabilities for names
		    	double[] spanProbs = nameFinder.probs(nameSpans);
		    	
		    	//3. print names
		    	/*for( int i = 0; i<nameSpans.length; i++) {
		    		System.out.println("Span: "+nameSpans[i].toString());
		    		 .out.println("Covered text is: "+tokens[nameSpans[i].getStart()] + " " + tokens[nameSpans[i].getStart()+1]);
		    		System.out.println("Probability is: "+spanProbs[i]);
		    	}	*/
                         
                             
        
      //Finding the names of a location 
      // System.out.println(tokens);
      /* int v=0;
                for (String term : tokens) {
                    
                        System.out.print(" "+term);
                        //sent[v]=term;
                        v++;
                        
                    }
*/
                       Span nameSpansl[] = nameFinderl.find(tokens);        
      //Printing the spans of the locations in the sentence 
                        for(Span sl: nameSpansl)        
                          System.out.println(s.toString()+"  "+tokens[sl.getStart()]); 
                }
                
       
      //Printing the spans of the names in the sentence 
                   
   }    
}      