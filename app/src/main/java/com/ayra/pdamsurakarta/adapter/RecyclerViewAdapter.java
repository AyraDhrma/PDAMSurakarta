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
            String s = "";
            Inquiry.RespData data1 = (Inquiry.RespData) holder.checkBox.getTag();
            data1.setSelected(holder.checkBox.isChecked());
            respDatas.get(position).setSelected(holder.checkBox.isChecked());
            periode.clear();
            for (int i = 0; i < respDatas.size(); i++) {
                if (respDatas.get(i).isSelected()) {
                    total += respDatas.get(i).getTotal();
                    totalTagihan += respDatas.get(i).getTagihan() + Integer.parseInt(respDatas.get(i).getDenda());
                    periode.add(respDatas.get(i).getBlth());
                    s = periode.toString().replace("[", "['").replace("]", "']").replace(" ", "").replace(",", "','");
                }
            }
            Intent intent = new Intent("total_price");
            intent.putExtra("total", total);
            intent.putExtra("total_tagihan", totalTagihan);
            intent.putExtra("periode", s);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        });

    }

    // Set View
    @SuppressLint("SetTextI18n")
    private void setView(Inquiry.RespData respData, int position, ViewHolder holder) {
        holder.month.setText(respData.getBlth());
        int denda = Integer.parseInt(respData.getDenda());
        holder.tagihan.setText("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTagihan()));
        holder.denda.setText("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(denda));
        holder.admin.setText("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getAdmin()));
        holder.total.setText("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(respData.getTotal()));
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
        @BindView(R.id.tagihan_content)
        TextView tagihan;
        @BindView(R.id.denda_content)
        TextView denda;
        @BindView(R.id.admin_content)
        TextView admin;
        @BindView(R.id.total_content)
        TextView total;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
