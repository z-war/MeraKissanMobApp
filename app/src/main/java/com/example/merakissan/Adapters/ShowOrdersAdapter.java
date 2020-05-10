package com.example.merakissan.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merakissan.Models.Orders;
import com.example.merakissan.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.zip.Inflater;

public class ShowOrdersAdapter extends FirestoreRecyclerAdapter<Orders, ShowOrdersAdapter.AppViewHolder> {


    private OnItemClickListner listner;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public ShowOrdersAdapter(@NonNull FirestoreRecyclerOptions<Orders> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final AppViewHolder appViewHolder, int i, @NonNull Orders orders) {
        appViewHolder.order_from.setText("Order From : " + orders.getOrderBy());

        appViewHolder.order_date.setText("Order Date : " + orders.getCreatedDate());
        appViewHolder.status.setText("Status : " + orders.getOrderStatus());
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false);
        return new AppViewHolder(row);
    }

    class AppViewHolder extends RecyclerView.ViewHolder {

        TextView order_from, order_date, status;
        Button confirm;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            order_date = itemView.findViewById(R.id.order_dateTV);

            order_from = itemView.findViewById(R.id.customer_nameTV);
            status = itemView.findViewById(R.id.order_statusTV);
            confirm = itemView.findViewById(R.id.confirm_btn);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && listner != null) {
                        listner.onItemClick(getSnapshots().getSnapshot(pos), pos);

                    }
                }
            });
        }
    }

    public interface OnItemClickListner {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }


    public void setOnItemClickListner(OnItemClickListner listner) {
        this.listner = listner;

    }


}
