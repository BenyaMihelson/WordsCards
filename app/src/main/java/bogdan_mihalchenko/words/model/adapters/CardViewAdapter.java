package bogdan_mihalchenko.words.model.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.internal.ForegroundLinearLayout;
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
import bogdan_mihalchenko.words.model.pojo.Category;

/**
 * Created by Shipohvost on 27.09.2016.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {
    public static final String LOG_TAG = "CardViewAdapter";
    private CategoryClickListener mListener;
    private Context mContext;
    private View mRootView;
    private PopUpMenuButtonClickListener mPopUpClickListener;
    private List<Category> list;

    boolean isClicable;



    public CardViewAdapter(CategoryClickListener listener, Context context, List<Category> list,
                           PopUpMenuButtonClickListener popUplistener, boolean isClicable) {
        mListener = listener;
        mPopUpClickListener = popUplistener;
        mContext = context;
        this.list = list;
        this.isClicable = isClicable;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.categories_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        Log.d(LOG_TAG, "Element " + position + " set.");
        final Category currentCategory =  list.get(position);

        if(currentCategory.isComplete()){
            viewHolder.icComplete.setVisibility(View.VISIBLE);
        }

        viewHolder.tvContentTitle.setText(currentCategory.getCategoryTitle());
        viewHolder.popUpMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPopUpClickListener.onPopUpButtonClickListener(position);
                mPopUpClickListener.onClick(v, position);
                /*
                mEditButtonClickListener.onEditButtonClickListener(position);*/

                   }
        });

    }

    public void addCategoryItem(Category category){
        //list.add(0, category);
       // notifyItemInserted(0);

        DataBase mDb = new DataBase(mContext);
        Log.d(LOG_TAG, category.getCategoryTitle() + " catrgoryName in adapter");

        mDb.addCategoryItem(category);

        list.clear();


        list = mDb.getCategoriesList();
        notifyDataSetChanged();
    }

    public void deleteCategoryItem(int position){
        DataBase mDb = new DataBase(mContext);
        mDb.deleteCategoryItem(list.get(position).getId());

        list = mDb.getCategoriesList();

        notifyDataSetChanged();

        /*list.remove(position);
        notifyDataSetChanged();
*/
    }
    public void editCategoryItem(int position, String string){
        DataBase mDb = new DataBase(mContext);

        mDb.editCategoryItem(list.get(position).getId(), string);

        list = mDb.getCategoriesList();


        notifyDataSetChanged();
    }

    public Category getSelectedItem(int position){
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public  TextView tvContentTitle;
        public ImageView /*editBtn, deleteBtn,*/ popUpMenuBtn, icComplete;

        public ViewHolder(View itemView) {
            super(itemView);

            tvContentTitle = (TextView)itemView.findViewById(R.id.tvCategoryTitle);
            popUpMenuBtn = (ImageView) itemView.findViewById(R.id.ic_popUp);
            icComplete = (ImageView) itemView.findViewById(R.id.ic_complete);
            icComplete.setVisibility(View.INVISIBLE);

            if(isClicable){
                itemView.setOnClickListener(this);
            }else{
                itemView.setOnClickListener(null);
            }




        }

        @Override
        public void onClick(View v) {

            mListener.onClick(getLayoutPosition());

        }
  }




    public interface CategoryClickListener{
        void onClick(int position);
    }

    public interface PopUpMenuButtonClickListener{
        void onClick(View v, int position);
        void onPopUpButtonClickListener(int position);
    }
}
