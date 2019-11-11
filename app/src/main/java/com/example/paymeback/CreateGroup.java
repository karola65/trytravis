package com.example.paymeback;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class CreateGroup extends AppCompatActivity  {
    private Button addMember;
    private EditText groupName;
    private LinearLayout main;
    private Button confirmGroup;
    private User mainuser;
    private TextView groupCreator;
    private EditText currency;
    private EditText memberUserName;
    private int clickAddMemberTimes;
    private Memory mem;
    private AlertDialog.Builder builder;
    private HashMap <String, User>  userNameToUserdictionary;
    private ArrayList <User> existingUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);

        Intent intent = getIntent();
        mem = (Memory) intent.getSerializableExtra("mem");
        mainuser = mem.getMainuser();

        addMember = findViewById(R.id.button2);
        main = findViewById(R.id.buttonlayout);
        confirmGroup = findViewById(R.id.confirmgroup);
        groupName = findViewById(R.id.GroupName);
        memberUserName = findViewById(R.id.addMemberUserName);
        groupCreator = findViewById(R.id.groupCreator);
        groupCreator.setText(mainuser.userName);
        currency = findViewById(R.id.currencyValue);
        clickAddMemberTimes = 0;
        builder = new AlertDialog.Builder(this);
        existingUser = new ArrayList<User>();
        userNameToUserdictionary = new HashMap<>();


        addMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String addMemberUserName = memberUserName.getText().toString();
                if (addMemberUserName.isEmpty()){
                    String popup = "Sorry, but there are no User names entered !";
                    builder.setMessage(popup);
                    AlertDialog alert = builder.create();
                    alert.setTitle("Add Member Field is Empty");
                    alert.show();
                }
                else {
                    int count = 0;
                    for (User u : mem.getUsers()) {
                        if (addMemberUserName.equals(u.getUserName())) {
                            if (u.equals(mainuser)){
                                String popup = "Sorry, but you can't add yourself !";
                                builder.setMessage(popup);
                                AlertDialog alert = builder.create();
                                alert.setTitle("Can't add yourself");
                                alert.show();
                                count += 1;
                                break;
                            }
                            else if (existingUser.contains(u)) {
                                String popup = "Sorry, but " + addMemberUserName + " is already in the group!";
                                builder.setMessage(popup);
                                AlertDialog alert = builder.create();
                                alert.setTitle("Member already added!");
                                alert.show();
                                count += 1;
                                break;
                            }
                            else {
                                clickAddMemberTimes += 1;
                                existingUser.add(u);
                                TextView nametitle = new TextView(CreateGroup.this);
                                userNameToUserdictionary.put(addMemberUserName, u);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(20, 10, 20, 10);
                                //  params.weight = 1.0f;
                                params.gravity = Gravity.CENTER_HORIZONTAL;  /// this is layout gravity of textview
                                nametitle.setLayoutParams(params);
                                nametitle.setBackgroundColor(Color.parseColor("#3F51B5"));
                                nametitle.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                                nametitle.setTextColor(Color.parseColor("#ffffff"));
                                nametitle.setTypeface(null, Typeface.BOLD);
                                nametitle.setTextSize(18);
                                nametitle.setMinimumWidth(140);
                                nametitle.setText(addMemberUserName);
                                nametitle.setId(clickAddMemberTimes);
                                main.addView(nametitle);
                                count += 1;
                                break;
                            }
                        }
                    }
                    if (count == 0) {
                        String popup = "Sorry, but " +
                                addMemberUserName +
                                " is not a registered user in the system.";
                        builder.setMessage(popup);
                        AlertDialog alert = builder.create();
                        alert.setTitle("User does not exist");
                        alert.show();
                        memberUserName.setText(null);
                    }
                    else {
                        memberUserName.setText(null);
                    }
                }

            }
        });
        confirmGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (existingUser.isEmpty()){
                    String popup = "Please add a user into the transaction!";
                    builder.setMessage(popup);
                    AlertDialog alert = builder.create();
                    alert.setTitle("No users to add.");
                    alert.show();
                }
                else {
                    String group_name = groupName.getText().toString();
                    if (group_name.length() < 2){
                        String popup = "Please give a longer group name!";
                        builder.setMessage(popup);
                        AlertDialog alert = builder.create();
                        alert.setTitle("Group Name too short.");
                        alert.show();
                    }
                    else if (group_name.length() > 20){
                        String popup = "Please give a shorter group name, less than 20 characters!";
                        builder.setMessage(popup);
                        AlertDialog alert = builder.create();
                        alert.setTitle("Group Name too long.");
                        alert.show();
                    }
                    else {
                        String currency_string = currency.getText().toString();
                        Currency currency_new = Currency.contains(currency_string);
                        TripGroup newGroup = new TripGroup(group_name);
                        newGroup.addMember(mainuser);
                        for (int i = 1; i <= clickAddMemberTimes; i++){
                            TextView member_user_name = findViewById(i);
                            String user_name_of_member = member_user_name.getText().toString();
                            newGroup.addMember(userNameToUserdictionary.get(user_name_of_member));
                        }
                        if (currency_new == null){
                            newGroup.setCurrency(Currency.SGD);
                        }
                        else {
                            newGroup.setCurrency(currency_new);
                        }
                        Intent intent = new Intent (CreateGroup.this, AllGroupList.class);
                        mem.addGroup(newGroup);
                        intent.putExtra("mem", mem);
                        String currentXML = MainActivity.writeXml(mem);
                        MainActivity.SaveXML(currentXML);
                        startActivity(intent);
                    }
                }

            }
        });
    }
}

