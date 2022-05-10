package androidsamples.java.tictactoe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OpenGamesAdapter extends RecyclerView.Adapter<OpenGamesAdapter.ViewHolder> {

  private final LayoutInflater mInflater;
  List<OnlineGameInfo> mEntries;
  private onItemClickListener listener;
  public OpenGamesAdapter(Context context,List<OnlineGameInfo> mEntries ) {
    mInflater = LayoutInflater.from(context);
    this.mEntries=mEntries;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.fragment_item, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
    // TODO bind the item at the given position to the holder
    if( mEntries!= null){
      OnlineGameInfo current = mEntries.get(position);
      holder.mContentView.setText(current.getUsername());
    }
  }

  @Override
  public int getItemCount() {
    return (mEntries == null) ? 0 : mEntries.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mContentView;

    public ViewHolder(View view) {
      super(view);
      mView = view;
      //mIdView = view.findViewById(R.id.item_number);
      mContentView = view.findViewById(R.id.content);
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position=getAdapterPosition();
          if(listener != null && position!=RecyclerView.NO_POSITION)
            listener.onItemClick(itemView,position);
        }
      });
    }

    @NonNull
    @Override
    public String toString() {
      return super.toString() + " '" + mContentView.getText() + "'";
    }
  }
  public interface onItemClickListener{
    void onItemClick(View itemView, int position);
  }
  public void setOnItemClickListener(onItemClickListener listener){
    this.listener=listener;
  }
}