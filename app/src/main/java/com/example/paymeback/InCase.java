package com.example.paymeback;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InCase extends AppCompatActivity {

    public TextView textView;
    public TextView textView2;
    public TextView textView3;
    public EditText Email;
    public EditText Answer;
    public Button AddF;
    private AlertDialog.Builder builder;

    private Memory mem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_case);
        Intent intent = getIntent();
        mem = (Memory) intent.getSerializableExtra("mem");


        Answer = (EditText)findViewById(R.id.answer);
        AddF = (Button)findViewById(R.id.buttonAdd);
        textView = (TextView)findViewById(R.id.textView);
        textView3 = (TextView)findViewById(R.id.textView3);
        builder = new AlertDialog.Builder(this);

        AddF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Answer.getText().toString().equals(""))
                {
                    String popupmesssgae = "Sorry you need to fill in all of the boxes to create the account! " ;
                    builder.setMessage(popupmesssgae);

                    AlertDialog alert = builder.create();
                    alert.setTitle("Ups!");
                    alert.show();
                }
                mem.getUsers().get(mem.getUsers().size()-1).answer = Answer.getText().toString();
                Intent intent = new Intent(InCase.this, Main2Activity.class);
                intent.putExtra("mem",mem);
                String currentXML = MainActivity.writeXml(mem);
                MainActivity.SaveXML(currentXML);
                startActivity(intent);
            }
        });

    }
}