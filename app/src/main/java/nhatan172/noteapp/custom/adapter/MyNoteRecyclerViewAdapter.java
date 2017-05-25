package nhatan172.noteapp.custom.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import nhatan172.noteapp.R;
import nhatan172.noteapp.activity.fragment.NoteFragment;
import nhatan172.noteapp.model.Note;

public class MyNoteRecyclerViewAdapter extends RecyclerView.Adapter<MyNoteRecyclerViewAdapter.ViewHolder> {

    private final List<Note> mValues;
    private final NoteFragment.OnListFragmentInteractionListener mListener;

    public MyNoteRecyclerViewAdapter(List<Note> items, NoteFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fragment_note, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        Note note = mValues.get(position);
        String updateTime = note.getUpdatedTime();
        holder.mTitle.setText(note.getTitle());
        holder.mNote.setText(note.getNote());
        holder.mTime.setText(updateTime.substring(0,5) + updateTime.substring(10));
        holder.mNoteLayout.setBackgroundColor(Color.parseColor(mValues.get(position).getColor()));
        holder.mNoteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(position);
                }
            }
        });
        if (!mValues.get(position).hasAlarm())
            holder.mImageView.setVisibility(View.INVISIBLE);
    }
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public  View mView;
        public LinearLayout mNoteLayout;
        public  TextView mTitle;
        public  TextView mNote;
        public  TextView mTime;
        public  ImageView mImageView;
        public Note mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNoteLayout = (LinearLayout)view.findViewById(R.id.ll_note_item);
            mImageView = (ImageView)view.findViewById(R.id.iv_alarm);
            mTitle = (TextView) view.findViewById(R.id.tv_title);
            mNote = (TextView) view.findViewById(R.id.tv_note);
            mTime = (TextView)view.findViewById(R.id.tv_time);
        }
    }
}
