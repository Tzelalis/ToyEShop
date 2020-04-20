package com.example.toyshishop.UI;

import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toyshishop.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsRecyclerViewAdapter  extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder>{
    private ArrayList<Product> productsList;

    public ProductsRecyclerViewAdapter(ArrayList<Product> productsList) {
        this.productsList = productsList;
    }

    //
    @NonNull
    @Override
    public ProductsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycling_view_product_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsRecyclerViewAdapter.ViewHolder holder, int position) {
        final Product product = productsList.get(position);
        holder.descTextView.setText(product.getShortDescription());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));

        //these 2 lines strike the text of oldpriceTextView
        holder.oldpriceTextView.setText(String.valueOf(product.getOldPrice()));
        holder.oldpriceTextView.setPaintFlags(holder.oldpriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+ product.getName() ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //return productsList elements count
    @Override
    public int getItemCount() {
        return productsList.size();
    }

    //ViewHolder which give us the custom list item (recycling_view_product_item)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descTextView;
        public TextView priceTextView;
        public TextView oldpriceTextView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.descTextView = (TextView) itemView.findViewById(R.id.productDescTextView);
            this.priceTextView = itemView.findViewById(R.id.productPriceTextView);
            this.oldpriceTextView = itemView.findViewById(R.id.productOldPriceTextView);

            this.relativeLayout = (RelativeLayout)itemView.findViewById(R.id.rv_product_item_layout);
        }
    }
}
