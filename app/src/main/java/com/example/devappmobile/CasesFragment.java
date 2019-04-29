package com.example.devappmobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.devappmobile.dummy.DummyContent;
import com.example.devappmobile.dummy.DummyContent.DummyItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class CasesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<ProcessList> list = new ArrayList<>();
    private String token= "";//getArguments().getString("token");;
    public  MyItemRecyclerViewAdapter mAdapter;


    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://process.isiforge.tn/")
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit= builder.build();
    UserClient userClient= retrofit.create(UserClient.class);
    SharedPreferences prefs;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CasesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static CasesFragment newInstance(int columnCount) {
        CasesFragment fragment = new CasesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        prefs= this.getActivity().getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        String restoredText = prefs.getString("token", null);
        if (restoredText != null) {
            token = prefs.getString("token", "No token defined");
            //System.out.println(token);

        }
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mAdapter = new MyItemRecyclerViewAdapter(list, mListener);
            recyclerView.setAdapter(mAdapter);
            doAdapter();
        }
        return view;
    }
    public Boolean verifyToken() {
         String dateString = prefs.getString("Expires_in", "Error token!!");
        DateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {

            Date date = formatter.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            Date datenow = calendar.getTime();

            return datenow.before(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    private void refresh_Token() {

        String refreshToken = prefs.getString("refresh", "Error token!!");

        Login auth = new Login(refreshToken);

       
        Call<User> call = userClient.login(auth);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("i m in refresh response");


                if (response.isSuccessful()) {


                    Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
                    System.out.println("dateBEFORE" + calendar.getTime().toString());
                    calendar.add(Calendar.SECOND, Integer.parseInt(response.body().getExpires_in()));

                    SharedPreferences.Editor sh = getActivity().getSharedPreferences("MyPrefsFile", MODE_PRIVATE).edit();
                    sh.putString("token", "Bearer " + response.body().getAccess_token());
                    sh.putString("refresh", response.body().getRefresh_token());
                    sh.putString("Expires_in",calendar.getTime().toString()) ;
                    sh.commit();

                    prepareData();

                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("failed refresh");
            }
        });
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
                Toast.makeText(getContext(), "Failed get cases", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void doAdapter(){

        if (verifyToken()){
           prepareData();
        }else {
            System.out.println("I m in refresh tokeen ");
           refresh_Token();
        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ProcessList item);
    }
}
