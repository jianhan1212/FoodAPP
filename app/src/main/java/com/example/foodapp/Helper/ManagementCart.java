package com.example.foodapp.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.foodapp.Domain.FoodDomain;
import com.example.foodapp.Interface.ChangNumberItemsListener;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB =new TinyDB(context);
    }
    public void insertFood(FoodDomain item){
        ArrayList<FoodDomain>listFood=getListCart();
        boolean existAlready=false;
        int n=0;
        for(int i=0;i<listFood.size();i++){
            if(listFood.get(i).getTitle().equals(item.getTitle())){
                existAlready=true;
                n=i;
                break;
            }
        }

        if(existAlready){
            listFood.get(n).setNumberInCart(item.getNumberInCart());
        }else {
            listFood.add(item);
        }
        tinyDB.putListObject("CartList",listFood);
        Toast.makeText(context, "Added To Your Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<FoodDomain>getListCart(){
        return tinyDB.getListObject("CartList");
    }

    public void plusNumberFood(ArrayList<FoodDomain>listFood, int postion, ChangNumberItemsListener changNumberItemsListener){
        listFood.get(postion).setNumberInCart(listFood.get(postion).getNumberInCart()+1);
        tinyDB.putListObject("CartList",listFood);
        changNumberItemsListener.changed();
    }

    public void minusNumberFood(ArrayList<FoodDomain>listFood, int postion, ChangNumberItemsListener changNumberItemsListener){
        if(listFood.get(postion).getNumberInCart()==1){
            listFood.remove(postion);
        }else{
            listFood.get(postion).setNumberInCart(listFood.get(postion).getNumberInCart()-1);
        }
        tinyDB.putListObject("CartList",listFood);
        changNumberItemsListener.changed();
    }

    public Double getTotalFee(){
        ArrayList<FoodDomain> listfood=getListCart();
        double fee=0;
        for (int i=0;i<listfood.size();i++){
            fee=fee+(listfood.get(i).getFee()*listfood.get(i).getNumberInCart());
        }
        return fee;
    }


}
