package com.example.janethdelgado.dndnme;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.janethdelgado.dndnme.Models.Profile;
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
        private RelativeLayout container_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivShortImage = itemView.findViewById(R.id.ivShortImage);
            tvShortUsername = itemView.findViewById(R.id.tvShortUsername);
            tvShortBio = itemView.findViewById(R.id.tvShortBio);
            container_profile = itemView.findViewById(R.id.container_profile);
        }

        public void bind (final Profile profile) {
            ParseFile shortImage = profile.getProfileImage();
            if(shortImage != null) {
                Glide.with(context).load(Uri.parse(shortImage.getUrl())).into(ivShortImage);
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

            //add click listener on the whole row
            container_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //navigate to selected profile
                    Intent i = new Intent(context, ProfileActivity.class);
                    i.putExtra("user", profile.getUser());
                    context.startActivity(i);
                }
            });
        }
    }
}