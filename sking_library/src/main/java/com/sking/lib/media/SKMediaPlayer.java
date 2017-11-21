package com.sking.lib.media;

import android.media.MediaPlayer;

import com.sking.lib.utils.SKLogger;


class SKMediaPlayer extends MediaPlayer {
	private static final String TAG = "VoicePlayer";

	private boolean prepared;
	private SKVoicePlayer xtomVoicePlayer;

	private PreparedListener preparedListener;
	private CompletionListener completionListener;
	private SeekCompleteListener seekCompleteListener;
	private ErrorListener errorListener;

	SKMediaPlayer(SKVoicePlayer xtomVoicePlayer) {
		this.xtomVoicePlayer = xtomVoicePlayer;
		this.preparedListener = new PreparedListener();
		this.completionListener = new CompletionListener();
		this.seekCompleteListener = new SeekCompleteListener();
		this.errorListener = new ErrorListener();
		setListener();
	}

	private void setListener() {
		setOnPreparedListener(preparedListener);
		setOnErrorListener(errorListener);
		setOnCompletionListener(completionListener);
		setOnSeekCompleteListener(seekCompleteListener);
	}

	@Override
	public void reset() {
		super.reset();
		prepared = false;
		setListener();
	}

	@Override
	public void stop() throws IllegalStateException {
		super.stop();
		reset();
	}

	private class PreparedListener implements OnPreparedListener {

		@Override
		public void onPrepared(MediaPlayer mp) {
			prepared = true;
		}
	}

	private class CompletionListener implements OnCompletionListener {

		@Override
		public void onCompletion(MediaPlayer mp) {
			xtomVoicePlayer.cancelTimeThread();
			SKVoicePlayer.XtomVoicePlayListener listener = xtomVoicePlayer
					.getXtomVoicePlayListener();
			if (listener != null)
				listener.onComplete(xtomVoicePlayer);

		}
	}

	private class SeekCompleteListener implements OnSeekCompleteListener {

		@Override
		public void onSeekComplete(MediaPlayer mp) {
			SKLogger.i(TAG, "onSeekComplete");
		}
	}

	private class ErrorListener implements OnErrorListener {

		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			SKVoicePlayer.XtomVoicePlayListener listener = xtomVoicePlayer
					.getXtomVoicePlayListener();
			if (listener != null)
				listener.onError(xtomVoicePlayer);
			return false;
		}
	}

	public boolean isPrepared() {
		return prepared;
	}

}
