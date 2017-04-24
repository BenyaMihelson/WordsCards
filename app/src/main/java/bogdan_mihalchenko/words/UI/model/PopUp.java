package bogdan_mihalchenko.words.UI.model;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import bogdan_mihalchenko.words.R;
import bogdan_mihalchenko.words.UI.PlayActivity;
import bogdan_mihalchenko.words.UI.VariantsPlayActivity;
import bogdan_mihalchenko.words.UI.WordsListActivity;

/**
 * Created by Shipohvost on 28.02.2017.
 */

public class PopUp {

    View view;
    protected Button okButton, cancelButton, btnPlay, startGame, cancelGame;
    protected LinearLayout buttonCards, buttonVariants;


   /* public PopUp(View view) {
        this.view = view;
       // fab = (FloatingActionButton) view.findViewById(R.id.fab);
        buttonCards = (LinearLayout)view.findViewById(R.id.buttonCards);
        buttonVariants = (LinearLayout) view.findViewById(R.id.buttonVariants);
        startGame = (Button) view.findViewById(R.id.startPlayButton);
        cancelGame = (Button) view.findViewById(R.id.cancelPlayButton);
        engRus = (RadioButton) findViewById(R.id.selectEnglishRussianType);
        rusEng = (RadioButton) findViewById(R.id.selectRussianEnglishType);



    }

    private void showSelectGameTypeDialog() {
        btnPlay.setEnabled(false);
        *//*init select Game dialog controls*//*
        c = (LinearLayout)findViewById(R.id.buttonCards);
        buttonVariants = (LinearLayout) findViewById(R.id.buttonVariants);
        startGame = (Button) findViewById(R.id.startPlayButton);
        cancelGame = (Button) findViewById(R.id.cancelPlayButton);

        selectLangType = (RadioGroup) findViewById(R.id.selectLangType);

        engRus = (RadioButton) findViewById(R.id.selectEnglishRussianType);
        rusEng = (RadioButton) findViewById(R.id.selectRussianEnglishType);
        *//* end of init controls*//*

        selectTypeOfGameDialog.setVisibility(View.VISIBLE);

        startGame.setEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startGame.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
        }
        *//*
        * gameType - 0  -  cards
        * gameType  - 1 = Variants
        * langGameType = 0 - russwian-english
        * langGameType = 1  - english-russian
        * *//*

        buttonCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCards.setBackgroundColor(getResources().getColor(R.color.selectGameTypeButtonColoOnClick));
                buttonVariants.setBackgroundColor(getResources().getColor(R.color.bg_card));
                gameType = 0;
                startGame.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startGame.setBackground(getResources().getDrawable(R.drawable.button_bg));
                }



            }
        });
        buttonVariants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonVariants.setBackgroundColor(getResources().getColor(R.color.selectGameTypeButtonColoOnClick));
                buttonCards.setBackgroundColor(getResources().getColor(R.color.bg_card));
                gameType = 1;
                startGame.setEnabled(true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startGame.setBackground(getResources().getDrawable(R.drawable.button_bg));
                }

            }
        });

        selectLangType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.selectEnglishRussianType:
                        langGameType = 0;

                        Toast.makeText(getApplicationContext(), "Первый переключатель",
                                Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.selectRussianEnglishType:
                        langGameType = 1;

                        Toast.makeText(getApplicationContext(), "Второй переключатель",
                                Toast.LENGTH_SHORT).show();

                        break;
                }
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class activity;

                if(gameType == 0){
                    activity = PlayActivity.class;

                }else{
                    activity = VariantsPlayActivity.class;

                }
                Intent intent =  new Intent(WordsListActivity.this, activity);
                intent.putExtra("catId", categoryId);
                intent.putExtra("categoryTitle", categoryTitle);
                intent.putExtra("type", langGameType);
                startActivity(intent);

            }
        });

        cancelGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setVisibility(View.VISIBLE);
                btnPlay.setEnabled(true);
                selectTypeOfGameDialog.setVisibility(View.INVISIBLE);

            }
        });



    }
*/}
