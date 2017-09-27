package com.equiniti.wfh.filemanipulation;

import com.equiniti.wfh.dbconnectivity.PostgreSQLJDBC;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rajaser
 */
public class FileManipulation {

    private final String filePath = "C:\\wfh_db.txt";
    private PostgreSQLJDBC postgreSQLJDBC;
    public FileManipulation(){
        postgreSQLJDBC = PostgreSQLJDBC.getInstance();
    }
    public void insertBreak(int timeTrackerId, Date startDate) throws IOException {
        insert(timeTrackerId, startDate, "breakId");
    }

    public void updateBreak(int timeTrackerId, Date stopDate) throws IOException {
        update(timeTrackerId, stopDate, "breakId");
    }

    public void insertEffective(int timeTrackerId, Date startDate) throws IOException {
        insert(timeTrackerId, startDate, "effectiveId");
    }

    public void updateEffective(int timeTrackerId, Date stopDate) throws IOException {
        update(timeTrackerId, stopDate, "effectiveId");
    }

    public void insertIdle(int timeTrackerId, Date startDate) throws IOException {
        insert(timeTrackerId, startDate, "idleId");
    }

    public void updateIdle(int timeTrackerId, Date stopDate) throws IOException {
        update(timeTrackerId, stopDate, "idleId");
    }

    private void update(int timeTrackerId, Date stopDate, String idType) throws IOException {
        System.out.println("update " + idType);
        Timestamp timestamp = new Timestamp(stopDate.getTime());
        File file = new File(filePath);
        file.createNewFile();
        File tempDB = new File("C:\\wfh_db_temp.txt");
        file.createNewFile();
        String record;
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempDB, true));
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((record = br.readLine()) != null) {
            System.out.println("record " + record + " null chk " + record.contains("null"));
            if (record.contains("null") && record.contains(idType) && record.contains("" + timeTrackerId)) {
                StringTokenizer st = new StringTokenizer(record, ",");
                String newstr = st.nextToken() + "," + st.nextToken() + "," + st.nextToken() + "," + timestamp;
                bw.write(newstr);
            } else {
                bw.write(record);
            }
            bw.newLine();
            bw.flush();
        }
        bw.close();
        br.close();
        boolean bool = file.delete();
        System.out.println("bool " + bool);
        boolean success = tempDB.renameTo(file);
        System.out.println(success);
    }

    void insert(int timeTrackerId, Date date, String idType) throws IOException {
        System.out.println("insert " + idType);
        Timestamp timestamp = new Timestamp(date.getTime());
        File file = new File(filePath);
        file.createNewFile();
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        bw.write(idType + "," + timeTrackerId + "," + timestamp + "," + null);
        bw.newLine();
        bw.flush();
        bw.close();

    }

    void updateFileContentsToDataBase() throws IOException, SQLException {
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String record;
            StringTokenizer stringTokenizer;
            while ((record = br.readLine()) != null) {
                System.out.println("record " + record + " null chk " + record.contains("null"));
                stringTokenizer = new StringTokenizer(record, ",");
                //callDatabase(stringTokenizer.nextToken(), stringTokenizer.nextToken(), stringTokenizer.nextToken(), stringTokenizer.nextToken());
            }
            //need TO DO read line by line update it to DB based on the ID type in file e.g breakId 
            //

    }

    private void callDatabase(String idType, int timeTrackerId, Date startTime, String stopTime) throws SQLException {
        switch(idType){
            case "effectiveId":
                postgreSQLJDBC.insert(timeTrackerId, startTime, "");
                break;
            case "breakId":
                postgreSQLJDBC.insert(timeTrackerId, startTime, "");
                break;
            case "idleId":
                postgreSQLJDBC.insert(timeTrackerId, startTime, "");
                break;
        }
    }
}
