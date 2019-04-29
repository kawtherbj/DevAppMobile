package com.example.devappmobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemFragment extends Fragment {


     private String token= "";//getArguments().getString("token");;
    private MyAdapter mAdapter;


    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://process.isiforge.tn/")
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit= builder.build();
    UserClient userClient= retrofit.create(UserClient.class);

    private OnListFragmentInteractionListener mListener;
    private List<ProcessList> list = new ArrayList<>();

    public DemFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        SharedPreferences prefs= this.getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String restoredText = prefs.getString("token", null);
        if (restoredText != null) {
            token = prefs.getString("token", "No token defined");
            //System.out.println(token);

        }
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new MyAdapter(list, new MyAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ProcessList item) {
                    navigatetoHomeActivity(item);

                    Toast.makeText(getContext(), item.getPro_uid(), Toast.LENGTH_LONG).show();
                }
            });
            recyclerView.setAdapter(mAdapter);
            prepareData();
        }
        return view;
    }



    private void prepareData() {

        System.out.println(token);
        Call<List<ProcessList>> cal = userClient.getSecret(token);
        cal.enqueue(new Callback<List<ProcessList>>() {
            @Override
            public void onResponse(Call<List<ProcessList>> call, Response<List<ProcessList>> response) {
                Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
                if( response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++)
                        list.add(response.body().get(i));
                    mAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<ProcessList>> call, Throwable t) {
                Toast.makeText(getContext(), "scss", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ProcessList item);
    }

    public void navigatetoHomeActivity(ProcessList token){

      /*  Intent homeIntent = new Intent(getContext(),FormActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("pro_uid",token.getPro_uid());
        mBundle.putString("tas_uid",token.getTas_uid());
        homeIntent.putExtras(mBundle);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
*/
    }


}
