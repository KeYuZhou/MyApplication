package com.example.chirag.slidingtabsusingviewpager;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chirag.slidingtabsusingviewpager.Crawler.Book;
import com.example.chirag.slidingtabsusingviewpager.Crawler.FromISBNtoBook;
import com.google.zxing.Result;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ZXingScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;

    String accountNo;
    AlertDialog.Builder notfoundBuilder;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountNo = getIntent().getStringExtra("accountNo");
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        int currentApiVersion = Build.VERSION.SDK_INT;

        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "TRY SCAN A BARCODE OF A BOOK!", Toast.LENGTH_LONG).show();
            } else {
                requestPermission();
            }
        }

        notfoundBuilder = new AlertDialog.Builder(this);
        notfoundBuilder.setTitle("Oops! Not Found!");
        notfoundBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(ZXingScannerActivity.this);
            }
        });


        builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(ZXingScannerActivity.this);
            }
        });


    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ZXingScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    final Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    System.out.println("isbn to book");
                    final Book book = (Book) msg.obj;

                    if (book == null) {
                        AlertDialog alert1 = notfoundBuilder.create();
                        alert1.show();
                    } else {
                        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myResult));
//				startActivity(browserIntent);


                                Intent intent = new Intent(ZXingScannerActivity.this, MainActivity.class);
                                intent.putExtra("accountNo", accountNo);
                                intent.putExtra("query", book.getTitle());
                                intent.putExtra("author", book.getAuthorName());
                                intent.putExtra("imgUrl", book.getimageLink());
                                intent.putExtra("douban", book.getContent());
                                intent.putExtra("callno", book.getCallNo());
                                intent.putExtra("publicion", book.getPublisherInformation());
                                intent.putExtra("avil", book.getAvailable());
                                intent.putExtra("accountNo", accountNo);
                                startActivity(intent);
                            }
                        });
                        builder.setMessage(book.getTitle());

//                        Intent intent = new Intent(ZXingScannerActivity.this, MainActivity.class);
//                        intent.putExtra("accountNo", accountNo);
//                        intent.putExtra("query", book.getTitle());
//                        intent.putExtra("author", book.getAuthorName());
//                        intent.putExtra("imgUrl", book.getimageLink());
//                        intent.putExtra("douban", book.getContent());
//                        intent.putExtra("callno", book.getCallNo());
//                        intent.putExtra("publicion", book.getPublisherInformation());
//                        intent.putExtra("avil", book.getAvailable());
//                        intent.putExtra("accountNo", accountNo);
//                        startActivity(intent);
                    }
                    //searchRecommendAdapter.notifyDataSetChanged();


                    break;
            }
        }


    };

    @Override
    public void handleResult(final Result result) {
        final String myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Book book = FromISBNtoBook.getBookByISBN(myResult);
                Message message = new Message();
                message.what = 0;
                message.obj = book;
                handle.sendMessage(message);


            }
        }).start();

        //      final Book book = FromISBNtoBook.getBookByISBN(myResult);


//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("Scan Result");
//		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				scannerView.resumeCameraPreview(ZXingScannerActivity.this);
//			}
//		});
//		builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
////				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myResult));
////				startActivity(browserIntent);
//
//
//				Intent intent = new Intent(ZXingScannerActivity.this, MainActivity.class);
//				intent.putExtra("accountNo", accountNo);
//				intent.putExtra("query", book.getTitle());
//				intent.putExtra("author", book.getAuthorName());
//				intent.putExtra("imgUrl", book.getimageLink());
//				intent.putExtra("douban", book.getContent());
//				intent.putExtra("callno", book.getCallNo());
//				intent.putExtra("publicion", book.getPublisherInformation());
//				intent.putExtra("avil", book.getAvailable());
//				intent.putExtra("accountNo", accountNo);
//				startActivity(intent);
//			}
//		});
//		builder.setMessage(result.getText());
//


//
//		if (book==null){
//			AlertDialog alert1 = notfoundBuilder.create();
//			alert1.show();
//		}else{
//			AlertDialog alert1 = builder.create();
//			alert1.show();
//		}


    }
}



