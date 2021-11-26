package com.example.sookmyungchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {
    EditText et;
    ListView listView;

    ArrayList<MessageItem> messageItems = new ArrayList<>();
    ChatAdapter adapter;

    //파이어베이스 DB 객체 참조변수 & chat 노드의 참조객체 참조변수
    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setTitle(Save.nickName);
        et = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
        adapter = new ChatAdapter(messageItems, getLayoutInflater());
        listView.setAdapter(adapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        chatRef = firebaseDatabase.getReference("chat");

        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //새로 추가된 msg 값 가져오기
                MessageItem messageItem = dataSnapshot.getValue(MessageItem.class);
                //새로운 메세지를 리스트뷰에 추가하기 위해 ArrayList 추가하기
                messageItems.add(messageItem);

                adapter.notifyDataSetChanged();
                //리스트뷰의 마지막 위피로 스크롤 위치를 이동하기 위해
                listView.setSelection(messageItems.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void clickSend(View view) {

        //firebase DB에 저장할 값들( 닉네임, 메세지, 프로필 이미지URL, 시간)
        String nickName = Save.nickName;
        String message = et.getText().toString();
        String profileUrl = Save.profileUrl;

        //메세지 작성 시간 문자열로..
        Calendar calendar = Calendar.getInstance(); //현재 시간을 가지고 있는 객체
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE); //14:16

        //firebase DB에 저장할 값(MessageItem 객체) 설정
        MessageItem messageItem = new MessageItem(nickName, message, time, profileUrl);
        //chat 노드에 MessageItem 객체
        chatRef.push().setValue(messageItem);

        //EditText 에 있는 글씨 지우기
        et.setText("");

        //소프트키패드를 안보이게
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

    }

}