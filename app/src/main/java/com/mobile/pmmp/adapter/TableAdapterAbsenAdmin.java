package com.mobile.pmmp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.pmmp.R;
import com.mobile.pmmp.model.Absen;
import com.mobile.pmmp.model.Jadwal;

import java.util.List;

public class TableAdapterAbsenAdmin extends RecyclerView.Adapter {
    private Context mContext;
    private List<Jadwal> absenList;
    private RecyclerViewClickListener mListener;


    public TableAdapterAbsenAdmin(List<Jadwal> absenList, Context context,RecyclerViewClickListener listener) {
        this.absenList = absenList;
        this.mContext = context;
        this.mListener = listener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.absen_petugas_items, parent, false);

        return new RowViewHolder(itemView,mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {

            rowViewHolder.txtTanggal.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtNama.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtShift.setBackgroundResource(R.drawable.table_header_cell_bg);

            rowViewHolder.txtTanggal.setTextColor(Color.WHITE);
            rowViewHolder.txtNama.setTextColor(Color.WHITE);
            rowViewHolder.txtShift.setTextColor(Color.WHITE);

            rowViewHolder.txtTanggal.setText("Tanggal");
            rowViewHolder.txtNama.setText("Nama");
            rowViewHolder.txtShift.setText("Shift");
        } else {
            Jadwal modal = absenList.get(rowPos - 1);

            rowViewHolder.txtTanggal.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtNama.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtShift.setBackgroundResource(R.drawable.table_content_cell_bg);

            rowViewHolder.txtTanggal.setTextColor(Color.BLACK);
            rowViewHolder.txtNama.setTextColor(Color.BLACK);
            rowViewHolder.txtTanggal.setText(modal.getTanggal() + "");
            rowViewHolder.txtNama.setText(modal.getNama() + "");
            rowViewHolder.txtShift.setText(modal.getShift() + "");

        }
    }

    @Override
    public int getItemCount() {
        return absenList.size() + 1;
    }


    public interface RecyclerViewClickListener {
        void onRowClick(View view, int position);
    }

    public class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTanggal;
        TextView txtNama;
        TextView txtShift;
        ConstraintLayout rvItemsAbsen;
        RecyclerViewClickListener mListener;

        RowViewHolder(View itemView,RecyclerViewClickListener listener) {
            super(itemView);
            txtTanggal = itemView.findViewById(R.id.txt_tanggal);
            txtNama = itemView.findViewById(R.id.txt_nama);
            txtShift = itemView.findViewById(R.id.txt_shift);
            rvItemsAbsen = itemView.findViewById(R.id.rv_item_absen);
            mListener = listener;
            rvItemsAbsen.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rv_item_absen:
                    mListener.onRowClick(rvItemsAbsen, getAdapterPosition());
                    break;

                default:
                    break;
            }

        }
    }
}
