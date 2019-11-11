package com.example.paymeback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 * A view for seeing the members of the selected groups
 *
 * This intent shows the users in the group, and each user has its own button.
 * When the mainuser clicks a button, the mainuser will be sent to PersonInGroup
 * intent. The user can also create a new expense in this intent by click the
 * "Add New Expense" button. When this button is clicked, AddtransactionActivity
 * intent is opened.
 *
 * @see PersonInGroup
 * @see AddtransactionActivity
 *
 * @author Berke
 */

public class GroupView extends AppCompatActivity{

    /**
     * The tripgroup object that is shown in this intent
     */
    public TripGroup group;

    /**
     * A list of user who are a member of (TripGroup) group
     */
    public ArrayList<User> users;

    /**
     * The name of of (TripGroup) group
     */
    public String tname;

    /**
     * The user who logged in to the app
     */
    private User mainuser;

    /**
     * A memory object which stores the critical data
     */
    private Memory mem;

    /**
     * To initialize the intent
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        //Getting the critical data from the previous intent
        Intent intent = getIntent();
        mem = (Memory) intent.getSerializableExtra("mem");
        mainuser = mem.getMainuser();
        int index = mem.getOnegroup();
        group = mem.getGroups().get(index);
        users = group.getMembers();
        tname = group.getName();

        TextView group_name = findViewById(R.id.GroupName);
        group_name.setText(tname);

        //To view the the members of the selected group
        LinearLayout mainLinear = (LinearLayout) findViewById(R.id.HolderLayout);
        for (int i = 0; i < users.size(); i++) {
            final User u = users.get(i);
            if (mainuser.equals(u)) {continue;}
            String name = u.getFirstName() + " " + u.getLastName();
            Button newbutton = new Button(GroupView.this);
            newbutton.setText(name);
            newbutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(GroupView.this, PersonInGroup.class);
                    intent.putExtra("user",u);
                    intent.putExtra("mem",mem);
                    String currentXML = MainActivity.writeXml(mem);
                    MainActivity.SaveXML(currentXML);
                    startActivity(intent);
                }
            });
            mainLinear.addView(newbutton);
        }

    }

    /**
     * onClick method for Add New Expense button
     *
     * It used to create a new transaction within the group. When this function is called,
     * an AddtransactionActivityintent is opened.
     *
     * @see AddtransactionActivity
     * @param view
     */
    public void addExpense(View view) {
        Intent intent = new Intent(this, AddtransactionActivity.class);
        intent.putExtra("mem",mem);
        String currentXML = MainActivity.writeXml(mem);
        MainActivity.SaveXML(currentXML);
        startActivity(intent);
    }

    /**
     * Sets the GoBack button
     *
     * When the user wants to go back to the previous page by pressing the button
     * on their mobile device, they will be sent to AllGroupList activity
     *
     * @see AllGroupList
     */
    public void onBackPressed() {
        Intent intent = new Intent (GroupView.this, AllGroupList.class);
        intent.putExtra("mem", mem);
        String currentXML = MainActivity.writeXml(mem);
        MainActivity.SaveXML(currentXML);
        startActivity(intent);
    }

}
