package com.example.sookmyungchat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    EditText etName;
    CircleImageView ivProfile;

    Uri imgUri; // 선택한 이미지의 경로 uri

    boolean firstTime = true;
    boolean profileChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.et_name);
        ivProfile = findViewById(R.id.iv_profile);

        loadData();
        if(Save.nickName!=null){
            etName.setText(Save.nickName);
            //Glide.with(this).load(imgUri).circleCrop().into(ivProfile);
            Picasso.get().load(Save.profileUrl).into(ivProfile);

            firstTime = false;
        }

    }
    public void clickImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    imgUri = data.getData();
                    //Glide.with(this).load(imgUri).circleCrop().into(ivProfile);
                    Picasso.get().load(imgUri).into(ivProfile);

                    profileChanged =true;
                }
                break;
        }
    }
    public void clickBtn(View view){
        //바꾼적 없 처음 접속도 아닐 때
        if(!profileChanged && !firstTime){
            //ChatActivity 로 전환
            Intent intent= new Intent(this, ChatActivity.class);
            startActivity(intent);
            finish();
        }else{
            //save
            saveData();
        }
    }
    void saveData(){
        // EditText 의 닉네임을 전역변수에 가져오기
        Save.nickName = etName.getText().toString();

        //이미지를 선택하지 않았을 경우
        if(imgUri==null) return;

        //파이어베이스 스토리지에 저장할 파일명은 날짜 기반으로 구분
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
        String fileName = sdf.format(new Date())+".png";

        //파이어베이스 스토리지에 저장
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference imgRef= firebaseStorage.getReference("profileImages/"+fileName);

        //파일 업로드
        UploadTask uploadTask = imgRef.putFile(imgUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Save.profileUrl = uri.toString();
                        Toast.makeText(MainActivity.this,"profile saved",Toast.LENGTH_SHORT).show();
                        //파이어베이스에 닉네임 프로파일 uri 를 저장하고 DB 객체를 연다.
                        //profile 이라는 이름의 자식 노드를 참조 객체로 가져온다.
                        FirebaseDatabase firebaseDatabase =FirebaseDatabase.getInstance();
                        DatabaseReference profileRef = firebaseDatabase.getReference("profiles");
                        //닉네임을 key 로 지정하고 프로필 이미지의 주소 값으로 저장
                        profileRef.child(Save.nickName).setValue(Save.profileUrl);

                        //별도로 폰에 저장하는 부분
                        SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putString("nickName",Save.nickName);
                        editor.putString("profileUrl",Save.profileUrl);
                        editor.commit();

                        //저장 완료 후  chatActivity 로 전환
                        Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            }
        });

    }
    void loadData(){
        SharedPreferences preferences = getSharedPreferences("account",MODE_PRIVATE);
        Save.nickName = preferences.getString("nickName",null);
        Save.profileUrl = preferences.getString("profileUrl",null);
    }
}