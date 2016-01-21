package lybrica.easyreminders.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import lybrica.easyreminders.Activities.MainActivity;
import lybrica.easyreminders.DBAdapter;
import lybrica.easyreminders.R;

public class FragmentB extends Fragment {

    ListView lr;
    FragmentMore fragmentMore;
    DBAdapter myDb;
    SimpleCursorAdapter myCursorAdapter;

    public static FragmentB getInstance(int position) {
        FragmentB fragmentB = new FragmentB();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragmentB.setArguments(args);
        return fragmentB;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_b, container, false);

        openDB();
        //populateListViewFromDB();

        lr = (ListView) layout.findViewById(R.id.listR);

        return layout;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Cursor cursor = myDb.getAllRows();

        // Manages cursor lifetime
        getActivity().startManagingCursor(cursor);

        // Mapping cursor
        String[] fromFieldNames = new String[]
                {DBAdapter.KEY_TITLE, DBAdapter.KEY_NAME, DBAdapter.KEY_FAVCOLOUR, DBAdapter.KEY_STUDENTNUM};
        int[] toViewIDs = new int[]{R.id.item_title, R.id.item_name, R.id.item_color, R.id.item_time};

        myCursorAdapter =
                new SimpleCursorAdapter(
                        getActivity(),        // Context
                        R.layout.item_layout,    // Row layout template
                        cursor,                    // cursor (set of DB records to map)
                        fromFieldNames,            // DB Column names
                        toViewIDs                // View IDs to put information in
                );

        // Set the adapter for the list view
        lr.setAdapter(myCursorAdapter);


        lr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long idInDB) {

                showMoreDialog(idInDB);
            }
        });
        lr.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {


                ((MainActivity) getActivity()).itemDone(id);

                return true;
            }
        });

        lr.setVisibility((myCursorAdapter.isEmpty()) ? View.GONE : View.VISIBLE);
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }



    private void openDB() {
        myDb = new DBAdapter(getActivity());
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    private void showMoreDialog(long idInDB) {
        Cursor cursor = myDb.getRow(idInDB);
        if (cursor.moveToFirst()) {
            long idDB = cursor.getLong(DBAdapter.COL_ROWID);
            String title = cursor.getString(DBAdapter.COL_TITLE);
            String name = cursor.getString(DBAdapter.COL_NAME);
            String studentNum = cursor.getString(DBAdapter.COL_STUDENTNUM);
            String favColour = cursor.getString(DBAdapter.COL_FAVCOLOUR);
            int style = cursor.getInt(DBAdapter.COL_STYLE);

            ((MainActivity)getActivity()).showMore(idDB, title, name, studentNum,favColour,style);
        }
        cursor.close();
    }

}