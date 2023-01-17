package com.fourrunstudios.raidfinderlite.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.fourrunstudios.raidfinderlite.R;
import com.fourrunstudios.raidfinderlite.Tweet;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.MyViewHolder> {
    Context context;
    List<Tweet> tweets;

    public RecyclerViewAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.raid_layout, parent, false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        tweets.get(position).init();
        holder.raidView.setText(tweets.get(position).getRaidId());
        holder.dateView.setText(tweets.get(position).getCreated_at());
        holder.extraText.setText(tweets.get(position).getExtraText());

        ConstraintLayout layout = (ConstraintLayout) holder.dateView.getParent();
        layout.setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData copyRaidId = ClipData.newPlainText("RaidId",tweets.get(position).getRaidId());
            clipboardManager.setPrimaryClip(copyRaidId);
            Toast.makeText(context, "Raid ID Copied!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView raidView;
        TextView dateView;
        TextView extraText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            raidView = itemView.findViewById(R.id.raid_id);
            dateView = itemView.findViewById(R.id.timer);
            extraText = itemView.findViewById(R.id.extra_text);
        }

    }
}
