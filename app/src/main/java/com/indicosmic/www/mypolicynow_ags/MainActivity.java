package com.indicosmic.www.mypolicynow_ags;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.indicosmic.www.mypolicynow_sdk.QuotationActivity;
import com.indicosmic.www.mypolicynow_sdk.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_sdk.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow_sdk.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow_sdk.webservices.RestClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.www.mypolicynow_ags.RestClient.ROOT_URL2;
import static com.indicosmic.www.mypolicynow_sdk.utils.CommonMethods.md5;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    String StrMobile,StrUserName,StrUserId,StrEmail,StrUniqueId,POS_TOKEN="";
    TextView TV_WelcomeTxt,TV_TodayDate;
    ProgressDialog myDialog;
    private static final String TAG = "MainActivity";
    LinearLayout LayoutCar,LayoutBike,LayoutCommercial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getString("pos_token") != null) {
            POS_TOKEN = bundle.getString("pos_token");
        } else {
            POS_TOKEN = "";
        }
        init();

        if(POS_TOKEN!=null && POS_TOKEN.equalsIgnoreCase("")){
            CommonMethods.DisplayToast(this,"Invalid Access. Please try again.");
        }

        //getPasswordForMPN();

    }

    private void init() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //TextView nav_header_userId = navigationView.findViewById(R.id.nav_header_userId);
        View hView =  navigationView.getHeaderView(0);
        ImageView iv_logo = (ImageView)hView.findViewById(R.id.iv_logo);
        ImageView iv_facebook = (ImageView)navigationView.findViewById(R.id.iv_facebook);
        ImageView iv_google_plus = (ImageView)navigationView.findViewById(R.id.iv_google_plus);
        ImageView iv_twitter = (ImageView)navigationView.findViewById(R.id.iv_twitter);
        ImageView iv_youtube = (ImageView)navigationView.findViewById(R.id.iv_youtube);
        ImageView iv_linked_in = (ImageView)navigationView.findViewById(R.id.iv_linked_in);

        iv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.facebook_page)));
                startActivity(i);
            }
        });

        iv_google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.google_plus_page)));
                startActivity(i);
            }
        });

        iv_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.twitter_page)));
                startActivity(i);
            }
        });

        iv_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.youtube_page)));
                startActivity(i);
            }
        });

        iv_linked_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.linked_in_page)));
                startActivity(i);
            }
        });



        TextView nav_header_userName = (TextView)hView.findViewById(R.id.nav_header_userName);
        TextView nav_user_email = (TextView)hView.findViewById(R.id.nav_Email);
        if(StrUserName!=null && !StrUserName.equalsIgnoreCase("")){
            nav_header_userName.setText(StrUserName.toUpperCase());
        }
        //nav_user_email.setText("Email: "+StrEmail);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        myDialog = new ProgressDialog(MainActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        //getPasswordForMPN();

        LayoutCar = (LinearLayout)findViewById(R.id.LayoutCar);
        LayoutBike = (LinearLayout)findViewById(R.id.LayoutBike);
        LayoutCommercial = (LinearLayout)findViewById(R.id.LayoutCommercial);


        LayoutCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Car");
                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                    Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                    i.putExtra("pos_token", POS_TOKEN);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }else {
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Invalid Token");
                }
            }
        });

        LayoutBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Bike");
                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                    Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                    i.putExtra("pos_token", POS_TOKEN);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }else {
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Invalid Token");
                }
            }
        });


        LayoutCommercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Commercial");
                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                    Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                    i.putExtra("pos_token", POS_TOKEN);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }else {
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Invalid Token");
                }
            }
        });

    }

  /*  private void getMpnDataFromPOS() {
        final ProgressDialog myDialog = new ProgressDialog(MainActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

      *//*   StrMobile = "8169972611";
         StrEmail = "sush.rokade@gmail.com";

        String terminal_id="PX918512";
        String merchant_id = "PX9400000000012";
*//*

        String URL = ROOT_URL2+"getpassword";
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Log.d("URL", "" + URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);

                        JSONObject data_obj = jsonresponse.getJSONObject("data");

                          = data_obj.getString("token");
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"MerchantId",merchant_id);
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"TerminalId",terminal_id);

*//*

                        //TextView BtnBuyInsurance = (TextView)findViewById(R.id.BtnBuyInsurance);
                        BtnBuyInsurance.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Bike");
                                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                                    Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                                    i.putExtra("pos_token", POS_TOKEN);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });
*//*


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Something went wrong. Please try again later.");

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<String, String>();

                    map.put("POS_TOKEN", POS_TOKEN);

                    return map;
                }
            };


            int socketTimeout = 50000; //30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }else {
            CommonMethods.DisplayToast(getApplicationContext(),"Please check Internet Connection");
        }




    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_about_us) {
            Intent i = new Intent(getApplicationContext(), AboutUsActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        } else if(id == R.id.nav_contact_us){
            Intent i = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
