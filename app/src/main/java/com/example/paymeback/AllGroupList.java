package com.example.paymeback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import java.util.ArrayList;

public class AllGroupList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private GroupListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button buttonOK;
    private ImageView Logout;

    private Memory mem;
    private ArrayList<Integer> positions = new ArrayList<>();

    private ArrayList<GroupItem> mGroupList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anna_groups);

        Intent intent = getIntent();
        mem = (Memory) intent.getSerializableExtra("mem");
        Logout = (ImageView) findViewById(R.id.avatar);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AllGroupList.this, Main2Activity.class);
                intent.putExtra("mem",mem);
                String currentXML = MainActivity.writeXml(mem);
                MainActivity.SaveXML(currentXML);
                startActivity(intent);

            }
        });

        createGroupList();
        buildRecyclerView();


    }
    public GroupItem tripGrouptoGroupItem(TripGroup g) {
        String groupName = g.getName();
        ArrayList<User> user_list = g.getMembers();
        String groupItem_users = "Members: ";
        for (User u : user_list) {
            String user_firstname = u.userName;
            if (user_list.lastIndexOf(u) != user_list.size() - 1) {
                groupItem_users = groupItem_users + user_firstname + ", ";
            } else {
                groupItem_users = groupItem_users + user_firstname;
            }
        }
        GroupItem groupItem = new GroupItem(R.drawable.trip_image_resized, groupName, groupItem_users);
        return groupItem;
    }

    public void insertItem(int position){
        mGroupList.add(position, new GroupItem(R.drawable.trip_image_resized, "KL", "Berke, Wayne"));
        mAdapter.notifyItemInserted(position);
    }

    public void removeItem(int position){
        mGroupList.remove(position);
        Intent intent = new Intent(AllGroupList.this, AllGroupList.class);
        intent.putExtra("mem",mem);
        String currentXML = MainActivity.writeXml(mem);
        MainActivity.SaveXML(currentXML);
        startActivity(intent);
        mAdapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, String name){
        mGroupList.get(position).changeGroupName(name);
        mAdapter.notifyItemChanged(position);
    }

    public void createGroupList(){
        mGroupList = new ArrayList<>();
        int i = 0;
        for (TripGroup g : mem.getGroups()){
            if(g.isUserInGroup(mem.getMainuser())){
                mGroupList.add(tripGrouptoGroupItem(g));
                positions.add(i);
            }
            i++;
        }
        //mGroupList.add(new GroupItem(R.drawable.trip_image_resized, "Route66", "Berke, Wayne, Karolina, Anna"));
    }

    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new GroupListAdapter(mGroupList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new GroupListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //go to the group's page
                Intent intent = new Intent(AllGroupList.this, GroupView.class);
                mem.setOnegroup(positions.get(position));
                intent.putExtra("mem",mem);
                String currentXML = MainActivity.writeXml(mem);
                MainActivity.SaveXML(currentXML);
                startActivity(intent);

            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
                mem.deleteGroup(positions.get(position));
                Intent intent = new Intent(AllGroupList.this, AllGroupList.class);
                intent.putExtra("mem",mem);
                String currentXML = MainActivity.writeXml(mem);
                MainActivity.SaveXML(currentXML);
                startActivity(intent);

            }

        });

    }

    public void dothis(View view){

        Intent intent = new Intent(AllGroupList.this, CreateGroup.class);
        intent.putExtra("mem",mem);
        String currentXML = MainActivity.writeXml(mem);
        MainActivity.SaveXML(currentXML);
        startActivity(intent);
        //insertItem(mAdapter.getItemCount());

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (AllGroupList.this, AllGroupList.class);
        intent.putExtra("mem", mem);
        String currentXML = MainActivity.writeXml(mem);
        MainActivity.SaveXML(currentXML);
        startActivity(intent);
    }
}