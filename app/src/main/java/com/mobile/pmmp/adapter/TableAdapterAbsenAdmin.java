package com.mobile.pmmp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.pmmp.R;
import com.mobile.pmmp.model.Absen;

import java.util.List;

public class TableAdapterAbsenAdmin extends RecyclerView.Adapter {
    private Context mContext;
    private List<Absen> absenList;

    public TableAdapterAbsenAdmin(List<Absen> absenList, Context context) {
        this.absenList = absenList;
        this.mContext = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.riwayat_absen_admin_items, parent, false);

        return new RowViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {

            rowViewHolder.txtNo.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtNama.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtShift.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtStatus.setBackgroundResource(R.drawable.table_header_cell_bg);

            rowViewHolder.txtNo.setTextColor(Color.WHITE);
            rowViewHolder.txtNama.setTextColor(Color.WHITE);
            rowViewHolder.txtShift.setTextColor(Color.WHITE);
            rowViewHolder.txtStatus.setTextColor(Color.WHITE);

            rowViewHolder.txtNo.setText("No");
            rowViewHolder.txtNama.setText("Nama");
            rowViewHolder.txtShift.setText("Shift");
            rowViewHolder.txtStatus.setText("Status");
        } else {
            Absen modal = absenList.get(rowPos - 1);

            rowViewHolder.txtNo.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtNama.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtShift.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtStatus.setBackgroundResource(R.drawable.table_content_cell_bg);

            rowViewHolder.txtNo.setTextColor(Color.BLACK);
            rowViewHolder.txtNama.setTextColor(Color.BLACK);
            rowViewHolder.txtStatus.setTextColor(Color.BLACK);
            rowViewHolder.txtNo.setText(modal.getNo() + "");
            rowViewHolder.txtNama.setText(modal.getNama() + "");
            rowViewHolder.txtShift.setText(modal.getShift() + "");
            rowViewHolder.txtStatus.setText(modal.getStatus() + "");

        }
    }

    @Override
    public int getItemCount() {
        return absenList.size() + 1;
    }



    public class RowViewHolder extends RecyclerView.ViewHolder{
        TextView txtNo;
        TextView txtNama;
        TextView txtShift;
        TextView txtStatus;

        RowViewHolder(View itemView) {
            super(itemView);
            txtNo = itemView.findViewById(R.id.txt_no);
            txtNama = itemView.findViewById(R.id.txt_nama);
            txtShift = itemView.findViewById(R.id.txt_shift);
            txtStatus = itemView.findViewById(R.id.txt_status);
        }


    }
}
