/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfidf;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Mubin Shrestha
 */
public class TfIdfMain {
    
    /**
     * Main method
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void main(String args[]) throws FileNotFoundException, IOException
    {
        DocumentParser dp = new DocumentParser();
        dp.parseFiles("/home/cec/simi/");
        //dp.tfIdfCalculator(); //calculates tfidf
        //dp.getCosineSimilarity(); //calculated cosine similarity   
    }
}