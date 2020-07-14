package com.msgkatz.ratesapp.presentation.ui.chart;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.msgkatz.ratesapp.R;
import com.msgkatz.ratesapp.domain.entities.Interval;
import com.msgkatz.ratesapp.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChartIntervalsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static String TAG = ChartIntervalsAdapter.class.getName();

    public interface OnItemClickListener {
        void onItemClicked(Interval interval);
    }

    private Context context;
    private List<Interval> intervals = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private String selectedIntervalName;


    public ChartIntervalsAdapter(Context context, String selectedIntervalName)
    {
        this.context = context;
        this.selectedIntervalName = selectedIntervalName;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<Interval> intervalList)
    {
        if (intervalList != null && intervalList.size() != 0) {

            this.intervals.addAll(intervalList);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new ChartIntervalsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rv_chart_interval, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        Interval interval = intervals.get(position);

        ChartIntervalsViewHolder mHolder = (ChartIntervalsViewHolder) viewHolder;

        mHolder.mTitleMain.setText(interval.getSymbol());

        if (selectedIntervalName.equals(interval.getSymbol()))
        {
            Typeface typeface = ResourcesCompat.getFont(context, R.font.normativepro_bold);
            mHolder.mTitleMain.setTypeface(typeface);
            mHolder.mTitleMain.setTextColor(CommonUtil.getColor(context, R.color.theme_white));
            //mHolder.mTitleMain.setTypeface(mHolder.mTitleMain.setTypeface().getTypeface(), Typeface.BOLD);
            mHolder.mTitleBackground.setVisibility(View.VISIBLE);
        }
        else
        {
            //Typeface typeface = ResourcesCompat.getFont(context, R.font.normativepro_regular);
            Typeface typeface = ResourcesCompat.getFont(context, R.font.normativepro_medium);
            mHolder.mTitleMain.setTypeface(typeface);
            mHolder.mTitleMain.setTextColor(CommonUtil.getColor(context, R.color.chart_title_main_2));
            //mHolder.mTitleMain.setTypeface(mHolder.mTitleMain.getTypeface(), Typeface.NORMAL);
            mHolder.mTitleBackground.setVisibility(View.INVISIBLE);
        }

        mHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIntervalName = interval.getSymbol();
                notifyDataSetChanged();
                onItemClickListener.onItemClicked(interval);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (intervals == null) ? 0 : intervals.size();
    }

    public static class ChartIntervalsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title_main) TextView mTitleMain;
        @BindView(R.id.title_bg) ImageView mTitleBackground;

        public ChartIntervalsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
