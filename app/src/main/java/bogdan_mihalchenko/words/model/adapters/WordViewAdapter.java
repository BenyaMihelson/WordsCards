package bogdan_mihalchenko.words.model.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bogdan_mihalchenko.words.R;
import bogdan_mihalchenko.words.model.database.DataBase;
import bogdan_mihalchenko.words.model.pojo.Word;

/**
 * Created by Shipohvost on 29.09.2016.
 */

public class WordViewAdapter extends RecyclerView.Adapter<WordViewAdapter.ViewHolder> {
    public static final String LOG_TAG = "WordViewAdapter";

    private List<Word> list;
    private Context mContext;
    private PopUpMenuButtonClickListener popUpMenuButtonClickListener;
    private SoundButtonClickListener soundButtonClickListener;


    public WordViewAdapter(List<Word> list, Context mContext,
                           PopUpMenuButtonClickListener popUpMenuButtonClickListener,
                           SoundButtonClickListener soundButtonClickListener) {
        this.list = list;
        this.mContext = mContext;
        this.popUpMenuButtonClickListener = popUpMenuButtonClickListener;
        this.soundButtonClickListener = soundButtonClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.words_row_item, viewGroup, false);

        return new WordViewAdapter.ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.d(LOG_TAG, "Element " + position + " set.");

        final Word word =list.get(position);
        holder.tvWordOrigin.setText(word.getWordOrign());
        holder.tvWordTranscription.setText("[" + word.getWordTrancription()+"]");
        holder.tvWordTranslate.setText(word.getWordTranslate());

        if(!word.isWordStatus()){
            holder.isCompleteImg.setVisibility(View.INVISIBLE);
        }

        holder.popUpMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               popUpMenuButtonClickListener.onClick(v, position);

            }
        });
        holder.soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundButtonClickListener.onSoundButtonClick(word.getWordTranslate());

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addWordItem(Word word, int catrgoryId){
        //int categoryId = list.get()

       /* list.add(0, word);
        notifyItemInserted(0);
       */ DataBase mDb = new DataBase(mContext);
        //Log.d(LOG_TAG, category.getCategoryTitle() + " catrgoryName in adapter");

        mDb.addWordItem(word);
        list = mDb.getWordsList(catrgoryId,false);
        notifyDataSetChanged();
    }
    public void editWordItem(int position, String wordOrigin, String wordTranslate, String wordTrancription, int categoryId){
        DataBase mDb = new DataBase(mContext);

        /*list.get(position).setWordOrign(wordOrigin);
        list.get(position).setWordTranslate(wordTranslate);
        list.get(position).setWordTrancription(wordTrancription);
        */mDb.editWordItem(list.get(position).getId(), wordOrigin, wordTranslate, wordTrancription);
        list = mDb.getWordsList(categoryId, false);

        notifyDataSetChanged();
    }

    public void deleteWordItem(int position, int categoryId){
        DataBase mDb = new DataBase(mContext);
        mDb.deleteWordItem(list.get(position).getId());

        //list.remove(position);
        list = mDb.getWordsList(categoryId, false);

        notifyDataSetChanged();

    }

    public Word getSelectedItem(int position){
        return list.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvWordOrigin, tvWordTranslate, tvWordTranscription;
        public ImageView editBtn, deleteBtn, popUpMenuBtn, isCompleteImg, soundButton;


        public ViewHolder(View itemView) {
            super(itemView);

           /* editBtn = (ImageView)itemView.findViewById(R.id.ic_edit);
            deleteBtn = (ImageView)itemView.findViewById(R.id.ic_delete);
           */
            isCompleteImg = (ImageView) itemView.findViewById(R.id.isCompleteImg);
            soundButton  =(ImageView) itemView.findViewById(R.id.icSound);
            popUpMenuBtn = (ImageView) itemView.findViewById(R.id.ic_popUp);
            tvWordOrigin = (TextView)itemView.findViewById(R.id.word_origyn);
            tvWordTranslate = (TextView)itemView.findViewById(R.id.word_translate);
            tvWordTranscription = (TextView)itemView.findViewById(R.id.word_transcription);

        }
    }

    public interface PopUpMenuButtonClickListener{
        void onClick(View v, int position);
    }
    public interface SoundButtonClickListener{
        void onSoundButtonClick(String str);
    }
}
