package com.boomer;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UserFragment extends Fragment {
    private ListView list;
    private String API_SERVER = "";
    ArrayList<HashMap<String, String>> users = new ArrayList<HashMap<String, String>>();
    private List<Model> friends = new ArrayList<Model>();
    Button refresh,logout,friendFrag,map,uploadboomer,camera;
    List<NameValuePair> params;
    SharedPreferences prefs;

    // Listview Adapter
    ArrayAdapter<String> adapter;

    // Search EditText
    EditText inputSearch;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.user_fragment, container, false);
        prefs = getActivity().getSharedPreferences("Chat", 0);

        // Friends List
        list = (ListView)view.findViewById(R.id.listView);
        //refresh = (Button)view.findViewById(R.id.refresh);
        friendFrag = (Button)view.findViewById(R.id.friends);
        logout = (Button)view.findViewById(R.id.logout);
        inputSearch = (EditText)view.findViewById(R.id.inputSearch);
        map = (Button)view.findViewById(R.id.map);
        uploadboomer = (Button)view.findViewById(R.id.uploadboomer);
        camera = (Button)view.findViewById(R.id.camera);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new  Logout().execute();
            }
        });

//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.camera_preview)).commit();
//                Fragment reg = new UserFragment();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.camera_preview, reg);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , MapActivity.class);
                startActivity(intent);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , CameraActivity.class);
                startActivity(intent);
            }
        });
        uploadboomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , UploadActivity.class);
                startActivity(intent);
            }
        });
        friendFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment reg = new FriendListFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.camera_preview, reg);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        new Load().execute();

        return view;
    }

    private class Load extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nick", prefs.getString("REG_FROM","")));
            //Log.i("LGOUT", "Loggging out");
            JSONArray jAry = json.getJSONArray(API_SERVER+"/getusers",params);

           // Log.i("LOADING", jAry.toString());

            return jAry;
        }
        @Override
        protected void onPostExecute(JSONArray json) {
            for(int i = 0; i < json.length(); i++){
                JSONObject c = null;
                try {
                    c = json.getJSONObject(i);
                    String name = c.getString("name");
                    //Log.i("jsonOBJ",c.toString());

                    //Log.i("NAME",name);
                    //HashMap<String, String> map = new HashMap<String, String>();
                    //map.put("name", name);
                    Model m =  new Model(name);
                    friends.add(m);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            // init list adapter
            //adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.friend_name,friends);
            ArrayAdapter<Model> adapter = new InteractiveArrayAdapter(getActivity(), friends);
//            ListAdapter adapter = new SimpleAdapter(getActivity(), users,
//                    R.layout.user_list_single,
//                    new String[] { "name","nick" }, new int[] {
//                    R.id.name, R.id.nick});
            list.setAdapter(adapter);

            // Update list based on text
            inputSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                    // When user changed the Text
                    UserFragment.this.adapter.getFilter().filter(cs);
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }
    private class Logout extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nick", prefs.getString("REG_FROM","")));
            JSONObject jObj = json.getJSONFromUrl("http://6eef6e0b.ngrok.com/logout",params);

            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {

            String res = null;
            try {
                res = json.getString("response");
                Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
                if(res.equals("Removed Sucessfully")) {
                    Fragment reg = new LoginFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.camera_preview, reg);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.addToBackStack(null);
                    ft.commit();
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_FROM", "");
                    edit.commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}