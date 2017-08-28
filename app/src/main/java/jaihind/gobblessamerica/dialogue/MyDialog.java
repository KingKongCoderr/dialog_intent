package jaihind.gobblessamerica.dialogue;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyDialog extends DialogFragment {
Button mshare_bt,msend_bt,mcontacts_bt,mnumber_bt,mcamera_bt,mMycamera_bt;
    Intent call_intent=new Intent(Intent.ACTION_CALL);
    ImageView mcamera_iv;
    String mCurrentPhotoPath, mMyphotopath;

    public MyDialog() {
        // Required empty public constructor
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v=getActivity().getLayoutInflater().inflate(R.layout.dialog,null);
        mshare_bt=(Button)v.findViewById(R.id.share_bt);
        msend_bt=(Button)v.findViewById(R.id.send_bt);
        mcontacts_bt=(Button)v.findViewById(R.id.contacts_bt);
        mnumber_bt=(Button)v.findViewById(R.id.number_bt);
        mcamera_bt=(Button)v.findViewById(R.id.camera_bt);
        mcamera_iv=(ImageView)v.findViewById(R.id.camera_iv);
        mMycamera_bt=(Button)v.findViewById(R.id.mycamera_bt);
        final TextView txt=(TextView)v.findViewById(R.id.dialog_text);
        txt.setText("hello i am nandeesh");
        txt.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       txt.setTextColor(Color.BLUE);
                                   }
                               }
        );

        mshare_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();

                call_intent.setData(Uri.parse("tel:"+"6605287176"));
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!=
                        PackageManager.PERMISSION_GRANTED){
                    requestPermission(1);
                }else {
                startActivity(call_intent);}
            }
        });
        msend_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send_intent=new Intent(Intent.ACTION_SEND);
                send_intent.setType("text/plain");
                send_intent.putExtra(Intent.EXTRA_TEXT,"my intent endeavors");
                send_intent.putExtra(Intent.EXTRA_SUBJECT,"dafiohhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhasdbnspadibvaopsih");
                send_intent= Intent.createChooser(send_intent,"Choose your app");
                if (send_intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(send_intent);
                }
            }
        });
        final Intent contacts_intent=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        mcontacts_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(contacts_intent,1);
            }
        });

        final Intent number_intent=new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);


        mnumber_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(number_intent,2);
            }
        });

        mcamera_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {

                        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                                PackageManager.PERMISSION_GRANTED){
                            requestPermission(2);
                        }else {
                            photoFile = createImageFile();}



                    } catch (Exception ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                       /* Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                                "com.example.android.fileprovider",
                                photoFile);*/
                        Uri photoURI= Uri.fromFile(photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, 123);
                    }
                }
            }
        });
        mMycamera_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myPictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (myPictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Here, we are making a folder named picFolder to store
                    // pics taken by the camera using this application.
                    final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Dialog_Nandeesh/";
                    File newdir = new File(dir);
                    newdir.mkdirs();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String file= dir+timeStamp+".jpg";
                    File newfile=new File(file);
                    try{
                        newfile.createNewFile();
                    }catch (IOException e){

                    }
                    mMyphotopath=newfile.getAbsolutePath();
                    Uri outputfileUri=Uri.fromFile(newfile);
                    myPictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputfileUri);

                    startActivityForResult(myPictureIntent,1234);


                }


            }
        });



        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("Testing").create();
    }
    private void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() {
// Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir= this.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            String location = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Dialog_app/";
            File storageDir=new File(location);
            storageDir.mkdirs();
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                        storageDir  /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && data!=null){
            Uri contacturi=data.getData();
            String[] queryfields= new String[]{ContactsContract.Contacts.DISPLAY_NAME};

            Cursor c= getActivity().getContentResolver().query(contacturi,queryfields,null,null,null);
            try{

                c.moveToFirst();
                String name= c.getString(0);
                String name1=name;
            }finally {
                c.close();
            }
        }

        if(requestCode==2 && data!=null){
            Uri numberuri=data.getData();
           // String[] numquery={ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor num_cursor=getActivity().getContentResolver().query(numberuri,null,null,null,null);
            try{
                num_cursor.moveToFirst();
                int phone_index=num_cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                String phone_number=num_cursor.getString(phone_index);
                call_intent.setData(Uri.parse("tel:"+phone_number));
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!=
                        PackageManager.PERMISSION_GRANTED){
                    requestPermission(1);
                }else {
                    startActivity(call_intent);}

            }finally {
                num_cursor.close();
            }
        }
        if (requestCode == 123 && resultCode == RESULT_OK) {
           // Bundle extras = data.getExtras();
           /* Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            mcamera_iv.setImageBitmap(imageBitmap);*/
            setPic(mCurrentPhotoPath);
            galleryAddPic(mCurrentPhotoPath);
        }

        if(requestCode==1234 && resultCode == RESULT_OK){
            setPic(mMyphotopath);
            galleryAddPic(mMyphotopath);
        }
    }

    private void setPic(String path) {
        // Get the dimensions of the View
        int targetW = mcamera_iv.getWidth();
        int targetH = mcamera_iv.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        //Bitmap bitmap=(Bitmap)data.getExtras().get(mCurrentPhotoPath);
        mcamera_iv.setImageBitmap(bitmap);
    }
    private void requestPermission(int requestcode) {
        if(requestcode==1){
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);}
        if(requestcode==2){
       ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);}
    }
}
