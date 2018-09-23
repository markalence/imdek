package com.example.mark.mstutor;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SchoolAdapter extends ArrayAdapter<String> {
    Context mContext;
    int layout;
    public SchoolAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        mContext = context;
        layout = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        ViewHolder mainViewHolder = null;

        if(convertView == null){

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.schoolTextView = convertView.findViewById(R.id.schoolTextView);
            viewHolder.checkBox = convertView.findViewById(R.id.schoolCheckBox);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {

                    StudentDetails.school = getItem(position);

                }
            });
            convertView.setTag(viewHolder);

        }

        else{

            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.schoolTextView = convertView.findViewById(R.id.schoolTextView);
            mainViewHolder.checkBox = convertView.findViewById(R.id.schoolCheckBox);
            mainViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StudentDetails.school = getItem(position);
                }
            });
        }
return  convertView;
    }



    public class ViewHolder{
        CheckBox checkBox;
        TextView schoolTextView;
    }
}
