package com.example.bearbit.Main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bearbit.R;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mItemBrands = new ArrayList<>();
    private ArrayList<String> mItemNames = new ArrayList<>();
    private ArrayList<Integer> mItemValues = new ArrayList<>();

    private Context mContext;

    public RecyclerViewAdapter(Context mContext, ArrayList<String> mItemBrands, ArrayList<String> mItemNames, ArrayList<Integer> mItemValues) {
        this.mItemBrands = mItemBrands;
        this.mItemNames = mItemNames;
        this.mItemValues = mItemValues;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        if((mItemValues.get(position) >= 0) && (mItemValues.get(position) <= 40))
        {
            holder.coloredLabel.setBackgroundResource(R.drawable.green_colored_label);
        }
        else if ((mItemValues.get(position) >= 41) && (mItemValues.get(position) <= 90)) {
            holder.coloredLabel.setBackgroundResource(R.drawable.yellow_colored_label);
        }
        else {
            holder.coloredLabel.setBackgroundResource(R.drawable.red_colored_label);
        }

        holder.itemBrand.setText(mItemBrands.get(position));
        holder.itemName.setText(mItemNames.get(position));
        holder.itemValue.setText(mItemValues.get(position) + " kcal");


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on" + mItemNames.get(position));

                Toast.makeText(mContext, mItemNames.get(position), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mItemNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemBrand;
        TextView itemName;
        TextView itemValue;

        RelativeLayout parentLayout;
        LinearLayout coloredLabel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemBrand = itemView.findViewById(R.id.item_brand);
            itemName = itemView.findViewById(R.id.item_name);
            itemValue = itemView.findViewById(R.id.item_value);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            coloredLabel = itemView.findViewById(R.id.item_color_tag);

        }
    }
}
