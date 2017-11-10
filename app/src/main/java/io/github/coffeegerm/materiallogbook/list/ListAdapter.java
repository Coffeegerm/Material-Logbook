package io.github.coffeegerm.materiallogbook.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.activity.EditEntryActivity;
import io.github.coffeegerm.materiallogbook.activity.MainActivity;
import io.github.coffeegerm.materiallogbook.model.EntryItem;
import io.github.coffeegerm.materiallogbook.utils.Constants;

import static io.github.coffeegerm.materiallogbook.utils.Constants.DATE_FORMAT;
import static io.github.coffeegerm.materiallogbook.utils.Constants.PREF_DARK_MODE;
import static io.github.coffeegerm.materiallogbook.utils.Constants.TWELVE_HOUR_TIME_FORMAT;
import static io.github.coffeegerm.materiallogbook.utils.Constants.TWENTY_FOUR_HOUR_TIME_FORMAT;

/**
 * Created by David Yarzebinski on 6/25/2017.
 * <p>
 * Adapter used for filling RecyclerView within ListFragment
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private static final String TAG = "ListAdapter";
    private static int shortClickHintCount = 0;
    private LayoutInflater inflater;
    private Context context;
    public EntryItem item;
    private List<EntryItem> entryItemList;

    ListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setListItems(List<EntryItem> providedList) {
        this.entryItemList = providedList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "View Created");
        return new ListViewHolder(inflater.inflate(R.layout.item_card_list, parent, false));
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView date;
        @BindView(R.id.tv_time)
        TextView time;
        @BindView(R.id.tv_blood_glucose)
        TextView bloodGlucose;
        @BindView(R.id.tv_insulin)
        TextView insulin;
        @BindView(R.id.tv_carbs)
        TextView carbohydrates;
        @BindView(R.id.imgInsulins)
        ImageView ivInsulin;
        @BindView(R.id.imgCarbs)
        ImageView ivCarbs;
        @BindView(R.id.imgFinger)
        ImageView ivFinger;
        @BindView(R.id.line1)
        View line1;
        @BindView(R.id.line2)
        View line2;
        private View view;

        ListViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, itemView);

            if (MainActivity.sharedPreferences.getBoolean(PREF_DARK_MODE, false)) {
                ivFinger.setImageResource(R.drawable.ic_finger_dark);
                ivCarbs.setImageResource(R.drawable.ic_food_dark);
                ivInsulin.setImageResource(R.drawable.ic_syringe_dark);
                int white = context.getResources().getColor(R.color.white);
                line1.setBackgroundColor(white);
                line2.setBackgroundColor(white);
            } else {
                ivFinger.setImageResource(R.drawable.ic_finger);
                ivCarbs.setImageResource(R.drawable.ic_food);
                ivInsulin.setImageResource(R.drawable.ic_syringe);
            }
        }
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        item = entryItemList.get(holder.getAdapterPosition());

        // Check if today or yesterday and set date accordingly
        Calendar today = Calendar.getInstance();
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        int yesterdayDay = yesterday.get(Calendar.DAY_OF_MONTH);
        Calendar itemCalendar = Calendar.getInstance();
        itemCalendar.setTime(item.getDate());
        int itemDay = itemCalendar.get(Calendar.DAY_OF_MONTH);
        if (itemDay == todayDay) holder.date.setText(R.string.today);
        else if (itemDay == yesterdayDay) holder.date.setText(R.string.yesterday);
        else {
            holder.date.setText(DATE_FORMAT.format(item.getDate()));
        }

        // Set time based on user preference
        if (MainActivity.sharedPreferences.getBoolean(Constants.MILITARY_TIME, false)) {
            holder.time.setText(TWENTY_FOUR_HOUR_TIME_FORMAT.format(item.getDate()));
        } else {
            holder.time.setText(TWELVE_HOUR_TIME_FORMAT.format(item.getDate()));
        }

        /*
        * Handle item clicks on List Fragment
        * Long click will start EditEntryActivity
        * */
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shortClickHintCount <= 5) {
                    Toast.makeText(context, R.string.list_item_short_click, Toast.LENGTH_SHORT).show();
                    shortClickHintCount++;
                }
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent editEntryActivity = new Intent(context, EditEntryActivity.class);
                EntryItem selectedItem = entryItemList.get(holder.getAdapterPosition());
                editEntryActivity.putExtra(EditEntryActivity.ITEM_ID, selectedItem.getId());
                context.startActivity(editEntryActivity);
                return true;
            }
        });

        if (String.valueOf(item.getBloodGlucose()).equals("0"))
            holder.bloodGlucose.setText(R.string.dash);
        else
            holder.bloodGlucose.setText(String.valueOf(item.getBloodGlucose()));

        if (String.valueOf(item.getCarbohydrates()).equals("0"))
            holder.carbohydrates.setText(R.string.dash);
        else
            holder.carbohydrates.setText(String.valueOf(item.getCarbohydrates()));

        if (String.valueOf(item.getInsulin()).equals("0.0"))
            holder.insulin.setText(R.string.dash);
        else
            holder.insulin.setText(String.valueOf(item.getInsulin()));
    }

    @Override
    public int getItemCount() {
        return entryItemList.size();
    }
}
