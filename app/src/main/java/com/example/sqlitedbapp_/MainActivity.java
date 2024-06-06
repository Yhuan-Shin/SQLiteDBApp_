package com.example.sqlitedbapp_;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    EditText etStudentID, etStudentName, etStudentProg;
    Button btAdd, btDelete, btSearch, btView;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etStudentID = findViewById(R.id.etID);
        etStudentName = findViewById(R.id.etName);
        etStudentProg = findViewById(R.id.etProgram);

        btAdd = findViewById(R.id.btnAdd);
        btDelete = findViewById(R.id.btnDelete);
        btSearch = findViewById(R.id.btnSearch);
        btView = findViewById(R.id.btnViewAll);


        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(stdnt_id VARCHAR, stdnt_name VARCHAR, stdnt_prog VARCHAR);");

    }
    public void clearText(){
        etStudentID.setText("");
        etStudentName.setText("");
        etStudentProg.setText("");
        etStudentID.requestFocus();
    }
    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    @Override
    public void onClick(View view) {
        if(view == btAdd){
            db.execSQL("INSERT INTO student VALUES('" +etStudentID.getText() +"','" +etStudentName.getText() +
                    "','"+etStudentProg.getText()+"');");
            showMessage("Success", "Record added.");
            clearText();
        } else if(view == btDelete){
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdnt_id='" + etStudentID.getText()+"'",null);
            if(c.moveToFirst()){
                db.execSQL("DELETE FROM student WHERE stdnt_id='"+etStudentID.getText()+"'");
                showMessage("Success", "Record deleted");
            }
            clearText();
        } else if (view == btSearch) {
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdnt_id='" + etStudentID.getText()+"'",null);
            StringBuffer buffer = new StringBuffer();

            if(c.moveToFirst()){
                buffer.append("Name: ").append(c.getString(1)).append("\n");
                buffer.append("Program: ").append(c.getString(2)).append("\n\n");

            }
            showMessage("Student Details: ",buffer.toString());
        } else if (view == btView) {
            Cursor c = db.rawQuery("SELECT * FROM student", null);
            if(c.getCount()==0) {
                showMessage("Error", "No records found.");
                return;
            }
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()){
                buffer.append("ID: ").append(c.getString(0)).append("\n");
                buffer.append("Name: ").append(c.getString(1)).append("\n");
                buffer.append("Program: ").append(c.getString(2)).append("\n\n");
            }
            showMessage("Student Details", buffer.toString());

        }

    }
}