package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import jung.jung.shinhan.shinhanex1.AddContactActivity;
import jung.jung.shinhan.shinhanex1.R;
import jung.jung.shinhan.shinhanex1.UpdateContactActivity;


/**
 * Created by man on 2015-09-17.
 */

public class MainMenuAddDialog extends Dialog {


    public MainMenuAddDialog(Context context) {
        super(context);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_main_menu_add);
// ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "namsan.ttf"));


        final Activity activity = (Activity) context;


        TextView person = (TextView) findViewById(R.id.tv_person);
        TextView excel = (TextView) findViewById(R.id.tv_excel);
        TextView contact = (TextView) findViewById(R.id.tv_contact);
        TextView sms = (TextView) findViewById(R.id.tv_sms);



        TextView o = (TextView) findViewById(R.id.o);



        person.setTypeface(Typeface.createFromAsset(context.getAssets(), "namsan.ttf"));
        excel.setTypeface(Typeface.createFromAsset(context.getAssets(), "namsan.ttf"));
        contact.setTypeface(Typeface.createFromAsset(context.getAssets(), "namsan.ttf"));
        sms.setTypeface(Typeface.createFromAsset(context.getAssets(), "namsan.ttf"));
        o.setTypeface(Typeface.createFromAsset(context.getAssets(), "namsan.ttf"));

        // btn.setVisibility(View.GONE);
        o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();

                Intent intent = new Intent(activity, UpdateContactActivity.class);
                intent.putExtra("type", false);
                activity.startActivity(intent);

                dismiss();
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, AddContactActivity.class));
                dismiss();
            }
        });
        excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

}