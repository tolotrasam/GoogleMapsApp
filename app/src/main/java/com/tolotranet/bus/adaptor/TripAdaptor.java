package com.tolotranet.bus.adaptor;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;


import com.tolotranet.bus.R;
import com.tolotranet.bus.model.Trip;

import java.util.ArrayList;

public class TripAdaptor extends BaseAdapter {
    private static ArrayList<Trip> MyArrayObjects = new ArrayList<Trip>();
    private static ArrayList<Trip> FilteredObjects = new ArrayList<Trip>();
    private Context context;
    private LayoutInflater mInflater;
    private ItemFilter myFilter = new ItemFilter();

    public TripAdaptor(Context c, ArrayList<Trip> MyList) {
        this.context = c;
        MyArrayObjects = MyList;
        FilteredObjects = MyList;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return FilteredObjects.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return FilteredObjects.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public Filter getFilter() {
        return myFilter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_event, null);
            holder = new ViewHolder();
            holder.NameTV = (TextView) convertView.findViewById(R.id.Contact_name_tv);
            holder.BottomTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
            holder.TimeTV = (TextView) convertView.findViewById(R.id.Contact_bottom_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String Name = FilteredObjects.get(position).getDestination();
        String BottomText = FilteredObjects.get(position).getDistance();
        String Time= FilteredObjects.get(position).getTime();

        holder.NameTV.setText(Name);
        holder.BottomTV.setText(BottomText);
        holder.TimeTV.setText(Time);

        return convertView;
    }

    public void update(ArrayList<Trip> arrayListMember_object) {
        FilteredObjects = arrayListMember_object;
        this.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView NameTV;
        TextView BottomTV;
        TextView TimeTV;
        public ImageView PersonImageView;
    }

    private class ItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence arg0) {

            return null;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            // TODO Auto-generated method stub
            FilteredObjects = (ArrayList<Trip>) arg1.values;
            notifyDataSetChanged();
        }

    }

}
