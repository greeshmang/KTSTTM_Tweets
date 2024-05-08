/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to read documents
 *
 * @author Mubin Shrestha
 */
public class DocumentParser {

    //This variable will hold all terms of each document in an array.
    private List<String[]> termsDocsArray = new ArrayList<String[]>();
    public List<String> allTerms = new ArrayList<String>(); //to hold all terms
    public List<String> clTerms = new ArrayList<String>(); 
     public List<String> cNN = new ArrayList<String>(); 
    private List<double[]> tfidfDocsVector = new ArrayList<double[]>();
    int termmatrix[][];
    int terms[];

    /**
     * Method to read files and store in array.
     * @param filePath : source file path
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void cNN() throws FileNotFoundException, IOException {
        //File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
         in = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/nounlist.txt"));
             for (String doc; (doc = in.readLine()) != null;) {
               String[] tokenizedTerms = doc.trim().split("\\s+");
                //String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                for (String term : tokenizedTerms) {
                    if (!cNN.contains(term)) {  //avoid duplicate entry
                        cNN.add(term);
                        //System.out.print(term);
                    }
                }
                //writer.newLine();
                //System.out.println();
                
               } //termsDocsArray.add(tokenizedTerms);
            }
    public void parseFiles(String filePath) throws FileNotFoundException, IOException {
        //File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
        System.out.println("reading"+filePath);
       //for (File f : allfiles) {
          //  if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newscorpusNN.txt"));
                
                int i=0;
               
               for (String doc; (doc = in.readLine()) != null;) {
                   
               
                String[] tokenizedTerms = doc.trim().split("\\s+");
                //String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
               // int flag=0;
                for (String term : tokenizedTerms) {
                    
                    if (!allTerms.contains(term)) 
                    {
                        allTerms.add(term);
                    
                    }
                
                }
                //writer.newLine();
               // System.out.println();
                
               } //termsDocsArray.add(tokenizedTerms);
            }
    public void createanno() throws FileNotFoundException, IOException {
        //File[] allfiles = new File(filePath).listFiles();
        BufferedReader in = null;
        //System.out.println("reading"+filePath);
       //for (File f : allfiles) {
          //  if (f.getName().endsWith(".txt")) {
                in = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/ner_dataset.csv"));
                BufferedWriter writers = new BufferedWriter(new FileWriter("/home/cec/STTM/src/dataset/mydataset.csv"));
                
                int i=0;
               
               for (String doc; (doc = in.readLine()) != null;) {
                   
               i++;
               if(i==50000)
                   break;
               // String[] tokenizedTerms = doc.trim().split("\\s+");
                //String[] tokenizedTerms = sb.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
               // int flag=0;
               String[] tokenizedTerms= doc.split(",", -2);
                    
                    
                   // if (tokenizedTerms[0]=="Sentence:") 
                    //{
                       // writers.write(tokenizedTerms[1]);
                        writers.write(tokenizedTerms[1]+"\t"+tokenizedTerms[3]+"\n");
                        //System.out.print(tokenizedTerms[1]);
                        //System.out.println("\t"+tokenizedTerms[3]);
                    
                   // }
                
                
                //writer.newLine();
               // System.out.println();
                
               } //termsDocsArray.add(tokenizedTerms);
            }
        //}
public void countterms(String filePath) throws FileNotFoundException, IOException 
{
    terms = new int[allTerms.size()]; 
    for(int k=0;k<allTerms.size();k++)
        terms[k]=0;
        BufferedReader in = null;
        System.out.println("reading"+filePath);
                in = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newscorpusNN.txt"));
                int i=0;
               for (String doc; (doc = in.readLine()) != null;) 
               {
                String[] tokenizedTerms = doc.trim().split("\\s+");
                int flag=0;
                for (String term : tokenizedTerms) 
                    {
                    i=allTerms.indexOf(term);
                    terms[i]++;
                    }
                }
    
}
    /**
     * Method to create termVector according to its tfidf score.
     */
    /*public void tfIdfCalculator() {
        double tf; //term frequency
        double idf; //inverse document frequency
        double tfidf; //term requency inverse document frequency        
        for (String[] docTermsArray : termsDocsArray) {
            double[] tfidfvectors = new double[allTerms.size()];
            int count = 0;
            for (String terms : allTerms) {
                tf = new TfIdf().tfCalculator(docTermsArray, terms);
                idf = new TfIdf().idfCalculator(termsDocsArray, terms);
                tfidf = tf * idf;
                tfidfvectors[count] = tfidf;
                count++;
            }
            tfidfDocsVector.add(tfidfvectors);  //storing document vectors;            
        }
    }*/
    public void terms() throws FileNotFoundException, IOException {
        System.out.println("allTerms.size()="+allTerms.size());
        
         termmatrix = new int[allTerms.size()][allTerms.size()]; 
         /*
         for(int p=0;p<allTerms.size();p++)
        {
            for(int pk=0;pk<allTerms.size();pk++)
                System.out.print(termmatrix[p][pk]);
           // System.out.println();
        }*/
          
      
            
            int count = 0;
            BufferedReader inn = null;
            inn = new BufferedReader(new FileReader("/home/cec/STTM/src/dataset/newscorpusNN.txt"));
                //StringBuilder sb = new StringBuilder();
                String s = null;
                while ((s = inn.readLine()) != null) {
                    //sb.append(s);
                   // System.out.println(s);
                    String[] tokenizedTerms = s.trim().split("\\s+");
                
                //String[] tokenizedTerms = s.toString().replaceAll("[\\W&&[^\\s]]", "").split("\\W+");   //to get individual terms
                //System.out.println( tokenizedTerms);
              for(String term1:tokenizedTerms)
                    for(String term2:tokenizedTerms){
                    if (term1!=term2) { 
                        if(!allTerms.contains(term1))
                                System.out.println(term1 +"not found");
                        else if(!allTerms.contains(term2))
                            System.out.println(term2 +"not found");
                        else{
                        int index1=allTerms.indexOf(term1);
                        int index2=allTerms.indexOf(term2);
                      //  System.out.println("index1 "+term1+" "+index1);
                      //   System.out.println("index2 "+term2+" "+index2);
                      //  System.out.println("termmatrix[index1][index2]"+termmatrix[index1][index2]);
                     //  System.out.println("termmatrix[index2][index1]"+termmatrix[index2][index1]);
                       termmatrix[index1][index2]++;
                        termmatrix[index2][index1]++;
                       // System.out.println("termmatrix[index1][index2]"+termmatrix[index1][index2]);
                      //  System.out.println("termmatrix[index2][index1]"+termmatrix[index2][index1]);
                        }
                        
                         //System.out.print(tfidfvectors[index1][index2]);
                    
                    }
                }}
        
       /* for(int p=0;p<allTerms.size();p++)
        {
            for(int pk=0;pk<allTerms.size();pk++)
                System.out.print(" "+termmatrix[p][pk]);
            System.out.println();
        }
          */
        
    }
    public void clusterterms()
    {
        String term="cricket";
       int index=allTerms.indexOf(term);
       //addterms(index);
       addtermsjacard(index);
       if(clTerms.size()==0)
           System.out.println("no elements in cluster");
       for (int i = 0; i < clTerms.size(); i++) {
           System.out.println(clTerms.get(i));
                
           }
    
    //}
    }
    public void addterms(int t )
    { String isterm=allTerms.get(t);
        //System.out.println(" finding related words of "+isterm);
        for (int i = 0; i < allTerms.size(); i++) 
        {
        String newterm=allTerms.get(i);
        //System.out.println("finding relation of --"+newterm);
               if(termmatrix[t][i]>15)
               { 
                   if(!clTerms.contains(newterm))
                    {
                      //  System.out.println("new term is not in the cluster");
                        clTerms.add(newterm);
                       //System.out.println("adding term"+newterm);
                        addterms(i); 
               }
              
        }
        }
    }
         public void addtermsjacard(int t )
    { String isterm=allTerms.get(t);
        //System.out.println(" finding related words of "+isterm);
        for (int i = 0; i < allTerms.size(); i++) 
        {
        String newterm=allTerms.get(i);
        //System.out.println("finding relation of --"+newterm);
        int ab=termmatrix[t][i];
        int a=terms[i];
        int b=terms[t];
        double j=ab/(a+b-ab);
               if(j>0.2)
               { 
                   if(!clTerms.contains(newterm))
                    {
                      //  System.out.println("new term is not in the cluster");
                        clTerms.add(newterm);
                       //System.out.println("adding term"+newterm);
                        addterms(i); 
               }
              
        }
        }
    
    }
    

    /**
     * Method to calculate cosine similarity between all the documents.
     */
   /* public void getCosineSimilarity() {
        for (int i = 0; i < tfidfDocsVector.size(); i++) {
            for (int j = 0; j < tfidfDocsVector.size(); j++) {
                System.out.println("between " + i + " and " + j + "  =  "
                                   + new CosineSimilarity().cosineSimilarity
                                       (
                                         tfidfDocsVector.get(i), 
                                         tfidfDocsVector.get(j)
                                       )
                                  );
            }
        }
    }*/
}
