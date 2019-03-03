package com.example.android.bakemate.ui.step;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.bakemate.R;
import com.example.android.bakemate.di.Injectable;
import com.example.android.bakemate.model.Step;
import com.example.android.bakemate.util.StringModifier;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment implements Injectable {

    private static final String STEP_ID = "stepId";
    private static final String STEP = "step";
    private static final String STEP_LIST_LENGTH = "stepListLength";

    private static final String NO_INTERNET = "noInternet";
    private static final String NO_URL = "noURL";

    private static final String CURRENT_POSITION = "CURRENT_POSITION";
    private static final String PLAY_WHEN_READY = "PLAY_WHEN_READY";
    private static final String CURRENT_WINDOW = "CURRENT_WINDOW";

    @BindView(R.id.tb_step)
    Toolbar toolbar;
    @BindView(R.id.sv_step)
    NestedScrollView nestedScrollView;
    @BindView(R.id.tv_step_count)
    TextView stepCount;
    @BindView(R.id.tv_step_short_description)
    TextView stepShortDescription;
    @BindView(R.id.tv_step_full_description)
    TextView stepFullDescription;

    @BindView(R.id.player_layout)
    RelativeLayout playerLayout;
    @BindView(R.id.player_view)
    PlayerView playerView;
    @BindView(R.id.no_video_image)
    ImageView noVideoImage;
    @BindView(R.id.no_video_text)
    TextView noVideoText;

    boolean isTwoPane;
    int stepId;
    Step currentStep;
    int stepListLength;

    private ExoPlayer player;
    private boolean playWhenReady = true;
    private long currentPosition;
    private int currentWindow;

    private Dialog fullScreenDialog;
    private boolean exoPlayerFullScreen = false;

    private String currentUrl;

    public StepFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isTwoPane = getResources().getBoolean(R.bool.isTwoPane);

        if (isTwoPane) {
            if (getArguments() != null) {
                toolbar.setVisibility(View.GONE);
                nestedScrollView.setPadding(0,0,0,0);

                stepId = getArguments().getInt(STEP_ID);
                currentStep = getArguments().getParcelable(STEP);
                stepListLength = getArguments().getInt(STEP_LIST_LENGTH);
            }
        } else {
            toolbar.setTitleTextColor(Color.WHITE);
            toolbar.setTitle(R.string.step_title);

            stepId = Objects.requireNonNull(Objects.
                    requireNonNull(getActivity()).getIntent().getExtras()).getInt(STEP_ID);
            currentStep = Objects.requireNonNull(getActivity().getIntent().getExtras()).getParcelable(STEP);
            stepListLength = getActivity().getIntent().getExtras().getInt(STEP_LIST_LENGTH);
        }

        setCurrentStepViews(stepId, currentStep, stepListLength);

        currentUrl = setAvaliableVideoUrl(currentStep);

        if (savedInstanceState == null) {
            if (isConnected()) {
                if (currentUrl.equals("")) {
                    showErrorScreen(NO_URL);
                } else {
                    showPlayer();
                    initializePlayer(currentUrl);
                }
            } else {
                if (currentUrl.equals("")) {
                    showErrorScreen(NO_URL);
                } else {
                    showErrorScreen(NO_INTERNET);
                }
            }
        } else {
            if (isConnected()) {
                if (currentUrl.equals("")) {
                    showErrorScreen(NO_URL);
                } else {
                    currentPosition = savedInstanceState.getLong(CURRENT_POSITION);
                    playWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY);
                    currentWindow = savedInstanceState.getInt(CURRENT_WINDOW);
                    showPlayer();
                    initializePlayer(currentUrl);
                }
            } else {
                if (currentUrl.equals("")) {
                    showErrorScreen(NO_URL);
                } else {
                    showErrorScreen(NO_INTERNET);
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    public void setCurrentStepViews(int stepId, Step currentStep, int stepListLength) {

        stepCount.setText(String.format("STEP %d of %d", stepId + 1, stepListLength));

        stepShortDescription.setText(currentStep.getShortDescription());

        stepFullDescription.setText(StringModifier.removeLeadingNumberFromDescription(
                currentStep.getDescription(), currentStep.getId()));
    }

    /**
     * Helper method to check network connection
     */
    public boolean isConnected() {
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager = (ConnectivityManager)
                Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        // Return network connection info as boolean value
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Helper method to check for links to video
     */
    public String setAvaliableVideoUrl(Step step) {
        String noUrl = "";
        if (!step.getVideoURL().equals("")) {
            return step.getVideoURL();
        } else if (!step.getThumbnailURL().equals("")) {
            return step.getThumbnailURL();
        }
        return noUrl;
    }

    private void showPlayer() {
        playerView.setVisibility(View.VISIBLE);
        noVideoImage.setVisibility(View.INVISIBLE);
        noVideoText.setVisibility(View.INVISIBLE);
    }

    private void showErrorScreen(String errorMessage) {
        playerView.setVisibility(View.INVISIBLE);
        noVideoImage.setVisibility(View.VISIBLE);
        noVideoText.setVisibility(View.VISIBLE);

        if (errorMessage.equals(NO_INTERNET)) {
            noVideoText.setText(R.string.no_internet);
        } else {
            noVideoText.setText(R.string.no_video);
        }
    }

    private void initializePlayer(String videoUrl) {
        currentUrl = videoUrl;

        player = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
        playerView.setPlayer(player);

        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("Bakemate"))
                .createMediaSource(Uri.parse(videoUrl));

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentPosition);
        player.prepare(mediaSource, false, false);
    }

    private void releasePlayer() {
        if (player != null) {
            currentPosition = Math.max(0, player.getCurrentPosition());
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();

        if (fullScreenDialog != null) {
            fullScreenDialog.dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentUrl != null && player == null) {
            initializePlayer(currentUrl);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(CURRENT_POSITION, currentPosition);
        outState.putBoolean(PLAY_WHEN_READY, playWhenReady);
        outState.putInt(CURRENT_POSITION, currentWindow);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        handleFullScreenDialog();
    }

    private void initFullScreenDialog() {
        fullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (exoPlayerFullScreen) {
                    closeFullScreenDialog();
                }
                super.onBackPressed();
            }
        };
    }

    private void openFullScreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        fullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        exoPlayerFullScreen = true;
        fullScreenDialog.show();
    }

    private void closeFullScreenDialog() {
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        playerLayout.addView(playerView);
        exoPlayerFullScreen = false;
        fullScreenDialog.dismiss();
    }

    private void handleFullScreenDialog() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!currentUrl.equals("") && isConnected()) {
                initFullScreenDialog();
                openFullScreenDialog();
            }
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            if (exoPlayerFullScreen) {
                closeFullScreenDialog();
            }
        }
    }
}
