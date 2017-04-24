package bogdan_mihalchenko.words.UI;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import bogdan_mihalchenko.words.R;
import bogdan_mihalchenko.words.model.database.DataBase;
import bogdan_mihalchenko.words.model.pojo.Word;

public class VariantsPlayActivity extends AppCompatActivity implements View.OnClickListener {
    private final String LOG = "VariantsPlayActivity";

    /* views */
    private ImageView backRow;
    private LinearLayout answerVar_0, answerVar_1, answerVar_2;
    private TextView tvVar_0, tvVar_1, tvVar_2, tvWord;

    /* Variables*/
    private int langType;
    private List<Word> list;
    private List<Word> tempFullList;
    private long catId;
    private Word word;
    private String categoryTitle;
    private boolean isStop = true;

    private String wordTask;
    int id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variants_play);

        Intent intent  = getIntent();

        catId = (long) intent.getLongExtra("catId", 0);
        categoryTitle = intent.getStringExtra("categoryTitle");

        langType = intent.getIntExtra("type", 0);

        Log.d(LOG, langType + " lamg TYPE");

        list = new DataBase(this).getWordsList(catId, false);
        tempFullList = new DataBase(this).getWordsList(catId, false);



        setView();

        game();
    }

    void setView(){
        backRow = (ImageView) findViewById(R.id.back_row);
        backRow.setOnClickListener(this);
        answerVar_0 = (LinearLayout) findViewById(R.id.answerVariant_0);
        answerVar_1 = (LinearLayout) findViewById(R.id.answerVariant_1);
        answerVar_2 = (LinearLayout) findViewById(R.id.answerVariant_2);
        tvVar_0 = (TextView) findViewById(R.id.tv_answerVariant_0);
        tvVar_1 = (TextView) findViewById(R.id.tv_answerVariant_1);
        tvVar_2 = (TextView) findViewById(R.id.tv_answerVariant_2);
        tvWord = (TextView) findViewById(R.id.tvCardFront);

    }

    void game(){

        answerVar_0.setBackgroundColor(getResources().getColor(R.color.bg_card));
        answerVar_1.setBackgroundColor(getResources().getColor(R.color.bg_card));
        answerVar_2.setBackgroundColor(getResources().getColor(R.color.bg_card));

        if(list.size()>0){

            setViewsValues();

            waitForResponse();

        }else{
            endGame();
        }



    }
    void waitForResponse(){
        answerVar_0.setOnClickListener(this);
        answerVar_1.setOnClickListener(this);
        answerVar_2.setOnClickListener(this);

    }

    void setViewsValues(){

        Log.d(LOG, list.size()  + " list of words size");
        Log.d(LOG, tempFullList.size()  + " tempFullList list of words size");


        int temp_id_1= 0;
        int temp_id_2 = 0;
        Random random = new Random();
        id = random.nextInt(list.size());

        /* get random word from set*/
        word = list.get(id);
        Log.d(LOG, word.getWordOrign().toString()  + "selected word");


        /* init list of variants*/
        List<Word> varList = new ArrayList();

        /*add rigth variant to tje list of variants*/
        varList.add(list.get(id));

        Log.d(LOG, list.get(id).getWordTranslate() + " add right variant to VarList");
        Log.d(LOG, id + " selectetd ID");

        while (varList.size()<3){
            do{
                temp_id_1 = random.nextInt(tempFullList.size());
                //varList.add(tempFullList.get(temp_id_1));
            }while (tempFullList.get(temp_id_1).getWordTranslate().equals(word.getWordTranslate()));
            varList.add(tempFullList.get(temp_id_1));
            Log.d(LOG, varList.size() + " Variants list Size after first ADDED");
            do{
                temp_id_2 = random.nextInt(tempFullList.size());
            }while (tempFullList.get(temp_id_2).getWordTranslate().equals(word.getWordTranslate())|
                    tempFullList.get(temp_id_2).getWordTranslate().equals(tempFullList.get(temp_id_1).getWordTranslate()));
            varList.add(tempFullList.get(temp_id_2));


        }



        List<TextView> tvList =new ArrayList<>();
        tvList.add(tvVar_0);
        tvList.add(tvVar_1);
        tvList.add(tvVar_2);

        int i = 0;
        if(langType == 0){
            tvWord.setText(word.getWordOrign());

            while (tvList.size()>0){
                int tvId = random.nextInt(tvList.size());
                tvList.get(tvId).setText(varList.get(i).getWordTranslate());
                Log.d("TAG", tvList.get(tvId).getText().toString() + " text");
                i++;
                tvList.remove(tvId);
            }

        }else{
            tvWord.setText(word.getWordTranslate());
            while (tvList.size()>0){
                int tvId = random.nextInt(tvList.size());
                tvList.get(tvId).setText(varList.get(i).getWordOrign());
                Log.d("TAG", tvList.get(tvId).getText().toString() + " text");
                i++;
                tvList.remove(tvId);
            }

        }




    }

    void endGame(){
        setContentView(R.layout.activity_play_finish);
        Button btnResetGame = (Button)findViewById(R.id.resetGameBtn);
        btnResetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] myList = {"Русский - Английский", "Английский - Русский"};
                AlertDialog.Builder builder = new AlertDialog.Builder(VariantsPlayActivity.this);
                builder.setTitle("Выберете тип игры");
                builder.setCancelable(true);
                builder.setItems(myList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent  = new Intent(VariantsPlayActivity.this, VariantsPlayActivity.class);
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
                Intent intent =  new Intent(VariantsPlayActivity.this, CategoriesListActivity.class);
                startActivity(intent);
            }
        });

        Toast.makeText(this, "CONGRATULATIONS", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {
        if (langType==0){
            wordTask = word.getWordTranslate();
        }else{
            wordTask = word.getWordOrign();
        }

        switch (v.getId()){
            case R.id.answerVariant_0:
                if (wordTask.equals(tvVar_0.getText().toString())){
                    list.remove(id);
                    game();
                }else{
                    answerVar_0.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    Toast.makeText(getApplicationContext(), "Не првильно",
                            Toast.LENGTH_SHORT).show();

                }


                break;
            case R.id.answerVariant_1:

                if (wordTask.equals(tvVar_1.getText().toString())){
                    list.remove(id);
                    game();
                }else{
                    answerVar_1.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));

                    Toast.makeText(getApplicationContext(), "Не правильно",
                            Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.answerVariant_2:
                if (wordTask.equals(tvVar_2.getText().toString())){
                    list.remove(id);
                    game();
                }else{
                    answerVar_2.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));

                    Toast.makeText(getApplicationContext(), "Не правильно",
                            Toast.LENGTH_SHORT).show();

                }

                break;
            case R.id.back_row:
                Intent intent = new Intent(this, WordsListActivity.class);
                intent.putExtra("categoryId", catId);
                intent.putExtra("categoryTitle", categoryTitle);
                startActivity(intent);

                break;
        }

    }
}
