package com.example.janethdelgado.dndnme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.ParseFile;

import java.util.List;

public class ProfilesAdapter extends RecyclerView.Adapter<ProfilesAdapter.ViewHolder> {

    private Context context;
    private List<Profile> profiles;

    public ProfilesAdapter(Context context, List<Profile> profiles) {
        this.context = context;
        this.profiles = profiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_short_profile, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Profile profile = profiles.get(i);
        viewHolder.bind(profile);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivShortImage;
        private TextView tvShortUsername;
        private TextView tvShortBio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivShortImage = itemView.findViewById(R.id.ivShortImage);
            tvShortUsername = itemView.findViewById(R.id.tvShortUsername);
            tvShortBio = itemView.findViewById(R.id.tvShortBio);
        }

        public void bind (Profile profile) {
            ParseFile shortImage = profile.getProfileImage();
            if(shortImage != null) {
                Glide.with(context).load(shortImage.getUrl()).into(ivShortImage);
            }

            String username = profile.getUser().getUsername();
            if (username != null && !(username.equals("")))
                tvShortUsername.setText("@" + username);
            else
                tvShortUsername.setText("@Default");

            String shortBio = profile.getShortBio();
            if (shortBio != null && !(shortBio.equals("")))
                tvShortBio.setText(shortBio);
            else
                tvShortBio.setText("This is the default text for the short bio.");
        }
    }
}
