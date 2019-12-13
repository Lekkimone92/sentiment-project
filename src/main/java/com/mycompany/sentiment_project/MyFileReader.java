/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sentiment_project;

import au.com.bytecode.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.lucene.document.Document;

/**
 *
 * @author 21713885
 */
public class MyFileReader {
    
    String path;
    ArrayList<String> columns ;
    ArrayList<String[]> lines ;
    String base = "/home/etudiants/21713885/Documents/m1/patternAvance/sentiment_project/sample.txt";
    public MyFileReader(String path){
        this.path = path;
        this.columns = new ArrayList<>();
        this.lines = new ArrayList<>();
    }
    
    public void readAndCreateFile() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(this.path));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        for (CSVRecord csvRecord : csvParser) {
            // Accessing Values by Column Index
            String value = csvRecord.get(11);
            
            String titles = csvRecord.get(1);
            this.columns.add(value);
        }
        
//        Path file = Paths.get(this.base);
//        Files.write(file, this.columns, StandardCharsets.UTF_8);
    }
    
    public void oneByOne() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(this.path));
        CSVReader csvReader = new CSVReader(reader);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
           lines.add(line);
        }
        System.out.println(this.lines.get(0)); 
        reader.close();
        csvReader.close();
    }
    
    public void writeResToFile(String path, List<Document> res) throws IOException 
    {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(path));
        if(res.size() > 1){
            for(Document doc: res){
                fileWriter.write("titre: "+ doc.get("title"));
                fileWriter.write("\n");
                fileWriter.write("reponses: "+ doc.get("response"));
                fileWriter.write("\n\n");
            }
        }else{
            fileWriter.write("titre: "+ res.get(0).get("title"));
            fileWriter.write("reponses: "+ res.get(0).get("response"));
        }
        
        
        fileWriter.close();
    }
    
    public ArrayList<String[]> getLines(){
        return this.lines;
    }
}
