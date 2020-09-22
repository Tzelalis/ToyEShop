package com.example.toyshishop;

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

public class ProductsAdminRecyclerViewAdapter extends RecyclerView.Adapter<ProductsAdminRecyclerViewAdapter.ViewHolder> implements Filterable {
    private ArrayList<Products> list = new ArrayList<Products>();
    private ArrayList<Products> listAll;
    private OnItemClickListener listener;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<Products> filteredList = new ArrayList<Products>();
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(listAll);
                }else{
                    for(Products product : listAll){
                        if (product.getProductName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            product.getTextProductId().toLowerCase().contains(constraint.toString().toLowerCase())){
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

    public interface OnItemClickListener {
        void onItemClick(Products item);
    }

    public ProductsAdminRecyclerViewAdapter(ArrayList<Products> list, OnItemClickListener listener) {
        this.list = list;
        this.listAll = new ArrayList<>(list);
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycling_view_product_admin_item, parent, false);
        ProductsAdminRecyclerViewAdapter.ViewHolder viewHolder = new ProductsAdminRecyclerViewAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position), listener);

        Products item = list.get(position);

        holder.idTextView.setText(item.getTextProductId());
        holder.nameTextView.setText(item.getProductName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView idTextView;
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.productAdminImageView);
            idTextView = itemView.findViewById(R.id.productAdminIdTextView);
            nameTextView = itemView.findViewById(R.id.productAdminNameTextView);
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
