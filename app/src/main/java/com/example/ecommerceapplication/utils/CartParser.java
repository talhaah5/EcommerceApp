package com.example.ecommerceapplication.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;

import com.example.ecommerceapplication.models.Product;

import com.example.ecommerceapplication.global.Global;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CartParser {

    private static final String TAG = "CartParser";
    public JSONArray cartObject;
    SharedPrefs sharedPrefs;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public CartParser(Context context) {

        this.context = context;
        sharedPrefs = new SharedPrefs(context);
        if(sharedPrefs.getCart() != null)
            cartObject = sharedPrefs.getCart();
        else
            cartObject = new JSONArray();
    }

    public boolean addUpdateProduct(Product product) {
        boolean isExist = false;
        int index = -1;
        for(int i =0;i<cartObject.length();i++){
            try {
            JSONObject productObject = null;

                productObject = cartObject.getJSONObject(i);
            if(product.getId().equals(productObject.getString("productId"))) {
                isExist = true;
                index = i;
            }
                Global.Companion.getPRODUCT_HASHMAP().put(product.getId(),product.getQuantity());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //product already exist so only update the quantity
        if(isExist){
            try{
                JSONObject productObj = cartObject.getJSONObject(index);
                productObj.put("quantity",product.getQuantity());
                cartObject.put(index,productObj);
                sharedPrefs.updateCart(cartObject);
                return true;
            }catch (Exception e){
                Log.d(TAG, "addUpdateProduct: "+e);
            }
        }//product don't exist
        else{
           try{
               JSONObject productObj = new JSONObject();
               productObj.put("productId",product.getId());
               productObj.put("quantity",product.getQuantity());
               productObj.put("price",product.getPrice());
               productObj.put("title",product.getTitle());
               productObj.put("image",product.getImage());
               productObj.put("description",product.getDescription());
               cartObject.put(productObj);
               sharedPrefs.updateCart(cartObject);
               return true;
           }catch (Exception e){
               Log.d(TAG, "addUpdateProduct: "+e);
           }
        }
        return false;

    }

    public ArrayList<Product> getCartProducts(){
        ArrayList<Product> products = new ArrayList<>();
        for(int i =0;i<cartObject.length();i++){
           try{
               JSONObject productObj = cartObject.getJSONObject(i);
               Product product = new Product();
               product.setId(productObj.getString("productId"));
               product.setQuantity(productObj.getInt("quantity"));
               product.setPrice(productObj.getString("price"));
               product.setTitle(productObj.getString("title"));
               product.setImage(productObj.getString("image"));
               product.setDescription(productObj.getString("description"));
               products.add(product);
           }catch (Exception e){
               Log.d(TAG, "getCartProducts: "+e);
           }

        }
        return products;
    }


    public boolean removeProduct(Product product){
        boolean isExist = false;
        int index = -1;
        for(int i =0;i<cartObject.length();i++){
            try {
                JSONObject productObject = null;

                productObject = cartObject.getJSONObject(i);
                if(product.getId().equals(productObject.getString("productId"))) {
                    isExist = true;
                    index = i;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(isExist){
            cartObject.remove(index);
            sharedPrefs.updateCart(cartObject);
            Global.Companion.getPRODUCT_HASHMAP().remove(product.getId());
            return true;
        }else return false;
    }
    public void getHashMapOfProduct() {
        try {
             HashMap<String, Integer> hashMap = new  HashMap<String, Integer>();
            for(int i =0;i<cartObject.length();i++){
                try {
                    JSONObject productObject = cartObject.getJSONObject(i);
                        hashMap.put(productObject.getString("productId"),productObject.getInt("quantity"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Global.Companion.setPRODUCT_HASHMAP(hashMap);

        } catch (Exception e) {
            Log.d(TAG, "getHashMapOfProduct: "+e);
        }

    }

}
