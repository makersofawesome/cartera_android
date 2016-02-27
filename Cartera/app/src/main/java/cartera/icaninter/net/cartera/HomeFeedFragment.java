package cartera.icaninter.net.cartera;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cartera.icaninter.net.cartera.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFeedFragment extends Fragment {


    private static final String LOG_TAG = Utils.makeLogTag(HomeFeedFragment.class);

    public HomeFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_feed, container, false);
    }

}
