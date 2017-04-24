package bogdan_mihalchenko.words.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.design.internal.ForegroundLinearLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import bogdan_mihalchenko.words.R;
import bogdan_mihalchenko.words.model.adapters.WordViewAdapter;
import bogdan_mihalchenko.words.model.database.DataBase;
import bogdan_mihalchenko.words.model.keyboards.CustomKeyboard;
import bogdan_mihalchenko.words.model.pojo.Word;

import static bogdan_mihalchenko.words.UI.CategoriesListActivity.isTablet;

public class WordsListActivity extends Activity implements
        WordViewAdapter.PopUpMenuButtonClickListener, WordViewAdapter.SoundButtonClickListener{
    public static final String LOG_TAG = "WordsListActivity";

    protected RecyclerView mRecyclerView;
    protected WordViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected WordsListActivity.LayoutManagerType mCurrentLayoutManagerType;

    protected List<Word> wordsList;
    protected long categoryId;
    protected String categoryTitle;
    protected CustomKeyboard mCustomKeyboardEN, mCustomKeyboardRU,  mCustomKeyboardTrancroption;


    protected ForegroundLinearLayout addWordLyout;
    protected ForegroundLinearLayout selectTypeOfGameDialog;
    protected Button okButton, cancelButton, btnPlay, startGame, cancelGame;
    protected RadioGroup selectLangType;
    protected RadioButton rusEng, engRus;
    protected LinearLayout buttonCards, buttonVariants;
    protected FloatingActionButton fab;
    protected EditText etWordOrigin, etWordTranslate, etWordTranscription;

    protected TextView tvNothingToShow, tvDialogTitle, activityTitle;

    protected ImageView btnBack;

    protected TextToSpeech textToSpeech;


    private static final int SPAN_COUNT = 2;


    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }

    protected int gameType;
    protected int langGameType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_list);

        Intent intent = getIntent();
        categoryId = (long) intent.getLongExtra("categoryId",0);
        categoryTitle = (String) intent.getStringExtra("categoryTitle");
        setTextToSpeech();
        setView();
    }

    void setTextToSpeech(){
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.UK);

                }
            }
        });

    }

    private void setView(){

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpWindow();

            }
        });

        btnBack = (ImageView)findViewById(R.id.back_row);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  =  new Intent(WordsListActivity.this, CategoriesListActivity.class);
                startActivity(intent);
         //WordsListActivity.this.finish();
            }
        });


        btnPlay = (Button)findViewById(R.id.startPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectGameTypeDialog();
            }
        });


       /* btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] myList = {"Русский - Английский", "Английский - Русский"};
                AlertDialog.Builder builder = new AlertDialog.Builder(WordsListActivity.this);
                builder.setTitle("Выберете тип игры");
                builder.setItems(myList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "You Choose : " + myList[which],
                                Toast.LENGTH_LONG).show();
                        Intent intent =  new Intent(WordsListActivity.this, PlayActivity.class);
                        intent.putExtra("catId", categoryId);
                        intent.putExtra("categoryTitle", categoryTitle);
                        intent.putExtra("type", which);
                        startActivity(intent);
                    }
                });
                builder.show();

            }
        });

       */ tvDialogTitle = (TextView)findViewById(R.id.wordsDialogTitle);

        activityTitle = (TextView)findViewById(R.id.tvWordTitle);
        activityTitle.setText(categoryTitle);

        tvNothingToShow = (TextView)findViewById(R.id.tvNothingToShow);
        tvNothingToShow.setVisibility(View.INVISIBLE);

        addWordLyout  =(ForegroundLinearLayout)findViewById(R.id.inputWordLayout);
        addWordLyout.setVisibility(View.INVISIBLE);

        selectTypeOfGameDialog = (ForegroundLinearLayout) findViewById(R.id.selecTypeOfGame);
        selectTypeOfGameDialog.setVisibility(View.INVISIBLE);

        mRecyclerView = (RecyclerView)findViewById(R.id.wrodsRecyclerView);
        //btnAddWord = (Button)findViewById(R.id.btnAddNewWord);
        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = WordsListActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;
        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);


    }

    private void showSelectGameTypeDialog() {
        fab.setVisibility(View.INVISIBLE);
        btnPlay.setEnabled(false);
        /*init select Game dialog controls*/
        buttonCards = (LinearLayout)findViewById(R.id.buttonCards);
        buttonVariants = (LinearLayout) findViewById(R.id.buttonVariants);
        startGame = (Button) findViewById(R.id.startPlayButton);
        cancelGame = (Button) findViewById(R.id.cancelPlayButton);

        selectLangType = (RadioGroup) findViewById(R.id.selectLangType);

        engRus = (RadioButton) findViewById(R.id.selectEnglishRussianType);
        rusEng = (RadioButton) findViewById(R.id.selectRussianEnglishType);
        /* end of init controls*/

        selectTypeOfGameDialog.setVisibility(View.VISIBLE);

        startGame.setEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            startGame.setBackground(getResources().getDrawable(R.drawable.disable_button_bg));
        }
        /*
        * gameType - 0  -  cards
        * gameType  - 1 = Variants
        * langGameType = 0 - russwian-english
        * langGameType = 1  - english-russian
        * */

        buttonCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCards.setBackgroundColor(getResources().getColor(R.color.selectGameTypeButtonColoOnClick));
                buttonVariants.setBackgroundColor(getResources().getColor(R.color.bg_card));
                gameType = 0;

                if(wordsList.size()<2){

                    AlertDialog.Builder builder = new AlertDialog.Builder(WordsListActivity.this);
                    builder.setTitle("Ошибка");
                    builder.setMessage(getApplication().getString(R.string.warning_text_cards));
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                        }
                    });
                    builder.setCancelable(true);
                    builder.show();

                }else{

                    startGame.setEnabled(true);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startGame.setBackground(getResources().getDrawable(R.drawable.button_bg));
                    }


                }



            }
        });
        buttonVariants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonVariants.setBackgroundColor(getResources().getColor(R.color.selectGameTypeButtonColoOnClick));
                buttonCards.setBackgroundColor(getResources().getColor(R.color.bg_card));
                gameType = 1;

                if(wordsList.size()<2){

                    AlertDialog.Builder builder = new AlertDialog.Builder(WordsListActivity.this);
                    builder.setTitle("Ошибка");
                    builder.setMessage(getApplication().getString(R.string.warning_text_variants));
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setCancelable(true);
                    builder.show();

                }else{
                    startGame.setEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        startGame.setBackground(getResources().getDrawable(R.drawable.button_bg));
                    }

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

    private void openPopUpWindow(){
        fab.setVisibility(View.INVISIBLE);
        btnPlay.setEnabled(false);

        addWordLyout.setVisibility(View.VISIBLE);
        tvDialogTitle.setText(R.string.add_word_dialog_title);

        etWordOrigin = (EditText)findViewById(R.id.etWordOrigin);
        etWordOrigin.setInputType(etWordOrigin.getInputType() | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        etWordOrigin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    mCustomKeyboardRU = new CustomKeyboard(WordsListActivity.this, R.id.keyboardviewWords, R.xml.kbd_ru);//russian



                    if( hasFocus ) mCustomKeyboardRU.showCustomKeyboard(v); else mCustomKeyboardRU.hideCustomKeyboard();
                    etWordOrigin.setSelection(etWordOrigin.length());

                }
            }
        });

        etWordOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mCustomKeyboardRU.showCustomKeyboard(v);
                etWordOrigin.setSelection(etWordOrigin.length());

            }
        });

        etWordOrigin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                int inType = etWordOrigin.getInputType();       // Backup the input type
                etWordOrigin.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS); // Disable standard keyboard
                etWordOrigin.onTouchEvent(event);               // Call native handler
                etWordOrigin.setInputType(inType);              // Restore input type
                etWordOrigin.setSelection(etWordOrigin.length());

                return true;
            }
        });


        etWordTranslate = (EditText)findViewById(R.id.etWordTranslate);
        etWordTranslate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(LOG_TAG, mCustomKeyboardRU + " mCustomKeyboardRU OBJECT ");
                if(hasFocus){
                    mCustomKeyboardEN = new CustomKeyboard(WordsListActivity.this, R.id.keyboardviewWords, R.xml.kbd_en);//english
                    if( hasFocus ) mCustomKeyboardEN.showCustomKeyboard(v); else mCustomKeyboardEN.hideCustomKeyboard();

                    etWordTranslate.setSelection(etWordTranslate.length());

                }
            }
        });
        etWordTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomKeyboardEN.showCustomKeyboard(v);
                etWordTranslate.setSelection(etWordTranslate.length());

            }
        });

        etWordTranslate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = etWordTranslate.getInputType();       // Backup the input type
                etWordTranslate.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                etWordTranslate.onTouchEvent(event);               // Call native handler
                etWordTranslate.setInputType(inType);              // Restore input type
                etWordTranslate.setSelection(etWordTranslate.length());

                return true;
            }
        });


        etWordTranscription = (EditText)findViewById(R.id.etWordTrancription);

        etWordTranscription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
         mCustomKeyboardTrancroption = new CustomKeyboard(WordsListActivity.this, R.id.keyboardviewWords, R.xml.kbd_transcription);//english

                if (hasFocus) mCustomKeyboardTrancroption.showCustomKeyboard(v);
                else mCustomKeyboardTrancroption.hideCustomKeyboard();
                etWordTranscription.setSelection(etWordTranscription.length());
            }

        });

        etWordTranscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomKeyboardTrancroption.showCustomKeyboard(v);
                etWordTranscription.setSelection(etWordTranscription.length());

            }
        });

        etWordTranscription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = etWordTranslate.getInputType();       // Backup the input type
                etWordTranscription.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                etWordTranscription.onTouchEvent(event);               // Call native handler
                etWordTranscription.setInputType(inType);              // Restore input type
                etWordTranscription.setSelection(etWordTranscription.length());
                return true;
            }
        });


        okButton = (Button) findViewById(R.id.addButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word word  = new Word();
                word.setWordOrign(etWordOrigin.getText().toString());
                word.setWordTranslate(etWordTranslate.getText().toString());
                word.setWordTrancription(etWordTranscription.getText().toString());
                word.setWordCategory((int) categoryId);
                word.setWordStatus(false);

                mAdapter.addWordItem(word, (int)categoryId);

                tvNothingToShow.setVisibility(View.INVISIBLE);
                tvNothingToShow.setTextSize(0);
                btnPlay.setVisibility(View.VISIBLE);

                closeFloatWindow();

                mLayoutManager.scrollToPosition(0);

            }
        });

        cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFloatWindow();

            }
        });
    }

    private void closeFloatWindow() {
        etWordOrigin.setText("");
        etWordTranslate.setText("");
        etWordTranscription.setText("");
        addWordLyout.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        btnPlay.setEnabled(true);


        if(mCustomKeyboardRU!=null){
            mCustomKeyboardRU.hideCustomKeyboard();

        }
        if(mCustomKeyboardEN!=null){
            mCustomKeyboardEN.hideCustomKeyboard();

        }
        if(mCustomKeyboardTrancroption!=null){
            mCustomKeyboardTrancroption.hideCustomKeyboard();
        }
        initDataset();
        addWordLyout.setVisibility(View.INVISIBLE);
        okButton.setOnClickListener(null);

    }

    @Override public void onBackPressed() {
        // NOTE Trap the back key: when the CustomKeyboard is still visible hide it, only when it is invisible, finish activity
        if(mCustomKeyboardRU!=null){
            mCustomKeyboardRU.hideCustomKeyboard();

        }
        if(mCustomKeyboardEN!=null){
            mCustomKeyboardEN.hideCustomKeyboard();

        }
        if(mCustomKeyboardTrancroption!=null){
            mCustomKeyboardTrancroption.hideCustomKeyboard();
        }
        if(!btnPlay.isEnabled()){
            closeFloatWindow();
        }else{
            Intent intent =  new Intent(this, CategoriesListActivity.class);
            startActivity(intent);
        }

    }

    public void setRecyclerViewLayoutManager(WordsListActivity.LayoutManagerType layoutManagerType) {
        if(isTablet(this)){
            mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
            mCurrentLayoutManagerType = WordsListActivity.LayoutManagerType.GRID_LAYOUT_MANAGER;
        }else{
            mLayoutManager = new LinearLayoutManager(this);
            mCurrentLayoutManagerType = WordsListActivity.LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        initDataset();

    }


    private void initDataset() {
        wordsList = new DataBase(this).getWordsList(categoryId, false);
        mAdapter = new WordViewAdapter(wordsList, this /*context*/, this/* menuButton listener*/, this /*sound button liestener*/);
        mRecyclerView.setAdapter(mAdapter);

        if(wordsList.size()==0){
            tvNothingToShow.setVisibility(View.VISIBLE);
            btnPlay.setVisibility(View.INVISIBLE);

        }else{
            tvNothingToShow.setVisibility(View.INVISIBLE);
            btnPlay.setVisibility(View.VISIBLE);
            tvNothingToShow.setTextSize(0);
        }
    }

    @Override
    public void onClick(View v, final int position) {
        final PopupMenu menu =  new PopupMenu(this, v);

        menu.inflate(R.menu.popup_menu_words);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_edit:
                        addWordLyout.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.INVISIBLE);
                        openPopUpWindow();
                        tvDialogTitle.setText(R.string.edit_word_dialog_title);
                        etWordOrigin.setText(mAdapter.getSelectedItem(position).getWordOrign().toString());
                        etWordTranslate.setText(mAdapter.getSelectedItem(position).getWordTranslate().toString());
                        etWordTranscription.setText(mAdapter.getSelectedItem(position).getWordTrancription().toString());

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAdapter.editWordItem(position, etWordOrigin.getText().toString(),
                                        etWordTranslate.getText().toString(), etWordTranscription.getText().toString(), (int) categoryId);
                                closeFloatWindow();
                            }
                        });



                        break;

                    case R.id.item_delete:
                        AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(WordsListActivity.this);
                        dialoBuilder.setTitle(R.string.delete_category_dialog_title);
                        dialoBuilder.setMessage(R.string.delete_category_dialog_message);
                        dialoBuilder.setPositiveButton(R.string.delete_category_dialog_positive_button,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mAdapter.deleteWordItem(position, (int) categoryId);

                                    }
                                });
                        dialoBuilder.setNegativeButton(R.string.add_category_dialog_negative_button,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert = dialoBuilder.create();
                        alert.show();

                        break;


                }

                return true;
            }
        });
        menu.show();


    }

    @Override
    public void onSoundButtonClick(String str) {

        textToSpeech.speak(str,TextToSpeech.QUEUE_FLUSH, null);
    }



   /* @Override
    public void onEditButtonClickListener(final int position) {
        addWordLyout.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        openPopUpWindow();
        tvDialogTitle.setText(R.string.edit_word_dialog_title);
        etWordOrigin.setText(mAdapter.getSelectedItem(position).getWordOrign().toString());
        etWordTranslate.setText(mAdapter.getSelectedItem(position).getWordTranslate().toString());
        etWordTranscription.setText(mAdapter.getSelectedItem(position).getWordTrancription().toString());

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.editWordItem(position, etWordOrigin.getText().toString(),
                        etWordTranslate.getText().toString(), etWordTranscription.getText().toString());
                closeFloatWindow();
            }
        });


    }
*/
}
