package com.example.foodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodapp.Adapter.CategoryAdapter;
import com.example.foodapp.Adapter.PopularAdapter;
import com.example.foodapp.Domain.CategoryDomain;
import com.example.foodapp.Domain.FoodDomain;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter,adapter2;
    private RecyclerView recyclerViewCategoryList,recyclerViewPopularList;
    TextView loginname;
    FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userId;
    ImageButton profileImage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginname=findViewById(R.id.textView_loginname);
        fAuth=FirebaseAuth.getInstance(); //user
        fStore= FirebaseFirestore.getInstance(); //userreference
        storageReference= FirebaseStorage.getInstance().getReference(); //profileimage
        profileImage=findViewById(R.id.image_logout);
        profileImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        userId= fAuth.getUid();

        if(fAuth.getCurrentUser()!=null){

            initView();
        }

        recyclerViewCategory();
        recyclerViewPopular();
        buttonNavigation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=fAuth.getCurrentUser();
        if(currentUser!=null){
            reload();
        }
    }



    public void logout(View view){
        Toast.makeText(this, "Log out", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
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
                startActivity(new Intent(MainActivity.this,CartListActivity.class));
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Login.class));
            }
        });

        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,OrderHistory.class));
            }
        });

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SetImage.class));
            }
        });

    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewCategoryList=findViewById(R.id.recyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> category=new ArrayList<>();
        category.add(new CategoryDomain("Pizza","cat_1"));
        category.add(new CategoryDomain("Burger","cat_2"));
        category.add(new CategoryDomain("Hotdog","cat_3"));
        category.add(new CategoryDomain("Drink","cat_4"));
        category.add(new CategoryDomain("Donut","cat_5"));

        adapter=new CategoryAdapter(category);
        recyclerViewCategoryList.setAdapter(adapter);
    }

    private void recyclerViewPopular(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPopularList=findViewById(R.id.recyclerView2);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);

        ArrayList<FoodDomain> foodList=new ArrayList<>();
        foodList.add(new FoodDomain("Pepperoni pizza","pizza1","slices pepperoni,mozzerella cheese,fresh orange,ground black pepper,pizza sauce",9.76));
        foodList.add(new FoodDomain("Cheese burger","burger","beef,Gouda Cheese,Special Sauce,Lettuce,tomato",8.79));
        foodList.add(new FoodDomain("Vegetable pizza","pizza2","olive oil,Vegetable oil,pitted kalata,cherry,pizza sauce",9.76));
        foodList.add(new FoodDomain("Black Coffee","menu1","來自阿拉比卡咖啡豆",2.3));
        foodList.add(new FoodDomain("Cappuccino","menu2","老闆用心拉出來的",6.4));
        foodList.add(new FoodDomain("Chowder","menu4","集結你聽過的所有蔬菜熬出來的美味濃湯",7.58));
        foodList.add(new FoodDomain("Spaghetti cuttlefish ink","menu6","深海烏賊所噴出來的汁，老闆特選捲心麵",9.22));

        adapter2=new PopularAdapter(foodList);
        recyclerViewPopularList.setAdapter(adapter2);
    }
    private void initView(){
        StorageReference profileRef=storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });
        DocumentReference documentReference=fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                loginname.setText(documentSnapshot.getString("fName"));
            }
        });
    }
    private void reload() { }

}