package com.example.paymeback;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * A view for seeing the common transactions with another member in the group
 *
 * This class sets the graphical interface of showing the transactions between two users.
 * The first user is the mainuser who logged in, and the second user is the user that is clicked
 * in GroupView. The main user can see all the transactions and the amounts associated to them.
 * The main user can also see the total balance, and if the other user owes some money to the
 * main user (in other words, when the balance is positive), the main user can send a notification
 * to the other user or let the server know that the user has paid its debts.
 *
 * @see TripGroup
 * @see Transaction
 * @see GroupView
 *
 * @author Berke
 */
public class PersonInGroup extends AppCompatActivity {

    /**
     * The user who logged in to the app
     */
    private User mainuser;

    /**
     * A memory object which stores the critical data
     */
    private Memory mem;

    /**
     * The tripgroup object that is shown in this intent
     */
    private TripGroup tripGroup;

    /**
     * The user who is clicked in GroupView intent
     *
     * @see GroupView
     */
    private User user;

    /**
     * The index value of the selected TripGroup object
     */
    private int index;

    /**
     * AlertDialog that is used in this intent
     */
    private AlertDialog.Builder builder;

    /**
     * Buttons that are used in this intent
     */
    private Button not, pad;

    /**
     * To initialize the intent
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_in_group);

        //Getting and setting the critical data from the previous intent
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        mem = (Memory) intent.getSerializableExtra("mem");
        mainuser = mem.getMainuser();
        index = mem.getOnegroup();
        tripGroup = mem.getGroups().get(index);
        String tname = tripGroup.getName();
        String currency = tripGroup.getStringCurrency();
        ArrayList<Transaction> lst = tripGroup.getTransactionList(mainuser,user);
        Balance b = new Balance(mainuser,user,lst,tripGroup.getCurrency());
        double balance = b.displayBalance();

        builder = new AlertDialog.Builder(this);
        not = (Button)findViewById(R.id.notify);
        pad = (Button)findViewById(R.id.paid);

        if(balance <= 0) {
            not.setEnabled(false);
            pad.setEnabled(false);
        }

        String bal = String.format("%.2f",balance);
        String mname = user.getFirstName() + " " + user.getLastName();

        TextView group_name = findViewById(R.id.GroupName);
        group_name.setText(tname);

        TextView member_name = findViewById(R.id.name_payee);
        member_name.setText(mname);

        TextView cur_name = findViewById(R.id.cur);
        cur_name.setText("  "+currency+"  ");

        TextView tbalance = findViewById(R.id.balance);
        tbalance.setText(bal);

        //To view the transactions between mainuser and user
        LinearLayout mainLinear = (LinearLayout) findViewById(R.id.HolderLayout);
        for (int i = 0; i < lst.size(); i++) {
            Transaction t = lst.get(i);
            String name = t.getName();
            double a = t.getAmountPerson();
            String output;
            if(t.isPayer(mainuser)) {
                output= String.format(" %s\n%.2f ",name,a);
            }
            else {
                output= String.format(" %s\n-%.2f ",name,a);
            }
            TextView newText = new TextView(PersonInGroup.this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 10, 5, 10);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            newText.setLayoutParams(params);
            newText.setBackgroundColor(Color.parseColor("#ffffff"));
            newText.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
            newText.setTextColor(Color.parseColor("#000000"));
            newText.setTypeface(null, Typeface.BOLD);
            newText.setTextSize(18);
            newText.setText(output);
            mainLinear.addView(newText);
        }
    }

    /**
     * onClick method for Paid button
     *
     * It used to indicate that the user has paid all his/her debts to the mainuser.
     * The function updates this information
     *
     * @param view
     */
    public void updatePaid(View view) {
        int ngroups = tripGroup.getTransaction().size();
        for(int i = 0; i < ngroups; i++) {
            Transaction t = tripGroup.getTransaction().get(i);
            if(t.isInTransaction(mainuser) && t.isInTransaction(user)){
                if(t.isPayer(mainuser)) {
                    tripGroup.editTransaction(i,user);
                }
                else{
                    tripGroup.editTransaction(i,mainuser);
                }
            }

        }
        mem.setGroup(index,tripGroup);
        Intent intent = new Intent(PersonInGroup.this, GroupView.class);
        intent.putExtra("mem",mem);
        String currentXML = MainActivity.writeXml(mem);
        MainActivity.SaveXML(currentXML);
        startActivity(intent);
    }

    /**
     * onClick method for Notify button
     *
     * IN-PROGRESS: Pop-ups a message about the notification being sent to
     * the user who owes money. Currently, the notification is not sent to
     * the user. This should be updated in the future release
     *
     * @param view
     */
    public void sendNotification(View view) {
        String popup = "A notification has been sent to " + user.getUserName() + "!";
        builder.setMessage(popup);
        AlertDialog alert = builder.create();
        alert.setTitle("Notification Sent");
        alert.show();
        not.setEnabled(false);
    }
}
