package com.cluelesstech.grocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cluelesstech.grocerylist.Activities.DetailActivity;
import com.cluelesstech.grocerylist.Database.DatabaseHandler;
import com.cluelesstech.grocerylist.Model.Grocery;
import com.cluelesstech.grocerylist.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Grocery> groceryList;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        Grocery grocery = groceryList.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQty());
        holder.dateAdded.setText(grocery.getDateAdded());

    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView groceryItemName;
        public TextView dateAdded;
        public TextView quantity;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            groceryItemName = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            dateAdded = itemView.findViewById(R.id.dataAdded);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQty());
                    intent.putExtra("date", grocery.getDateAdded());

                    context.startActivity(intent);

                }
            });
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);
                    editItem(grocery);
                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    grocery = groceryList.get(position);
                    deleteItem(grocery.getId());
                    break;
            }
        }


        public void deleteItem(final int id) {
            dialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noBtn = view.findViewById(R.id.noButton);
            Button yesBtn = view.findViewById(R.id.yesButton);

            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            dialog.show();

            noBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteGrocery(id);
                    groceryList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();
                }
            });
        }

        public void editItem(final Grocery grocery) {
            dialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.pop_up, null);

            final EditText groceryItem = view.findViewById(R.id.grocery_item);
            final EditText quantity = view.findViewById(R.id.quantity);
            final TextView title = view.findViewById(R.id.title);

            title.setText("Edit Grocery");
            groceryItem.setText(grocery.getName());

            Button saveButton = (Button) view.findViewById(R.id.saveButton);

            dialogBuilder.setView(view);
            dialog = dialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);

                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQty(quantity.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty()) {
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(), grocery);
                    } else {
                        Snackbar.make(view, "Add Grocery and quantity", Snackbar.LENGTH_LONG).show();
                    }
                    dialog.dismiss();
                }
            });
        }
    }


}
