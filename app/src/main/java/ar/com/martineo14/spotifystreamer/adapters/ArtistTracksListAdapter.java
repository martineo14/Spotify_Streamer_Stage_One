/*
 * Copyright 2015 Sergio Martin Pueyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ar.com.martineo14.spotifystreamer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ar.com.martineo14.spotifystreamer.R;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by sergiomartinpueyo on 7/4/15.
 */
public class ArtistTracksListAdapter  extends ArrayAdapter<Track> {

    private Context context;

    public ArtistTracksListAdapter(Activity context, List<Track> trackModels) {
        super(context, 0, trackModels);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track trackModel = getItem(position);
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.artist_top_ten_item, parent, false);

        ImageView artist_image = (ImageView) rootView.findViewById(R.id.img_artist_top_ten_item);
        List<Image> images = trackModel.album.images;
        if (images != null && images.size() > 0) {
            Picasso.with(context).load(trackModel.album.images.get(2).url).into(artist_image);
        }
        else
        {
            // I used an default image as placeholder
            // /http://www.the-music-shop.com/wp-content/uploads/2015/02/placeholder.png
            Picasso.with(context).load(R.drawable.placeholder_music).into(artist_image);
        }

        TextView artist_album_name = (TextView) rootView.findViewById(R.id.album_name_artist_top_ten_item);
        artist_album_name.setText(trackModel.album.name);

        TextView artist_track_name = (TextView) rootView.findViewById(R.id.track_name_artist_top_ten_item);
        artist_track_name.setText(trackModel.name);

        return rootView;
    }
}
