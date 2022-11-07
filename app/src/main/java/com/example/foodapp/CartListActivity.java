package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Adapter.CartListAdapter;
import com.example.foodapp.Domain.OrderFood;
import com.example.foodapp.Helper.ManagementCart;
import com.example.foodapp.Interface.ChangNumberItemsListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartListActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    TextView totalFeeTxt,taxTxt,deliveryTxt,totalTxt,emptyTxt;
    private double tax;
    private ScrollView scrollView;
    private TextView checkout;
    private FirebaseAuth fAuth;
    private FirebaseDatabase fdatabase;
    private FirebaseFirestore fStore;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        managementCart=new ManagementCart(this);
        fdatabase=FirebaseDatabase.getInstance();
        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        userId= fAuth.getUid();
//        if (fAuth.getCurrentUser() != null) {
            Calendar mCal = Calendar.getInstance();
            CharSequence s = DateFormat.format("yyyy-MM-dd kk:mm:ss", mCal.getTime()); //點餐確認當下時間
            initView();

            DatabaseReference myRef=fdatabase.getReference("Data");

            initList();

            CalculateCart();
            checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fAuth.getCurrentUser()!=null){
                        Toast.makeText(CartListActivity.this, "Already Order!", Toast.LENGTH_SHORT).show();
                        myRef.child(fAuth.getCurrentUser().getUid()).setValue(new OrderFood((String) s,totalTxt.getText().toString()));//data放到realtime database
                        Map<String,Object> user=new HashMap<>();
                        DocumentReference documentReference=fStore.collection("orderhistory").document(userId).collection("detail").document();
//                    user.put("user",userId);
                        user.put("date",s);
                        user.put("price",totalTxt.getText().toString());
                        documentReference.getParent().add(user);
                    }else {
                        Toast.makeText(CartListActivity.this, "Please Login First!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CartListActivity.this,Login.class));
                    }
                }
            });

//        }


        buttonNavigation();


    }

    private void buttonNavigation(){
        FloatingActionButton floatingActionButton=findViewById(R.id.cartBtn);
        LinearLayout homeBtn=findViewById(R.id.homBtn);
        LinearLayout profileBtn=findViewById(R.id.profileBtn);  //profileBtn
        LinearLayout supportBtn=findViewById(R.id.supportBtn);  //supportBtn
        LinearLayout settingBtn=findViewById(R.id.settingBtn);  //settingBtn

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this,CartListActivity.class));
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this,MainActivity.class));
            }
        });
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this,Login.class));
            }
        });

        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this,OrderHistory.class));
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartListActivity.this,SetImage.class));
            }
        });


    }

    private void initView() {
        recyclerViewList=findViewById(R.id.recyclerView);
        totalFeeTxt=findViewById(R.id.totalFeeTxt);
        taxTxt=findViewById(R.id.taxTxt);
        deliveryTxt=findViewById(R.id.deliveryTxt);
        totalTxt=findViewById(R.id.totalTxt);
        emptyTxt=findViewById(R.id.empty);
        scrollView=findViewById(R.id.scrollView3);
        recyclerViewList=findViewById(R.id.cartView);
        checkout=findViewById(R.id.textView_checkout);
    }
    private void  initList(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter=new CartListAdapter(managementCart.getListCart(), this, new ChangNumberItemsListener() {
            @Override
            public void changed() {
                CalculateCart();
            }
        });

        recyclerViewList.setAdapter(adapter);
        if(managementCart.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else{
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private  void CalculateCart(){
        double percentTax=0.02;
        double delivery=10;

        tax=Math.round((managementCart.getTotalFee()*percentTax)*100)/100;
        double total=Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100;
        double itemTotal=Math.round(managementCart.getTotalFee()*100)/100;

        totalFeeTxt.setText("$"+itemTotal);
        taxTxt.setText("$"+tax);
        deliveryTxt.setText("$"+delivery);
        totalTxt.setText("$"+total);
    }


}