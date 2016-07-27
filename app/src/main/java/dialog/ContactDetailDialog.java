package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import adapter.DBHelper;
import jung.jung.shinhan.shinhanex1.MainActivity;
import jung.jung.shinhan.shinhanex1.R;
import jung.jung.shinhan.shinhanex1.UpdateContactActivity;
import object.ChildInfo;


/**
 * Created by man on 2015-09-17.
 */

public class ContactDetailDialog extends Dialog {

    public ContactDetailDialog(Context context, final ChildInfo childInfo) {
        super(context);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_contatct_detail);


        final Activity activity = (Activity) context;


        TextView name = (TextView) findViewById(R.id.tv_name);
        TextView num1 = (TextView) findViewById(R.id.tv_number1);
        TextView num2 = (TextView) findViewById(R.id.tv_number2);
        TextView email = (TextView) findViewById(R.id.tv_email);
        ImageView call = (ImageView) findViewById(R.id.img_call);
        ImageView msg = (ImageView) findViewById(R.id.img_message);
        ImageView gallery = (ImageView) findViewById(R.id.img_gallery);
        ImageView memo = (ImageView) findViewById(R.id.img_memo);

        TextView update = (TextView) findViewById(R.id.update);
        TextView delete = (TextView) findViewById(R.id.delete);
        TextView o = (TextView) findViewById(R.id.o);

        try {
            name.setText(childInfo.getName());
        } catch (Exception e) {
        }
        try {
/*
            String ph1 = childInfo.getPh1();
            char[] cArr = ph1.toCharArray();
            ph1 = "";
            for(int i = 0; i < cArr.length; i++) {
                if(i ==3 || i==7) {
                    ph1 += "-";
                }
                ph1 += cArr[i] ;
            }
*/
            num1.setText(childInfo.getPh1());

        } catch (Exception e) {
        }
        try {
            num2.setText(childInfo.getPh2());
        } catch (Exception e) {
        }
        try {
            email.setText(childInfo.getEmail());
        } catch (Exception e) {
        }
//http://blog.naver.com/i_ehdfyd?Redirect=Log&logNo=50134125081
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+strPhoneNumber)); 전화걸기
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + childInfo.getPh1()));
                activity.startActivity(intent);
            }
        });
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                Uri uri = Uri.parse("sms:" + childInfo.getPh1());
                intent.setData(uri);
                activity.startActivity(intent);
            }
        });

        //http://mashroom.tistory.com/17
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{childInfo.getEmail()});
                email.putExtra(Intent.EXTRA_SUBJECT, "제목");
                email.putExtra(Intent.EXTRA_TEXT, " ");
                email.setType("message/rfc822");
                activity.startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
        memo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ContactDetailMemoDialog contactDetailMemoDialog = new ContactDetailMemoDialog(activity,childInfo);
                contactDetailMemoDialog.show();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ContactDetailGalleryDialog contactDetailGalleryDialog = new ContactDetailGalleryDialog(activity,childInfo);
                contactDetailGalleryDialog.show();
            }
        });
        // btn.setVisibility(View.GONE);
        o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                activity.finish();
                Intent intent = new Intent(activity, UpdateContactActivity.class);
                intent.putExtra("type", true);
                intent.putExtra("id", childInfo.getId());
                intent.putExtra("company", childInfo.getCompany());
                intent.putExtra("department", childInfo.getDepartment());
                intent.putExtra("service", childInfo.getService());
                intent.putExtra("name", childInfo.getName());
                intent.putExtra("grade", childInfo.getGrade());
                intent.putExtra("ph1", childInfo.getPh1());
                intent.putExtra("ph2", childInfo.getPh2());
                intent.putExtra("email", childInfo.getEmail());
                intent.putExtra("photo", childInfo.getPh2());
                activity.startActivity(intent);
                dismiss();
            }
        });

        final DBHelper events = new DBHelper(activity);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SQLiteDatabase db = events.getWritableDatabase();

                String sql = "DELETE FROM contacts WHERE company=" +
                        "'" + childInfo.getCompany() + "'AND department = " +
                        "'" + childInfo.getDepartment() + "'AND service = " +
                        "'" + childInfo.getService() + "'AND grade = " +
                        "'" + childInfo.getGrade() + "'AND name = " +
                        "'" + childInfo.getName() + "'AND ph1=" +
                        "'" + childInfo.getPh1() + "';";
                db.execSQL(sql);
                dismiss();
                activity.finish();
                activity.startActivity(new Intent(activity, MainActivity.class));
            }
        });


    }

}