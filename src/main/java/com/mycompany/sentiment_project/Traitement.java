/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sentiment_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 *
 * @author 21713885
 */
public class Traitement {
    
    
    private SentenceIterator iter;
    private TokenizerFactory t;
    private Word2Vec vec;
    
    public Traitement(String filePath){
        iter = new LineSentenceIterator(new File(filePath));
        iter.setPreProcessor(new SentencePreProcessor() {
            @Override
            public String preProcess(String sentence) {
                return sentence.toLowerCase();
            }
        });
        
        // Split on white spaces in the line to get words
        t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());
    }
        
        
    public void buildModel(String storefilePath){
        System.err.println("Building model....");
        this.vec = new Word2Vec.Builder()
                .minWordFrequency(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        System.err.println("Fitting Word2Vec model....");
        vec.fit();
        
        try {
            // Write word vectors
            WordVectorSerializer.writeWordVectors(this.vec,storefilePath);
        } catch (IOException ex) {
            Logger.getLogger(DeepLearning.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Word2Vec getVec(){
        return this.vec;
    }
    
}
