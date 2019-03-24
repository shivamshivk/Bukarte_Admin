package map.android.mr_auspicious.bukarteadmin.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import org.w3c.dom.Text;
import java.util.List;
import map.android.mr_auspicious.bukarteadmin.Model.Search;
import map.android.mr_auspicious.bukarteadmin.R;


public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.MyViewHolder> {

    private List<Search> search_terms;
    private Context mContext;
    private int selected_position = -1;
    private ClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView search_term;
        TextView searched;

        public MyViewHolder(View view) {
            super(view);

            search_term = (TextView) view.findViewById(R.id.term);
            searched = (TextView) view.findViewById(R.id.searched_);

        }

    }


    public Search_Adapter(Context context,List<Search> search_terms) {
        this.mContext = context;
        this.search_terms = search_terms;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_term_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Search search = search_terms.get(position);
        holder.search_term.setText(search.getBook_name());
        holder.searched.setText(search.getTime());

    }

    private View.OnClickListener onStateChangedListener(final CheckBox checkBox, final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    selected_position = position;
                } else {
                    selected_position = -1;
                }
                notifyDataSetChanged();
            }
        };
    }


    @Override
    public int getItemCount() {
        return search_terms.size();
    }


    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }



        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e){

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}