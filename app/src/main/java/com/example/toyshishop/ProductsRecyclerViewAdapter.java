package com.example.toyshishop;

import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.toyshishop.data.entities.Products;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> implements Filterable {
    private ArrayList<Products> list;
    private ArrayList<Products> listAll;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Products item);
    }

    public ProductsRecyclerViewAdapter(ArrayList<Products> productsList, OnItemClickListener listener) {
        this.list = productsList;
        this.listAll = new ArrayList<>(productsList);
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Products> filteredList = new ArrayList<Products>();
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(listAll);
                } else {
                    for (Products product : listAll) {
                        if (product.getProductName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                product.getTextProductId().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(product);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<Products>) results.values;
                notifyDataSetChanged();
            }
        };
    }

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

        holder.bind(list.get(position), listener);

        final Products product = list.get(position);

        holder.idTextView.setText(product.getTextProductId());
        holder.descTextView.setText(product.getProductName());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));

        if (product.getOldPrice() > product.getPrice())
            holder.oldPriceTextView.setText(String.valueOf(product.getOldPrice()));

        holder.oldPriceTextView.setPaintFlags(holder.oldPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);   // strike the text of oldpriceTextView

        //set discount if exists
        if (product.getDiscount() != null) {
            holder.discountTextView.setText(product.getDiscount());
            holder.discountTextView.setVisibility(View.VISIBLE);
        } else {
            holder.discountTextView.setVisibility(View.INVISIBLE);
        }

        holder.imageView.getLayoutParams().height = 50;
        holder.imageView.requestLayout();

        if (product.getImagePath() != null) {
            holder.imageView.setImageURI(Uri.parse(product.getImagePath()));
        }

    }

    //return productsList elements count
    @Override
    public int getItemCount() {
        return list.size();
    }

    //ViewHolder which give us the custom list item (recycling_view_product_item)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView descTextView;
        TextView priceTextView;
        TextView oldPriceTextView;
        ImageView imageView;
        TextView idTextView;
        TextView discountTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.descTextView = itemView.findViewById(R.id.productDescTextView);
            this.priceTextView = itemView.findViewById(R.id.productPriceTextView);
            this.oldPriceTextView = itemView.findViewById(R.id.productOldPriceTextView);
            this.imageView = itemView.findViewById(R.id.productImageView);
            this.idTextView = itemView.findViewById(R.id.productIDTextView);
            this.discountTextView = itemView.findViewById(R.id.discountTextView);
        }

        public void bind(final Products item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
