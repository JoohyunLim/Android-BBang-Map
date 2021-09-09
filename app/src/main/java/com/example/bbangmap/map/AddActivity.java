package com.example.bbangmap.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.bbangmap.R;

public class AddActivity extends AppCompatActivity {
    private PopupWindow mPopupWindow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bbang);

        EditText name = (EditText) this.findViewById(R.id.nameTextField);
        EditText address = (EditText) this.findViewById(R.id.qnaText);
        ImageView check = (ImageView) this.findViewById(R.id.check);
        Button submit = (Button) this.findViewById(R.id.button);
        Button textButton = (Button) this.findViewById(R.id.textButton);
        textButton.setPaintFlags(textButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                builder.setIcon(R.drawable.icon);
                builder.setTitle("신청 유의사항");
                builder.setMessage("*반려 대상*\n\n1) 포털 사이트에 등록되어 있지 않아 정보 확인이 어려운 가게\n\n2) 대형 프랜차이즈 가게 (단, 지역을 대표하는 빵집, 특색 있는 지점 등은 예외적으로 등록 가능)\n\n3) 빵집보다 카페에 가까운 가게 (단, 베이커리카페는 등록 가능)\n\n4) 해외에 위치한 가게\n");
                builder.setPositiveButton("확인", null);

                AlertDialog dialog = builder.create();
                dialog.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                    }
                });
                dialog.show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().equals("") || name.getText().toString() == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "빵집의 이름을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 130);
                    toast.show();
                } else if (address.getText().toString().equals("") || address.getText().toString() == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "빵집의 주소를 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 130);
                    toast.show();
                } else{
                        showAlertDialogButtonClicked(view, name, address, check);
                }
            }
        });
    }

    public void showAlertDialogButtonClicked(View view, EditText name, EditText address, ImageView check) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("제출하시겠습니까?");
        builder.setMessage(name.getText().toString()+" ("+address.getText().toString()+")");
        // add the buttons

        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                address.setText(null);
                name.setText(null);

                check.setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(getApplicationContext(), "신청된 장소는 검수 후 지도에 등록됩니다.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 0, 130);
                toast.show();

                Animation fadeOutAnim = AnimationUtils.loadAnimation(AddActivity.this, R.anim.fade_out);
                check.startAnimation(fadeOutAnim);
                check.setVisibility(View.INVISIBLE);

                dialog.dismiss();
            }
        });

        //builder.setPositiveButton("네", null);
        builder.setNegativeButton("아니오", null);
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
            }
        });
        dialog.show();
    }
    //메뉴 뒤로가기 클릭 시 이전 화면으로 이동
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}