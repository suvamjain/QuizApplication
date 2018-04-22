package suvamjain.example.com.quizapp;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.List;

import suvamjain.example.com.quizapp.modal.ResultCard;

/**
 * Created by SUVAM JAIN on 22-04-2018.
 */

class ResultsInfoRecyclerAdapter extends RecyclerView.Adapter<ResultsInfoRecyclerAdapter.ResultCardViewHolder> {

    private List<ResultCard> resultList;
    private Context mContext;

    public ResultsInfoRecyclerAdapter(Context context, List<ResultCard> resultList) {
        this.resultList = resultList;
        this.mContext = context;
    }

    @Override
    public ResultsInfoRecyclerAdapter.ResultCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // inflating faculty recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.results_recycler_adapter, parent, false);

        return new ResultsInfoRecyclerAdapter.ResultCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsInfoRecyclerAdapter.ResultCardViewHolder holder, int position) {

        holder.Question.setText(resultList.get(position).getQuestion());
        holder.Myans.setText(resultList.get(position).getMyAns());
        holder.CorrAns.setText(resultList.get(position).getCorrAns());
        if (resultList.get(position).getStatus().equals("1")){
            holder.status.setImageResource(R.drawable.thumb_up);
            holder.resultRoot.setBackgroundColor(mContext.getResources().getColor(R.color.dialer_call));
        }

    }


    @Override
    public int getItemCount() {
        Log.v(ResultsInfoRecyclerAdapter.class.getSimpleName(), "" + resultList.size());
        return resultList.size();
    }

    /**
     * ViewHolder class
     */
    public class ResultCardViewHolder extends RecyclerView.ViewHolder {

        public AppCompatTextView Question;
        public AppCompatTextView Myans;
        public AppCompatTextView CorrAns;
        public ImageButton status;
        public LinearLayout resultRoot;

        public ResultCardViewHolder(View view) {
            super(view);
            Question = (AppCompatTextView) view.findViewById(R.id.Quest);
            Myans = (AppCompatTextView) view.findViewById(R.id.saved);
            CorrAns = (AppCompatTextView) view.findViewById(R.id.correct);
            status = (ImageButton)view.findViewById(R.id.ansStatus);
            resultRoot = view.findViewById(R.id.resultParent);
        }
    }
}