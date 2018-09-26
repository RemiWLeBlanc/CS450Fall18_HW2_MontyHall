package com.example.remileblanc.montyhall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class MainFragment extends Fragment {


    public static final String PREF_NAME = "MontyHall";
    public static final String NEW_CLICKED = "NewClicked";
    public static final String CONT_CLICKED = "ContinueClicked";
    private OnFragmentInteractionListener mListener;


    private AudioAttributes aa = null;
    private SoundPool soundPool = null;
    private int clickSound = 0;

    public MainFragment() {
        // Required empty public constructor
    }


    //onCreate get called when te fragment is created
    //before the UI views are constructed
    //initialize data needed for the fragment
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); //it will keep the values of instance variables

        this.aa = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();

        this.soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(aa).build();
        this.clickSound = this.soundPool.load(getContext(), R.raw.click, 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_main, container, false);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        View aboutButton = rootView.findViewById(R.id.about_button);
        aboutButton.setSoundEffectsEnabled(false);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); //this gets the activity that the fragment is in
                builder.setTitle(R.string.about_title_text);
                builder.setMessage(R.string.about_text);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                builder.show();
                soundPool.play(clickSound, 1f, 1f,1,0, 1f);

            }
        });

        View newButton = rootView.findViewById(R.id.new_button);
        newButton.setSoundEffectsEnabled(false);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor pref_ed = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
                pref_ed.putBoolean(NEW_CLICKED, true).apply();

                SharedPreferences.Editor pref_ed2 = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
                pref_ed2.putBoolean(CONT_CLICKED, false).apply();

                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
                soundPool.play(clickSound, 1f, 1f,1,0, 1f);



            }
        });

        View contButton = rootView.findViewById(R.id.cont_button);
        contButton.setSoundEffectsEnabled(false);
        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor pref_ed = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
                pref_ed.putBoolean(CONT_CLICKED, true).apply();

                SharedPreferences.Editor pref_ed2 = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
                pref_ed2.putBoolean(NEW_CLICKED, false).apply();

                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
                soundPool.play(clickSound, 1f, 1f,1,0, 1f);

            }
        });

        return rootView;
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
