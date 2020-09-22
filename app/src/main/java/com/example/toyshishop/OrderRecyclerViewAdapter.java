package com.example.toyshishop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.toyshishop.data.entities.OrdersAndCustomers;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> implements Filterable {

    private ArrayList<OrdersAndCustomers> list;
    private ArrayList<OrdersAndCustomers> listAll;
    private OrderRecyclerViewAdapter.OnItemClickListener listener;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<OrdersAndCustomers> filteredList = new ArrayList<OrdersAndCustomers>();
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(listAll);
                }else{
                    for(OrdersAndCustomers ordersAndCustomers : listAll){
                        if (String.valueOf(ordersAndCustomers.getOrder().getOrderId()).toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                ordersAndCustomers.getCustomer().getLastName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                ordersAndCustomers.getCustomer().getFirstName().toLowerCase().contains(constraint.toString().toLowerCase())){
                            filteredList.add(ordersAndCustomers);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<OrdersAndCustomers>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener{
        public void onItemClick(OrdersAndCustomers item);
    }

    public OrderRecyclerViewAdapter(ArrayList<OrdersAndCustomers> list, OrderRecyclerViewAdapter.OnItemClickListener listener) {
        this.list = list;
        this.listAll = new ArrayList<>(list);
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycler_view_order_item, parent, false);
        OrderRecyclerViewAdapter.ViewHolder viewHolder = new OrderRecyclerViewAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position), listener);

        OrdersAndCustomers orderAndCustomer = list.get(position);
        holder.lastnameTextView.setText(orderAndCustomer.getCustomer().getLastName());
        holder.firstnameTextView.setText(orderAndCustomer.getCustomer().getFirstName());
        holder.orderIdTextView.setText(String.valueOf(orderAndCustomer.getOrder().getOrderId()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView firstnameTextView;
        TextView lastnameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderIdTextView = itemView.findViewById(R.id.recyclerViewOrderIdTextBox);
            firstnameTextView = itemView.findViewById(R.id.recyclerViewCustomerFirstnameTextBox);
            lastnameTextView = itemView.findViewById(R.id.recyclerViewCustomerLastnameTextBox);
        }


        public void bind(final OrdersAndCustomers item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
