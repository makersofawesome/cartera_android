package cartera.icaninter.net.cartera.wizard;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import butterknife.Bind;
import butterknife.ButterKnife;
import cartera.icaninter.net.cartera.R;
import cartera.icaninter.net.cartera.main.LoginActivity;

public class RegisterActivity extends AppCompatActivity{

   @Bind(R.id.firstName)
    EditText firstName;

    @Bind(R.id.lastName)
    EditText lastName;

    @Bind(R.id.email)
    EditText email;

    @Bind(R.id.userName)
    EditText user;

    @Bind(R.id.password)
    EditText password;

    @Bind(R.id.bank)
    EditText accnt;

    @Bind(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String first = firstName.getText().toString();
                String last = lastName.getText().toString();
                String mail = email.getText().toString();
                String pass = password.getText().toString();
                String uname = user.getText().toString();
                String account =  accnt.getText().toString();

                String msg = null;
                boolean cont = true;
                try {
                    if ((msg = isValidEmail(mail)) != null) {
                        email.setError(msg);
                        cont = false;
                    }
                    if ((msg = isValidBank(account)) != null) {
                        accnt.setError(msg);
                        cont = false;
                    }
                    if ((msg = isValidUser(uname)) != null) {
                        user.setError(msg);
                        cont = false;
                    }


                }catch (Exception e){

                }
            }
        });

    }


    private String isValidEmail(String s) throws ParseException {
        if(s.isEmpty()) return "Required field";

        if(!s.contains("@") || !s.contains(".")){
            return "Invalid email address";
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("_User");
        if(query.whereEqualTo("email", s.toLowerCase()).find().size() > 0){
            return "Email is already taken!";
        }


        return null;
    }


    private String isValidUser(String s) throws ParseException {
        if(s.isEmpty()) return "Required field";

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("_User");
        if(query.whereEqualTo("username", s.toLowerCase()).find().size() > 0){
            return "User is already taken!";
        }
        return null;
    }

    private String isValidBank(String s){
        if(s.isEmpty()) return "Required field";

        return null;
    }


}
