package com.safalfrom2050.projectaudio.ui.allsongs;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.safalfrom2050.projectaudio.R;
import com.safalfrom2050.projectaudio.SongDetail;

import java.util.List;

public class AllSongsAdapter extends RecyclerView.Adapter<AllSongsAdapter.Viewholder> {

    List<SongDetail> songs;
    SparseBooleanArray checkListState = new SparseBooleanArray();

    SongListInterface songListInterface;

    public AllSongsAdapter(List<SongDetail> songs) {
        this.songs = songs;
    }

    public void setOnSongListInterfaceListener(SongListInterface songListInterface){
        this.songListInterface = songListInterface;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allsongs,parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.songTitleView.setText(songs.get(position).getTitle());

        holder.songCheckbox.setChecked(checkListState.get(position, false));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    class Viewholder extends RecyclerView.ViewHolder implements CheckBox.OnCheckedChangeListener {
        TextView songTitleView;
        CheckBox songCheckbox;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            songTitleView = itemView.findViewById(R.id.song_title);
            songCheckbox = itemView.findViewById(R.id.song_checkbox);
            songCheckbox.setOnCheckedChangeListener(this);

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checkListState.put(getAdapterPosition(), isChecked);
            if(songListInterface!=null){
                songListInterface.OnShareListUpdated(checkListState);
            }
        }
    }

    public interface SongListInterface{
        void OnShareListUpdated(SparseBooleanArray updatedCheckStates);
    }

}
