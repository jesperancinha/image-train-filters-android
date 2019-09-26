package org.jesperancinha.itf.android.mail;

import android.content.Intent;
import android.net.Uri;

import lombok.AllArgsConstructor;
import lombok.Builder;

import static android.content.Intent.ACTION_SEND;

@Builder
@AllArgsConstructor
public class MailIntentCreator {

    private final String[] to;

    private final String[] cc;

    private final String[] bcc;

    private final String subject;

    private final String text;

    private final Uri uri;

    public Intent build() {
        final Intent emailIntent = new Intent(ACTION_SEND);
        emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_BCC, bcc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return emailIntent;
    }


}
