package com.example.bbangmap.mypage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Patterns;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.bbangmap.R;

import java.util.regex.Pattern;

public class QnaActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qna);

        EditText email = (EditText) this.findViewById(R.id.email);
        EditText qnatext = (EditText) this.findViewById(R.id.qnaText);
        //엔터버튼 대신 완료버튼
        qnatext.setImeOptions(EditorInfo.IME_ACTION_DONE);
        qnatext.setRawInputType(InputType.TYPE_CLASS_TEXT);

        ImageView check2 = (ImageView) this.findViewById(R.id.check2);
        Button submit = (Button) this.findViewById(R.id.button);

        //개인정보수집
        CheckBox checkBox = (CheckBox) this.findViewById(R.id.checkBox);
        ImageButton expand = (ImageButton) this.findViewById(R.id.expandButton);
        TextView infoName = (TextView) this.findViewById(R.id.infoName);
        TextView infoContent = (TextView) this.findViewById(R.id.infoContent);

        //개인정보수집 더보기 아이콘 이벤트
        expand.setOnClickListener(new View.OnClickListener() {
            int stat = 0;
            @Override
            public void onClick(View view) {
                if(stat==0){
                    expand.setImageResource(R.drawable.ic_expand_less);
                    infoName.setVisibility(View.VISIBLE);
                    infoContent.setVisibility(View.VISIBLE);
                    stat = 1;
                } else if(stat==1){
                    expand.setImageResource(R.drawable.ic_expand_more);
                    infoName.setVisibility(View.GONE);
                    infoContent.setVisibility(View.GONE);
                    stat = 0;
                }
            }
            });

        submit.setOnClickListener(new View.OnClickListener() {
            final Pattern pattern = Patterns.EMAIL_ADDRESS;
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "이메일 주소를 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 130);
                    toast.show();
                } else if (qnatext.getText().toString().equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "문의 내용을 입력해주세요.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 130);
                    toast.show();
                } else if(!(pattern.matcher(email.getText().toString()).matches())) {
                    Toast toast = Toast.makeText(getApplicationContext(), "잘못된 이메일 형식입니다.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 130);
                    toast.show();
                } else if(!checkBox.isChecked()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "개인정보 수집 및 이용 동의 필수", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 130);
                    toast.show();
                } else {
                    showAlertDialogButtonClicked(view, email, qnatext, checkBox, check2);
                }
            }
        });
    }
    public void showAlertDialogButtonClicked(View view, EditText email, EditText qnatext, CheckBox checkBox, ImageView check2) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("제출하시겠습니까?");
        builder.setMessage("문의하신 내용에 대한 답변은 " + email.getText().toString()+" 으로 전송됩니다.");

        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                //이메일전송
                SendMail mailServer = new SendMail();
                int ret = mailServer.sendSecurityCode(getApplicationContext(), "[🍞빵맵🍞 문의하기]", "jjuha.dev@gmail.com",
                        email.getText().toString(), qnatext.getText().toString());

                if(ret==0){
                    qnatext.setText(null);
                    email.setText(null);
                    checkBox.setChecked(false);

                    check2.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(getApplicationContext(), "제출되었습니다.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 130);
                    toast.show();

                    Animation fadeOutAnim = AnimationUtils.loadAnimation(QnaActivity.this, R.anim.fade_out);
                    check2.startAnimation(fadeOutAnim);
                    check2.setVisibility(View.INVISIBLE);
                } else if(ret==2){
                    Toast toast = Toast.makeText(getApplicationContext(), "전송 실패! 인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 130);
                    toast.show();
                } else if(ret==3){
                    Toast toast = Toast.makeText(getApplicationContext(), "전송 실패! 잠시 후 다시 시도해주세요.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 130);
                    toast.show();
                }

                dialog.dismiss();
            }
        });
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
        if (item.getItemId() == android.R.id.home){
            Intent parentIntent = NavUtils.getParentActivityIntent(this);
            parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(parentIntent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

