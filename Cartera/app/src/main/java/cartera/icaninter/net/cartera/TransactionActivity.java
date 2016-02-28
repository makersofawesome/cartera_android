package cartera.icaninter.net.cartera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.reimaginebanking.api.java.Constants.TransactionMedium;
import com.reimaginebanking.api.java.NessieClient;
import com.reimaginebanking.api.java.NessieException;
import com.reimaginebanking.api.java.NessieResultsListener;
import com.reimaginebanking.api.java.models.Account;
import com.reimaginebanking.api.java.models.Transfer;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TransactionActivity extends AppCompatActivity {

    public static final String EXTRA_AMOUNT = "amount";
    public static final String EXTRA_LONG = "longitude";
    public static final String EXTRA_LAT = "latitude";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_REQUESTER = "requester";
    public static final String EXTRA_REQUEST = "request";

    @Bind(R.id.amount_text)
    TextView amount;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.btn_nav)
    Button nav;

    @Bind(R.id.btn_cancel)
    Button cancel;

    @Bind(R.id.code)
    TextView code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        ButterKnife.bind(this);

        final Intent intent = getIntent();

        amount.setText("$" + intent.getIntExtra(EXTRA_AMOUNT, 0));
        name.setText(intent.getStringExtra(EXTRA_NAME));

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences prefs = getSharedPreferences("Cartera", 0);

                String uri = String.format(Locale.ENGLISH,
                        "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                        prefs.getFloat("latitude", Float.MAX_VALUE),
                        prefs.getFloat("longitude", Float.MAX_VALUE),
                        intent.getDoubleExtra(EXTRA_LAT, Double.MAX_VALUE),
                        intent.getDoubleExtra(EXTRA_LONG, Double.MAX_VALUE));
                Intent tent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(tent);
            }
        });

        int transactionCode =(int) (Math.random() * Integer.MAX_VALUE) + 1;
        code.setText("Transaction Code: " + transactionCode);


        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        final NessieClient nessieClient = NessieClient.getInstance();
                        nessieClient.setAPIKey("5948b3880cdae0efb761d44b9be7d96f");
                        try { String id = ParseUser.getQuery().
                                whereEqualTo("_id", intent.getStringExtra(EXTRA_REQUESTER)).find().get(0).get("account_id").toString();
                            nessieClient.createTransfer(id, new Transfer.Builder()
                                    .amount(Integer.parseInt(amount.getText().toString().substring(1)))
                                    .payee_id(ParseUser.getCurrentUser().get("account_id").toString())
                                    .medium(TransactionMedium.BALANCE)
                                    .build(), new NessieResultsListener() {

                                @Override
                                public void onSuccess(Object o, NessieException e) {
                                    ParseObject object = ParseObject.create("Request");
                                    object.add("_id", intent.getStringExtra(EXTRA_REQUEST));
                                    try {
                                        object.delete();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                    nessieClient.getAccount(ParseUser.getCurrentUser().get("account_id").toString(), new NessieResultsListener() {
                                        @Override
                                        public void onSuccess(Object o, NessieException e) {
                                            new AlertDialog.Builder(TransactionActivity.this)
                                                    .setTitle("Transfer successful!")
                                                    .setMessage("Amount: " + amount.getText().toString() + "\nNew Balance: $" + ((Account) o).getBalance())
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            finish();
                                                        }
                                                    }).create()
                                                    .show();
                                        }
                                    });
                                }
                            });
                        }catch(Exception e){

                        }
                        }

                },
                20000
        );

    }


    /*

     */
}
