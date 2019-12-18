package com.ayra.pdamsurakarta.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayra.pdamsurakarta.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {

    private static final int MSG_DATA = 0;
    private static final int DATE_DIVIDER = 1;
    private ArrayList<Object> data;
    private LayoutInflater inflater;
    private TextView title, from, time, header, newMessage;

    public ListViewAdapter(Context context, ArrayList<Object> data) {
        this.data = data;
        this.inflater = (LayoutInflater) context.getSystemService((Context.LAYOUT_INFLATER_SERVICE));
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        String[] split = getItem(i).toString().split("\\|");
        switch (type) {
            case DATE_DIVIDER:
                view = inflater.inflate(R.layout.item_report, viewGroup, false);
                header = view.findViewById(R.id.header);
                header.setText(split[0]);
                break;
            case MSG_DATA:
                view = inflater.inflate(R.layout.item_message, viewGroup, false);
                title = view.findViewById(R.id.title);
                from = view.findViewById(R.id.from);
                time = view.findViewById(R.id.time);
                newMessage = view.findViewById(R.id.newMessage);

                title.setText(split[2]);
                title.setTypeface(null, Typeface.NORMAL);
                time.setText(split[1] + " lbr");
                from.setText("Total : " + "Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(Integer.valueOf(split[3])));
                newMessage.setVisibility(View.GONE);
                break;
        }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        String[] split = getItem(position).toString().split("\\|");
        if (split[0].substring(0, 4).equals("TELE")) {
            return MSG_DATA;
        }
        return DATE_DIVIDER;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == MSG_DATA);
    }

}
