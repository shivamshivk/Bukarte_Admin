package map.android.mr_auspicious.bukarteadmin.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import java.util.List;

import map.android.mr_auspicious.bukarteadmin.Model.Categories;
import map.android.mr_auspicious.bukarteadmin.R;

public class Selection_Adapter extends RecyclerView.Adapter<Selection_Adapter.MyViewHolder> {

    private List<Categories> categories;
    private ClickListener clickListener;
    private int selectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkbox;

        public MyViewHolder(View view) {
            super(view);
            checkbox = (CheckBox) view.findViewById(R.id.select);

        }
    }


    public Selection_Adapter(List<Categories> categories, ClickListener click) {
        this.categories = categories;
        this.clickListener = click;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_, parent, false);




        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Categories category = categories.get(position);

        holder.checkbox.setText(category.getCategoryName());

        holder.checkbox.setTag(position);

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.checkbox.isChecked()){
                    clickListener.onSelectClick(buttonView,position);
                }
            }
        });

        if (position == selectedPosition) {
            holder.checkbox.setChecked(true);
        } else holder.checkbox.setChecked(false);

        holder.checkbox.setOnClickListener(onStateChangedListener(holder.checkbox, position));


    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selectedPosition = position;
                } else {
                    selectedPosition = -1;
                }
                notifyDataSetChanged();
            }
        };
    }

    public interface ClickListener {
        void onSelectClick(View view, int position);
    }

    public interface SelectListener {
        void categorySelect(boolean value, int position);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}