package jung.jung.shinhan.shinhanex1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import adapter.CompareRule;
import adapter.DBHelper;
import dialog.ContactDetailDialog;
import dialog.ContactDetailGalleryDialog;
import dialog.MainMenuAddDialog;
import object.ChildInfo;
import object.FirstInfo;
import object.SecondInfo;
import util.utils;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private SearchView search;

    private LinkedHashMap<String, FirstInfo> firstInfoHash = new LinkedHashMap<String, FirstInfo>();
    private ArrayList<FirstInfo> firstList = new ArrayList<FirstInfo>();

    private SearchListAdapter listAdapter;
    private ExpandableListView myList;

    boolean all = false;
    static public ArrayList<ChildInfo> contactsList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("외부 연락처 관리 앱");
        }

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) findViewById(R.id.search);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);

        ImageView img_all = (ImageView) findViewById(R.id.img_all);
        img_all.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (all) {
                    collapseAll();
                    all = false;
                } else {
                    expandAll();
                    all = true;
                }
            }
        });


        final Button group_company = (Button) findViewById(R.id.group_company);
        final Button group_service = (Button) findViewById(R.id.group_service);
        group_company.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.TYPE = utils.COMPANY;
                changeGroup();
                group_company.setTextColor(getResources().getColor(R.color.white));
                group_company.setBackgroundColor(getResources().getColor(R.color.pointBlue));
                group_service.setTextColor(getResources().getColor(R.color.black));
                group_service.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        group_service.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                utils.TYPE = utils.SERVICE;
                changeGroup();
                group_company.setTextColor(getResources().getColor(R.color.black));
                group_company.setBackgroundColor(getResources().getColor(R.color.white));
                group_service.setTextColor(getResources().getColor(R.color.white));
                group_service.setBackgroundColor(getResources().getColor(R.color.pointBlue));
            }
        });
        //Just add some data to start with
        loadData();

        myList = (ExpandableListView) findViewById(R.id.myList);
        listAdapter = new SearchListAdapter(MainActivity.this, firstList);
        myList.setAdapter(listAdapter);
        collapseAll();

    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.expandGroup(i);
        }
    }


    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            myList.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData() {
        DBHelper events;
        events = new DBHelper(this);

        SQLiteDatabase db = events.getReadableDatabase();
        contactsList.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM contacts ORDER BY id DESC", null);
        startManagingCursor(cursor);

        StringBuffer buffer = new StringBuffer();

        while (cursor.moveToNext()) {
            ChildInfo childInfo = new ChildInfo();

            childInfo.setCompany(cursor.getString(1));
            childInfo.setDepartment(cursor.getString(2));
            childInfo.setService(cursor.getString(3));
            childInfo.setGrade(cursor.getString(4));
            childInfo.setName(cursor.getString(5));
            childInfo.setPh1(cursor.getString(6));
            childInfo.setPh2(cursor.getString(7));
            childInfo.setEmail(cursor.getString(8));
            childInfo.setPhoto(cursor.getString(9));
            childInfo.setMemo(cursor.getString(10));

            if (utils.TYPE == utils.COMPANY) {
                addProduct(childInfo.getCompany(), childInfo.getDepartment());
            } else if (utils.TYPE == utils.SERVICE) {
                addProduct(childInfo.getService(), childInfo.getDepartment());
            }
            contactsList.add(childInfo);


        }


    }


    //here we maintain our products in various departments
    private int addProduct(String first, String second) {

        int groupPosition = 0;

        FirstInfo firstInfo = firstInfoHash.get(first);
        if (firstInfo == null) {
            firstInfo = new FirstInfo();
            firstInfo.setName(first);
            firstInfoHash.put(first, firstInfo);
            firstList.add(firstInfo);
        }

        ArrayList<SecondInfo> secondInfoList = firstInfo.getSecondList();
        boolean checked = true;
        for (int i = 0; i < secondInfoList.size(); i++) {
            if (secondInfoList.get(i).getName().equals(second)) {
                checked = false;
                break;
            }
        }

        if (checked) {
            SecondInfo detailInfo = new SecondInfo();
            detailInfo.setName(second);
            secondInfoList.add(detailInfo);
            Collections.sort(secondInfoList, new CompareRule());
            firstInfo.setSecondList(secondInfoList);
        }


        //find the group position inside the list
        groupPosition = firstList.indexOf(firstInfo);
        return groupPosition;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private void changeGroup() {

        ResetloadData();
        myList = (ExpandableListView) findViewById(R.id.myList);
        listAdapter = new SearchListAdapter(MainActivity.this, firstList);
        myList.setAdapter(listAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                startActivity(new Intent(MainActivity.this, ShareActivity.class));
                //adapter.expandAll();
                return true;
            case R.id.action_excel:
                // startActivity(new Intent(MainActivity.this, TestTest.class));
                MainMenuAddDialog mainMenuAddDialog = new MainMenuAddDialog(MainActivity.this);
                mainMenuAddDialog.show();
                //          .setAction("Action", null).show();
                // adapter.collapseAll();MainMenuAddDialog addDailog = new MainMenuAddDialog(getAct)
                //Snackbar.make(, "준비중입니다", Snackbar.LENGTH_LONG)
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onClose() {
        ResetloadData();
        listAdapter = new SearchListAdapter(MainActivity.this, firstList);
        //attach the adapter to the list
        myList.setAdapter(listAdapter);
        return false;
    }

    private void ResetloadData() {
        firstInfoHash.clear();
        firstList.clear();

        for (int i = 0; i < contactsList.size(); i++) {
            ChildInfo childInfo = new ChildInfo();
            childInfo = contactsList.get(i);
            if (utils.TYPE == utils.COMPANY) {
                addProduct(childInfo.getCompany(), childInfo.getDepartment());
            } else if (utils.TYPE == utils.SERVICE) {
                addProduct(childInfo.getService(), childInfo.getDepartment());
            }
        }
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (query.equals("")) {
            ResetloadData();
            listAdapter = new SearchListAdapter(MainActivity.this, firstList);
            //attach the adapter to the list
            myList.setAdapter(listAdapter);
        } else {
            listAdapter.filterData(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.equals("")) {
            ResetloadData();
            listAdapter = new SearchListAdapter(MainActivity.this, firstList);
            //attach the adapter to the list
            myList.setAdapter(listAdapter);
        } else {
            listAdapter.filterData(query);
            expandAll();
        }
        return false;
    }


    public class SearchListAdapter extends BaseExpandableListAdapter {

        public void filterData(String query) {

            ArrayList<ChildInfo> contactsTempList = new ArrayList<>();
            contactsTempList.clear();

            ResetloadData();
            originalList = firstInfoList;

            if (query.isEmpty() || query.equals("")) {
                contactsTempList.addAll(contactsList);
            } else {
                for (FirstInfo continent : originalList) {
                    ArrayList<SecondInfo> secondlist = continent.getSecondList();
                    for (SecondInfo second : secondlist) {
                        ArrayList<ChildInfo> childlist = JsonChildInfo(continent.getName(), second.getName());
                        //ArrayList<ChildInfo> childlist = second.getChildList();

                        for (int i = 0; i < childlist.size() - 1; i++) {
                            ChildInfo temp = childlist.get(i);
                            boolean check = false;
                            try {

                                check = temp.getName().toString().contains(query)
                                        || temp.getCompany().toString().contains(query)
                                        || temp.getDepartment().toString().contains(query)
                                        || temp.getService().toString().contains(query);

                            } catch (Exception e) {
                                Log.v("lowercaseERROR", temp.getName());
                                check = false;
                            }

                            if (check) {
                                contactsTempList.add(temp);
                            }
                        }
                    }
                }
                firstInfoHash.clear();
                firstList.clear();
                for (int i = 0; i < contactsTempList.size(); i++) {

                    if (utils.TYPE == utils.COMPANY) {
                        addProduct(contactsTempList.get(i).getCompany(), contactsTempList.get(i).getDepartment());
                    } else if (utils.TYPE == utils.SERVICE) {
                        addProduct(contactsTempList.get(i).getService(), contactsTempList.get(i).getDepartment());
                    }
                }
                listAdapter = new SearchListAdapter(MainActivity.this, firstList);
                //attach the adapter to the list
                myList.setAdapter(listAdapter);

                expandAll();
            }
        }

        private Context context;
        private ArrayList<FirstInfo> firstInfoList;
        private ArrayList<FirstInfo> originalList;

        public SearchListAdapter(Context context, ArrayList<FirstInfo> firstInfoList) {
            this.context = context;
            this.firstInfoList = new ArrayList<FirstInfo>();
            this.firstInfoList = firstInfoList;
            this.originalList = new ArrayList<FirstInfo>();
            this.originalList.addAll(firstInfoList);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            ArrayList<SecondInfo> secondList = firstInfoList.get(groupPosition).getSecondList();
            return secondList.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View view, ViewGroup parent) {

            SecondInfo secondInfo = (SecondInfo) getChild(groupPosition, childPosition);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.item_second_row, null);
            }

            ListView child_list = (ListView) view.findViewById(R.id.lv);
            ArrayList<ChildInfo> childIInfoList = JsonChildInfo(firstInfoList.get(groupPosition).getName(), secondInfo.getName());
            adapter1 = new ViewAdapter(MainActivity.this, R.layout.activity_main, childIInfoList
                    , firstInfoList.get(groupPosition).getName(), secondInfo.getName());
            child_list.setAdapter(adapter1);
            setListViewHeightBasedOnItems(child_list);
            final ImageView arrow = (ImageView) view.findViewById(R.id.item_arrow);

            //secondInfo.setChildList(childIInfoList);
            // ((SecondInfo) getChild(groupPosition, childPosition)).setChildList(childIInfoList);

            RelativeLayout item = (RelativeLayout) view.findViewById(R.id.item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.findViewById(R.id.lv).getVisibility() == View.VISIBLE) {
                        arrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_down));
                        view.findViewById(R.id.lv).setVisibility(View.GONE);
                    } else {
                        arrow.setImageDrawable(getResources().getDrawable(R.drawable.arrow_right));

                        view.findViewById(R.id.lv).setVisibility(View.VISIBLE);
                    }
                }
            });

            TextView childItem = (TextView) view.findViewById(R.id.childItem);
            childItem.setText(secondInfo.getName().trim());

            return view;
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            ArrayList<SecondInfo> productList = firstInfoList.get(groupPosition).getSecondList();
            return productList.size();

        }

        @Override
        public Object getGroup(int groupPosition) {
            return firstInfoList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return firstInfoList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isLastChild, View view,
                                 ViewGroup parent) {

            FirstInfo headerInfo = (FirstInfo) getGroup(groupPosition);
            if (view == null) {
                LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inf.inflate(R.layout.item_first_row, null);
            }

            TextView heading = (TextView) view.findViewById(R.id.heading);
            heading.setText(headerInfo.getName().trim());

            return view;
        }

        public void setListViewHeightBasedOnItems(ListView listView) {

            // Get list adpter of listview;
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) return;

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


    }


    ViewAdapter adapter1;

    class ViewAdapter extends ArrayAdapter<ChildInfo> {

        ArrayList<ChildInfo> list;
        String company, department;

        public ViewAdapter(Context context, int resource, ArrayList<ChildInfo> objects, String company, String department) {
            super(context, resource, objects);
            list = objects;
            this.company = company;
            this.department = department;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ChildInfo child = list.get(position);
            if (child != null) {

                if (!child.getName().equals("lastlast")) {
                    LayoutInflater linf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = linf.inflate(R.layout.item_third_row, null);



                    if (utils.TYPE == utils.COMPANY) {
                        ((TextView) convertView.findViewById(R.id.item_name)).setText("[" + child.getService() + "]" + child.getName() + child.getGrade());
                    } else {
                        ((TextView) convertView.findViewById(R.id.item_name)).setText("[" + child.getCompany() + "]" + child.getName() + child.getGrade());
                    }
                    convertView.findViewById(R.id.item).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ContactDetailDialog contactDetailDialog = new ContactDetailDialog(MainActivity.this, child);
                            contactDetailDialog.show();
                        }
                    });

                    convertView.findViewById(R.id.msg).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            Uri uri = Uri.parse("sms:" + child.getPh1());
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                    convertView.findViewById(R.id.call).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + child.getPh1()));
                            startActivity(intent);
                        }
                    });
                    convertView.findViewById(R.id.gallery).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ContactDetailGalleryDialog contactDetailGalleryDialog = new ContactDetailGalleryDialog(MainActivity.this,child);
                            contactDetailGalleryDialog.show();
                        }
                    });

                    //
                } else {
                    LayoutInflater linf = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = linf.inflate(R.layout.item_third_add_row, null);
                    convertView.findViewById(R.id.item).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, UpdateContactActivity.class);
                            intent.putExtra("type", false);
                            intent.putExtra("company", company);
                            intent.putExtra("department", department);
                            startActivity(intent);
                        }
                    });
                }
            }

            return convertView;
        }

    }

    private ArrayList<ChildInfo> JsonChildInfo(String company, String department) {


        ArrayList<ChildInfo> list = new ArrayList<ChildInfo>();
        list.clear();


        for (int i = 0; i < contactsList.size(); i++) {
            ChildInfo childInfo = contactsList.get(i);

            if (utils.TYPE == utils.COMPANY) {
                if (childInfo.getCompany().equals(company) && childInfo.getDepartment().equals(department)) {
                    list.add(childInfo);
                }
            } else {
                if (childInfo.getService().equals(company) && childInfo.getDepartment().equals(department)) {
                    list.add(childInfo);
                }
            }
        }
        ChildInfo childInfo = new ChildInfo();
        childInfo.setName("lastlast");
        list.add(childInfo);


        return list;
    }


}

