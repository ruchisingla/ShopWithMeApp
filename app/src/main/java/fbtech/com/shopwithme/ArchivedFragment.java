package fbtech.com.shopwithme;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fbtech.com.shopwithme.database.AppDatabase;
import fbtech.com.shopwithme.database.ItemEntity;
import fbtech.com.shopwithme.database.ItemViewModel;
import fbtech.com.shopwithme.database.ItemViewModelFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ArchivedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArchivedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArchivedFragment extends Fragment implements ItemAdapter.onItemClickListner{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AppDatabase dbInstance = null;
    private static final String LOG_TAG = ListItemsActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private final int REQ_CODE = 100;
    private String listName,units;
    private float quantity;
    private static int taskId = 0;

    public ArchivedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArchivedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArchivedFragment newInstance(String param1, String param2) {
        ArchivedFragment fragment = new ArchivedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void fetchitemList()
    {
        Log.d(LOG_TAG,"I am in on resume of mainactivity");

        ItemViewModelFactory factory = new ItemViewModelFactory(dbInstance,taskId);
        final ItemViewModel viewModel= ViewModelProviders.of(this,factory).get(ItemViewModel.class);

        viewModel.getItems().observe(this, new Observer<List<ItemEntity>>() {
            @Override
            public void onChanged(@Nullable List<ItemEntity> itemEntities) {
                //viewModel.getItems().removeObserver(this);
                itemAdapter.setItemList(itemEntities);
            }
        });


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =inflater.inflate(R.layout.activity_list_items, container, false);
     

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onImageClick(ItemEntity itemEntity, View v) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
