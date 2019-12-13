/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sentiment_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;

/**
 *
 * @author 21713885
 */
public class DeepLearning {
    
    
    public static void main(String[] args) throws IOException{
        
        String base = "/home/etudiants/21713885/Documents/m1/patternAvance/sentiment_project/";
        String query = "produits";
        
        MyFileReader fr = new MyFileReader(base+"LA_TRANSITION_ECOLOGIQUE.csv");
        
        //Classe qui utilise Word2Vec, créé les token et les vecteurs
        Traitement t = new Traitement(base + "sample.txt");
        
        System.err.println("Load data....");
        
        // La méthode qui créé le fichier sur lequel Word2Vec fait son apprentissage
        //fr.readAndCreateFile();
        
        //construire le model et stoquer les vecteurs dans le 'vectorRes.txt'
        t.buildModel(base+"vectorRes.txt");
        System.err.println("Closest Words:");
        Collection<String> lst = t.getVec().wordsNearest(query, 3);
        System.out.println(lst);

        fr.oneByOne();
        ArrayList<String[]> docs = fr.getLines();
        DocumentIndexer dIndexer = new DocumentIndexer(docs);
        try {
            dIndexer.indexDocuments();
            String finalQuery = String.join(" ", lst) + " " + query;
            System.out.println(finalQuery);
            List<Document> documents = dIndexer.searchIndex("response", finalQuery);
        
            if (documents.isEmpty()) {

                System.out.println("document is empty");
            }else{
                fr.writeResToFile(base + "/resultats_recherche.txt", documents);
            }
        } catch (IOException ex) {
            Logger.getLogger(DeepLearning.class.getName()).log(Level.SEVERE, null, ex);
        }

	
    }
    
}
