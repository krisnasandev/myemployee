package my.id.raditsan.myemployee;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import my.id.raditsan.myemployee.databinding.FragmentFirstBinding;
import my.id.raditsan.myemployee.utils.HttpHandler;
import my.id.raditsan.myemployee.utils.Response;

public class FirstFragment extends Fragment {

    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactList = new ArrayList<>();
        lv = binding.list;
        new GetEmployee().execute();
        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    private class GetEmployee extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(), "Please wait.", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://tiraapi-dev.tigaraksa.co.id/tes-programer-mobile/api/karyawan/all";
            Response response = sh.makeServiceCall(url, "GET", null);
            String jsonStr = response.getResult();
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("values");
                    Log.i(TAG, "hai " + contacts.length());
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        Log.i(TAG, "cccc" + c);

                        String nik = c.getString("nik");
                        String first_name = c.getString("first_name");
                        String last_name = c.getString("last_name");
                        String alamat = c.getString("alamat");
                        Boolean aktif = c.getBoolean("aktif");
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("nik", "Nik " + nik);
                        contact.put("name", first_name + " " + last_name);
                        contact.put("first_name", first_name);
                        contact.put("last_name", last_name);
                        contact.put("alamat", alamat);
                        contact.put("aktif", "Status " + (aktif ? "Aktif" : "Tidak Aktif"));

                        Log.i(TAG, "index ke" + contact);
                        contactList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Couldn't get data from server.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(getContext(), contactList,
                    R.layout.employee_item, new String[]{"nik","name","aktif","alamat"},
                    new int[]{R.id.nik, R.id.name, R.id.status, R.id.alamat});
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}