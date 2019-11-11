package com.example.paymeback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.GroupListViewHolder> {
    private ArrayList<GroupItem> mGroupList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;

    }

    public static class GroupListViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mGroupName;
        public TextView mGroupMembers;
        public ImageView mDeleteGroup;


        public GroupListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.trip_image);
            mGroupName = itemView.findViewById(R.id.trip_name);
            mGroupMembers = itemView.findViewById(R.id.trip_members);
            mDeleteGroup = itemView.findViewById(R.id.delete);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }

                }
            });

            mDeleteGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });


        }
    }

    public GroupListAdapter(ArrayList<GroupItem> groupItemList){
        mGroupList = groupItemList;
    }

    @NonNull
    @Override
    public GroupListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupitem, parent, false);
        GroupListViewHolder gvh = new GroupListViewHolder(v, mListener);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListViewHolder holder, int position) {
        GroupItem currentItem = mGroupList.get(position);

        holder.mImageView.setImageResource(currentItem.getmImageResource());
        holder.mGroupName.setText(currentItem.getmGroupName());
        holder.mGroupMembers.setText(currentItem.getmGroupMembers());
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }
}



