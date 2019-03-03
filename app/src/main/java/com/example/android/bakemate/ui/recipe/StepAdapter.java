package com.example.android.bakemate.ui.recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakemate.R;
import com.example.android.bakemate.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    public final StepAdapterOnClickHandler stepAdapterOnClickHandler;
    private List<Step> stepList;

    public interface StepAdapterOnClickHandler {
        void onClick(int stepId, Step currentStep, int stepListLength);
    }

    public StepAdapter(StepAdapterOnClickHandler stepAdapterOnClickHandler) {
        this.stepAdapterOnClickHandler = stepAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_step, viewGroup, false);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder stepViewHolder, int position) {
        final Step currentStep = stepList.get(position);

        stepViewHolder.stepNumber.setText(String.format("STEP %s", position + 1));

        stepViewHolder.stepShortDescription.setText(currentStep.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (stepList == null) return 0;
        return stepList.size();
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
        notifyDataSetChanged();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_step_number)
        TextView stepNumber;
        @BindView(R.id.step_short_description)
        TextView stepShortDescription;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Step currentStep = stepList.get(getAdapterPosition());
            int stepListLength = stepList.size();
            stepAdapterOnClickHandler.onClick(getAdapterPosition(), currentStep, stepListLength);
        }
    }
}
