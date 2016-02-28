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

import com.google.android.gms.games.request.Requests;
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
public class ActivityFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<ArrayList<Request>>{

    @Bind(R.id.home_feed)
    RecyclerView homeFeed;

    @Bind(R.id.empty_view)
    TextView emptyText;

    private RequestsAdapter mAdapter;


    public ActivityFragment() {
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

        return v;    }


    @Override
    public android.support.v4.content.Loader<ArrayList<Request>> onCreateLoader(int id, Bundle args) {
        Log.d("Loader", "Init");
        SharedPreferences pref = getActivity().getSharedPreferences("Cartera", 0);
        return new RequestHistoryLoader(getContext(), pref.getFloat("longitude", 0), pref.getFloat("latitude", 0));
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<ArrayList<Request>> loader, ArrayList<Request> data) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<ArrayList<Request>> loader) {

    }


    public static class RequestHistoryLoader extends AsyncTaskLoader<ArrayList<Request>> {


        private int mRange;
        private float mLong;
        private float mLat;

        private static final int numPoints = 4;

        @Override
        public boolean takeContentChanged() {
            return super.takeContentChanged();
        }

        public RequestHistoryLoader(Context context, float lon, float lat) {
            super(context);
            mLong = lon;
            mLat = lat;
        }

        @Override
        public ArrayList<Request> loadInBackground() {



            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Request");

            List<ParseObject> pObjects = null;
            try {
                pObjects = query.whereEqualTo("requestorId", ParseUser.getCurrentUser().getObjectId().toString()).find();

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
                    r.setUserId(user.getObjectId());
                    r.setRequestId(p.getObjectId());
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
