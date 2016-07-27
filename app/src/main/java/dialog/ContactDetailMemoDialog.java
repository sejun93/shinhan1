package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import jung.jung.shinhan.shinhanex1.R;
import object.ChildInfo;


/**
 * Created by man on 2015-09-17.
 */

public class ContactDetailMemoDialog extends Dialog {

    public ContactDetailMemoDialog(Context context, final ChildInfo childInfo) {
        super(context);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_contatct_memo_detail);


        final Activity activity = (Activity) context;


        TextView memo = (TextView) findViewById(R.id.memo);
        memo.setText(childInfo.getMemo());
        TextView o = (TextView) findViewById(R.id.o);


        // btn.setVisibility(View.GONE);
        o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });




    }

}