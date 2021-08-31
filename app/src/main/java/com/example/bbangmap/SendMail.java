package com.example.bbangmap;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail extends AppCompatActivity {
    String user = "bbangmap"; // 보내는 계정의 id
    String password = "BBangs01!"; // 보내는 계정의 pw



    public int sendSecurityCode(Context context, String sendTo, String sentFrom, String content) {
        try {
            GMailSender gMailSender = new GMailSender(user, password);
            gMailSender.sendMail("빵맵에서 보낸 문의 메일입니다.", "보냄: "+sentFrom+"\n\n"+content, sendTo);
            return 0;
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
            return 1;
        } catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            return 2;
        } catch (Exception e) {
            e.printStackTrace();
            return 3;
        }
    }
}

