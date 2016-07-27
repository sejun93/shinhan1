package jung.jung.shinhan.shinhanex1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import adapter.DBHelper;
import object.ChildInfo;

public class UpdateContactActivity extends AppCompatActivity {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private ImageView ivImage;
    EditText service, name, grade, ph1, ph2, email, memo;
    Button ok, cancle;
    String photo;
    boolean update = false;
    AutoCompleteTextView autoCompleteCompany, autoCompleteDepartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        update = getIntent().getBooleanExtra("type", false);
        if (update)
            actionBar.setTitle("연락처 수정");
        else
            actionBar.setTitle("연락처 추가");


        ok = (Button) findViewById(R.id.ok);
        cancle = (Button) findViewById(R.id.cancle);
        autoCompleteCompany = (AutoCompleteTextView) findViewById(R.id.tv_company);
        autoCompleteDepartment = (AutoCompleteTextView) findViewById(R.id.tv_department);
        service = (EditText) findViewById(R.id.tv_service);
        name = (EditText) findViewById(R.id.tv_name);
        grade = (EditText) findViewById(R.id.tv_grade);
        ph1 = (EditText) findViewById(R.id.tv_ph1);
        ph2 = (EditText) findViewById(R.id.tv_ph2);
        email = (EditText) findViewById(R.id.tv_email);
        memo = (EditText) findViewById(R.id.tv_memo);


        final ChildInfo childInfo = new ChildInfo();
        childInfo.setId(getIntent().getStringExtra("id"));
        childInfo.setCompany(getIntent().getStringExtra("company"));
        childInfo.setDepartment(getIntent().getStringExtra("department"));
        childInfo.setService(getIntent().getStringExtra("service"));
        childInfo.setName(getIntent().getStringExtra("name"));
        childInfo.setGrade(getIntent().getStringExtra("grade"));
        childInfo.setPh1(getIntent().getStringExtra("ph1"));
        childInfo.setPh2(getIntent().getStringExtra("ph2"));
        childInfo.setEmail(getIntent().getStringExtra("email"));
        childInfo.setPhoto(getIntent().getStringExtra("photo"));
        childInfo.setMemo(getIntent().getStringExtra("memp"));

        if (childInfo.getCompany() != null) {
            autoCompleteCompany.setText(childInfo.getCompany().toString());
        }
        if (childInfo.getDepartment() != null) {
            autoCompleteDepartment.setText(childInfo.getDepartment().toString());
        }

        if (childInfo.getService() != null) {
            service.setText(childInfo.getService().toString());
        }
        if (childInfo.getName() != null) {
            name.setText(childInfo.getName().toString());
        }
        if (childInfo.getGrade() != null) {
            grade.setText(childInfo.getGrade().toString());
        }
        if (childInfo.getPh1() != null) {
            ph1.setText(childInfo.getPh1().toString());
        }
        if (childInfo.getPh2() != null) {
            ph2.setText(childInfo.getPh2().toString());
        }
        if (childInfo.getEmail() != null) {
            email.setText(childInfo.getEmail().toString());
        }
        if (childInfo.getMemo() != null) {
            memo.setText(childInfo.getMemo().toString());
        }


        ArrayList<String> companyList = new ArrayList<>();
        ArrayList<String> departmentList = new ArrayList<>();
        for (int i = 0; i < MainActivity.contactsList.size(); i++) {
            companyList.add(MainActivity.contactsList.get(i).getCompany());
            departmentList.add(MainActivity.contactsList.get(i).getDepartment());
            Log.v("listDebugg", i + ":" + companyList.get(i).toString());
        }

        companyList = new ArrayList<String>(new HashSet<String>(companyList));
        Collections.sort(companyList);
        departmentList = new ArrayList<String>(new HashSet<String>(departmentList));
        Collections.sort(departmentList);

        autoCompleteCompany.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, companyList));
        autoCompleteCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        autoCompleteDepartment.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, departmentList));
        autoCompleteDepartment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ivImage = (ImageView) findViewById(R.id.img);
        findViewById(R.id.btn_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactPhotoDialog contactPhotoDialog = new ContactPhotoDialog(UpdateContactActivity.this);
                contactPhotoDialog.show();

            }
        });

        final DBHelper events = new DBHelper(this);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (update) {    //수정화면이면
                    SQLiteDatabase db = events.getWritableDatabase();

                    String sql = "DELETE FROM contacts WHERE company=" +
                            "'" + childInfo.getCompany() + "'AND department = " +
                            "'" + childInfo.getDepartment() + "'AND service = " +
                            "'" + childInfo.getService() + "'AND grade = " +
                            "'" + childInfo.getGrade() + "'AND name = " +
                            "'" + childInfo.getName() + "'AND ph1=" +
                            "'" + childInfo.getPh1() + "';";
                    db.execSQL(sql);

                    sql = "INSERT INTO contacts (id, company,department,service,grade,name,ph1,ph2,email,photo,memo) " +
                            "VALUES (NULL," +
                            "'" + autoCompleteCompany.getText().toString() + "', " +
                            "'" + autoCompleteDepartment.getText().toString() + "'," +
                            "'" + service.getText().toString() + "'," +
                            "'" + grade.getText().toString() + "'," +
                            "'" + name.getText().toString() + "'," +
                            "'" + ph1.getText().toString() + "'," +
                            "'" + ph2.getText().toString() + "'," +
                            "'" + email.getText().toString() + "'," +
                            "'" + photo + "'," +
                            "'" + memo.getText().toString() + "');";
                    db.execSQL(sql);
                } else {  // 추가화면이면

                    SQLiteDatabase db = events.getWritableDatabase();
                    String sql = "INSERT INTO contacts (id, company,department,service,grade,name,ph1,ph2,email,photo,memo) " +
                            "VALUES (NULL," +
                            "'" + autoCompleteCompany.getText().toString() + "', " +
                            "'" + autoCompleteDepartment.getText().toString() + "'," +
                            "'" + service.getText().toString() + "'," +
                            "'" + grade.getText().toString() + "'," +
                            "'" + name.getText().toString() + "'," +
                            "'" + ph1.getText().toString() + "'," +
                            "'" + ph2.getText().toString() + "'," +
                            "'" + email.getText().toString() + "'," +
                            "'" + photo + "'," +
                            "'" + memo.getText().toString() + "');";
                    db.execSQL(sql);
                }
                finish();
                startActivity(new Intent(UpdateContactActivity.this, MainActivity.class));
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                startActivity(new Intent(UpdateContactActivity.this, MainActivity.class));

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(UpdateContactActivity.this, MainActivity.class));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                startActivity(new Intent(UpdateContactActivity.this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        final String strpath = Environment.getExternalStorageDirectory().getAbsolutePath();
        final File myDir = new File(strpath + "/shinhanPhoto");
        myDir.mkdir();

        photo = "/shinhanPhoto" + System.currentTimeMillis() + ".jpg";
        File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + photo);

        photo = Environment.getExternalStorageDirectory().getAbsolutePath() + photo;

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        Uri uri = data.getData();
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ;
        Toast.makeText(UpdateContactActivity.this, uriToPath(uri), Toast.LENGTH_LONG).show();

        ivImage.setImageBitmap(bm);
    }

    private String uriToPath(Uri uri) {
        String res = null;
        String[] image_data = {MediaStore.Images.Media.DATA};
        Cursor cur = getContentResolver().query(uri, image_data, null, null, null);
        if (cur.moveToFirst()) {
            int col = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cur.getString(col);
        }
        cur.close();
        photo = res;
        return res;
    }

    public class ContactPhotoDialog extends Dialog {

        public ContactPhotoDialog(Context context) {
            super(context);


            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_contact_photo);

            Button camera = (Button) findViewById(R.id.btn_camera);
            Button gallery = (Button) findViewById(R.id.btn_gallery);
            Button o = (Button) findViewById(R.id.btn_o);


            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();

                    cameraIntent();
                }
            });

            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    galleryIntent();
                }
            });

            o.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });


        }

    }


}

