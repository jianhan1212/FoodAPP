package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.foodapp.Domain.OrderFood;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OrderHistory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth fAuth;
    private String userId;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);


        recyclerView =findViewById(R.id.historyList);
        firebaseFirestore=FirebaseFirestore.getInstance();
        fAuth=FirebaseAuth.getInstance();
        userId=fAuth.getUid();

        //Query
        Query query=firebaseFirestore.collection("orderhistory").document(userId).collection("detail");
        //RecyclerOptions
        FirestoreRecyclerOptions<OrderFood> options = new FirestoreRecyclerOptions.Builder<OrderFood>()
                .setQuery(query, OrderFood.class)
                .build();
        adapter= new FirestoreRecyclerAdapter<OrderFood, OrderFoodViewHolder>(options) {
            @NonNull
            @Override
            public OrderFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
                return new OrderFoodViewHolder(v);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderFoodViewHolder holder, int position, @NonNull OrderFood model) {
                holder.date.setText(model.getDate());
                holder.price.setText(model.getPrice());
            }
        };
        //view Holder
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        buttonNavigation();
    }

    private void buttonNavigation(){
        FloatingActionButton floatingActionButton=findViewById(R.id.cartBtn);
        LinearLayout homeBtn=findViewById(R.id.homBtn);  //homeBtn
        LinearLayout profileBtn=findViewById(R.id.profileBtn);  //profileBtn
        LinearLayout supportBtn=findViewById(R.id.supportBtn);  //supportBtn
        LinearLayout settingBtn=findViewById(R.id.settingBtn);  //settingBtn

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderHistory.this,CartListActivity.class));
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderHistory.this,MainActivity.class));
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderHistory.this,Login.class));
            }
        });

        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderHistory.this,OrderHistory.class));
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderHistory.this,SetImage.class));
            }
        });

    }



    private class OrderFoodViewHolder extends RecyclerView.ViewHolder{
        private TextView date,price;
        public OrderFoodViewHolder(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.history_date);
            price=itemView.findViewById(R.id.history_price);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}