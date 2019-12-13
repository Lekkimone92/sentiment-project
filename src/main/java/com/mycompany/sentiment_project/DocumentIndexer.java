/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sentiment_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.bytedeco.javacpp.indexer.Indexer;

/**
 *
 * @author 21713885
 */
public class DocumentIndexer {
    private static final StandardAnalyzer analyzer = new StandardAnalyzer();
    private IndexWriter writer ;
    private Directory memoryIndex;
    private IndexWriterConfig indexWriterConfig;
    private List<String[]> docsArray;
  
  
    public DocumentIndexer(ArrayList<String[]> docs){
        this.docsArray = docs;
        this.memoryIndex = new RAMDirectory();
        this.indexWriterConfig = new IndexWriterConfig(this.analyzer);
        try {
            this.writer = new IndexWriter(this.memoryIndex,this.indexWriterConfig);
            this.writer.commit();
        } catch (IOException ex) {
            Logger.getLogger(DocumentIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void indexDocuments() throws IOException {
        HashMap<String, String> docHash = new HashMap<String, String>();
        System.err.println("taille : "+ this.docsArray.size());
    
        for (String[] doc: this.docsArray) {
           if(doc.length > 11){
            String ref = doc[0];
            String title = doc[1];
            String createdAt = doc[2];
            String response = doc[11];
            
            docHash.put("ref", ref);
            docHash.put("title", title);
            docHash.put("createdAt", createdAt);
            docHash.put("response", response);
            this.addDoc(docHash);
            //System.err.println("fine"); 
           }else{
               //System.err.println("not fine"); 
           }
            
        }
        this.writer.close();
        
        //this.writer.commit();
    }
    
    public List<Document> searchIndex(String inField, String queryString){
          List<Document> documents = new ArrayList<>();
          System.out.println("string query "+ queryString);
          try {
              Query q = new QueryParser(inField, this.analyzer).parse(queryString);
              try {
                  IndexReader indexR = DirectoryReader.open(this.memoryIndex);
                  IndexSearcher searcher = new IndexSearcher(indexR);
                  TopDocs topDocs = searcher.search(q, 50);
                  System.out.println("search result "+ topDocs.scoreDocs.length);
                  for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                      System.out.println("doc found " + scoreDoc.doc);
                      documents.add(searcher.doc(scoreDoc.doc));
                  }
              } catch (IOException ex) {
                  Logger.getLogger(DocumentIndexer.class.getName()).log(Level.SEVERE, null, ex);
              }
          } catch (ParseException ex) {
              Logger.getLogger(DocumentIndexer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return documents;
    }

    private void addDoc(HashMap<String, String> map) throws IOException{
        Document doc = new Document();
        for(String i : map.keySet()){
            doc.add(new TextField(i, map.get(i), Field.Store.YES));
        }

        writer.addDocument(doc);
    }
    
}
