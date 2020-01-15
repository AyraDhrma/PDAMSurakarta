package com.ayra.pdamsurakarta.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.entity.Inquiry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private String text;
    private SpannableString colorText;

    private Context context;
    private List<Inquiry.RespData> respDatas;
    private String product;

    public RecyclerViewAdapter(Context context, List<Inquiry.RespData> respDatas, String product) {
        this.context = context;
        this.respDatas = respDatas;
        this.product = product;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cv_post_paid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inquiry.RespData respData = respDatas.get(position);
        setView(respData, position, holder);

        ArrayList<String> periode = new ArrayList<>();
        // Checkbox Item
        holder.checkBox.setChecked(respData.isSelected());
        holder.checkBox.setTag(respDatas.get(position));

        holder.checkBox.setOnClickListener(view -> {
            int total = 0;
            int totalTagihan = 0;
            Inquiry.RespData data1 = (Inquiry.RespData) holder.checkBox.getTag();
            data1.setSelected(holder.checkBox.isChecked());
            respDatas.get(position).setSelected(holder.checkBox.isChecked());
            periode.clear();
            for (int i = 0; i < respDatas.size(); i++) {
                if (respDatas.get(i).isSelected()) {
                    total += respDatas.get(i).getTotal();
                    totalTagihan += respDatas.get(i).getTagihan();
                    periode.add(respDatas.get(i).getBlth());
                }
            }
            Intent intent = new Intent("total_price");
            intent.putExtra("total", total);
            intent.putExtra("total_tagihan", totalTagihan);
            intent.putExtra("periode", periode);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        });

    }

    // Set View
    @SuppressLint("SetTextI18n")
    private void setView(Inquiry.RespData respData, int position, ViewHolder holder) {
        if (product.equals("bpjs")) {
            holder.month.setText("# Data " + (position + 1));
            text = "\tCust ID  : " + respData.getCustno() + "\n" +
                    "\tNama     : " + respData.getCustname() + "\n" +
                    "\tTagihan  : " + "Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getAmount());

            colorText = new SpannableString(text);
            colorText.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5722")), text.indexOf(respData.getCustno()), text.indexOf(respData.getCustno()) + respData.getCustno().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            colorText.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5722")), text.indexOf(respData.getCustname()), text.indexOf(respData.getCustname()) + respData.getCustname().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            colorText.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5722")), text.indexOf("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getAmount())), text.indexOf("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getAmount())) + ("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getAmount())).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        } else {
            holder.month.setText(respData.getBlth());
            text = "\tTagihan  : " + "Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTagihan()) + "\n" +
                    "\tAdmin    : " + "Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getAdmin()) + "\n" +
                    "\tTotal    : " + "Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTotal());

            colorText = new SpannableString(text);
            colorText.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5722")), text.indexOf("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTagihan())), text.indexOf("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTagihan())) + ("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTagihan())).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            colorText.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5722")), text.indexOf("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getAdmin())), text.indexOf("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getAdmin())) + ("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getAdmin())).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            colorText.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5722")), text.indexOf("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTotal())), text.indexOf("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTotal())) + ("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTotal())).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.secondData.setText(colorText);
    }

    @Override
    public int getItemCount() {
        return respDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.inquiry_checkbox)
        CheckBox checkBox;
        @BindView(R.id.bulan)
        TextView month;
        @BindView(R.id.secondData)
        TextView secondData;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
