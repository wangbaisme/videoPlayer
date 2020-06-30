package com.zc.xxj.utils;

import android.content.Context;
import android.media.AudioManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

public class AudioMngHelper {

    private AudioManager audioManager;
    private int NOW_AUDIO_TYPE = TYPE_MUSIC;
    private int NOW_FLAG = FLAG_NOTHING;
    private int VOICE_STEP_100 = 2;

    public final static int TYPE_MUSIC = AudioManager.STREAM_MUSIC;
    public final static int TYPE_ALARM = AudioManager.STREAM_ALARM;
    public final static int TYPE_RING = AudioManager.STREAM_RING;
    @IntDef({TYPE_MUSIC, TYPE_ALARM, TYPE_RING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {}


    public final static int FLAG_SHOW_UI = AudioManager.FLAG_SHOW_UI;
    public final static int FLAG_PLAY_SOUND = AudioManager.FLAG_PLAY_SOUND;
    public final static int FLAG_NOTHING = 0;
    @IntDef({FLAG_SHOW_UI, FLAG_PLAY_SOUND, FLAG_NOTHING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FLAG {}

    public AudioMngHelper(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public int getSystemMaxVolume() {
        return audioManager.getStreamMaxVolume(NOW_AUDIO_TYPE);
    }

    public int getSystemCurrentVolume() {
        return audioManager.getStreamVolume(NOW_AUDIO_TYPE);
    }

    public int get100CurrentVolume() {
        return 100*getSystemCurrentVolume()/getSystemMaxVolume();
    }

    public AudioMngHelper setVoiceStep100(int step) {
        VOICE_STEP_100 = step;
        return this;
    }

    public AudioMngHelper setAudioType(@TYPE int type) {
        NOW_AUDIO_TYPE = type;
        return this;
    }

    public AudioMngHelper setFlag(@FLAG int flag) {
        NOW_FLAG = flag;
        return this;
    }

    public AudioMngHelper addVoiceSystem() {
        audioManager.adjustStreamVolume(NOW_AUDIO_TYPE,AudioManager.ADJUST_RAISE,NOW_FLAG);
        return this;
    }

    public AudioMngHelper subVoiceSystem() {
        audioManager.adjustStreamVolume(NOW_AUDIO_TYPE,AudioManager.ADJUST_LOWER,NOW_FLAG);
        return this;
    }

    public int setVoice100(int num) {
        int a = (int) Math.ceil((num)*getSystemMaxVolume()*0.01);
        a = a<=0 ? 0 : a;
        a = a>=100 ? 100 : a;
        audioManager.setStreamVolume(NOW_AUDIO_TYPE,a,0);
        return get100CurrentVolume();
    }

    public int setVoice(int num) {
        num = num > getSystemMaxVolume() ? getSystemMaxVolume() : num;
        num = num < 0 ? 0 : num;
        audioManager.setStreamVolume(NOW_AUDIO_TYPE,num,0);
        return get100CurrentVolume();
    }

    public int addVoice100() {
        int a = (int) Math.ceil((VOICE_STEP_100 + get100CurrentVolume())*getSystemMaxVolume()*0.01);
        a = a<=0 ? 0 : a;
        a = a>=100 ? 100 : a;
        audioManager.setStreamVolume(NOW_AUDIO_TYPE,a,NOW_FLAG);
        return get100CurrentVolume();
    }

    public int subVoice100() {
        int a = (int) Math.floor((get100CurrentVolume() - VOICE_STEP_100)*getSystemMaxVolume()*0.01);
        a = a<=0 ? 0 : a;
        a = a>=100 ? 100 : a;
        audioManager.setStreamVolume(NOW_AUDIO_TYPE,a,NOW_FLAG);
        return get100CurrentVolume();
    }

}
