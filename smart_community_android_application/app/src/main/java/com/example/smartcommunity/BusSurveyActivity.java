// Submitting the Bus survey screen
package com.example.smartcommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class BusSurveyActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    RadioGroup rg1;
    RadioButton rgB1;
    RadioGroup rg2;
    RadioButton rgB2;
    RadioGroup rg3;
    RadioButton rgB3;
    Button button;
    Spinner BusSpinner;
    String sixth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_survey);
        mAuth= FirebaseAuth.getInstance();
        final EditText etFourth=findViewById(R.id.editText2);
        final EditText etFifth=findViewById(R.id.editText3);
        rg1=findViewById(R.id.first);
        rg2=findViewById(R.id.second);
        rg3=findViewById(R.id.third);
        button=findViewById(R.id.button1);
        BusSpinner=findViewById(R.id.spinnerB);
        BusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sixth=BusSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Initialise the request queue to submit request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fourth=etFourth.getText().toString();
                String fifth=etFifth.getText().toString();
                int id1=rg1.getCheckedRadioButtonId();
                rgB1=(RadioButton)findViewById(id1);
                String first= (String) rgB1.getText();
                int id2=rg2.getCheckedRadioButtonId();
                rgB2=(RadioButton)findViewById(id2);
                String second=(String) rgB2.getText();
                int id3=rg3.getCheckedRadioButtonId();
                rgB3=(RadioButton)findViewById(id3);
                String third=(String)rgB3.getText();
                JSONObject jsonObject=new JSONObject();
                try{
                    jsonObject.put("first",first);
                    jsonObject.put("second",second);
                    jsonObject.put("third",third);
                    jsonObject.put("fourth",fourth);
                    jsonObject.put("fifth",fifth);
                    jsonObject.put("sixth",sixth);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                final String mRequestBody = jsonObject.toString();
                //Posting the Bussurvey API
                String url = "http://ec2-3-81-211-29.compute-1.amazonaws.com:9500/busSurvey";
                StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                        Toast.makeText(BusSurveyActivity.this,"Submitted survey",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {

                            responseString = String.valueOf(response.statusCode);

                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };
                requestQueue.add(postRequest);


            }
        });
    }
    //Inflating the menu options for navigating to other screens
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu_bus_survey, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toast.makeText(this, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.problem:
                Intent intent1=new Intent(BusSurveyActivity.this,HomeActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                return true;
            case R.id.cOVID:
                Intent intent2=new Intent(BusSurveyActivity.this,Covid19Activity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                return true;
            case R.id.logout:
                // logout();
                mAuth.signOut();
                Intent intent=new Intent(BusSurveyActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
