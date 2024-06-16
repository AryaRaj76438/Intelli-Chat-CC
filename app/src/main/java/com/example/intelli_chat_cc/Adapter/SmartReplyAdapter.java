// SmartReplyAdapter.java
package com.example.intelli_chat_cc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intelli_chat_cc.R;

import java.util.List;

public class SmartReplyAdapter extends RecyclerView.Adapter<SmartReplyAdapter.SmartReplyViewHolder> {
    private final List<String> smartReplies;
    private final OnReplyClickListener onReplyClickListener;

    public SmartReplyAdapter(List<String> smartReplies, OnReplyClickListener onReplyClickListener) {
        this.smartReplies = smartReplies;
        this.onReplyClickListener = onReplyClickListener;
    }

    @NonNull
    @Override
    public SmartReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_list_layout, parent, false);
        return new SmartReplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmartReplyViewHolder holder, int position) {
        String reply = smartReplies.get(position);
        holder.replyText.setText(reply);
        holder.itemView.setOnClickListener(v -> onReplyClickListener.onReplyClick(reply));
    }

    @Override
    public int getItemCount() {
        return smartReplies.size();
    }

    public static class SmartReplyViewHolder extends RecyclerView.ViewHolder {
        TextView replyText;

        public SmartReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            replyText = itemView.findViewById(R.id.smartReplyText);
        }
    }
    public void updateSuggestion(List<String> newSuggestions) {
        smartReplies.clear();
        smartReplies.addAll(newSuggestions);
        notifyDataSetChanged();
    }

    public interface OnReplyClickListener {
        void onReplyClick(String reply);
    }
}
