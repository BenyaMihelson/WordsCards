package bogdan_mihalchenko.words.UI;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import bogdan_mihalchenko.words.R;
import bogdan_mihalchenko.words.model.database.DataBase;
import bogdan_mihalchenko.words.model.pojo.Word;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener, Animator.AnimatorListener {
    public static final String LOG = "PlayActivity";

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean mIsBackVisible = false;
    private View mCardFrontLayout;
    private View mCardBackLayout;
    private FrameLayout mCardLayot;


    private int TYPE_OF_PLAY;
    private long catId;
    private List<Word> list;
    private Word word;

    private int startSumWords;
    private int totalSumWord;

    String categoryTitle;

    String wordOrigin, wordTranslate;

    private boolean mShowingBack = false;
    private TextView tvCardFront, tvCardBack,tvCardTrancription, tvSumWords, tvSumUncompleteWords, tvSumCompleteWords;
    private Button btnYes, btnNo;
    private ImageView backRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent  = getIntent();

        catId = (long) intent.getLongExtra("catId", 0);
        categoryTitle = intent.getStringExtra("categoryTitle");

        TYPE_OF_PLAY = intent.getIntExtra("type", 0);
        list = new DataBase(this).getWordsList(catId, true);

        totalSumWord = new DataBase(this).getWordsList(catId, false).size();

        startSumWords = list.size();

        if(TYPE_OF_PLAY == 0){
            setContentView(R.layout.activity_play);
        }
        if(TYPE_OF_PLAY == 1){
            setContentView(R.layout.activity_play_r);
        }

        tvSumWords = (TextView) findViewById(R.id.tvNuberOfWords);
        tvSumCompleteWords = (TextView) findViewById(R.id.tvCompleteWords);
        tvSumUncompleteWords = (TextView)findViewById(R.id.tvNotCompleteWords);
        tvSumWords.setText(String.valueOf(totalSumWord));

        setStatisticView();

        //findViews();

        game();

    }
    int id;

    private void game(){


        if(list.size()>0){

            findViews();
            loadAnimations();
            changeCameraDistance();

            tvCardBack.setVisibility(View.VISIBLE);
            tvCardFront.setVisibility(View.VISIBLE);
            tvCardTrancription.setVisibility(View.VISIBLE);



            if(mSetRightOut!=null&mSetLeftIn!=null){
                mSetRightOut.removeListener(this);
                mSetLeftIn.removeListener(this);

            }

            Random random = new Random();
            id = random.nextInt(list.size());
            word = list.get(id);

            tvCardFront.setText(word.getWordOrign());
            tvCardBack.setText(word.getWordTranslate());
            tvCardTrancription.setText("[ " + word.getWordTrancription()+ " ]");


            mCardLayot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    flipCard();

                    btnYes.setVisibility(View.VISIBLE);
                    btnNo.setVisibility(View.VISIBLE);
                }
            });

            btnYes.setOnClickListener(this);

            btnNo.setOnClickListener(this);

        }else{
            finalOfGame();

        }

        }

    private void finalOfGame() {

        new DataBase(this).updateCategoryStatus(catId, true);

        setContentView(R.layout.activity_play_finish);
        Button btnResetGame = (Button)findViewById(R.id.resetGameBtn);
        btnResetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] myList = {"Русский - Английский", "Английский - Русский"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayActivity.this);
                builder.setTitle("Выберете тип игры");
                builder.setCancelable(true);
                builder.setItems(myList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataBase dataBase = new DataBase(PlayActivity.this);
                        dataBase.updateCategoryStatus(catId, false);
                        dataBase.uncompleteAllWordStatus();
                        Intent intent  = new Intent(PlayActivity.this, PlayActivity.class);
                        ComponentName cn = intent.getComponent();
                        Intent mIntent = IntentCompat.makeRestartActivityTask(cn);
                        mIntent.putExtra("catId", catId);
                        mIntent.putExtra("categoryTitle", categoryTitle);
                        intent.putExtra("type", which);
                        startActivity(mIntent);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                builder.show();



            }
        });

        Button backBtn = (Button)findViewById(R.id.buttonBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(PlayActivity.this, CategoriesListActivity.class);
                startActivity(intent);
            }
        });

        Toast.makeText(this, "CONGRATULATIONS", Toast.LENGTH_SHORT).show();


    }


    private void nextStep(){

        tvCardBack.setVisibility(View.INVISIBLE);
        tvCardFront.setVisibility(View.INVISIBLE);
        tvCardTrancription.setVisibility(View.INVISIBLE);

        mSetLeftIn.addListener(PlayActivity.this);
        mSetRightOut.addListener(PlayActivity.this);

        mIsBackVisible = true;

        flipCard();
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;

        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);
    }

    private void loadAnimations() {

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);

    }

    private void findViews() {
        if (TYPE_OF_PLAY == 0){
            mCardBackLayout = findViewById(R.id.card_back);
            mCardFrontLayout = findViewById(R.id.card_front);
        }if(TYPE_OF_PLAY == 1){
            mCardBackLayout = findViewById(R.id.card_front);
            mCardFrontLayout = findViewById(R.id.card_back);
        }

  /*    */


        mCardLayot = (FrameLayout) findViewById(R.id.card_frame);
        //mCardBackLayout.setOnClickListener(this);

        btnYes = (Button) findViewById(R.id.btnYes);
        btnYes.setVisibility(View.INVISIBLE);
        btnNo = (Button)findViewById(R.id.btnNo);
        btnNo.setVisibility(View.INVISIBLE);

        backRow = (ImageView) findViewById(R.id.back_row);
        backRow.setOnClickListener(this);

        tvCardFront = (TextView)findViewById(R.id.tvCardFront);
        tvCardBack = (TextView) findViewById(R.id.tvCardBack);
        tvCardTrancription = (TextView) findViewById(R.id.tvCardBackTrancription);

    }

    public void flipCard() {
        if (!mIsBackVisible) {

            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);

            mSetRightOut.start();
            mSetLeftIn.start();

            mIsBackVisible = true;
            mShowingBack = true;

        } else {

            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);

            mSetRightOut.start();
            mSetLeftIn.start();

            mIsBackVisible = false;
            mShowingBack = false;
        }
    }

    private void setStatisticView(){
        int numberOfUnComleteWords = list.size();
        int numberOfComplete = totalSumWord - numberOfUnComleteWords;

        tvSumUncompleteWords.setText(String.valueOf(numberOfUnComleteWords));
        tvSumCompleteWords.setText(String.valueOf(numberOfComplete));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_frame:

                break;
            case R.id.btnYes:
                list.remove(id);
                word.setWordStatus(true);
                DataBase db = new DataBase(this);
                db.updateWordStatus(word.getId());

                setStatisticView();

                nextStep();
                break;

            case R.id.btnNo:
                nextStep();
                break;

            case R.id.back_row:
                Intent intent = new Intent(this, WordsListActivity.class);
                intent.putExtra("categoryId", catId);
                intent.putExtra("categoryTitle", categoryTitle);
                startActivity(intent);

                /*finish();*/
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        game();

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}