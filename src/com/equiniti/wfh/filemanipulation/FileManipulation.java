package com.equiniti.wfh.filemanipulation;

import com.equiniti.wfh.TimeTrackerDAO;
import com.equiniti.wfh.dbconnectivity.PostgreSQLJDBC;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

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

    private final String filePath = "C:\\";
   // private TimeTrackerDAO timeTrackerDAO;
    private SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd");

    public FileManipulation() {
      //  timeTrackerDAO = TimeTrackerDAO.getInstance();
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
        bw.write(idType + "," + timestamp + "," + null);
        bw.newLine();
        bw.flush();
        bw.close();

    }
}
