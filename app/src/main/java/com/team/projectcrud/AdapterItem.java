package com.team.projectcrud;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemHolder> {

    LinkedList<Item> listItem;
    private Activity activity;

    public AdapterItem(Activity activity) {
        this.activity = activity;
    }

    public LinkedList<Item> getListItem() {
        return listItem;
    }

    public void setListItem(LinkedList<Item> listItem) {
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public AdapterItem.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main,parent,false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItem.ItemHolder holder, int position) {
        holder.txmail.setText(listItem.get(position).getEmail());
        holder.txname.setText(listItem.get(position).getName());
        holder.txphone.setText(listItem.get(position).getPhone());
        holder.txaddress.setText(listItem.get(position).getAddress());
        holder.cvUser.setOnClickListener(new CustomClick(position, (view, position1) -> {
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_POSITION,position1);
            intent.putExtra(DetailActivity.EXTRA_USER,getListItem().get(position1));
            activity.startActivityForResult(intent,DetailActivity.REQUEST_UPDATE);
        }));
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        TextView txmail, txname, txphone, txaddress;
        CardView cvUser;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            txmail = itemView.findViewById(R.id.txemail);
            txname = itemView.findViewById(R.id.txname);
            txphone = itemView.findViewById(R.id.txphone);
            txaddress = itemView.findViewById(R.id.txaddress);
            cvUser = itemView.findViewById(R.id.cvuser);
        }
    }
}
