package com.example.toyshishop.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.toyshishop.MainActivity;
import com.example.toyshishop.R;
import com.example.toyshishop.data.entities.Products;
import com.example.toyshishop.data.repository.ProductRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {
    private View rootView;
    private String imagePath;
    final static private int PRODUCT_IMAGE_CODE = 444;

    public AddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePath = null;

        //fragment has menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_product, container, false);

        MaterialButton addImageButton = rootView.findViewById(R.id.addImageProductButton);
        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);  //ACTION_OPEN_DOCUMENT FOR LONG TIME, GET DOCUMENT FOR SHORT TIME (loose data after restart app)
                startActivityForResult(Intent.createChooser(intent, "Επιλέξτε μια εικόνα."), PRODUCT_IMAGE_CODE);
            }
        });

        MaterialButton removeImageButton = rootView.findViewById(R.id.removeImageTextButton);
        removeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePath = null;
                Toast.makeText(getContext(), "Η εικόνα αφαιρέθηκε.", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //set back button to toolbar
        ((MainActivity)getActivity()).setNavigtionListener();

        menu.clear();
        inflater.inflate(R.menu.add_toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.addToolbarMenuAddItem);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                boolean flag = true;    //flag for let user insert product

                TextInputEditText nameEditText = rootView.findViewById(R.id.addProductNameEditText);
                TextInputEditText descriptionEditText = rootView.findViewById(R.id.addProductDescriptionEditText);
                TextInputEditText priceEditText = rootView.findViewById(R.id.addProductPriceEditText);
                TextInputEditText oldPriceEditText = rootView.findViewById(R.id.addProductOldPriceEditText);
                RadioGroup categoryRadioGroup = rootView.findViewById(R.id.addProductCategoryRadioGroup);
                TextInputEditText quantityEditText = rootView.findViewById(R.id.addProductquantityEditText);


                String name = nameEditText.getText().toString().trim();
                if (name.isEmpty()) {
                    nameEditText.setError("Συμπληρώστε το όνομα του προϊόντος");
                    flag = false;
                }

                String description = descriptionEditText.getText().toString().trim();

                double price = 0;
                try {
                    price = Double.parseDouble(priceEditText.getText().toString());
                } catch (NumberFormatException e) {
                    priceEditText.setError("Συμπληρώστε τη τιμή του προιόντος");
                    flag = false;
                }

                //-1 for no old price
                double oldPrice;
                try {
                    oldPrice = Double.parseDouble(oldPriceEditText.getText().toString());
                } catch (NumberFormatException e) {
                    oldPrice = -1;
                }

                int quantity = 0;
                try {
                    quantity = Integer.parseInt(quantityEditText.getText().toString());
                } catch (NumberFormatException e) {
                    quantityEditText.setError("Συμπληρώστε το απόθεμα του προϊόντος");
                    flag = false;
                }


                int categoryRadioButtonId = categoryRadioGroup.getCheckedRadioButtonId();
                int category = 0;
                switch (categoryRadioButtonId) {
                    case R.id.addProductShishaCategoryRadioButton:
                        category = 0;
                        break;
                    case R.id.addProductFlavorCategoryRadioButton:
                        category = 1;
                        break;
                    case R.id.addProductAccessoriesCategoryRadioButton:
                        category = 2;
                        break;
                }

                if (flag) {
                    new ProductRepository(getContext()).insertProduct(new Products(name, description, category, price, oldPrice, quantity, imagePath));
                }
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        return true;
    }
}
