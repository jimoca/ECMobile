package com.demo.ecclient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.demo.ecclient.databinding.FragmentSecondBinding;

import java.util.HashMap;

import com.demo.ecclient.model.PictureBase;
import com.demo.ecclient.model.PictureMask;
import com.demo.ecclient.model.PictureRaw;
import com.demo.ecclient.utils.BitmapHelper;
import com.demo.ecclient.utils.PaillierPixels;

import security.paillier.PaillierPrivateKey;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private PictureBase result;

    private PaillierPrivateKey privateKey;


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


        binding.buttonDecrypt.setOnClickListener(view1 -> binding.imageRes.setImageBitmap(
                BitmapHelper.setBitmapPixels(
                        PaillierPixels.decryptPixels(result.getPixels(), privateKey), result.getWidth(), result.getHeight())));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}