package jung.jung.shinhan.shinhanex1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

import adapter.Contact;
import adapter.ContactAdapter;
import adapter.DBHelper;

public class AddContactActivity extends AppCompatActivity {


    ListView listView;
    private ArrayList<Contact> list = new ArrayList<>();
    private LinkedHashMap<String, Contact> firstInfoHash = new LinkedHashMap<String, Contact>();
    private ArrayList<Contact> selected_contacts = new ArrayList<>();
    ArrayList<String> selected_phones = new ArrayList<String>();
    static final int DONE = Menu.FIRST;
    android.app.ActionBar acBar;
    Toolbar toolbar;

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.contact_list);

        context = getApplicationContext();
        acBar = getActionBar();


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Select Contact");
        }

        //acBar.setDisplayHomeAsUpEnabled(true);

        FetchContactonBackground();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
        startActivity(new Intent(AddContactActivity.this, MainActivity.class));
    }
    private void FetchContactonBackground() {
        final Handler handlert = new Handler();
        Thread loginUserThread = new Thread(new Runnable() {
            public void run() {
                FetchContact();
                handlert.post(new Runnable() {
                    public void run() {

                        ContactAdapter objAdapter = new ContactAdapter(context, list, true);
                        listView.setAdapter(objAdapter);
                        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                        listView.setTextFilterEnabled(true);
                        setClickEvents();

                    }

                });
            }
        });
        loginUserThread.start();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    private void FetchContact() {
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";

        Cursor phones = context.getContentResolver().query(

                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
                null, sort);
        while (phones.moveToNext()) {

            String name = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            String contactid = phones
                    .getString(phones
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

            Contact objContact = new Contact();
            objContact.setName(capitallizeString(name));
            objContact.setPhoneNo(phoneNumber);
            objContact.setContactId(contactid);
            objContact.setSelected(false);
            list.add(objContact);

        }


        phones.close();


    }

    public String capitallizeString(String text) {
        return String.valueOf(text.charAt(0)).toUpperCase(Locale.getDefault()) + text.subSequence(1, text.length());
    }


    private void setClickEvents() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkbox = (CheckBox) view.getTag(R.id.check);
                list.get(position).setSelected(view.isSelected()); // Set the value of checkbox to maintain its state.
                if (checkbox.isChecked()) {

                    firstInfoHash.remove(list.get(position).getPhoneNo());
                    checkbox.setChecked(false);
                } else {

                    firstInfoHash.put(list.get(position).getPhoneNo(), list.get(position));
                    checkbox.setChecked(true);
                }
                showSelected();
                //checkbox.setChecked(view.isSelected());
            }

        });
    }


    private void showSelected() {
        int selected = 0;
        String g=null;
        int len = listView.getCount();
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        for (int i = 0; i < len; i++) {
            if (checked.get(i)) {
                g = list.get(i).getPhoneNo();
                g = g.replace("-", "");
                g = g.replace(" ", "");
                g = g.replace("(", "");
                g = g.replace(")", "");
                selected_phones.add(g);
                selected++;
            }
        }
        getSupportActionBar().setTitle(selected + " selected");




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, DONE, Menu.NONE, "DONE")
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                startActivity(new Intent(AddContactActivity.this, MainActivity.class));
                return true;
            case DONE:
                SendDataBack();
                return true;
            default:
                return false;
        }
    }


    private void SendDataBack() {
        DBHelper events; events = new DBHelper(this);
        SQLiteDatabase db = events.getWritableDatabase();

        for(int i=0;i<list.size();i++){

            if(firstInfoHash.get(list.get(i).getPhoneNo())!=null) {
                Contact temp = list.get(i);
                if(temp.getImage()==null) temp.setImage("null");
                if(temp.getPhoneNo()==null) temp.setPhoneNo("null");
                if(temp.getName()==null) temp.setName("null");
                String sql = "INSERT INTO contacts (id, company,department,service,grade,name,ph1,ph2,email,photo) " +
                        "VALUES (NULL,'미정', '미정','미정','미정','"  +
                        temp.getName()+
                        "','"+
                        temp.getPhoneNo() +
                        "','미정','미정','" +
                        temp.getImage()+"');";
                db.execSQL(sql);
            }
        }
        Intent returnIntent = new Intent();
        //Utilities.toast(context, "Selected: "+selected_phones);
        returnIntent.putExtra("result", selected_phones);
        setResult(RESULT_OK, returnIntent);

        finish();
        startActivity(new Intent(AddContactActivity.this, MainActivity.class));
    }


    private void returnEmptyData() {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }


}