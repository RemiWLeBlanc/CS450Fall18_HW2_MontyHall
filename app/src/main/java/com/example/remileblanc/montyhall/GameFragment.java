package com.example.remileblanc.montyhall;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    private ImageButton door1 = null;
    private ImageButton door2 = null;
    private ImageButton door3 = null;
    private int carDoor = -1;
    private boolean imageClicked = false;
    private LinearLayout switchLayout = null;
    private Button yesChange = null;
    private Button noChange = null;
    private LinearLayout playAgainLayout = null;
    private Button playAgain = null;
    private TextView winsView = null;
    private TextView lossesView = null;
    private TextView total = null;
    private int doorChosen = -1;
    private int doorToOpen = -1;
    private int wins;
    private int losses;
    private Timer timer;
    private Counter ctr;
    private AudioAttributes aa = null;
    private SoundPool soundPool = null;
    private int goatSound = 0;
    private int carSound = 1;
    private int clickSound = 2;
    //private int count = 0;


    public GameFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        gameSetUp();

        this.aa = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_GAME).build();

        this.soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(aa).build();
        this.goatSound = this.soundPool.load(getContext(), R.raw.goat, 1);
        this.carSound = this.soundPool.load(getContext(), R.raw.car, 1);
        this.clickSound = this.soundPool.load(getContext(), R.raw.click, 1);


    }

    @Override
    public void onPause() {
        super.onPause();

        this.getActivity().getPreferences(MODE_PRIVATE).edit().putInt("WINS", wins).apply();
        this.getActivity().getPreferences(MODE_PRIVATE).edit().putInt("LOSSES", losses).apply();


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View currentView = inflater.inflate(R.layout.fragment_game, container, false);
        initializeView(currentView);
        return currentView;
    }

    public void initializeView(View currentView){

        this.door1 = currentView.findViewById(R.id.door1);
        this.door2 = currentView.findViewById(R.id.door2);
        this.door3 = currentView.findViewById(R.id.door3);

        this.switchLayout = currentView.findViewById(R.id.switch_layout);
        this.noChange = currentView.findViewById(R.id.noChange);
        this.yesChange = currentView.findViewById(R.id.yesChange);
        noChange.setBackgroundColor(Color.RED);
        yesChange.setBackgroundColor(Color.GREEN);

        noChange.setSoundEffectsEnabled(false);
        yesChange.setSoundEffectsEnabled(false);


        switchLayout.setVisibility(View.INVISIBLE);

        this.playAgainLayout = currentView.findViewById(R.id.play_again_layout);
        this.playAgain = currentView.findViewById(R.id.play_again);

        playAgain.setSoundEffectsEnabled(false);
        playAgainLayout.setVisibility(View.INVISIBLE);

        this.winsView = currentView.findViewById(R.id.win_total);
        this.lossesView = currentView.findViewById(R.id.loss_total);
        this.total = currentView.findViewById(R.id.game_total);

        boolean contClicked = this.getActivity().getSharedPreferences("MontyHall", MODE_PRIVATE).getBoolean("ContinueClicked", false);
        boolean newClicked = this.getActivity().getSharedPreferences("MontyHall", MODE_PRIVATE).getBoolean("NewClicked", false);


        if(contClicked) {
            this.wins = this.getActivity().getPreferences(MODE_PRIVATE).getInt("WINS", 0);
            this.losses = this.getActivity().getPreferences(MODE_PRIVATE).getInt("LOSSES", 0);
        } else if(newClicked){
            wins = 0;
            losses = 0;
        } else {
            wins = 0;
            losses = 0;
        }

//        SharedPreferences sharedPreferences = getDefaultSharedPreferences(getContext());
//        Boolean contClicked = sharedPreferences.getBoolean("ContinueClicked", false);
//
//        System.out.println(contClicked);


        winsView.setText("\t"+Integer.toString(wins));
        lossesView.setText("\t"+Integer.toString(losses));
        int tot = wins+losses;
        total.setText("\t"+Integer.toString(tot));

        this.door1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imageClicked) {
                    doorChosen = 1;
                    door1.setImageLevel(1);
                    imageClicked = true;
                    showGoat();
                    switchLayout.setVisibility(View.VISIBLE);
                }
                Animator anim = AnimatorInflater.loadAnimator(getContext(), R.animator.door_shake);
                anim.setTarget(door1);
                anim.start();
            }
        });

        this.door2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imageClicked) {
                    doorChosen = 2;
                    door2.setImageLevel(1);
                    imageClicked = true;
                    showGoat();
                    switchLayout.setVisibility(View.VISIBLE);
                }
                Animator anim = AnimatorInflater.loadAnimator(getContext(), R.animator.door_shake);
                anim.setTarget(door2);
                anim.start();
            }
        });

        this.door3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imageClicked) {
                    doorChosen = 3;
                    door3.setImageLevel(1);
                    imageClicked = true;
                    showGoat();
                    switchLayout.setVisibility(View.VISIBLE);
                }
                Animator anim = AnimatorInflater.loadAnimator(getContext(), R.animator.door_shake);
                anim.setTarget(door3);
                anim.start();
            }
        });

        this.yesChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new Timer();
                ctr = new Counter();
                switch (doorChosen){
                    case 1:
                        door1.setImageLevel(0);
                        break;
                    case 2:
                        door2.setImageLevel(0);
                        break;
                    case 3:
                        door3.setImageLevel(0);
                        break;
                    default:
                        //shouldn't be here

                }

                if(doorToOpen != 1 && doorChosen != 1){
                    doorChosen = 1;
                } else if(doorToOpen != 2 && doorChosen != 2){
                    doorChosen = 2;
                } else if(doorToOpen != 3 && doorChosen != 3){
                    doorChosen = 3;
                }

                switch (doorChosen){
                    case 1:
                        timer.scheduleAtFixedRate(ctr, 0, 1000);
                        if(doorChosen == carDoor){
                            door1.setImageLevel(2);
                        } else {
                            door1.setImageLevel(3);
                        }
                        break;
                    case 2:
                        timer.scheduleAtFixedRate(ctr, 0, 1000);
                        if(doorChosen == carDoor){
                            door2.setImageLevel(2);
                        } else {
                            door2.setImageLevel(3);
                        }
                        break;
                    case 3:
                        timer.scheduleAtFixedRate(ctr, 0, 1000);
                        if(doorChosen == carDoor){
                            door3.setImageLevel(2);
                        } else {
                            door3.setImageLevel(3);
                        }
                        break;
                    default:
                        //shouldn't be here
                }
                switchLayout.setVisibility(View.INVISIBLE);
                soundPool.play(clickSound, 1f, 1f,1,0, 1f);
                //checkWin();
            }
        });

        this.noChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer = new Timer();
                ctr = new Counter();
                switch (doorChosen){
                    case 1:

                        timer.scheduleAtFixedRate(ctr, 0, 1000);

                        if(doorChosen == carDoor){
                            door1.setImageLevel(2);
                        } else {
                            door1.setImageLevel(3);
                        }
                        break;
                    case 2:
                        timer.scheduleAtFixedRate(ctr, 0, 1000);
                        if(doorChosen == carDoor){
                            door2.setImageLevel(2);
                        } else {
                            door2.setImageLevel(3);
                        }
                        break;
                    case 3:
                        timer.scheduleAtFixedRate(ctr, 0, 1000);
                        if(doorChosen == carDoor){
                            door3.setImageLevel(2);
                        } else {
                            door3.setImageLevel(3);
                        }
                        break;
                    default:
                        //shouldn't be here
                }
                switchLayout.setVisibility(View.INVISIBLE);
                soundPool.play(clickSound, 1f, 1f,1,0, 1f);

            }
        });

        this.playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                door1.setImageLevel(0);
                door2.setImageLevel(0);
                door3.setImageLevel(0);
                gameSetUp();
                imageClicked = false;
                playAgainLayout.setVisibility(View.INVISIBLE);
                soundPool.play(clickSound, 1f, 1f,1,0, 1f);

            }
        });
    }


    public void gameSetUp(){
        Random myRandom = new Random();
        carDoor = myRandom.nextInt(3) + 1;
//        wins = 0;
//        losses = 0;
    }


    public void showGoat(){
        ArrayList<Integer> choices = new ArrayList<Integer>();

        //int doorToOpen = -1;

        if(carDoor != 1 && doorChosen != 1){
            choices.add(1);
        }
        if(carDoor != 2 && doorChosen != 2){
            choices.add(2);
        }
        if(carDoor != 3 && doorChosen != 3){
            choices.add(3);
        }

        Random rand = new Random();
        int index = rand.nextInt(choices.size());
        doorToOpen = choices.get(index);

        //System.out.println("Door Chosen: "+ doorChosen +" Car behind: "+carDoor+" Door to open: "+doorToOpen);

        switch(doorToOpen){
            case 1:
                door1.setImageLevel(3);
                break;
            case 2:
                door2.setImageLevel(3);
                break;
            case 3:
                door3.setImageLevel(3);
                break;
            default:
                //shouldn't be here

        }
        soundPool.play(goatSound, 1f, 1f,1,0, 1f);
    }


    class Counter extends TimerTask {
        //TimerTask is a runnable, meaning we need a run method
        private int count = 0;
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    count++;
                    ImageButton dto = getDoorChosen();
                    if(count == 1){
                        dto.setImageLevel(4);
                    }
                    else if(count == 2){
                        dto.setImageLevel(5);
                    }
                    else if(count == 3){
                        dto.setImageLevel(6);
                    }
                    else if(count == 4){
                        if(carDoor == doorChosen){
                            dto.setImageLevel(2);
                        } else {
                            dto.setImageLevel(3);
                        }
                        timer.cancel();
                        ctr.cancel();
                        checkWin();
                    }

                }
            });
        }
    }

    public ImageButton getDoorChosen() {

        if(doorChosen == 1){
            return door1;
        }
        else if(doorChosen == 2){
            return door2;
        }
        else if(doorChosen == 3){
            return door3;
        }
        return null;
    }

    public void checkWin(){
        if(doorChosen == carDoor){
            wins++;
            String w = Integer.toString(wins);
            int tot = wins+losses;
            String t = Integer.toString(tot);
            w = "\t"+w;
            t = "\t"+t;
            winsView.setText(w);
            total.setText(t);
            soundPool.play(carSound, 1f, 1f,1,0, 1f);
        } else {
            losses++;
            String l = Integer.toString(losses);
            int tot = wins+losses;
            String t = Integer.toString(tot);
            l = "\t"+l;
            t = "\t"+t;
            lossesView.setText(l);
            total.setText(t);
            soundPool.play(goatSound, 1f, 1f,1,0, 1f);
        }
        playAgainLayout.setVisibility(View.VISIBLE);
        playAgain.setBackgroundColor(Color.rgb(135, 206, 250));
    }



}
