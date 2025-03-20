package com.armavi_bsd.robotispreconstructed_mega.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.armavi_bsd.robotispreconstructed_mega.R;
import com.armavi_bsd.robotispreconstructed_mega.model.Subjects;

import java.util.ArrayList;


/**
 * Created by Arm_AVI on 2/28/2018.
 */

public class ListViewAdapter extends ArrayAdapter<Subjects> {

    public ArrayList<Subjects> MainList;

    public ArrayList<Subjects> SubjectListTemp;

    public SubjectDataFilter subjectDataFilter ;

    public ListViewAdapter(Context context, int id, ArrayList<Subjects> subjectArrayList) {

        super(context, id, subjectArrayList);

        this.SubjectListTemp = new ArrayList<Subjects>();

        this.SubjectListTemp.addAll(subjectArrayList);

        this.MainList = new ArrayList<Subjects>();

        this.MainList.addAll(subjectArrayList);
    }

    @Override
    public Filter getFilter() {

        if (subjectDataFilter == null){

            subjectDataFilter  = new SubjectDataFilter();
        }
        return subjectDataFilter;
    }


    public class ViewHolder {

        TextView SubjectName;
        TextView SubjectFullForm;


        TextView areaName;
        TextView postCode;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = vi.inflate(R.layout.listview_items_layout, null);

            holder = new ViewHolder();

            holder.SubjectName = (TextView) convertView.findViewById(R.id.textviewName);

            holder.SubjectFullForm = (TextView) convertView.findViewById(R.id.textviewFullForm);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Subjects subjects = SubjectListTemp.get(position);

        holder.SubjectName.setText(subjects.getSubName());

        holder.SubjectFullForm.setText(subjects.getSubFullForm());


        return convertView;

    }

    private class SubjectDataFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            charSequence = charSequence.toString().toLowerCase();

            FilterResults filterResults = new FilterResults();

            if(charSequence != null && charSequence.toString().length() > 0)
            {
                ArrayList<Subjects> arrayList1 = new ArrayList<Subjects>();

                for(int i = 0, l = MainList.size(); i < l; i++)
                {
                    Subjects subjects = MainList.get(i);

                    if(subjects.toString().toLowerCase().contains(charSequence))

                        arrayList1.add(subjects);
                }
                filterResults.count = arrayList1.size();

                filterResults.values = arrayList1;
            }
            else
            {
                synchronized(this)
                {
                    filterResults.values = MainList;

                    filterResults.count = MainList.size();
                }
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            SubjectListTemp = (ArrayList<Subjects>)filterResults.values;

            notifyDataSetChanged();

            clear();

            for(int i = 0, l = SubjectListTemp.size(); i < l; i++)
                add(SubjectListTemp.get(i));

            notifyDataSetInvalidated();
        }
    }
}
