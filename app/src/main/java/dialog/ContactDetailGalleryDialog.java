package dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import jung.jung.shinhan.shinhanex1.R;
import object.ChildInfo;


/**
 * Created by man on 2015-09-17.
 */

public class ContactDetailGalleryDialog extends Dialog {

    public ContactDetailGalleryDialog(Context context, final ChildInfo childInfo) {
        super(context);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_contatct_gallery_detail);


        final Activity activity = (Activity) context;


        ImageView img = (ImageView) findViewById(R.id.img);
        TextView o = (TextView) findViewById(R.id.o);

        Bitmap bmp = loadBitmap(childInfo.getPhoto());
        img.setImageBitmap(bmp);


        // btn.setVisibility(View.GONE);
        o.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    //file에서  bitmap 불러오기
    Bitmap loadBitmap(String p)
    {
        //용량이 너무큰 그림 최적화
        BitmapFactory.Options option = new BitmapFactory.Options();
        return BitmapFactory.decodeFile(p, option);
    }


}