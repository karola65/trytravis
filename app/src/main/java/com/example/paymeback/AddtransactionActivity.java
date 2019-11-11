package com.example.paymeback;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AddtransactionActivity extends AppCompatActivity {
    private Button addMember, addAllMember;
    private LinearLayout addMemberLayout;
    private Button addTransaction;
    private TextView groupName;
    private EditText eventName;
    private EditText amountText;
    private EditText addUserTransaction;
    private TextView payerName;
    private int clickAddMemberTimes;

    private Memory mem;

    public TripGroup group;
    public User admin;
    public ArrayList<User> ref_users;
    public String tname;
    private AlertDialog.Builder builder;
    public ArrayList<User> paybackers = new ArrayList<User>();
    private ArrayList <User> existingUser;
    private HashMap <String, User>  userNameToUserdictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtransaction);

        Intent intent = getIntent();
        mem = (Memory) intent.getSerializableExtra("mem");
        admin = mem.getMainuser();
        int index = mem.getOnegroup();
        group = mem.getGroups().get(index);
        ref_users = group.getMembers();
        tname = group.getName();
        clickAddMemberTimes = 0;

        addMember = findViewById(R.id.addmember2);
        addAllMember = findViewById(R.id.addAll);
        addMemberLayout= findViewById(R.id.addmemberview);
        addTransaction = findViewById(R.id.addtransaction);
        groupName = findViewById(R.id.nameofgroup);
        groupName.setText(tname);
        payerName = findViewById(R.id.nameofpayer);
        payerName.setText(admin.getUserName());
        eventName = findViewById(R.id.eventName);
        amountText = findViewById(R.id.amount);
        addUserTransaction = findViewById(R.id.addUserforTransaction);
        builder = new AlertDialog.Builder(this);
        userNameToUserdictionary = new HashMap<>();
        existingUser = new ArrayList<User>();

        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addMemberUserName = addUserTransaction.getText().toString();
                if (addMemberUserName.isEmpty()){
                    String popup = "Sorry, but there are no User names entered !";
                    builder.setMessage(popup);
                    AlertDialog alert = builder.create();
                    alert.setTitle("Add Member Field is Empty");
                    alert.show();
                }
                else {
                    int count = 0;
                    for (User u : group.getMembers()) {
                        if (addMemberUserName.equals(u.getUserName())) {
                            if (u.equals(admin)){
                                String popup = "Sorry, but you can't add yourself !";
                                builder.setMessage(popup);
                                AlertDialog alert = builder.create();
                                alert.setTitle("Can't add yourself");
                                alert.show();
                                count += 1;
                                break;
                            }
                            else if (existingUser.contains(u)) {
                                String popup = "Sorry, but " + addMemberUserName + " is already in the transaction!";
                                builder.setMessage(popup);
                                AlertDialog alert = builder.create();
                                alert.setTitle("Member already added into the transaction!");
                                alert.show();
                                count += 1;
                                break;
                            }
                            else {
                                clickAddMemberTimes += 1;
                                existingUser.add(u);
                                TextView nametitle = new TextView(AddtransactionActivity.this);
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
                                nametitle.setId(clickAddMemberTimes * 10000 - 1);
                                addMemberLayout.addView(nametitle);
                                count += 1;
                                break;
                            }
                        }
                    }
                    if (count == 0) {
                        String popup = "Sorry, but " +
                                addMemberUserName +
                                " is not in your group.";
                        builder.setMessage(popup);
                        AlertDialog alert = builder.create();
                        alert.setTitle("User not in this group");
                        alert.show();
                        addUserTransaction.setText(null);
                    }
                    else {
                        addUserTransaction.setText(null);
                    }
                }

            }
        });

        addAllMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (User u : group.getMembers()) {
                    if(admin.equals(u)){
                        continue;
                    }
                    clickAddMemberTimes += 1;
                    existingUser.add(u);
                    TextView nametitle = new TextView(AddtransactionActivity.this);
                    userNameToUserdictionary.put(u.getUserName(), u);
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
                    nametitle.setText(u.getUserName());
                    nametitle.setId(clickAddMemberTimes * 10000 - 1);
                    addMemberLayout.addView(nametitle);
                    addAllMember.setEnabled(false);
                }
            }
        });

        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transaction_name = eventName.getText().toString();
                String amount = amountText.getText().toString();
                boolean numeric = true;
                try {
                    double test_amount = Double.parseDouble(amount);
                } catch (NumberFormatException e) {
                    numeric = false;
                }
                // String currencyString = currencyText.getText().toString();
                if (numeric == true){
                    double amount_double = Double.valueOf(amount);
                    if (existingUser.isEmpty()){
                        String popup = "Please add a user into the transaction!";
                        builder.setMessage(popup);
                        AlertDialog alert = builder.create();
                        alert.setTitle("No users to add.");
                        alert.show();
                    }
                    else {
                        if (transaction_name.length() < 2){
                            String popup = "Please give a longer transaction name!";
                            builder.setMessage(popup);
                            AlertDialog alert = builder.create();
                            alert.setTitle("Transaction Name too short.");
                            alert.show();
                        }
                        else if (transaction_name.length() > 20){
                            String popup = "Please give a shorter transaction name, less than 20 characters!";
                            builder.setMessage(popup);
                            AlertDialog alert = builder.create();
                            alert.setTitle("Transaction Name too long.");
                            alert.show();
                        }
                        else {
                            for (int i = 1; i <= clickAddMemberTimes; i++){
                                TextView member_user_name = findViewById(i * 10000 - 1);
                                String user_name_of_member = member_user_name.getText().toString();
                                paybackers.add(userNameToUserdictionary.get(user_name_of_member));
                            }
                            Transaction transaction = new Transaction(transaction_name,admin, paybackers, amount_double);
                            Intent intent = new Intent (AddtransactionActivity.this, GroupView.class);
                            int index = mem.getOnegroup();
                            mem.getGroups().get(index).addTransaction(transaction);
                            intent.putExtra("mem", mem);
                            String currentXML = MainActivity.writeXml(mem);
                            MainActivity.SaveXML(currentXML);
                            startActivity(intent);
                        }
                    }
                }
                else {
                    String popup = "Entered transaction amount is not in the correct format!";
                    builder.setMessage(popup);
                    AlertDialog alert = builder.create();
                    alert.setTitle("Wrong format for Transaction.");
                    alert.show();
                }
            }
        });
    }
    protected void onStop() {
        super.onStop();
    }
    protected void onDestroy(){
        super.onDestroy();
    }
}