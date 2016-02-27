package cartera.icaninter.net.cartera.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import cartera.icaninter.net.cartera.R;
import cartera.icaninter.net.cartera.Utils;
import cartera.icaninter.net.cartera.adapters.RequestsAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFeedFragment extends Fragment {


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

    

}
