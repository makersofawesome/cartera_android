package cartera.icaninter.net.cartera.main;

import android.content.SharedPreferences;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import cartera.icaninter.net.cartera.R;

public class ComposeActivity extends AppCompatActivity {

    @Bind(R.id.amount_text)
    EditText amountText;

    @Bind(R.id.status)
    TextView statusText;

    @Bind(R.id.request_progress)
    ProgressBar progressBar;

    @Bind(R.id.amountLayout)
    LinearLayout amountLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        ButterKnife.bind(this);

        amountText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND && isValidAmount(amountText.getText().toString())) {
                    commitRequest();
                    return true;
                }

                return false;
            }
        });

    }

    private String formatAmount(String amount){
        int zeros = 3 - amount.length();
        if(zeros <= 0) return amount;
        StringBuilder builder = new StringBuilder(3);
        for(int i = 0; i < zeros; i++){
            builder.append('0');
        }
        builder.append(amount);
        return builder.toString();
    }


    private boolean isValidAmount(String amount){
        return true;
    }


    private void commitRequest(){


        amountLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setEnabled(true);

        ParseUser user = ParseUser.getCurrentUser();


        ParseObject request = ParseObject.create("Request");
        request.put("requesterId", user.getObjectId());
        request.put("amount", Integer.parseInt(amountText.getText().toString()));

        SharedPreferences preferences = getSharedPreferences("Cartera", 0);
        float longg = preferences.getFloat("longitude", Integer.MIN_VALUE);
        float latt = preferences.getFloat("latitude", Integer.MIN_VALUE);

        if(longg != Integer.MIN_VALUE && latt != Integer.MIN_VALUE){
            request.put("longitude", longg);
            request.put("latitude", latt);
        }else{
            //throw new UnknownError("Location unknown");
        }

        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Handler h = new Handler();
                Runnable r;
                if(e == null){
                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    statusText.setText("Request successfully submitted!");
                    statusText.setVisibility(View.VISIBLE);
                    r = new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                }else{
                    progressBar.setEnabled(false);
                    progressBar.setVisibility(View.GONE);
                    statusText.setText("Request failed!");
                    statusText.setVisibility(View.VISIBLE);
                    r = new Runnable() {
                        @Override
                        public void run() {
                            amountLayout.setVisibility(View.VISIBLE);
                            statusText.setVisibility(View.GONE);
                        }
                    };
                }
                h.postDelayed(r, 1000);
            }
        });



    }


}
