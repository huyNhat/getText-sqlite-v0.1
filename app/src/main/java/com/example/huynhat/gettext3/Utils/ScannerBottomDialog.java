package com.example.huynhat.gettext3.Utils;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.huynhat.gettext3.BarcodeScanner.CameraFace;
import com.example.huynhat.gettext3.R;

import me.dm7.barcodescanner.core.DisplayUtils;
import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * Created by huynhat on 2018-01-06.
 */

public class ScannerBottomDialog extends DialogFragment implements ZBarScannerView.ResultHandler {

    static final String TAG = ScannerBottomDialog.class.getSimpleName();

    CameraFace cameraFace;

    ZBarScannerView mScannerView;

    // Maintain a 4:3 ratio
    int mWindowWidth = 800;
    int mWindowHeight = 600;
    int mViewFinderPadding = 50;

    static final String BUNDLE_CAMERA_FACE = "cameraFace";

    OnQRCodeScanListener scanListener;

    public interface OnQRCodeScanListener
    {
        public void onQRCodeScan(String contents);
    }

    public static ScannerBottomDialog newInstance(CameraFace camera)
    {
        ScannerBottomDialog dialog = new ScannerBottomDialog();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_CAMERA_FACE, camera.ordinal());
        dialog.setArguments(args);
        return dialog;
    }





    public void onAttach(android.support.v4.app.Fragment activity)
    {
        super.onAttach(getActivity());

        try
        {
            scanListener = (OnQRCodeScanListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.getClass().getSimpleName() + " must implement OnQRCodeScanListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if(args != null)
        {
            cameraFace = CameraFace.values()[args.getInt(BUNDLE_CAMERA_FACE)];
        }
        else
        {
            cameraFace = CameraFace.BACK;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.dialog_scanner, container, false);

        mScannerView = new ZBarScannerView(getActivity());
        mScannerView.setResultHandler(this);
        mScannerView.setAutoFocus(false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        if(DisplayUtils.getScreenOrientation(getActivity()) == Configuration.ORIENTATION_PORTRAIT && mWindowWidth > mWindowHeight)
        {
            getDialog().getWindow().setLayout(mWindowHeight, mWindowWidth);
        }
        else
        {
            getDialog().getWindow().setLayout(mWindowWidth, mWindowHeight);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mScannerView.stopCamera();
    }

    private void playSound(int notificationType)
    {
        Uri notification = RingtoneManager.getDefaultUri(notificationType);
        Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
        r.play();
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result)
    {
        if(result.getBarcodeFormat() == BarcodeFormat.EAN13 || result.getBarcodeFormat() == BarcodeFormat.EAN8) {
            playSound(RingtoneManager.TYPE_NOTIFICATION);
            scanListener.onQRCodeScan(result.getContents());
            getDialog().dismiss();
        }
    }
}
