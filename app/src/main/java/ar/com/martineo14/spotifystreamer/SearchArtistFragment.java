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

package ar.com.martineo14.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import ar.com.martineo14.spotifystreamer.adapters.ArtistListAdapter;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class SearchArtistFragment extends Fragment {

    public static final String ARTIST_NAME = "artist_name";
    public static final String ARTIST_ID = "artist_id";
    List<Artist> artistResult;
    private String artistNameSearch;
    private ArtistListAdapter artistAdapter;
    private ListView listView;
    private SearchView searchView;

    public SearchArtistFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.list_artist);
        searchView = (SearchView) view.findViewById(R.id.search_artist);

        if (savedInstanceState != null) {
            artistNameSearch = savedInstanceState.getString(ARTIST_NAME);
            searchView.setQuery(artistNameSearch, false);
            updateArtistList();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                artistNameSearch = query;
                updateArtistList();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Artist artist = artistAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), ArtistDetailActivity.class);
                intent.putExtra(ARTIST_ID, artist.id);
                intent.putExtra(ARTIST_NAME, artist.name);
                startActivity(intent);
            }
        });
    }

    private void updateArtistList() {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        // I used this post of the forum to the call of the API.
        // https://discussions.udacity.com/t/spotify-api-examples/21933
        spotify.searchArtists(artistNameSearch, new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {
                if (artistsPager.artists.items.size() == 0) {
                    if (artistAdapter != null) {
                        artistAdapter.clear();
                    }
                    Toast.makeText(getActivity(), getString(R.string.msg_no_artist_found),
                            Toast.LENGTH_LONG).show();
                } else {
                    artistResult = artistsPager.artists.items;
                    artistAdapter = new ArtistListAdapter(getActivity(), artistResult);
                    listView.setAdapter(artistAdapter);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (artistAdapter != null) {
                    artistAdapter.clear();
                }
                Toast.makeText(getActivity(), getString(R.string.msg_error_default),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARTIST_NAME, artistNameSearch);
        super.onSaveInstanceState(outState);
    }
}
