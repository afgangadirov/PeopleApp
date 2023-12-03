/*
package com.example.kisileruygulamasi;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class KisilerDao {

    public ArrayList<Kisiler> tumKisiler(Veritabani vt){
        ArrayList<Kisiler> kisilerArrayList = new ArrayList<>();
        SQLiteDatabase db = vt.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM kisiler",null);

        while (c.moveToNext()){
            Kisiler k = new Kisiler(c.getInt(c.getColumnIndex("kisi_id"))
            ,c.getString(c.getColumnIndex("kisi_ad"))
            ,c.getString(c.getColumnIndex("kisi_tel")));

            kisilerArrayList.add(k);
        }
        db.close();
        return kisilerArrayList;

    }




    public ArrayList<Kisiler> kisilerAra(Veritabani vt, String kelimeAra){
        ArrayList<Kisiler> kisilerArrayList = new ArrayList<>();
        SQLiteDatabase db = vt.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM kisiler WHERE kisi_ad LIKE '%"+kelimeAra+"%'",null);

        while (c.moveToNext()){
            Kisiler k = new Kisiler(c.getInt(c.getColumnIndex("kisi_id"))
                    ,c.getString(c.getColumnIndex("kisi_ad"))
                    ,c.getString(c.getColumnIndex("kisi_tel")));

            kisilerArrayList.add(k);
        }
        db.close();
        return kisilerArrayList;

    }



    public void kisiSil(Veritabani vt, int kisi_id){
        SQLiteDatabase db = vt.getWritableDatabase();
        db.delete("kisiler","kisi_id=?", new String[]{String.valueOf(kisi_id)});
        db.close();
    }

    public void kisiEkle(Veritabani vt, String kisi_ad,String kisi_tel){
        SQLiteDatabase db = vt.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("kisi_ad",kisi_ad);
        values.put("kisi_tel",kisi_tel);

        db.insertOrThrow("kisiler",null,values);
        db.close();
    }


    public void kisiGuncelle(Veritabani vt, int kisi_id,String kisi_ad,String kisi_tel){
        SQLiteDatabase db = vt.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("kisi_ad",kisi_ad);
        values.put("kisi_tel",kisi_tel);

        db.update("kisiler",values,"kisi_id=?",new String[]{String.valueOf(kisi_id)});
        db.close();
    }



}
*/
