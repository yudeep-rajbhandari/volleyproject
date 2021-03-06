package com.example.user.volleyjson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class notes extends AppCompatActivity {
    Spinner spinner;
    HashMap<String ,String> hmLang = new HashMap<String,String>();
    ListView listView;
    NotesAdapter notesAdapter;
    notesgetter getnotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        spinner=(Spinner)findViewById(R.id.spinner_notes);
        String get_faculty = getIntent().getStringExtra("get_faculty");
        String get_semester = getIntent().getStringExtra("get_semester");
        final ArrayList<String> mArrayList = new ArrayList<String>();


        Toolbar toolbar= (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView=(ListView)findViewById(R.id.listview_notes);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mArrayList);
spinner.setAdapter(dataAdapter);
        if(get_faculty.equals("myfaculty") && get_semester.equals("mysemester")){
            DatabaseHelper db=new DatabaseHelper(getApplicationContext());
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    notesAdapter = new NotesAdapter(notes.this, R.layout.textlayout_notes);
                    listView.setAdapter(notesAdapter);
                    String reqsubjectcode=spinner.getSelectedItem().toString();
                    DatabaseHelper db=new DatabaseHelper(getApplicationContext());
                    ArrayList<DatabaseHelper.Notes> notelist=db.getNotes1(reqsubjectcode);
                    for(DatabaseHelper.Notes notes:notelist){

                        getnotes = new notesgetter(notes.topic, notes.link);

                        notesAdapter.add(getnotes);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    System.out.println("<<<<<<<<<<<");

                }
            });

            ArrayList<DatabaseHelper.Notes> notelist=db.getNotes();
            for(DatabaseHelper.Notes notes:notelist){
                //subject.printSubject();
              //  Routinedisplay routinedisplay = new Routinedisplay(notes.ID, notes.subject_name,notes.subject_code, notes.topic, notes.link);
                if (mArrayList.contains(notes.subject_code)) {
                    System.out.println("<<<<<<<<<");
                } else {
                    mArrayList.add(notes.subject_code);
                   // hmLang.put(subjectcode, subjectid);
                    dataAdapter.notifyDataSetChanged();
                }
            }
        }
        else {


            String url = "http://knowbook.herokuapp.com/notes/Requestsubject/?Faculty=" + get_faculty + "&Semester=" + get_semester;
            //String notesurl = "http://knowbook.herokuapp.com/notes/Requests/"+key;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String itemsel = adapterView.getItemAtPosition(i).toString();
                    notesAdapter = new NotesAdapter(notes.this, R.layout.textlayout_notes);
                    listView.setAdapter(notesAdapter);

                    String key = hmLang.get(itemsel);
                    String notesurl = "http://knowbook.herokuapp.com/notes/Requests/" + key;


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, notesurl, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println("<<<<<<<<<<<<<<<<<<<");
                                    try {
                                        System.out.println("<<<<<<<<<<<<<<<<<<<");
                                        JSONArray jsonObject = response.getJSONArray("data");
                                        for (int i = 0; i < jsonObject.length(); i++) {
                                            System.out.println("<<<<<<<<<<<<<<<<<<<");
                                            JSONObject object1 = jsonObject.getJSONObject(i);

                                            String topic = object1.getString("NoteTopic");
                                            JSONObject picture = object1.getJSONObject("pdf");
                                            String pdf = picture.getString("url");


                                            System.out.println("<<<<<<<<<<<<<<<<<<<");
                                            System.out.print(topic);
                                            System.out.println(pdf);
                                            getnotes = new notesgetter(topic, pdf);

                                            notesAdapter.add(getnotes);


//                                        mArrayList.add(subjectcode);
//                                        hmLang.put(subjectcode,subjectid);
//                                        dataAdapter.notifyDataSetChanged();
                                        }
//                            System.out.println(mArrayList);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {


                            Toast.makeText(notes.this, "something went wrong", Toast.LENGTH_LONG).show();

                        }

                    });
                    // System.out.println(mArrayList);
                    MySingleton.getInstance(notes.this).addToRequestQueue(jsonObjectRequest);


                    Toast.makeText(notes.this, key, Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Toast.makeText(notes.this, "select somthing", Toast.LENGTH_SHORT).show();

                }
            });


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("<<<<<<<<<<<<<<<<<<<");
                            try {
                                System.out.println("<<<<<<<<<<<<<<<<<<<");
                                JSONArray jsonObject = response.getJSONArray("data");
                                for (int i = 0; i < jsonObject.length(); i++) {
                                    System.out.println("<<<<<<<<<<<<<<<<<<<");
                                    JSONObject object1 = jsonObject.getJSONObject(i);

                                    JSONObject subject = object1.getJSONObject("Subjectid");
                                    String subjectcode = subject.getString("subjectcode");

                                    //String subjectcode = object1.getString("subjectcode");
                                    String subjectid = subject.getString("_id");


                                    System.out.println("<<<<<<<<<<<<<<<<<<<");
                                    System.out.print(subjectcode);
                                    System.out.println(subjectid);


                                    if (mArrayList.contains(subjectcode)) {
                                        System.out.println("<<<<<<<<<");
                                    } else {
                                        mArrayList.add(subjectcode);
                                        hmLang.put(subjectcode, subjectid);
                                        dataAdapter.notifyDataSetChanged();
                                    }
                                }
//                            System.out.println(mArrayList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    Toast.makeText(notes.this, "something went wrong", Toast.LENGTH_LONG).show();

                }

            });
            // System.out.println(mArrayList);
            MySingleton.getInstance(notes.this).addToRequestQueue(jsonObjectRequest);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
