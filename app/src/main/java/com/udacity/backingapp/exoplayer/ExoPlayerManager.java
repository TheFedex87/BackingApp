package com.udacity.backingapp.exoplayer;

import android.content.Context;
import android.net.Uri;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import javax.inject.Inject;

/**
 * Created by federico.creti on 29/03/2018.
 */

public class ExoPlayerManager implements ExoPlayer.EventListener {

    private Context context;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;

    private long startPosition = 0;

    @Inject
    public ExoPlayerManager(Context context){
        this.context = context;
    }

    public void initializePlayer(SimpleExoPlayerView simpleExoPlayerView){
        if (simpleExoPlayer == null){
            simpleExoPlayerView.requestFocus();

            //BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            //TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector();

            LoadControl loadControl = new DefaultLoadControl();

            //simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);

            simpleExoPlayer.addListener(this);
        }
    }

    public void setMediaAndPlay(Uri uri, Context context) throws Exception{
        if (simpleExoPlayer == null) {
            throw new Exception("ExoPlayer must be initialized before it's use. Use initializePlayer(simpleExoPlayerView)");
        }
        String userAgent = Util.getUserAgent(context, "BackingApp");

        MediaSource mediaSource = new ExtractorMediaSource(uri,
                new DefaultDataSourceFactory(context, userAgent),
                new DefaultExtractorsFactory(), null, null);

        simpleExoPlayer.prepare(mediaSource);
        simpleExoPlayer.seekTo(startPosition);
        simpleExoPlayer.setPlayWhenReady(true);
    }

    public void ReleasePlayer(){
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    public long getCurrentPosition(){
        if (simpleExoPlayer == null) return 0;
        return simpleExoPlayer.getCurrentPosition();
    }

    public void setCurrentPosition(long startPosition) { this.startPosition = startPosition; }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == SimpleExoPlayer.STATE_READY && playWhenReady){
            simpleExoPlayer.setPlayWhenReady(true);
        } else if(playbackState == SimpleExoPlayer.STATE_READY){
            simpleExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }


}
