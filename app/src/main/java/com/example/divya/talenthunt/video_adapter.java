package com.example.divya.talenthunt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Divya on 17-11-2016.
 */
public class video_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<Data> data= Collections.emptyList();
    Data current;
    int currentPos=0;
    Uri vidUri;
    String vidAddress;
    // create constructor to initialize context and data sent from MainActivity
    public video_adapter(Context context, List<Data> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.activity_container, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MediaMetadataRetriever mediaMetadataRetriever = null;
        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        Data current=data.get(position);
        myHolder.textuser_id.setText(current.user_id);
        vidAddress = "http://192.168.43.213/Talhunt/uploads/video/".concat(current.path);
        vidUri = Uri.parse(vidAddress);
        Bitmap bitmap;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(vidAddress, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(vidAddress);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
            myHolder.ib.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }



        /*myHolder.videoView.setVideoURI(vidUri);

        MediaController vidControl = new MediaController(context);
        vidControl.setAnchorView(myHolder.videoView);
        myHolder.videoView.setMediaController(vidControl);*/

        //myHolder.textuser_id.setText(current.user_id);
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textuser_id;
        ImageView ib;
        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textuser_id= (TextView) itemView.findViewById(R.id.user_id);
            ib=(ImageView)itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        // Click event for all items
        @Override
        public void onClick(View v) {
            Intent i=new Intent(context,play_video.class);
            i.putExtra("uri",vidAddress);
            context.startActivity(i);

        }

    }
}
