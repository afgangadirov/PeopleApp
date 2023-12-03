package com.example.kisileruygulamasi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private KisilerAdapter adapter;
    private ArrayList<Kisiler> kisilerArrayList;
    //private Veritabani vt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        rv = findViewById(R.id.rv);
        fab = findViewById(R.id.fab);

        toolbar.setTitle("Kisiler Uygulamasi");
        setSupportActionBar(toolbar);

        //vt = new Veritabani(this);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //kisilerArrayList = new KisilerDao().tumKisiler(vt);

        kisilerArrayList = new ArrayList<>();



        adapter = new KisilerAdapter(MainActivity.this,kisilerArrayList);

        rv.setAdapter(adapter);

        tumKisiler();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertGoster();

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);



        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.e("onQueryTextSubmit",query);

        //kisilerArrayList = new KisilerDao().kisilerAra(vt,query);

        kisiAra(query);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.e("onQueryTextChange",newText);


        //kisilerArrayList = new KisilerDao().kisilerAra(vt,newText);

        kisiAra(newText);

        return false;
    }


    public void alertGoster(){
        LayoutInflater layout = LayoutInflater.from(this);
        View tasarim = layout.inflate(R.layout.alert_tasarim,null);

        EditText editTextName = tasarim.findViewById(R.id.editTextName);
        EditText editTextPhone = tasarim.findViewById(R.id.editTextPhone);

        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Add a contact");
        ad.setView(tasarim);
        ad.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                
                String kisi_ad = editTextName.getText().toString().trim();
                String kisi_tel = editTextPhone.getText().toString().trim();

                /*new KisilerDao().kisiEkle(vt,kisi_ad,kisi_tel);
                kisilerArrayList = new KisilerDao().tumKisiler(vt);*/

                kisiEkle(kisi_ad,kisi_tel);


            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad.create();
        ad.show();

    }




    public void tumKisiler(){



        String url = "http://kasimadalan.pe.hu/kisiler/tum_kisiler.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                kisilerArrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray kisiler = jsonObject.getJSONArray("kisiler");

                    for (int i = 0; i<kisiler.length(); i++) {

                        JSONObject k = kisiler.getJSONObject(i);

                        int kisi_id = k.getInt("kisi_id");
                        String kisi_ad = k.getString("kisi_ad");
                        String kisi_tel = k.getString("kisi_tel");

                        Kisiler kisi = new Kisiler(kisi_id,kisi_ad,kisi_tel);

                        kisilerArrayList.add(kisi);

                    }

                    adapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }



    public void kisiAra(String kisi_ad){



        String url = "http://kasimadalan.pe.hu/kisiler/tum_kisiler_arama.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                kisilerArrayList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray kisiler = jsonObject.getJSONArray("kisiler");

                    for (int i = 0; i<kisiler.length(); i++){
                        JSONObject k = kisiler.getJSONObject(i);

                        int kisi_id = k.getInt("kisi_id");
                        String kisi_ad = k.getString("kisi_ad");
                        String kisi_tel = k.getString("kisi_tel");

                        Kisiler kisi = new Kisiler(kisi_id,kisi_ad,kisi_tel);

                        kisilerArrayList.add(kisi);
                    }

                    adapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("kisi_ad",kisi_ad);

                return params;
            }
        };

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }


    public void kisiEkle(String kisi_ad, String kisi_tel){

        String url = "http://kasimadalan.pe.hu/kisiler/insert_kisiler.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("kisiEkle Cavab",response);

                tumKisiler();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("kisi_ad",kisi_ad);
                params.put("kisi_tel",kisi_tel);

                return params;
            }
        };

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }


}