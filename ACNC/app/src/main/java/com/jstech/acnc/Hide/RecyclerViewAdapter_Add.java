package com.jstech.acnc.Hide;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jstech.acnc.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by SONY on 2017-03-19.
 */

public class RecyclerViewAdapter_Add extends RecyclerView.Adapter<RecyclerViewAdapter_Add.ViewHolder> {

    private ArrayList<AddData> mAddDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mtv_Name;
        public TextView mtv_PhoneNumber;
        public TextView mtv_EmailAddr;

        public ViewHolder(View view)
        {
            super(view);
            view.setOnClickListener(this);

            mtv_Name = (TextView)view.findViewById(R.id.tv_name_add);
            mtv_PhoneNumber = (TextView)view.findViewById(R.id.tv_phone_add);
            mtv_EmailAddr = (TextView)view.findViewById(R.id.tv_email_add);
        }

        //jinsub.kim//  숨김 및 차단에 대한 정보 설정 페이지로 이동.
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(v.getContext(), SetHideInfo.class);
            intent.putExtra("ID", (String)mtv_Name.getTag());
            intent.putExtra("NAME", mtv_Name.getText());
            intent.putExtra("PHONE", mtv_PhoneNumber.getText());
            intent.putExtra("EMAIL", mtv_EmailAddr.getText());

            v.getContext().startActivity(intent);
            ((Activity)v.getContext()).finish();
        }
    }

    public RecyclerViewAdapter_Add(ArrayList<AddData> mainDataSet)
    {
        mAddDataSet = mainDataSet;
    }

    @Override
    public RecyclerViewAdapter_Add.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_cardview_add, parent, false);
        RecyclerViewAdapter_Add.ViewHolder viewholder = new RecyclerViewAdapter_Add.ViewHolder(v);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter_Add.ViewHolder holder, int position) {

        if(mAddDataSet.get(position).strName != null)
        {
            holder.mtv_Name.setText(mAddDataSet.get(position).strName);
            holder.mtv_Name.setTag(mAddDataSet.get(position).strId);
        }
        else
        {
            holder.mtv_Name.setText("없음");
        }

        if(mAddDataSet.get(position).strPhoneNumber != null)
        {
            holder.mtv_PhoneNumber.setText(mAddDataSet.get(position).strPhoneNumber);
        }
        else
        {
            holder.mtv_PhoneNumber.setText("없음");
        }

        if(mAddDataSet.get(position).strMailAddr != null)
        {
            holder.mtv_EmailAddr.setText(mAddDataSet.get(position).strMailAddr);
        }
        else
        {
            holder.mtv_EmailAddr.setText("없음");
        }

    }

    @Override
    public int getItemCount() {
        return mAddDataSet.size();
    }
}
class AddData implements Serializable{

    public String strId;
    public String strName;
    public String strPhoneNumber;
    public String strMailAddr;

    public AddData(String strId, String strName, String strPhoneNumber, String strMailAddr)
    {
        this.strId = strId;
        this.strName = strName;
        this.strPhoneNumber = strPhoneNumber;
        this.strMailAddr = strMailAddr;
    }
}