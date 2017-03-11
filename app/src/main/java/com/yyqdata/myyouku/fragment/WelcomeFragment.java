package com.yyqdata.myyouku.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.yyqdata.myyouku.R;
import com.yyqdata.myyouku.adapter.WelcomeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WelcomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ImageButton mainSearchBtn;
    private ImageButton mainMenuBtn;
    private SlidingTabLayout mainTabLayout;
    private ViewPager welcomeFragmentVP;
    private List<Fragment> fragments;
    private WelcomeAdapter welcomeAdapter;
    private String[] names = {"头条","精选","火星情报局"};
    public WelcomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WelcomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WelcomeFragment newInstance(String param1, String param2) {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);
        initView(view);
        initDate();
        setAdapter();
        return view;
    }

    private void setAdapter() {
        welcomeAdapter = new WelcomeAdapter(getChildFragmentManager(),fragments);
        welcomeFragmentVP.setAdapter(welcomeAdapter);
        mainTabLayout.setIndicatorColor(Color.parseColor("#2FB3FF"));
        mainTabLayout.setIndicatorWidthEqualTitle(true);
        mainTabLayout.setTextUnselectColor(Color.parseColor("#000000"));
        mainTabLayout.setTextSelectColor(Color.parseColor("#2FB3FF"));
        mainTabLayout.setViewPager(welcomeFragmentVP,names);
        welcomeFragmentVP.setCurrentItem(1);
    }


    private void initDate() {
        fragments = new ArrayList<>();
        fragments.add(new FirstTipFragment());
        fragments.add(new WellChoseFragment());
        fragments.add(new LiyueFragment());
    }

    private void initView(View view) {
        mainSearchBtn = (ImageButton) view.findViewById(R.id.main_search_btn);
        mainMenuBtn = (ImageButton) view.findViewById(R.id.main_head_menu_btn);
        mainTabLayout = (SlidingTabLayout) view.findViewById(R.id.main_tab_layout);
        welcomeFragmentVP = (ViewPager) view.findViewById(R.id.welcome_fragment_vp);

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
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
