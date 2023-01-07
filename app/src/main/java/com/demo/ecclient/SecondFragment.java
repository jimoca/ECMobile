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

import model.PictureBase;
import model.PictureMask;
import model.PictureRaw;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;


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
            HashMap<String, PictureRaw> hashMap = (HashMap<String, PictureRaw>) getArguments().getSerializable("rawMap");
            PictureBase base = (PictureBase) hashMap.get("image");
            PictureMask mask = (PictureMask) hashMap.get("watermark");

            if (base != null) {
                binding.imageTwo.setImageBitmap(base.getBitmap());
            }
            if (mask != null) {
                binding.watermarkTwo.setImageBitmap(mask.getBitmap());
            }
        }




        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}