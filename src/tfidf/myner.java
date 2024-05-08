/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.sequences.SeqClassifierFlags;
import edu.stanford.nlp.util.StringUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.Tokenizer;

/**
 *
 * @author cec
 */

   public class myner
   {public List<String> cnounslist= new ArrayList<String>(); 
   public List<String> pnounslist= new ArrayList<String>(); 
   
       public void cnouns() throws FileNotFoundException, IOException
       {
            BufferedReader in = null;
       
       //for (File f : allfiles) {
          //  if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/nounlist.txt"));
                
             
               for (String doc; (doc = in.readLine()) != null;) {
                   
               
                String[] tokenizedTerms = doc.trim().split("\\s+");
                //String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                for (String term : tokenizedTerms) {
                     //avoid duplicate entry
                        cnounslist.add(term);
                         //System.out.println(term);
                    
                }
                
               } //termsDocsArray.add(tokenizedTerms);
               BufferedReader inc = null;
               inc = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newscorpus.txt"));
               BufferedWriter writeit = new BufferedWriter(new FileWriter("/home/cec/STTM/src/dataset/pnounlist.txt"));
             
                for (String doc; (doc = inc.readLine()) != null;) 
                    {
                        String[] tokenizedTerms = doc.trim().split("\\s+");
                        for (String term : tokenizedTerms) 
                        {
                         if(!cnounslist.contains(term))
                         {   
                             System.out.print(term+" ");
                             pnounslist.add(term);
                             writeit.write(term+" ");
                             
                             
                         }
                        }
                        writeit.newLine();
                       System.out.println();
                    }
               }
               
               
               
            
       
       public void writenames() throws FileNotFoundException, IOException
       {
           BufferedReader inn=null;
                //StringBuilder sb = new StringBuilder();
                String s = null;
                inn = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/id.txt"));
                BufferedWriter writers = new BufferedWriter(new FileWriter("/home/cec/STTM/src/dataset/mynames.txt"));
                 while ((s = inn.readLine()) != null) 
                 {
             
                     String[] sentence = s.trim().split("\\s+");
                    
                     // String [] sent=new String[sentence.length];
                        int v=0;
                         for (String term : sentence) 
                                {
                                System.out.println(" "+term);
                                //sent[v]=term;
                                 writers.write(term+"\t"+"person");
                                 writers.newLine();
                                 v++; 
                                 }
           
                 }
       }
       public void trainAndWrite(String modelOutPath, String prop, String trainingFilepath) 
       {
        Properties props = StringUtils.propFileToProperties(prop);
        props.setProperty("serializeTo", modelOutPath);
   
            trainingFilepath="/home/cec/STTM/src/dataset/mydataset.csv";
            modelOutPath="/home/cec/STTM/ner.model.ser.gz";
             //if input use that, else use from properties file.
             if (trainingFilepath != null) {
               props.setProperty("trainFile", trainingFilepath);
                }

                SeqClassifierFlags flags = new SeqClassifierFlags(props);
                CRFClassifier<CoreLabel> crf = new CRFClassifier<>(flags);
                crf.train();

                crf.serializeClassifier(modelOutPath);
        }
    
     
 public CRFClassifier getModel(String modelPath) {
    return CRFClassifier.getClassifierNoExceptions(modelPath);
}
      
  public void doTagging(CRFClassifier model, String input) {
  input = input.trim();
  System.out.println(input + "=>"  +  model.classifyToString(input));
}     
       public static void main(String args[]) throws Exception
                {
                     myner mn = new myner();
                    // mn.writenames();
                    mn.cnouns();
                     String tfp="/home/cec/STTM/src/dataset/mydataset.csv";
                    String mop="/home/cec/STTM/ner.model.ser.gz";
                    String pro="/home/cec/ner.model.props";
                    mn.trainAndWrite(tfp,pro,mop);
                    CRFClassifier model= mn.getModel(mop);
                     
                    InputStream modelInToken = null;
                    InputStream modelIn = null;
                    modelInToken = new FileInputStream("/home/cec/en-token.bin");
		    TokenizerModel modelToken = new TokenizerModel(modelInToken); 
		    Tokenizer tokenizer = new TokenizerME(modelToken); 
                        
                   //String[] tests = new String[] {"Unni", "aswathi", "table"};
                    BufferedReader inn = null;
                    inn = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newstext.txt"));
                StringBuilder sb = new StringBuilder();
                    String s = null;
                  while ((s = inn.readLine()) != null) 
                   {
                    String tokens[] = tokenizer.tokenize(s);
                        for (String item : tokens) 
                       {
                    mn.doTagging(model, item);
                       }
                
                   }
                   //mn.cnouns();

                }
             
          
          
    
}
