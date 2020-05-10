package com.example.merakissan.Models;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class Orders {
    private String OrderBy;
    private String OrderFrom;
    private String ProductId;
    private String OrderStatus;
    private String CreatedDate;
    private String UpdateDate;


    public String getOrderBy() {
        return OrderBy;
    }

    public String getOrderFrom() {
        return OrderFrom;
    }

    public String getProductId() {
        return ProductId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }



    public Orders() {

    }


}
