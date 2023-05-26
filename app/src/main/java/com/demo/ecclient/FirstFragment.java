package com.demo.ecclient;


import static com.demo.ecclient.utils.Constants.API_TAG;
import static com.demo.ecclient.utils.Constants.EDGE_API_URL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.demo.ecclient.databinding.FragmentFirstBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.demo.ecclient.model.DelegateModel;
import com.demo.ecclient.model.PictureBase;
import com.demo.ecclient.model.PictureMask;
import com.demo.ecclient.model.PictureRaw;
import com.demo.ecclient.model.RetrofitAPI;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import security.paillier.PaillierKeyPairGenerator;
import security.paillier.PaillierPrivateKey;
import security.paillier.PaillierPublicKey;

import com.demo.ecclient.model.TaskModel;
import com.demo.ecclient.utils.AsyncResponse;
import com.demo.ecclient.utils.BitmapHelper;
import com.demo.ecclient.utils.PaillierPixels;
import com.demo.ecclient.utils.QuorumConnection;
import com.demo.ecclient.utils.QuorumConnectionAsyncTask;
import com.demo.ecclient.utils.QuorumConnectionRes;
import com.demo.ecclient.utils.StreamResultTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FirstFragment extends Fragment implements AsyncResponse {

    private FragmentFirstBinding binding;

    private ImageView currentImageView;

    private ProgressBar progressBar;

    private Retrofit retrofit;

    private RetrofitAPI retrofitAPI;

    private int[] imagePixels;

    private int[] watermarkPixels;

    private Bitmap bitmapImage;

    private Bitmap bitmapWatermark;

    private PaillierPublicKey publicKey;

    private PaillierPrivateKey privateKey;



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
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(EDGE_API_URL)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(StreamingGsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);
        initializeKeyPair();
        binding.buttonDelegate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delegate();
            }
        });

        binding.imageOne.setOnClickListener(view1 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            currentImageView = binding.imageOne;
            activityResultLauncher.launch(intent);
        });

        binding.watermark.setOnClickListener(view2 -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            currentImageView = binding.watermark;
            activityResultLauncher.launch(intent);
        });

        progressBar = binding.loadingProgressBar;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImage);
                                currentImageView.setImageBitmap(bitmap);
                                if (binding.imageOne.equals(currentImageView)) {
                                    bitmapImage = bitmap;
                                    imagePixels = BitmapHelper.getBitmapPixels(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                                } else if (binding.watermark.equals(currentImageView)) {
                                    bitmapWatermark = bitmap;
                                    watermarkPixels = BitmapHelper.getBitmapPixels(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }
            });

    private void initializeKeyPair() {
        int KEY_SIZE = 2048;
        SecureRandom r = new SecureRandom();
        PaillierKeyPairGenerator p = new PaillierKeyPairGenerator();
        p.initialize(KEY_SIZE, r);
        KeyPair pe = p.generateKeyPair();
        publicKey = (PaillierPublicKey) pe.getPublic();
        privateKey = (PaillierPrivateKey) pe.getPrivate();
    }




    private void delegate() {
        progressBar.setVisibility(View.VISIBLE);
        PictureBase pictureBase = PictureBase.pictureBase(bitmapImage, imagePixels);
        PictureMask pictureMask = PictureMask.pictureMask(bitmapWatermark, watermarkPixels);
        pictureBase.setPixels(PaillierPixels
                .encryptPixels(pictureBase.getPixels(), publicKey));
        pictureMask.setPixels(PaillierPixels
                .encryptPixels(pictureMask.getPixels(), publicKey));
        DelegateModel delegateModel = new DelegateModel(pictureBase, pictureMask, publicKey);

        byte[] serializedData = objectSerialize(delegateModel);
        RequestBody requestBody = createByteArrToRequestBody(serializedData);
        retrofitAPI
                .delegate(requestBody)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            new StreamResultTask(FirstFragment.this).execute(response.body());
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(API_TAG, "Error: " + t.getMessage());
                    }
                });
    }

    private byte[] objectSerialize(DelegateModel delegateModel) {
        return Optional.of(delegateModel)
                .map(it -> {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = null;
                    try {
                        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(delegateModel);
                        objectOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return byteArrayOutputStream.toByteArray();
                }).orElse(new byte[]{});
    }

    private RequestBody createByteArrToRequestBody(byte[] serializedData) {
        return RequestBody.create(MediaType.parse("application/octet-stream"), serializedData);
    }

    @Override
    public void processStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void processFinish(PictureBase output) {
        progressBar.setVisibility(View.GONE);

        Bundle bundle = new Bundle();
        bundle.putSerializable("result", output);
        bundle.putSerializable("sk", privateKey);
        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
    }


}