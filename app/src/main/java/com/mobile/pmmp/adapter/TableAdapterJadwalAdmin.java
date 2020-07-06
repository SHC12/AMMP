package com.mobile.pmmp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.pmmp.R;
import com.mobile.pmmp.model.Jadwal;

import java.util.List;

public class TableAdapterJadwalAdmin extends RecyclerView.Adapter {
    private Context mContext;
    private List<Jadwal> jadwalList;
    private RecyclerViewClickListener mListener;

    public TableAdapterJadwalAdmin(List<Jadwal> jadwalList, Context context, RecyclerViewClickListener listener) {
        this.jadwalList = jadwalList;
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.jadwal_petugas_admin_items, parent, false);

        return new RowViewHolder(itemView, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {

            rowViewHolder.txtNo.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtNama.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtLokasi.setBackgroundResource(R.drawable.table_header_cell_bg);

            rowViewHolder.txtNo.setTextColor(Color.WHITE);
            rowViewHolder.txtNama.setTextColor(Color.WHITE);
            rowViewHolder.txtLokasi.setTextColor(Color.WHITE);

            rowViewHolder.txtNo.setText("No");
            rowViewHolder.txtNama.setText("Nama");
            rowViewHolder.txtLokasi.setText("Lokasi");
        } else {
            Jadwal modal = jadwalList.get(rowPos - 1);

            rowViewHolder.txtNo.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtNama.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtLokasi.setBackgroundResource(R.drawable.table_content_cell_bg);

            rowViewHolder.txtNo.setTextColor(Color.BLACK);
            rowViewHolder.txtNama.setTextColor(Color.BLACK);
            rowViewHolder.txtLokasi.setTextColor(Color.BLACK);
            rowViewHolder.txtNo.setText(modal.getKode_jadwa() + "");
            rowViewHolder.txtNama.setText(modal.getNama() + "");
            rowViewHolder.txtLokasi.setText(modal.getLokasi() + "");

        }
    }

    @Override
    public int getItemCount() {
        return jadwalList.size() + 1;
    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);
    }

    public class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNo;
        TextView txtNama;
        TextView txtLokasi;
        RelativeLayout rvItemsJadwal;
        TableAdapterJadwalAdmin.RecyclerViewClickListener mListener;

        RowViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            txtNo = itemView.findViewById(R.id.txt_no);
            txtNama = itemView.findViewById(R.id.txt_nama);
            txtLokasi = itemView.findViewById(R.id.txt_lokasi);
            rvItemsJadwal = itemView.findViewById(R.id.rv_item_jadwal_admin);
            mListener = listener;
            rvItemsJadwal.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rv_item_jadwal_admin:
                    mListener.onRowClick(rvItemsJadwal, getAdapterPosition());
                    break;

                default:
                    break;
            }
        }
    }
}
