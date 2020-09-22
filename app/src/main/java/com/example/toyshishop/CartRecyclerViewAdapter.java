package com.example.toyshishop;

import android.content.Context;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toyshishop.data.entities.CartAndProduct;
import com.example.toyshishop.data.entities.Products;
import com.example.toyshishop.data.repository.CartRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> implements Filterable {
    private ArrayList<CartAndProduct> list;
    private ArrayList<CartAndProduct> listAll;
    private CartRecyclerViewAdapter.OnItemClickListener listener;
    private Context context;

    public interface OnItemClickListener {
        void onItemClick(CartAndProduct cartItem);
    }

    //clickListeners for views in recycler view
    public interface ClickListener {
        void onPositionClicked(int position);
        void onLongClicked(int position);
    }

    public CartRecyclerViewAdapter(ArrayList<CartAndProduct> list, OnItemClickListener listener) {
        this.list = list;
        listAll = new ArrayList<>(list);
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            List<CartAndProduct> filteredList = new ArrayList<>();

            //runs on background thread
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()) {
                    filteredList.addAll(listAll);
                } else {
                    for (CartAndProduct cartAndProduct : listAll) {
                        Products product = cartAndProduct.getProduct();
                        if (product.getProductName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                                String.valueOf(product.getProductId()).toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredList.add(cartAndProduct);
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
                list = (ArrayList<CartAndProduct>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.recycling_view_cart_item, parent, false);
        CartRecyclerViewAdapter.ViewHolder viewHolder = new CartRecyclerViewAdapter.ViewHolder(listItem);
        this.context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder0, int position) {
        final CartAndProduct cartAndProduct = list.get(position);
        final ViewHolder holder = holder0;
        holder.bind(cartAndProduct, listener);
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
                holder.totalPriceTextView.setText("(" + num * cartAndProduct.getProduct().getPrice() +")");
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
        EditText quantityTextView;
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
            minusButton = itemView.findViewById(R.id.removeCartQuantityButton);
            addButton = itemView.findViewById(R.id.addCartQuantityButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });



        }

        void bind(final CartAndProduct item, final CartRecyclerViewAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    int mRecentlyDeletedItemPosition;
    CartAndProduct mRecentlyDeletedItem;

    public void deleteTask(int position) {
        mRecentlyDeletedItem = list.get(position);
        mRecentlyDeletedItemPosition = position;
        list.remove(position);
        notifyItemRemoved(position);
        showUndoSnackbar();
    }



    private void deletePrermantlyCartItemFromDataBase(){
        new CartRepository(context).deleteCartProvider(mRecentlyDeletedItem.getCartItem());
    }

    //show snackBar to bottom, so user can undo
    private void showUndoSnackbar() {
        View view = ((FragmentActivity)context).findViewById(R.id.cartRecyclerView);
        final Snackbar snackbar = Snackbar.make(view, R.string.snackBarText,
                Snackbar.LENGTH_LONG);

        //call back about snackbar action
        snackbar.addCallback(new Snackbar.Callback(){
            //call when dismissed
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                //snackbar dismissed itself
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    deletePrermantlyCartItemFromDataBase();
                }
            }
        });

        //button on snackbar listener
        snackbar.setAction(R.string.snackBarButton, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartRecyclerViewAdapter.this.undoDelete();
            }
        });
        snackbar.show();
    }

    //undo the item we had delete
    private void undoDelete() {
        list.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

}
