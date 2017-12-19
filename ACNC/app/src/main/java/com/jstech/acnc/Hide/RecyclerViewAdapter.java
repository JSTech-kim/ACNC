package com.jstech.acnc.Hide;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jstech.acnc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by SONY on 2017-03-05.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static ArrayList<MainData> mDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mtv_Name;
        public TextView mtv_StartDate;
        public TextView mtv_ExpireDate;
        public ImageView miv_Restore;
        public Context mContext;
        public TextView mtv_NoList;

        public ViewHolder(View view)
        {
            super(view);
            view.setOnClickListener(this);

            mtv_Name = (TextView)view.findViewById(R.id.tv_name);
            mtv_StartDate = (TextView)view.findViewById(R.id.tv_start);
            mtv_ExpireDate = (TextView)view.findViewById(R.id.tv_expire);
            miv_Restore = (ImageView)view.findViewById(R.id.iv_restore);
        }

        @Override
        public void onClick(View v) {

            mContext = v.getContext();

            if(miv_Restore.getTag().equals("Restore"))
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle(mtv_Name.getText().toString());

                alertDialogBuilder.setMessage("연락처를 복구하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("복구", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Thread thread = new Thread(new Runnable(){
                                    @Override
                                    public void run() {
                                        int index = (int)mtv_Name.getTag();
                                        RestoreContact(index);
                                        DeleteFromDB(index);
                                    }
                                });

                                thread.run();
                                try {
                                    thread.join();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(mContext, HideActivity.class);
                                mContext.startActivity(intent);
                                ((Activity)mContext).finish();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else
            {
                Toast.makeText(mContext, "아직 복구가능 시간이 아닙니다.", Toast.LENGTH_SHORT).show();
            }
        }

        public void RestoreContact(int position)
        {
            MainData UserData = mDataSet.get(position);
            String name = UserData.strName;
            String phone = UserData.strPhone;
            String email = UserData.strEmail;

            ArrayList<ContentProviderOperation> list = new ArrayList<>();
            try{
                list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build()
                );

                list.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)   //이름
                                .build()
                );

                list.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone)           //전화번호
                                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE  , ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)   //번호타입(Type_Mobile : 모바일)
                                .build()
                );

                list.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                                .withValue(ContactsContract.CommonDataKinds.Email.DATA  , email)  //이메일
                                .withValue(ContactsContract.CommonDataKinds.Email.TYPE  , ContactsContract.CommonDataKinds.Email.TYPE_WORK)     //이메일타입(Type_Work : 직장)
                                .build()
                );

                mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, list);
                list.clear();

            }catch(RemoteException e){
                e.printStackTrace();
            }catch(OperationApplicationException e){
                e.printStackTrace();
            }
        }

        public void DeleteFromDB(int index)
        {
            MainData UserData = mDataSet.get(index);
            String name = UserData.strName;
            String phone = UserData.strPhone;

            String strQuery = "delete from acnc_table where name = '" + name + "' and phone = '" + phone + "';";

            Log.i("Query", strQuery);
            try{
                SQLiteDatabase database = mContext.openOrCreateDatabase("acnc_db", MODE_PRIVATE, null);
                database.execSQL(strQuery);
                database.close();

            }catch(SQLiteException se)
            {
                Toast.makeText(mContext, "데이터베이스에서 삭제하지 못했습니다.", Toast.LENGTH_SHORT).show();
                se.printStackTrace();
            }
        }
    }

    public RecyclerViewAdapter(ArrayList<MainData> mainDataSet)
    {
        mDataSet = mainDataSet;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_cardview, parent, false);

        ViewHolder viewholder = new ViewHolder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mtv_Name.setTag(position);
        holder.mtv_Name.setText(mDataSet.get(position).strName);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        holder.mtv_StartDate.setText(dateformat.format(mDataSet.get(position).StartDate));
        holder.mtv_ExpireDate.setText(dateformat.format(mDataSet.get(position).ExpireDate));

        Date date = new Date(System.currentTimeMillis());
        String str_nowdate = dateformat.format(date);
        String str_expire = dateformat.format(mDataSet.get(position).ExpireDate);

        Date date_now = null, date_expire = null;
        try{
            date_now = dateformat.parse(str_nowdate);
            date_expire = dateformat.parse(str_expire);
        }catch(ParseException e)
        {
            e.printStackTrace();
        }

        int compare = date_now.compareTo(date_expire);
        //date_now > date_expire
        if(compare >= 0){
            holder.miv_Restore.setImageResource(R.drawable.ic_restore_act);
            holder.miv_Restore.setTag("Restore");
        }
        else
        {
            holder.miv_Restore.setImageResource(R.drawable.ic_restore_unact);
            holder.miv_Restore.setTag("Restore_Yet");
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

class MainData{
    public String strName;
    public String strPhone;
    public String strEmail;
    public Date StartDate;
    public Date ExpireDate;

    public MainData( String strName, String strPhone, String strEmail, Date StartDate, Date ExpireDate)
    {
        this.strName = strName;
        this.strPhone = strPhone;
        this.strEmail = strEmail;
        this.StartDate = StartDate;
        this.ExpireDate = ExpireDate;
    }
}
