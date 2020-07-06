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
import com.mobile.pmmp.model.Jadwal;

import java.util.List;

public class TableAdapterStatusMesin extends RecyclerView.Adapter {
    private Context mContext;
    private List<Jadwal> jadwalList;
    int no = 0;
    private RecyclerViewClickListener mListener;

    public TableAdapterStatusMesin(List<Jadwal> jadwalList, Context context, RecyclerViewClickListener listener) {
        this.jadwalList = jadwalList;
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.status_mesin_items, parent, false);

        return new RowViewHolder(itemView, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {

            rowViewHolder.txtKode.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtNo.setBackgroundResource(R.drawable.table_header_cell_bg);
            rowViewHolder.txtStatus.setBackgroundResource(R.drawable.table_header_cell_bg);

            rowViewHolder.txtKode.setTextColor(Color.WHITE);
            rowViewHolder.txtNo.setTextColor(Color.WHITE);
            rowViewHolder.txtStatus.setTextColor(Color.WHITE);

            rowViewHolder.txtKode.setText("Kode");
            rowViewHolder.txtNo.setText("No");
            rowViewHolder.txtStatus.setText("Status");
        } else {
            Jadwal modal = jadwalList.get(rowPos - 1);

            rowViewHolder.txtKode.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtNo.setBackgroundResource(R.drawable.table_content_cell_bg);
            rowViewHolder.txtStatus.setBackgroundResource(R.drawable.table_content_cell_bg);

            rowViewHolder.txtKode.setTextColor(Color.BLACK);
            rowViewHolder.txtNo.setTextColor(Color.BLACK);
            rowViewHolder.txtStatus.setTextColor(Color.BLACK);
            no++;
            rowViewHolder.txtKode.setText(modal.getKode_jadwa() + "");
            rowViewHolder.txtNo.setText(""+no);
            if(modal.getStatus_mesin().equals("0")){
                rowViewHolder.txtStatus.setText("Belum");
            }else {
                rowViewHolder.txtStatus.setText("Sudah");
            }

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
        TextView txtKode;
        TextView txtNo;
        TextView txtStatus;
        ConstraintLayout rvItemsStatusMesin;
        TableAdapterStatusMesin.RecyclerViewClickListener mListener;

        RowViewHolder(View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            txtKode = itemView.findViewById(R.id.txt_kode);
            txtNo = itemView.findViewById(R.id.txt_no);
            txtStatus = itemView.findViewById(R.id.txt_status);
            rvItemsStatusMesin = itemView.findViewById(R.id.rv_item_status_mesin);
            mListener = listener;
            rvItemsStatusMesin.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rv_item_status_mesin:
                    mListener.onRowClick(rvItemsStatusMesin, getAdapterPosition());
                    break;

                default:
                    break;
            }
        }
    }
}
