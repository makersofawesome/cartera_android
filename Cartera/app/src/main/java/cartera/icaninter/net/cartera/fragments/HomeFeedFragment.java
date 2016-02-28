package cartera.icaninter.net.cartera.fragments;


import android.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cartera.icaninter.net.cartera.R;
import cartera.icaninter.net.cartera.Utils;
import cartera.icaninter.net.cartera.adapters.RequestsAdapter;
import cartera.icaninter.net.cartera.models.Request;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFeedFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Request>> {


    private static final String LOG_TAG = Utils.makeLogTag(HomeFeedFragment.class);

    @Bind(R.id.home_feed)
    RecyclerView homeFeed;

    @Bind(R.id.empty_view)
    TextView emptyText;

    private RequestsAdapter mAdapter;

    public HomeFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home_feed, container, false);

        ButterKnife.bind(this, v);

        mAdapter = new RequestsAdapter(getContext(), null, emptyText);

        homeFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        homeFeed.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(
                0,
                null,
                this).forceLoad();
    }

    @Override
    public android.support.v4.content.Loader<ArrayList<Request>> onCreateLoader(int id, Bundle args) {
        Log.d("Loader", "Init");
        SharedPreferences pref = getActivity().getSharedPreferences("Cartera", 0);
        return new RequestLoader(getContext(), 5, pref.getFloat("longitude", 0), pref.getFloat("latitude", 0));
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<Request>> loader, ArrayList<Request> data) {
        Log.d("Loader", "finished");
        mAdapter.swapData(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<Request>> loader) {

    }



    public static class RequestLoader extends AsyncTaskLoader<ArrayList<Request>>{


        private int mRange;
        private float mLong;
        private float mLat;

        private static final int numPoints = 4;

        @Override
        public boolean takeContentChanged() {
            return super.takeContentChanged();
        }

        public RequestLoader(Context context, int range, float lon, float lat) {
            super(context);
            mRange = range;
            mLong = lon;
            mLat = lat;
        }

        @Override
        public ArrayList<Request> loadInBackground() {



            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Request");

            List<ParseObject> pObjects = null;
            try {
                pObjects = query.whereGreaterThanOrEqualTo("longitude", mLong - .07)
                        .whereLessThanOrEqualTo("longitude", mLong + .07)
                        .whereGreaterThanOrEqualTo("latitude", mLat - .07)
                        .whereLessThanOrEqualTo("latitude", mLat + .07)
                        .find();

                if (pObjects == null || pObjects.isEmpty()){
                    Log.e("Loader", "Empty");
                    return null;
                }

                List<Request> requests = new ArrayList<>(pObjects.size());

                for(ParseObject p: pObjects){


                    ParseUser user = new ParseQuery<ParseUser>("_User")
                            .whereEqualTo("_id", p.getString("requesterId"))
                            .find()
                            .get(0);

                    if(user.getUsername().equals(ParseUser.getCurrentUser().getUsername()))
                        continue;

                    Request r = new Request();
                    r.setRequesterName(new StringBuilder()
                            .append(user.get("first"))
                            .append(" ")
                            .append(user.get("last")).toString());
                    r.setLat(p.getDouble("latitude"));
                    r.setLon(p.getDouble("longitude"));
                    r.setRequestAmount(p.getInt("amount"));
                    try {
                        r.setDate(Utils.formatDate(p.get("_updated_at").toString()));
                    }catch (Exception e){

                    }

                    requests.add(r);
                }

                return (ArrayList<Request>) requests;
            }catch (ParseException e){
                Log.e("Loader", e.getMessage());
                return null;
            }


        }
    }
    

}
