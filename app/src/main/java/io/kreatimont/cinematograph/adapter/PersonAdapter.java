package io.kreatimont.cinematograph.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nadto.cinematograph.R;

import io.kreatimont.cinematograph.model.tmdb.people.Person;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {

    private List<Person> cast;
    private Context mContext;


    public PersonAdapter(Context context, List<Person> persons) {
        this.cast = persons;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_person_v2, parent, false);
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
        private LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.profileName);
            character = (TextView)itemView.findViewById(R.id.profileChar);
            profilePhoto = (ImageView)itemView.findViewById(R.id.profilePhoto);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.characterContainer);
        }

        void bind(final Person person) {
            name.setText(person.getName());
            character.setVisibility(View.GONE);

            Picasso.with(mContext).load(mContext.getString(R.string.image_base)  + mContext.getString(R.string.profile_size_medium) + person.getProfilePath()).into(profilePhoto);
        }
    }

}
