package my.id.raditsan.myemployee;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.HashMap;

import my.id.raditsan.myemployee.databinding.FragmentSecondBinding;
import my.id.raditsan.myemployee.utils.HttpHandler;
import my.id.raditsan.myemployee.utils.Response;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private String TAG = MainActivity.class.getSimpleName();
    HashMap<String, Object> payload;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nik = binding.nik;
                EditText firstName = binding.firstName;
                EditText lastName = binding.lastName;
                RadioGroup status = binding.status;
                EditText alamat = binding.alamat;
                int selectedId = binding.status.getCheckedRadioButtonId();
                RadioButton radioButton = getView().findViewById(selectedId);
                payload = new HashMap<>();
                payload.put("nik", nik.getText().toString());
                payload.put("first_name", firstName.getText().toString());
                payload.put("last_name", lastName.getText().toString());
                payload.put("alamat", alamat.getText().toString());
                payload.put("aktif", radioButton.getText() == "Aktif");

                Log.e(TAG, "aaaaaaaa " + payload);

                new PostEmployee().execute();
            }
        });
//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }

    private class PostEmployee extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://tiraapi-dev.tigaraksa.co.id/tes-programer-mobile/karyawan/insert";
            Response response = sh.makeServiceCall(url, "POST", payload);
            String jsonStr = response.getResult();
            Log.e(TAG, "Response from url: " + jsonStr);
            Log.e(TAG, "Status Code Response from url: " + response.getStatusCode());
            if (response.getStatusCode() == 204) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getView().getContext(), "Success", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getView().getContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}