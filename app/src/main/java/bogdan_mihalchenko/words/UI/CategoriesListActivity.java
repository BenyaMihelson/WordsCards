package bogdan_mihalchenko.words.UI;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.design.internal.ForegroundLinearLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import bogdan_mihalchenko.words.R;
import bogdan_mihalchenko.words.model.adapters.CardViewAdapter;
import bogdan_mihalchenko.words.model.database.DataBase;
import bogdan_mihalchenko.words.model.helper.Utils;
import bogdan_mihalchenko.words.model.pojo.Category;

public class CategoriesListActivity extends Activity implements CardViewAdapter.CategoryClickListener,
        CardViewAdapter.PopUpMenuButtonClickListener {

    private static final String TAG = "CategoriesListActivity";
    private static final String KEY_LAYOUT_MANAGER = "layoutManager";
    private static final int SPAN_COUNT = 2;
    private enum LayoutManagerType {
        GRID_LAYOUT_MANAGER,
        LINEAR_LAYOUT_MANAGER
    }
    protected RecyclerView mRecyclerView;
    protected CardViewAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected LayoutManagerType mCurrentLayoutManagerType;
    protected ForegroundLinearLayout addCategoryLyout;
    protected ForegroundLinearLayout selectTypeOfGameDialog;
    protected ForegroundLinearLayout reclamaWindow;
    protected Button okButton, cancelButton, btnPlay, startGame, cancelGame;
    protected EditText editText;

    protected RadioGroup selectLangType;
    protected RadioButton rusEng, engRus;
    protected LinearLayout buttonCards, buttonVariants;


    protected List<Category> categoriesList;

    protected TextView dialogTitle, tvNothingToShow;

    protected int gameType;
    protected int langGameType;


    View rootView;
    FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();

        showReclamaWindow(new Utils(this).isShow());


        initDataset();
    }

    private void showReclamaWindow(boolean show) {
        if(show){

            fab.setVisibility(View.INVISIBLE);
            fab.setEnabled(false);

            reclamaWindow.setVisibility(View.VISIBLE);

            mRecyclerView.setEnabled(false);

            Button goToSite = (Button)findViewById(R.id.goToSite);
            Button reminedLater = (Button)findViewById(R.id.showLater);
            Button dontShoe = (Button) findViewById(R.id.dontShow);

            goToSite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.comfortenglish.com/"));
                    startActivity(browserIntent);
                    new Utils(CategoriesListActivity.this).setPrefs(1);

                }
            });
            reminedLater.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeReclamaWindow();

                    new Utils(CategoriesListActivity.this).setPrefs(1);

                }
            });

            dontShoe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeReclamaWindow();

                    new Utils(CategoriesListActivity.this).setPrefs(2);

                }
            });

        }
    }

    private void closeReclamaWindow() {
        reclamaWindow.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.VISIBLE);
        fab.setEnabled(true);

        mRecyclerView.setEnabled(true);


    }

    protected void setViews(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTitle.setText(R.string.add_category_dialog_title);
                openPopUpWindow();
            }
        });

        tvNothingToShow = (TextView)findViewById(R.id.tvNothingToShowCat);
        tvNothingToShow.setVisibility(View.INVISIBLE);
       // tvNothingToShow.setTextSize(0);


        addCategoryLyout  = (ForegroundLinearLayout)findViewById(R.id.inputCategoryLayout);
        addCategoryLyout.setVisibility(View.INVISIBLE);

        selectTypeOfGameDialog = (ForegroundLinearLayout) findViewById(R.id.selecTypeOfGame);
        selectTypeOfGameDialog.setVisibility(View.INVISIBLE);
        Log.d(TAG, " selectTypeOfGameDialog - INVISIBLE");

        reclamaWindow = (ForegroundLinearLayout) findViewById(R.id.recklama);
        reclamaWindow.setVisibility(View.INVISIBLE);

        editText = (EditText) findViewById(R.id.etCategoryInput);
        okButton  = (Button)findViewById(R.id.addButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCategoryLyout.setVisibility(View.INVISIBLE);
                closePopUpWindow();
            }
        });

        dialogTitle = (TextView)findViewById(R.id.dialogTitle);



        mRecyclerView = (RecyclerView)findViewById(R.id.categoriesRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        setRecyclerViewLayoutManager(mCurrentLayoutManagerType);

        LayoutInflater layoutInflater = LayoutInflater.from(CategoriesListActivity.this);

        rootView = layoutInflater.inflate(R.layout.activity_words_list, null).getRootView();


    }

    private void openPopUpWindow() {
        addCategoryLyout.setVisibility(View.VISIBLE);
        fab.setVisibility(View.INVISIBLE);
        fab.setEnabled(false);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = new Category();
                category.setCategoryTitle(editText.getText().toString());
                category.setComplete(false);
                mAdapter.addCategoryItem(category);
                tvNothingToShow.setVisibility(View.INVISIBLE);

                closePopUpWindow();
            }
        });
    }

    private void openReclamaPopUp(){


    }


    public void setRecyclerViewLayoutManager(LayoutManagerType layoutManagerType) {
        if(isTablet(this)){
            mLayoutManager = new GridLayoutManager(this, SPAN_COUNT);
            mCurrentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER;
        }else{
            mLayoutManager = new LinearLayoutManager(this);
            mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;

        }
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }




    private void initDataset() {

        categoriesList = new DataBase(this).getCategoriesList();
        mAdapter = new CardViewAdapter(this,this, categoriesList, this, /*clickable*/ true);

        mRecyclerView.setAdapter(mAdapter);

        if(categoriesList.size()==0){
            tvNothingToShow.setVisibility(View.VISIBLE);
        }else{

            tvNothingToShow.setVisibility(View.INVISIBLE);
            tvNothingToShow.setTextSize(0);

        }

        Log.d(TAG, categoriesList.size() + " Size of received list");

    }

    private void closePopUpWindow() {
        addCategoryLyout.setVisibility(View.INVISIBLE);
        fab.setEnabled(true);
        fab.setVisibility(View.VISIBLE);
        View view = getCurrentFocus();
        editText.clearFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        initDataset();
        editText.setText("");

    }



    @Override
    public void onPopUpButtonClickListener(final int position) {



        /*addCategoryLyout.setVisibility(View.VISIBLE);
        dialogTitle.setText(R.string.edit_category_dialog_title);
        editText.setText(mAdapter.getSelectedItem(position).getCategoryTitle().toString());
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.editCategoryItem(position, editText.getText().toString());
                closePopUpWindow();
            }
        });*/

    }

    @Override
    public void onClick(View v, final int position) {
        final PopupMenu menu =  new PopupMenu(this, v);

        menu.inflate(R.menu.popup_menu_categorioes);

        if(new DataBase(this).getWordsList(mAdapter.getSelectedItem(position)
                .getId(),false).size()==0){

            menu.getMenu().findItem(R.id.item_play).setEnabled(false);
        }


        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_edit:
                        fab.setVisibility(View.INVISIBLE);
                        fab.setEnabled(false);
                        addCategoryLyout.setVisibility(View.VISIBLE);
                        dialogTitle.setText(R.string.edit_category_dialog_title);
                        editText.setText(mAdapter.getSelectedItem(position).getCategoryTitle().toString());
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAdapter.editCategoryItem(position, editText.getText().toString());
                                closePopUpWindow();
                            }
                        });

                    break;

                    case R.id.item_delete:
                        AlertDialog.Builder dialoBuilder = new AlertDialog.Builder(CategoriesListActivity.this);
                        dialoBuilder.setTitle(R.string.delete_category_dialog_title);
                        dialoBuilder.setMessage(R.string.delete_category_dialog_message);
                        dialoBuilder.setPositiveButton(R.string.delete_category_dialog_positive_button,
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.deleteCategoryItem(position);

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

                    case  R.id.item_play:

                        showSelectGameTypeDialog(position);

                        /*final CharSequence[] myList = {"Русский - Английский", "Английский - Русский"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesListActivity.this);
                        builder.setTitle("Выберете тип игры");
                        builder.setItems(myList, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),
                                        "You Choose : " + myList[which],
                                        Toast.LENGTH_LONG).show();
                                Intent intent =  new Intent(CategoriesListActivity.this, PlayActivity.class);
                                intent.putExtra("catId", mAdapter.getSelectedItem(position).getId());
                                intent.putExtra("categoryTitle", mAdapter.getSelectedItem(position).getCategoryTitle());
                                intent.putExtra("type", which);
                                startActivity(intent);
                            }
                        });
                        builder.show();
*/

                        break;

                }

                return true;
            }
        });
        menu.show();

    }



    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this, WordsListActivity.class);
        intent.putExtra("categoryId", mAdapter.getSelectedItem(position).getId());
        intent.putExtra("categoryTitle", mAdapter.getSelectedItem(position).getCategoryTitle());
        startActivity(intent);
    }



    private void showSelectGameTypeDialog(final int position) {

        final int wordListSize = new DataBase(this).getWordsList(categoriesList.get(position).getId(),false).size();
        Log.d(TAG, "showSelectGameTypeDialog - clicked");


        fab.setVisibility(View.INVISIBLE);

        mRecyclerView.setEnabled(false);
        mRecyclerView.setClickable(false);

        mAdapter = new CardViewAdapter(this,this, categoriesList, this, /*clickable*/ false);
        mRecyclerView.setAdapter(mAdapter);

        // btnPlay.setEnabled(false);
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

                if(wordListSize<2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesListActivity.this);
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

                if(wordListSize<4){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CategoriesListActivity.this);
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
                Intent intent =  new Intent(CategoriesListActivity.this, activity);
                intent.putExtra("catId", mAdapter.getSelectedItem(position).getId());
                intent.putExtra("categoryTitle", mAdapter.getSelectedItem(position).getCategoryTitle()); intent.putExtra("type", langGameType);
                startActivity(intent);

            }
        });

        cancelGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.setVisibility(View.VISIBLE);
                mRecyclerView.setEnabled(true);
                selectTypeOfGameDialog.setVisibility(View.INVISIBLE);
                initDataset();

            }
        });



    }


}
