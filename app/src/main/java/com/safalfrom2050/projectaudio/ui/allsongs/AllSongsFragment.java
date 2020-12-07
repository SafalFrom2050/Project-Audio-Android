package com.safalfrom2050.projectaudio.ui.allsongs;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.safalfrom2050.projectaudio.R;
import com.safalfrom2050.projectaudio.SongDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllSongsFragment extends Fragment {

    List<SongDetail> songs = new ArrayList<>();

    SparseBooleanArray checkedStates = new SparseBooleanArray();

    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_all_songs, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview_all_songs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        songs = getSongList();
        final AllSongsAdapter adapter = new AllSongsAdapter(songs);
        adapter.setOnSongListInterfaceListener(new AllSongsAdapter.SongListInterface() {
            @Override
            public void OnShareListUpdated(SparseBooleanArray updatedCheckStates) {
                updateShareList(updatedCheckStates);
            }
        });
        recyclerView.setAdapter(adapter);

        setShareRoomBtn();
    }

    public void setShareRoomBtn(){
        Button shareBtn = getView().findViewById(R.id.share_btn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMusicPostMsg();
            }
        });
    }

    public void sendMusicPostMsg(){
        String postUrl = getString(R.string.api_url);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JSONObject postData = new JSONObject();
        try {
            postData = getCheckedSongsJSON();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                setSharingView(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void setSharingView(boolean sharing){
        TextView statusView = getView().findViewById(R.id.status);
        if(sharing){
            statusView.setVisibility(View.VISIBLE);
        }else{
            statusView.setVisibility(View.GONE);
        }
        // TODO: Add Stoping functions
    }

    public JSONObject getCheckedSongsJSON() throws Exception{
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        for(int i=0; i<songs.size(); i++){
            if(checkedStates.get(i, false)){
                jsonObject = new JSONObject();
                jsonObject.put("name", songs.get(i).getTitle());
                jsonObject.put("artist", songs.get(i).getArtist());
                jsonObject.put("duration", songs.get(i).getDuration());
                jsonArray.put(jsonObject);
            }
        }
        jsonObject = new JSONObject();
        jsonObject.put("device_name", "Test Device");       //TODO: ADD Device Name
        jsonObject.put("pin", 12345 );     //TODO: ADD PIN

        jsonObject.put("music_list", jsonArray);
        System.out.println(jsonObject.toString());
        return jsonObject;
    }

    public void updateShareList(SparseBooleanArray checkedStates){
        this.checkedStates = checkedStates;
    }

    public List<SongDetail> getSongList(){
        List<SongDetail> songsList = new ArrayList<>();
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if(musicCursor!=null && musicCursor.moveToFirst()){

            //Columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);

            // Add to the list
            do {
                long id = musicCursor.getLong(idColumn);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                int duration = musicCursor.getInt(durationColumn);
                songsList.add(new SongDetail(title, artist, "album", duration, id));
            }
            while (musicCursor.moveToNext());

            return songsList;
        }
        return null;
    }
}
