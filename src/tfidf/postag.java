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

import opennlp.tools.postag.POSModel; 
import opennlp.tools.postag.POSSample; 
import opennlp.tools.postag.POSTaggerME; 
import opennlp.tools.tokenize.WhitespaceTokenizer;  

public class postag{ 
  
   public static void main(String args[]) throws Exception{ 
    
      //Loading Parts of speech-maxent model       
      InputStream inputStream = new 
         FileInputStream("/home/cec/en-pos-maxent.bin"); 
      POSModel model = new POSModel(inputStream); 
       
      //Instantiating POSTaggerME class 
      POSTaggerME tagger = new POSTaggerME(model); 
       BufferedReader inn = null;
            inn = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newscorpus.txt"));
                //StringBuilder sb = new StringBuilder();
                String s = null;
                int t=0;
                BufferedWriter writer = new BufferedWriter(new FileWriter("/home/cec/STTM/src/dataset/newscorpusNN.txt"));
                while ((s = inn.readLine()) != null) {
       
      String sentence = s;
      //Tokenizing the sentence using WhitespaceTokenizer class  
      WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE; 
      String[] tokens = whitespaceTokenizer.tokenize(sentence); 
       
      //Generating tags 
      String[] tags = tagger.tag(tokens);
      
      for(int i=0;i<(tags.length);i++)
      {
       if(tags[i].equals("NN"))
       {
        System.out.println(t+" "+tokens[i]);
        writer.write(tokens[i]+" ");
       }
      }
      writer.newLine();
      
      //Instantiating the POSSample class 
      //POSSample sample = new POSSample(tokens, tags); 
      //System.out.println(sample.toString()); 
      t++;
                }
   
   } 
}     