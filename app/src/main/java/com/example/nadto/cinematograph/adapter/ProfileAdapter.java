package com.example.nadto.cinematograph.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;
import com.example.nadto.cinematograph.model.Person;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private ArrayList<Person> cast;
    private Context mContext;


    public ProfileAdapter (Context context, ArrayList<Person> persons) {
        this.cast = persons;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.person_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(cast.get(position));
    }

    @Override
    public int getItemCount() {
        return cast.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, character;
        private ImageView profilePhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.profileName);
            character = (TextView)itemView.findViewById(R.id.profileChar);
            profilePhoto = (ImageView)itemView.findViewById(R.id.profilePhoto);

        }

        void bind(final Person person) {
            name.setText(person.getName());
            character.setText(person.getCurrentChar());

            Picasso.with(mContext).load(person.getProfilePath()).into(profilePhoto);

        }
    }

}
