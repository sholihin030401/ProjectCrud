package com.team.projectcrud;

import android.view.View;

public class CustomClick implements View.OnClickListener{

    private int position;
    private OnItemClickCallback onItemClickCallback;

    public CustomClick(int position, OnItemClickCallback onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public void onClick(View v) {
        onItemClickCallback.onItemClicked(v,position);
    }

    public interface OnItemClickCallback{
        void onItemClicked(View view, int position);
    }
}
