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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.pmmp.R;
import com.mobile.pmmp.model.Jadwal;
import com.mobile.pmmp.model.Laporan;

import java.util.List;

public class TableAdapterRiwayatLaporan extends RecyclerView.Adapter {
    private Context mContext;
    private List<Laporan> laporanList;
    private RecyclerViewClickListener mListener;
    private String adapterTrigger;
    private int no = 0;

    public TableAdapterRiwayatLaporan(List<Laporan> laporanList, Context context, RecyclerViewClickListener listener,String adapterTrigger) {
        this.laporanList = laporanList;
        this.mContext = context;
        this.mListener = listener;
        this.adapterTrigger = adapterTrigger;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.riwayar_laporan_items, parent, false);

        return new RowViewHolder(itemView, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {

            rowViewHolder.txtNo.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtTanggal.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtKode.setBackgroundResource(R.drawable.table_header_cell_bg);

            rowViewHolder.txtNo.setTextColor(Color.WHITE);
            rowViewHolder.txtTanggal.setTextColor(Color.WHITE);
            rowViewHolder.txtKode.setTextColor(Color.WHITE);
            if(adapterTrigger.equals("laporan")){
                rowViewHolder.txtNo.setText("Kode");
                rowViewHolder.txtTanggal.setText("Tanggal");
                rowViewHolder.txtKode.setText("Nama");
            }else{
                rowViewHolder.txtNo.setText("No");
                rowViewHolder.txtTanggal.setText("Tanggal");
                rowViewHolder.txtKode.setText("Kode");
            }

        } else {
            Laporan modal = laporanList.get(rowPos - 1);
            no++;
            rowViewHolder.txtNo.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtTanggal.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtKode.setBackgroundResource(R.drawable.table_content_cell_bg);

            rowViewHolder.txtNo.setTextColor(Color.BLACK);
            rowViewHolder.txtTanggal.setTextColor(Color.BLACK);
            rowViewHolder.txtKode.setTextColor(Color.BLACK);
            if(adapterTrigger.equals("laporan")){
                rowViewHolder.txtNo.setText(modal.getKode());
                rowViewHolder.txtTanggal.setText(modal.getTanggal() + "");
                rowViewHolder.txtKode.setText(modal.getNama_petugas() + "");
            }else{
                rowViewHolder.txtNo.setText(""+no);
                rowViewHolder.txtTanggal.setText(modal.getTanggal() + "");
                rowViewHolder.txtKode.setText(modal.getKode() + "");
            }


        }
    }

    @Override
    public int getItemCount() {
        return laporanList == null ? 0 : laporanList.size() + 1;
    }

    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);
    }

    public class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNo;
        TextView txtTanggal;
        TextView txtKode;
        ConstraintLayout rvItemsRiwayat;
        TableAdapterRiwayatLaporan.RecyclerViewClickListener mListener;

        RowViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            txtNo = itemView.findViewById(R.id.txt_no);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);
            txtKode = itemView.findViewById(R.id.txt_kode);
            rvItemsRiwayat = itemView.findViewById(R.id.rv_item_riwayat_laporan);
            mListener = listener;
            rvItemsRiwayat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rv_item_riwayat_laporan:
                    mListener.onRowClick(rvItemsRiwayat, getAdapterPosition());
                    break;

                default:
                    break;
            }
        }
    }
}
