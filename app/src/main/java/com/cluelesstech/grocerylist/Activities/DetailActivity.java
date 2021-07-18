package com.cluelesstech.grocerylist.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cluelesstech.grocerylist.R;

public class DetailActivity extends AppCompatActivity {

    private TextView itemName, quantity, dateAdded;
    private int groceryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        itemName = findViewById(R.id.itemNameDetail);
        quantity = findViewById(R.id.itemQtyDetail);
        dateAdded = findViewById(R.id.itemDateAddedDetail);

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            dateAdded.setText(bundle.getString("date"));

            groceryId = bundle.getInt("id");
        }
    }
}