package com.demo.ecclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.demo.ecclient.databinding.FragmentSecondBinding;

import java.math.BigInteger;
import java.util.HashMap;

import com.demo.ecclient.model.DecryptModel;
import com.demo.ecclient.model.PictureBase;
import com.demo.ecclient.model.PictureMask;
import com.demo.ecclient.model.PictureRaw;
import com.demo.ecclient.utils.BitmapHelper;
import com.demo.ecclient.utils.DecryptAsync;
import com.demo.ecclient.utils.DecryptAsyncTask;
import com.demo.ecclient.utils.PaillierPixels;

import security.paillier.PaillierPrivateKey;

public class SecondFragment extends Fragment implements DecryptAsync {

    private FragmentSecondBinding binding;

    private PictureBase result;

    private PaillierPrivateKey privateKey;

    private ProgressBar progressBar;

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
        if (getArguments() != null) {
            result = (PictureBase) getArguments().getSerializable("result");
            privateKey = (PaillierPrivateKey) getArguments().getSerializable("sk");
            if (result != null) {
                binding.imageRes.setImageBitmap(
                        BitmapHelper.setBitmapPixels(result.getPixels(), result.getWidth(), result.getHeight()));
            }
        }

        progressBar = binding.loadingProgressBar;
        binding.buttonDecrypt.setOnClickListener(view1 -> decryptImage());
    }

    private void decryptImage() {
        new DecryptAsyncTask(SecondFragment.this).execute(new DecryptModel(result, privateKey));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void processStart() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void processFinish(BigInteger[] output) {
        binding.imageRes.setImageBitmap(
                BitmapHelper.setBitmapPixels(output, result.getWidth(), result.getHeight()));

        progressBar.setVisibility(View.GONE);
    }
}