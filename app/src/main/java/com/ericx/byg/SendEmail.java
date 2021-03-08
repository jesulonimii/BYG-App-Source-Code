package com.ericx.byg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SendEmail extends AppCompatActivity {

    EditText subject, message;
    Button send;
    Toolbar sendToolbar;
    String mailSubject, mailMessage, toMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);

        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        sendToolbar = findViewById(R.id.mailToolbar);


        setSupportActionBar(sendToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        sendToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendEmail.super.onBackPressed();
            }
        });

        toMail = "beautifulyetgodly@gmail.com";


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // define Intent object
                // with action attribute as ACTION_SEND
                Intent intent = new Intent(Intent.ACTION_SEND);

                mailSubject = subject.getText().toString();
                mailMessage = message.getText().toString();

                // add three fields to intent using putExtra function
              //for an array of emails
                // intent.putExtra(Intent.EXTRA_EMAIL,
                      //  new String[] { toMail });

                //for a single email
                intent.putExtra(Intent.EXTRA_EMAIL, toMail);
                intent.putExtra(Intent.EXTRA_SUBJECT,  mailSubject);
                intent.putExtra(Intent.EXTRA_TEXT, mailMessage);

                // set type of intent
                intent.setType("message/rfc822");

                // startActivity with intent with chooser
                // as Email client using createChooser function
                startActivity(
                        Intent
                                .createChooser(intent,
                                        "Select your mail app"));
            }
        });
    }

}
