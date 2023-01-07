package com.demo.ecclient;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.demo.ecclient.databinding.FragmentFirstBinding;

import java.io.IOException;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.HashMap;

import model.PictureBase;
import model.PictureMask;
import model.PictureRaw;
import security.paillier.PaillierKeyPairGenerator;
import security.paillier.PaillierPrivateKey;
import security.paillier.PaillierPublicKey;
import utils.BitmapHelper;
import utils.PaillierPixels;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    private ImageView currentImageView;

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
        initializeKeyPair();
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PictureBase pictureBase = PictureBase.pictureBase(bitmapImage, imagePixels);
                PictureMask pictureMask = PictureMask.pictureMask(bitmapWatermark, watermarkPixels);
                pictureBase.setPixels(PaillierPixels
                        .encryptPixels(pictureBase.getPixels(), publicKey));
                pictureMask.setPixels(PaillierPixels
                        .encryptPixels(pictureMask.getPixels(), publicKey));

                Bundle bundle = new Bundle();
                HashMap<String, PictureRaw> rawMap = new HashMap<>();
                rawMap.put("image", pictureBase);
                rawMap.put("watermark", pictureMask);
                bundle.putSerializable("rawMap", rawMap);
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
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
}