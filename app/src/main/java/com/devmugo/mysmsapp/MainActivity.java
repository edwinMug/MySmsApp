package com.devmugo.mysmsapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MainActivity extends AppCompatActivity {

    private TextView myTextView, myTextView2;
    String date;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTextView = findViewById(R.id.textView);
        myTextView2 = findViewById(R.id.textView2);
        progressDialog = new ProgressDialog(this);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
    }

    public void Read_SMS(View view) {

        /*Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null,null,null);
        cursor.moveToFirst();

        myTextView.setText(cursor.getString(12));

        cursor.moveToNext();
        myTextView2.setText(cursor.getString(12));*/


        //how to retrieve various aspects of message
        //int date = cursor.getColumnIndex("address");
        //myTextView.setText(cursor.getString(date));

        //smses = getSMS();
        //System.out.println(smses);
        //System.out.println(smses.size());

        //HashMap smes = new HashMap();
        List<Map> smses;
        smses = getSMS();
        System.out.println(smses);

        addToExcelSheet(smses);


    }

    public List<Map> getSMS() {

        List<Map> sms = new ArrayList<Map>();
        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

        int address = cur.getColumnIndex("address");
        while (cur != null && cur.moveToNext()) {
            String phone = cur.getString(address);
            String date = cur.getString(cur.getColumnIndexOrThrow("date"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            HashMap smsMap = new HashMap();
            smsMap.put("Number", phone);
            smsMap.put("date",date);
            smsMap.put("Message", body);
            sms.add(smsMap);

        }

        if (cur != null) {
            cur.close();
        }
        return sms;
    }

    public void addToExcelSheet(List<Map> texts) {
        //Create workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //create Sheet
        XSSFSheet xssfSheet = workbook.createSheet("texts");


        for(int i=0;i<texts.size();i++) {
            //creating a new row
            Row row = xssfSheet.createRow(i);
            Cell cell1 = row.createCell(1);
            cell1.setCellValue((String) texts.get(i).get("Number"));
            Cell cell2 = row.createCell(2);
            cell2.setCellValue((String) texts.get(i).get("date"));
            Cell cell3 = row.createCell(3);
            cell3.setCellValue((String) texts.get(i).get("Message"));
            }

        try {

            // Writing the workbook
            FileOutputStream out = new FileOutputStream(
                    new File(getExternalFilesDir(null),"Texts.xlsx"));
            workbook.write(out);

            // Closing file output connections
            out.close();

            // Console message for successful execution of
            // program
            System.out.println(
                    "Texts.xlsx written successfully on disk.");
        }

        // Catch block to handle exceptions
        catch (Exception e) {

            // Display exceptions along with line number
            // using printStackTrace() method
            e.printStackTrace();
        }
        }


    }



