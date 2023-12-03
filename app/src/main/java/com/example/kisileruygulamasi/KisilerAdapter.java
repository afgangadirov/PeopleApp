package com.example.kisileruygulamasi;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KisilerAdapter extends RecyclerView.Adapter<KisilerAdapter.CardTasarimTutucu>{
    private Context mContext;
    private List<Kisiler> kisilerList;
    //private Veritabani vt;

    private Kisiler k1;


    public KisilerAdapter(Context mContext, List<Kisiler> kisilerList) {
        this.mContext = mContext;
        this.kisilerList = kisilerList;
    }

    @NonNull
    @Override
    public CardTasarimTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_tasarim,parent,false);
        return new CardTasarimTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimTutucu holder, int position) {
        Kisiler k = kisilerList.get(position);
        holder.textViewKisiBilgi.setText(k.getKisi_ad()+" - "+k.getKisi_tel());

        holder.imageViewNokta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext,holder.imageViewNokta);
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.action_delete:
                                Snackbar.make(holder.imageViewNokta,"Are You Sure?",Snackbar.LENGTH_SHORT)
                                        .setAction("Yes", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                /*new KisilerDao().kisiSil(vt,k.getKisi_id());

                                                kisilerList = new KisilerDao().tumKisiler(vt);*/

                                                //notifyDataSetChanged();

                                                kisiSil(k.getKisi_id());

                                            }
                                        }).show();
                                return true;
                            case R.id.action_update:
                                alertGoster(k);
                                return true;
                            default:
                                return false;
                        }


                    }
                });




                popupMenu.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return kisilerList.size();
    }


    public class CardTasarimTutucu extends RecyclerView.ViewHolder {
        private TextView textViewKisiBilgi;
        private ImageView imageViewNokta;


        public CardTasarimTutucu(@NonNull View itemView) {
            super(itemView);

            textViewKisiBilgi = itemView.findViewById(R.id.textViewKisiBilgi);
            imageViewNokta = itemView.findViewById(R.id.imageViewNokta);


        }
    }


    public void alertGoster(Kisiler k){
        LayoutInflater layout = LayoutInflater.from(mContext);
        View tasarim = layout.inflate(R.layout.alert_tasarim,null);

        EditText editTextName = tasarim.findViewById(R.id.editTextName);
        EditText editTextPhone = tasarim.findViewById(R.id.editTextPhone);

        editTextName.setText(k.getKisi_ad());
        editTextPhone.setText(k.getKisi_tel());

        AlertDialog.Builder ad = new AlertDialog.Builder(mContext);
        ad.setTitle("Update the contact");
        ad.setView(tasarim);
        ad.setPositiveButton("Upddate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String kisi_ad = editTextName.getText().toString().trim();
                String kisi_tel = editTextPhone.getText().toString().trim();

                /*new KisilerDao().kisiGuncelle(vt,k.getKisi_id(),kisi_ad,kisi_tel);

                kisilerList = new KisilerDao().tumKisiler(vt);*/

                //notifyDataSetChanged();

                kisiGuncelle(k.getKisi_id(),kisi_ad,kisi_tel);



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


    public void kisiSil(int kisi_id){

        String url = "http://kasimadalan.pe.hu/kisiler/delete_kisiler.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("kisiSil Cavab",response);

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
                params.put("kisi_id",String.valueOf(kisi_id));

                return params;
            }
        };


        Volley.newRequestQueue(mContext).add(stringRequest);


    }



    public void kisiGuncelle(int kisi_id,String kisi_ad,String kisi_tel){

        String url = "http://kasimadalan.pe.hu/kisiler/update_kisiler.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("kisiGuncelle Cavab",response);

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
                params.put("kisi_id",String.valueOf(kisi_id));
                params.put("kisi_ad",kisi_ad);
                params.put("kisi_tel",kisi_tel);
                return params;
            }
        };

        Volley.newRequestQueue(mContext).add(stringRequest);

    }



    public void tumKisiler(){



        String url = "http://kasimadalan.pe.hu/kisiler/tum_kisiler.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                kisilerList.clear();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray kisiler = jsonObject.getJSONArray("kisiler");

                    for (int i = 0; i<kisiler.length(); i++) {

                        JSONObject k = kisiler.getJSONObject(i);

                        int kisi_id = k.getInt("kisi_id");
                        String kisi_ad = k.getString("kisi_ad");
                        String kisi_tel = k.getString("kisi_tel");

                        Kisiler kisi = new Kisiler(kisi_id,kisi_ad,kisi_tel);

                        kisilerList.add(kisi);

                    }

                    notifyDataSetChanged();



                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(mContext).add(stringRequest);

    }

}
