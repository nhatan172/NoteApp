package nhatan172.noteapp.main;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import nhatan172.noteapp.NoteModel.Note;
import nhatan172.noteapp.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link NoteFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyNoteRecyclerViewAdapter extends RecyclerView.Adapter<MyNoteRecyclerViewAdapter.ViewHolder> {

    private final List<Note> mValues;
    private final NoteFragment.OnListFragmentInteractionListener mListener;
    private ViewGroup parent;
    public MyNoteRecyclerViewAdapter(List<Note> items, NoteFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTitle.setText(mValues.get(position).getTitle());
        holder.mNote.setText(mValues.get(position).getNote());
        holder.mTime.setText(mValues.get(position).getUpdated_time().substring(0,5)+mValues.get(position).getUpdated_time().substring(10));
        holder.mView.setBackgroundColor(Color.parseColor(mValues.get(position).getColor()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(position);
                }
            }
        });
        if(!mValues.get(position).hasAlarm())
            holder.mImageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public  TextView mTitle;
        public  TextView mNote;
        public  TextView mTime;
        public  ImageView mImageView;
        public Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView)view.findViewById(R.id.iv_alarm);
            mTitle = (TextView) view.findViewById(R.id.tv_title);
            mNote = (TextView) view.findViewById(R.id.tv_note);
            mTime = (TextView)view.findViewById(R.id.tv_time);
        }

    }
}
