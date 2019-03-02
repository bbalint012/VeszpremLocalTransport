package hu.unideb.bus.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import hu.unideb.bus.R;
import hu.unideb.bus.room.model.AutoCompleteItem;

public class CustomAdapter extends ArrayAdapter<AutoCompleteItem> {
    private Context context;
    private List<AutoCompleteItem> items;
    private List<AutoCompleteItem> filteredItems = new ArrayList<>();

    public CustomAdapter(@NonNull Context context, List<AutoCompleteItem> items) {
        super(context, R.layout.custom_autocomplete_layout, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Nullable
    @Override
    public AutoCompleteItem getItem(int position) {
        return filteredItems.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new ItemFilter(this, items);
    }

    @NonNull
    @Override
    @SuppressLint("ViewHolder")
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.custom_autocomplete_layout, parent, false);
        TextView stopName = (TextView) view.findViewById(R.id.stopName);
        TextView destination = (TextView) view.findViewById(R.id.destination);

        AutoCompleteItem item = filteredItems.get(position);
        stopName.setText(item.getName());
        destination.setText(String.format("%s felé", item.getDestination()));

        if (position % 2 == 0) {
            view.setBackgroundColor(context.getResources().getColor(R.color.odd));
        }
        return view;
    }

    private class ItemFilter extends Filter {
        private CustomAdapter adapter;
        private List<AutoCompleteItem> originalItems;
        private List<AutoCompleteItem> filteredItems;

        ItemFilter(CustomAdapter adapter, List<AutoCompleteItem> originalItems) {
            super();
            this.adapter = adapter;
            this.originalItems = originalItems;
            this.filteredItems = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredItems.clear();

            if (constraint == null || constraint.length() == 0) {
                filteredItems.addAll(originalItems);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (AutoCompleteItem s : originalItems) {
                    if (s.getName().toLowerCase().contains(filterPattern)) {
                        filteredItems.add(s);
                    }
                }
            }

            final FilterResults results = new FilterResults();
            results.values = filteredItems;
            results.count = filteredItems.size();
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredItems.clear();
            adapter.filteredItems.addAll((List) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
