package com.demo.ecclient;

import static com.demo.ecclient.utils.Constants.CENTER_API_URL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.demo.ecclient.databinding.FragmentDiscoverBinding;
import com.demo.ecclient.model.EdgeInfo;
import com.demo.ecclient.model.RetrofitAPI;
import com.demo.ecclient.placeholder.PlaceholderContent;

import java.math.BigDecimal;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DiscoverFragment extends Fragment {

    private FragmentDiscoverBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar progressBar;


    public DiscoverFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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
        binding = FragmentDiscoverBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonDiscover.setOnClickListener(v -> discover());

        progressBar = binding.loadingProgressBar;
    }

    private void discover() {
        progressBar.setVisibility(View.VISIBLE);
        PlaceholderContent.updateItem(edges());
        NavHostFragment.findNavController(DiscoverFragment.this)
                            .navigate(R.id.action_DiscoverFragment_to_EdgeInfoFragment);
                    progressBar.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(CENTER_API_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        RetrofitAPI service = retrofit.create(RetrofitAPI.class);
//        Call<List<EdgeInfo>> call = service.discover();
//        System.out.println("asdasdasd");
//        call.enqueue(new Callback<>() {
//            @Override
//            public void onResponse(Call<List<EdgeInfo>> call, Response<List<EdgeInfo>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    PlaceholderContent.updateItem(response.body());
//                    NavHostFragment.findNavController(DiscoverFragment.this)
//                            .navigate(R.id.action_DiscoverFragment_to_EdgeInfoFragment);
//                    progressBar.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<EdgeInfo>> call, Throwable t) {
//                // Handle error
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }


    private List<EdgeInfo> edges() {
        return List.of(
                new EdgeInfo(2L, "TW", "192.168.2.3", new BigDecimal("2.0"), 6L),
                new EdgeInfo(5L, "JP", "192.168.2.5", new BigDecimal("2.0"), 36L),
                new EdgeInfo(6L, "US", "192.168.2.6", new BigDecimal("2.0"), 82L),
                new EdgeInfo(8L, "AU", "192.168.2.8", new BigDecimal("2.0"), 73L)
        );
    }


}