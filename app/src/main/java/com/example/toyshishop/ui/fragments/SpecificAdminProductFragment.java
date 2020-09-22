package com.example.toyshishop.ui.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toyshishop.MainActivity;
import com.example.toyshishop.R;
import com.example.toyshishop.data.entities.Products;
import com.example.toyshishop.data.repository.ProductRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecificAdminProductFragment extends Fragment {
    private Products product;
    private MenuItem deleteItem;
    private View rootView;
    private String imagePath;
    final static private int PRODUCT_IMAGE_CODE = 444;

    public SpecificAdminProductFragment(Products product) {
        this.product = product;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_specific_product, container, false);

        //call method to set product's information to TextViews
        setUIFields();

        setHasOptionsMenu(true);    //to call onCreateOptionMenu

        MaterialButton addImageButton = rootView.findViewById(R.id.addImageEditProductButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);  //ACTION_OPEN_DOCUMENT FOR LONG TIME, GET DOCUMENT FOR SHORT TIME (loose data after restart app)
                startActivityForResult(Intent.createChooser(intent, "Επιλέξτε μια εικόνα."), PRODUCT_IMAGE_CODE);
            }
        });

        MaterialButton removeImageButton = rootView.findViewById(R.id.removeImageEditProductTextButton);
        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = null;
                Toast.makeText(getContext(), "Η εικόνα αφαιρέθηκε.", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }

    //change toolbar menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.second_toolbar, menu);

        //set back button to toolbar
        ((MainActivity)getActivity()).setNavigtionListener();

        //delete customer after alert dialog accept
        deleteItem = menu.findItem(R.id.secondToolbarDeleteButton);
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //show alert to delete forever a customer if user press delete
                AlertDialog dialog =  new MaterialAlertDialogBuilder(getContext())
                        .setTitle(getActivity().getResources().getString(R.string.deleteProductTitle))
                        .setMessage(getActivity().getResources().getString(R.string.deleteProductSupportText) + " " + product.getProductName() + " (" + product.getTextProductId() + ");")
                        .setPositiveButton(getActivity().getResources().getString(R.string.deleteProductAcceptButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new ProductRepository(getContext()).deleteProduct(product);
                            }
                        })
                        .setNeutralButton(getActivity().getResources().getString(R.string.deleteProductCancelButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.design_default_color_error));   //set delete button color red

                return false;
            }
        });

        //edit button listener
        MenuItem editItem = menu.findItem(R.id.secondToolbarEditButton);
        editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                getActivity().startActionMode(actionModeCallBack);

                return true;
            }
        });




        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PRODUCT_IMAGE_CODE) {
            if(resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                imagePath = selectedImageUri.toString();
                Toast.makeText(getContext(), "Η Εικόνα επιλέχθηκε.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ActionMode.Callback actionModeCallBack = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.accept_contextual_action_mode_menu, menu);
            setTextViewsEnable(true);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.accept_contextual_accept_item:
                    if(!checkFieldsValid()){
                        return false;
                    }

                    updateProductFields();
                    new ProductRepository(getContext(), mode).updateProductProvider(product);

                    break;
            }

            return false;
        }




        @Override
        public void onDestroyActionMode(ActionMode mode) {
            setTextViewsEnable(false);
            setUIFields();
        }

        private void updateProductFields(){
            TextView nameEditText = rootView.findViewById(R.id.editProductNameEditText);
            TextView descriptionEditText = rootView.findViewById(R.id.editProductDescriptionEditText);
            TextView priceText = rootView.findViewById(R.id.editProductPriceEditText);
            TextView oldPriceText = rootView.findViewById(R.id.editProductOldPriceEditText);
            TextView quantityTextView = rootView.findViewById(R.id.editProductQuantityEditText);
            RadioButton category0RadioButton = rootView.findViewById(R.id.editProductShishaCategoryRadioButton);
            RadioButton category1RadioButton = rootView.findViewById(R.id.editProductFlavorCategoryRadioButton);
            RadioButton category2RadioButton = rootView.findViewById(R.id.editProductAccessoriesCategoryRadioButton);

            product.setProductName(nameEditText.getText().toString());
            product.setDescription(descriptionEditText.getText().toString());
            product.setPrice(Double.parseDouble(priceText.getText().toString()));
            product.setImagePath(imagePath);

            //if old price is empty, then do it 0
            try{
                product.setOldPrice(Double.parseDouble(oldPriceText.getText().toString()));
            }catch (NumberFormatException e){
                product.setOldPrice(0);
            }

            product.setQuantity(Integer.parseInt(quantityTextView.getText().toString()));

            if(category0RadioButton.isChecked()){
                product.setProductCategory(0);
                return;
            }

            if(category1RadioButton.isChecked()){
                product.setProductCategory(1);
                return;
            }



            product.setProductCategory(2);
        }

        private boolean checkFieldsValid(){
            TextView nameEditText = rootView.findViewById(R.id.editProductNameEditText);
            TextView priceText = rootView.findViewById(R.id.editProductPriceEditText);
            TextView quantityTextView = rootView.findViewById(R.id.editProductQuantityEditText);

            boolean flag = true;

            if(nameEditText.getText().toString().trim().isEmpty()){
                flag = false;
            }

            if(priceText.getText().toString().trim().isEmpty()){
                flag = false;
            }

            if(quantityTextView.getText().toString().trim().isEmpty()){
                flag = false;
            }

            return flag;
        }
    };

    //set product's information to TextViews
    private void setUIFields(){
        TextView nameEditText = rootView.findViewById(R.id.editProductNameEditText);
        TextView descriptionEditText = rootView.findViewById(R.id.editProductDescriptionEditText);
        TextView priceText = rootView.findViewById(R.id.editProductPriceEditText);
        TextView oldPriceText = rootView.findViewById(R.id.editProductOldPriceEditText);
        TextView quantityTextView = rootView.findViewById(R.id.editProductQuantityEditText);
        RadioButton category0RadioButton = rootView.findViewById(R.id.editProductShishaCategoryRadioButton);
        RadioButton category1RadioButton = rootView.findViewById(R.id.editProductFlavorCategoryRadioButton);
        RadioButton category2RadioButton = rootView.findViewById(R.id.editProductAccessoriesCategoryRadioButton);

        nameEditText.setText(product.getProductName());
        priceText.setText(String.valueOf(product.getPrice()));
        quantityTextView.setText(String.valueOf(product.getQuantity()));

        //if description is empty put a space for right layout
        if(product.getDescription().isEmpty())
            descriptionEditText.setText(" ");
        else
            descriptionEditText.setText(product.getDescription());

        //oldprice = -1 for no old price. so we display nothing
        if(product.getOldPrice() == -1)
            oldPriceText.setText("");
        else
            oldPriceText.setText(String.valueOf(product.getOldPrice()));


        switch (product.getProductCategory()){
            case 0:
                category0RadioButton.setChecked(true);
                break;
            case 1:
                category1RadioButton.setChecked(true);
                break;
            case 2:
                category2RadioButton.setChecked(true);
                break;
        }
    }

    private void setTextViewsEnable(boolean flag) {
        TextView nameEditText = rootView.findViewById(R.id.editProductNameEditText);
        TextView descriptionEditText = rootView.findViewById(R.id.editProductDescriptionEditText);
        TextView priceText = rootView.findViewById(R.id.editProductPriceEditText);
        TextView oldPriceText = rootView.findViewById(R.id.editProductOldPriceEditText);
        TextView quantityTextView = rootView.findViewById(R.id.editProductQuantityEditText);
        RadioButton category0RadioButton = rootView.findViewById(R.id.editProductShishaCategoryRadioButton);
        RadioButton category1RadioButton = rootView.findViewById(R.id.editProductFlavorCategoryRadioButton);
        RadioButton category2RadioButton = rootView.findViewById(R.id.editProductAccessoriesCategoryRadioButton);
        MaterialButton addButton = rootView.findViewById(R.id.addImageEditProductButton);
        MaterialButton removeButton = rootView.findViewById(R.id.removeImageEditProductTextButton);

        nameEditText.setEnabled(flag);
        descriptionEditText.setEnabled(flag);
        priceText.setEnabled(flag);
        oldPriceText.setEnabled(flag);
        quantityTextView.setEnabled(flag);
        quantityTextView.setEnabled(flag);
        category0RadioButton.setEnabled(flag);
        category1RadioButton.setEnabled(flag);
        category2RadioButton.setEnabled(flag);
        addButton.setEnabled(flag);
        removeButton.setEnabled(flag);

        //restore space we put for right layout
        if(descriptionEditText.getText().toString().equals(" "))
            descriptionEditText.setText("");

        //we need this because we used oldPriceText.setText(" "), to have the right layout. now we want to erase that space.
        if(oldPriceText.getText().toString().equals(" "))
            oldPriceText.setText("");
    }
}
