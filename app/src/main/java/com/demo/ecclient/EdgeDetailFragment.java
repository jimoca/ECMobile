package com.demo.ecclient;

import static com.demo.ecclient.utils.Constants.EDGE_API_URL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.demo.ecclient.databinding.FragmentEdgeDetailBinding;
import com.demo.ecclient.databinding.FragmentFirstBinding;
import com.demo.ecclient.model.PayModel;
import com.demo.ecclient.model.RetrofitAPI;
import com.demo.ecclient.model.TaskModel;
import com.demo.ecclient.utils.PayAsyncRes;
import com.demo.ecclient.utils.PayAsyncTask;
import com.demo.ecclient.utils.QuorumConnection;
import com.demo.ecclient.utils.QuorumConnectionAsyncTask;
import com.demo.ecclient.utils.QuorumConnectionRes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EdgeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EdgeDetailFragment extends Fragment implements QuorumConnectionRes, PayAsyncRes {

    private FragmentEdgeDetailBinding binding;
    private ProgressBar progressBar;
    private Retrofit retrofit;
    private RetrofitAPI retrofitAPI;
    private QuorumConnection quorum;
    private TaskModel task;

    private TextView taskIdView;

    private TextView addressView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EdgeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EdgeDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EdgeDetailFragment newInstance(String param1, String param2) {
        EdgeDetailFragment fragment = new EdgeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentEdgeDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(EDGE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        binding.buttonStart.setOnClickListener(view1 -> startDelegation());
        binding.buttonPay.setOnClickListener(view1 -> pay());
        progressBar = binding.loadingProgressBar;
        taskIdView = binding.taskIdText;
        addressView = binding.addressText;
        binding.buttonPay.setEnabled(false);
        new QuorumConnectionAsyncTask(EdgeDetailFragment.this).execute(this.getContext().getCacheDir());
    }


    private void startDelegation() {
        progressBar.setVisibility((View.VISIBLE));
        retrofitAPI
                .start()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<TaskModel> call, Response<TaskModel> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            task = response.body();
                            quorum.setContractAddress(task.getContractAddress());
                            progressBar.setVisibility(View.GONE);
                            addressView.setText(task.getContractAddress());
                            taskIdView.setText(task.getTaskId().toString());
                            binding.buttonPay.setEnabled(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<TaskModel> call, Throwable t) {
                        // Handle error
                        progressBar.setVisibility(View.GONE);
                    }
                });

    }

    private void pay() {
       new PayAsyncTask(EdgeDetailFragment.this).execute(new PayModel(quorum, task.getTaskId(), new BigInteger("3")));
    }

    @Override
    public void processStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void processFinish(QuorumConnection quorumConnection) {
        progressBar.setVisibility(View.GONE);
        quorum = quorumConnection;


    }

    @Override
    public void payStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void payFinished() {
        progressBar.setVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("taskId", task.getTaskId());
        NavHostFragment.findNavController(EdgeDetailFragment.this)
                .navigate(R.id.action_EdgeDetailFragment_to_FirstFragment, bundle);
    }
}