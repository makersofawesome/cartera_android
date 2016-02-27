package cartera.icaninter.net.cartera.main;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import cartera.icaninter.net.cartera.ActivityFragment;
import cartera.icaninter.net.cartera.fragments.HomeFeedFragment;
import cartera.icaninter.net.cartera.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

//    @Bind(R.id.customer_name)
//    TextView nameText;

    @Bind(R.id.nav_view)
    NavigationView navigationView;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private ParseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ComposeActivity.class));
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        mUser = ParseUser.getCurrentUser();
        TextView nameText =
                (TextView) navigationView.
                getHeaderView(0).
                findViewById(R.id.customer_name);
        nameText.setText(mUser.getUsername());


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new HomeFeedFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new HomeFeedFragment())
                        .commit();
                break;
            case R.id.nav_activity:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new ActivityFragment())
                        .commit();
                break;
            case R.id.nav_signout:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        mUser = null;
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                });
            case R.id.nav_app_settings:
                break;
            case R.id.nav_acnt_settings:
                break;
            case R.id.nav_send:
                break;
            case R.id.nav_share:
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
