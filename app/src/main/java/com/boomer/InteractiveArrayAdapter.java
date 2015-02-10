package com.boomer;

/**
 * Created by Arin on 12/6/2014.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InteractiveArrayAdapter extends ArrayAdapter<Model> {

    private final List<Model> list;
    private final Activity context;
    List<NameValuePair> params;
    SharedPreferences prefs;

    public InteractiveArrayAdapter(Activity context, List<Model> list) {
        super(context, R.layout.user_fragment, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        prefs = context.getSharedPreferences("Chat", 0);
        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.list_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) view.findViewById(R.id.friend_name);
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkBox);
            viewHolder.checkbox
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            Model element = (Model) viewHolder.checkbox
                                    .getTag();
                            element.setSelected(buttonView.isChecked());
                            Log.i("LIST","Box Clicked!!");


                            // adding new friend
                            if(element.isSelected() == true){
                                Log.i("LIST","tryna add new friend");

                                params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("reg_id", prefs.getString("REG_ID","")));
                                params.add(new BasicNameValuePair("friend", element.getName()));
                                new AddFriend().execute();
                            }
                        }
                    });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(list.get(position));
        } else {
            view = convertView;
            ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(list.get(position).getName());
        holder.checkbox.setChecked(list.get(position).isSelected());
        return view;
    }

    private class AddFriend extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser json = new JSONParser();

            Log.i("LIST","Posting: "+params.toString());

            JSONObject jObj = json.getJSONFromUrl("http://16054c67.ngrok.com/addfriend",params);

            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            String res = null;
            try {
                res = json.getString("response");
                Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}