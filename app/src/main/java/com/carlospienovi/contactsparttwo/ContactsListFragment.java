package com.carlospienovi.contactsparttwo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactsListFragment extends ListFragment {


    private static final String LOG_TAG = ContactsListFragment.class.getSimpleName();
    private static final int REQUEST_CODE_CREATE_CONTACT = 1;

    ContactAdapter mAdapter;
    DatabaseHelper mDBHelper = null;

    public DatabaseHelper getDBHelper() {
        if (mDBHelper == null) {
            mDBHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return mDBHelper;
    }

    public ContactsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_stars_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                newContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void newContact() {
        Intent i = new Intent(getActivity(), CreateUpdateContactActivity.class);
        startActivityForResult(i, REQUEST_CODE_CREATE_CONTACT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CREATE_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    Contact newContact = data
                            .getParcelableExtra(CreateUpdateContactActivity.NEW_CONTACT);
                    int index = data.getIntExtra(CreateUpdateContactActivity.CONTACT_POSITION, -1);
                    if (index != -1) {
                        mAdapter.removeByPosition(index);
                    }
                    mAdapter.add(newContact);
                }
                if (resultCode == CreateUpdateContactActivity.DELETE_CONTACT) {
                    int index = data.getIntExtra(CreateUpdateContactActivity.CONTACT_POSITION, -1);
                    if (index != -1) {
                        mAdapter.removeByPosition(index);
                    }
                }
                break;
        }
    }

    private List<Contact> getContacts() {
        try {
            Dao<Contact, Integer> dao = getDBHelper().getDocumentDao();
            return dao.queryForAll();
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareListView();
    }

    private void prepareListView() {
        List<Contact> entries = getContacts();
        mAdapter = new ContactAdapter(getActivity(), getDBHelper(), entries);
        setListAdapter(mAdapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("coso", "contact clicked");
                Intent i = new Intent(getActivity(), CreateUpdateContactActivity.class);
                i.putExtra(CreateUpdateContactActivity.CONTACT_TO_EDIT, mAdapter.getItem(position));
                i.putExtra(CreateUpdateContactActivity.CONTACT_POSITION, position);
                startActivityForResult(i, REQUEST_CODE_CREATE_CONTACT);
            }
        });
    }
}
