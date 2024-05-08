/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

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
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 *
 * @author cec
 */
public class myname {
    public List<String> loclist= new ArrayList<String>(); 
    public List<String> namelist= new ArrayList<String>();
    public List<String> orglist= new ArrayList<String>();

    public void preparelist() throws FileNotFoundException, IOException
    {
        BufferedReader inn = null;
            inn = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/cities500.txt"));
                //StringBuilder sb = new StringBuilder();
                String s = null;
              
            //  BufferedWriter writer = new BufferedWriter(new FileWriter("/home/cec/STTM/src/dataset/locc.txt"));
                for (String doc; (doc = inn.readLine()) != null;) 
                    { int i=0;
                        String[] tokenizedTerms = doc.trim().split("\\s+");
                        s=tokenizedTerms[2];
                        
                         if(!loclist.contains(s))
                         {   i++;
                            
                             //System.out.println(s);
                             loclist.add(s);
                            // writer.write(s+"\t"+"location");
                             
                             
                         }
                        
                        //System.out.println(tokenizedTerms[2]);
                       // writer.newLine();
                       //System.out.println();
                    }
                BufferedReader in = null;
            in = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/mynames.txt"));
                //StringBuilder sb = new StringBuilder();
                String ss = null;
              
            //  BufferedWriter writer = new BufferedWriter(new FileWriter("/home/cec/STTM/src/dataset/locc.txt"));
                for (String doc; (doc = in.readLine()) != null;) 
                    { int i=0;
                        String[] tokenizedTerms = doc.trim().split("\\s+");
                        ss=tokenizedTerms[0];
                        
                         if(!loclist.contains(ss))
                         {   i++;
                            
                          //   System.out.println(s);
                             loclist.add(s);
                            // writer.write(s+"\t"+"location");
                             
                             
                         }
                        
                        //System.out.println(tokenizedTerms[2]);
                       // writer.newLine();
                       //System.out.println();
                    }
    
}
    public void markit() throws FileNotFoundException, IOException
    {
        BufferedReader in = null;
            in = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newstext.txt"));
                //StringBuilder sb = new StringBuilder();
                String ss = null;
              
              BufferedWriter writer = new BufferedWriter(new FileWriter("/home/cec/STTM/src/dataset/newsner.txt"));
                for (String doc; (doc = in.readLine()) != null;) 
                    { int i=0;
                        String[] tokenizedTerms = doc.trim().split("\\s+");
                        for (String term : tokenizedTerms) 
                            {
                            if(loclist.contains(term))
                                writer.write(term+"\tlocation\n");
                            
                            else if(namelist.contains(term))
                                writer.write(term+"\tperson\n");
                            else
                                writer.write(term+"\tother\n");
                            }
                     }
    }

    public static void main(String args[]) throws Exception
    { 
       
    
    myname mn= new myname();
    mn.preparelist();
    mn.markit();
    }
    
      
}
