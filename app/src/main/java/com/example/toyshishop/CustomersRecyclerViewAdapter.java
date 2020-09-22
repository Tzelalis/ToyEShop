package com.example.toyshishop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.toyshishop.data.entities.Customers;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomersRecyclerViewAdapter extends RecyclerView.Adapter<CustomersRecyclerViewAdapter.ViewHolder> implements Filterable {
    private ArrayList<Customers> list;
    private ArrayList<Customers> listAll;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Customers customer);
    }

    public CustomersRecyclerViewAdapter(ArrayList<Customers> list, OnItemClickListener listener) {
        this.list = list;
        this.listAll = new ArrayList<>(list);
        this.listener = listener;
    }

    //filter for search on list
    @Override
    public Filter getFilter() {
        return new Filter() {
            List<Customers> filteredList = new ArrayList<>();

            //runs on background thread
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(listAll);
                } else {
                    for (Customers customer : listAll) {
                        if (customer.getLastName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                customer.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                customer.getPhoneNumber().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                customer.getTextCustomerId().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(customer);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;

                return filterResults;
            }

            //run on ui thread
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<Customers>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public CustomersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycling_view_customer_item, parent, false);
        CustomersRecyclerViewAdapter.ViewHolder viewHolder = new CustomersRecyclerViewAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersRecyclerViewAdapter.ViewHolder holder, int position) {
        Customers c = list.get(position);

        holder.bind(c, listener);
        holder.id.setText(c.getTextCustomerId());
        holder.lastName.setText(c.getLastName());
        holder.firstName.setText(c.getFirstName());
        holder.phone.setText(c.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView firstName;
        public TextView lastName;
        public TextView phone;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById((R.id.rvCustomerItemIdTextBox));
            lastName = itemView.findViewById(R.id.rvCustomerItemLastnameTextBox);
            firstName = itemView.findViewById(R.id.rvCustomerItemFirstnameTextBox);
            phone = itemView.findViewById(R.id.rvCustomerItemPhoneTextBox);
        }

        void bind(final Customers item, final CustomersRecyclerViewAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
