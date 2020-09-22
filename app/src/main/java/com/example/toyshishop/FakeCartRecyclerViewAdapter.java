package com.example.toyshishop;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toyshishop.data.entities.CartAndProduct;
import com.example.toyshishop.data.repository.CartRepository;
import com.google.android.material.button.MaterialButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FakeCartRecyclerViewAdapter extends RecyclerView.Adapter<FakeCartRecyclerViewAdapter.ViewHolder>{
    private ArrayList<CartAndProduct> list;
    private ArrayList<CartAndProduct> listAll;
    private FakeCartRecyclerViewAdapter.OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(CartAndProduct cartItem);
    }

    //clickListeners for views in recycler view
    public interface ClickListener {
        void onPositionClicked(int position);
        void onLongClicked(int position);
    }

    public FakeCartRecyclerViewAdapter(ArrayList<CartAndProduct> list, OnItemClickListener listener) {
        this.list = list;
        listAll = new ArrayList<>(list);
        this.listener = listener;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycling_view_specific_order_product_item, parent, false);
        FakeCartRecyclerViewAdapter.ViewHolder viewHolder = new FakeCartRecyclerViewAdapter.ViewHolder(listItem);
        this.context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder0, int position) {
        final CartAndProduct cartAndProduct = list.get(position);
        final ViewHolder holder = holder0;
        holder.idTextVIew.setText(cartAndProduct.getProduct().getTextProductId());
        holder.nameTextView.setText(cartAndProduct.getProduct().getProductName());
        holder.quantityTextView.setText(String.valueOf(cartAndProduct.getCartItem().getQuantity()));
        holder.totalPriceTextView.setText("(" + cartAndProduct.getProduct().getPrice() * cartAndProduct.getCartItem().getQuantity() + "€)");

        if (cartAndProduct.getProduct().getImagePath() != null)
            holder.imageView.setImageURI(Uri.parse(cartAndProduct.getProduct().getImagePath()));




        holder.quantityTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int num = 1;
                try {
                    num = Integer.parseInt(s.toString());
                }catch (NumberFormatException e){
                    return;
                }

                if(num < 1){
                    num = 1;
                    holder.quantityTextView.setText("1");
                }else if(num > cartAndProduct.getProduct().getQuantity()){
                    num = cartAndProduct.getProduct().getQuantity();
                    holder.quantityTextView.setText(String.valueOf(num));
                    Toast.makeText(context, "Η ποσότητα της παραγγελείας δε μπορεί να υπερβαίνει το απόθεμα (" + cartAndProduct.getProduct().getQuantity() + ")", Toast.LENGTH_SHORT).show();
                }

                cartAndProduct.getCartItem().setQuantity(num);
                new CartRepository(context).updateCartProvider(cartAndProduct.getCartItem());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView idTextVIew;
        TextView nameTextView;
        TextView quantityTextView;
        TextView totalPriceTextView;
        ImageView imageView;
        MaterialButton minusButton;
        MaterialButton addButton;
        private WeakReference<ClickListener> listenerRef;   //weakreference to avoid memory leak

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            idTextVIew = itemView.findViewById(R.id.recyclerViewCartId);
            nameTextView = itemView.findViewById(R.id.recyclerViewCartName);
            quantityTextView = itemView.findViewById(R.id.orderQuantityEditText);
            imageView = itemView.findViewById(R.id.recyclerViewCartImageView);
            totalPriceTextView = itemView.findViewById(R.id.recyclerViewCartTotalPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }

    }
}
