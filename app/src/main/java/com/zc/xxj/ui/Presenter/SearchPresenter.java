package com.zc.xxj.ui.Presenter;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.zc.xxj.R;
import com.zc.xxj.ui.Contract.SearchContract;
import com.zc.xxj.ui.Model.SearchModel;
import com.zc.xxj.utils.FucUtil;
import com.zc.xxj.utils.JsonUtil;
import com.zc.xxj.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executors;

public class SearchPresenter implements SearchContract.SearchPresenter {
    private static final String TAG = SearchPresenter.class.getSimpleName();

    private SearchContract.SearchModel mSearchModel;
    private SearchContract.SearchView mView;
    private Context mContext;
    private SpeechRecognizer mIat;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private StringBuffer buffer = new StringBuffer();
    private int ret;

    public SearchPresenter(Context context, SearchContract.SearchView mView){
        this.mView = mView;
        this.mContext = context;
        mSearchModel = new SearchModel(context) {
            @Override
            public void callbackSearchHistory(List<String> list) {
                mView.refreshHistoryFlowView(list);
            }

            @Override
            public void callbackHotRecomment(List<String> list) {
                mView.refreshHotRecommentFlowView(list);
            }
        };
        init();
    }



    private void init(){
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        setParam();
    }

    private void setParam(){
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //设置语音输入语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
        // 取值范围{1000～10000}
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
        // 自动停止录音，范围{0~10000}
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        //设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    @Override
    public void refreshHistoryView() {
        mSearchModel.getSearchHistory();
    }

    @Override
    public void refreshRecommentView() {
        mSearchModel.getHotRecomment();
    }

    @Override
    public void updateSearchHistoryData(List<String> list) {
        mSearchModel.updateSearchHistory(list);
    }

    @Override
    public void updateSearchHistoryData(String currentSelectWord) {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                List<String> historyList = mView.getHistoryList();
                historyList.remove(currentSelectWord);
                historyList.add(currentSelectWord);
                mSearchModel.updateSearchHistory(historyList);
            }
        });
    }

    @Override
    public void initRecord() {
        init();
    }

    @Override
    public void startRecord() {
        mIat.startListening(mRecognizerListener);
    }

    @Override
    public void stopRecord() {
        mIat.stopListening();
    }

    @Override
    public void cancelRecord() {
        mIat.cancel();
    }

    @Override
    public void destroyRecord() {
        if( null != mIat ){
            mIat.cancel();
            mIat.destroy();
        }
    }


    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            mView.showRecordWaveView();
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            mView.hideRecordWaveView();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            printResult(results);
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {

        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonUtil.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        mView.recodeContentText(resultBuffer.toString());
    }

    private void showTip(String msg){
        ToastUtil.getInstance(mContext).showShortToast(msg);
    }

    //执行音频流识别操作,或许以后会用到
//    private void executeStream() {
//        buffer.setLength(0);
//        mIatResults.clear();
//        // 设置参数
//        setParam();
//        // 设置音频来源为外部文件
//        mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
//        // 也可以像以下这样直接设置音频文件路径识别（要求设置文件在sdcard上的全路径）：
//        // mIat.setParameter(SpeechConstant.AUDIO_SOURCE, "-2");
//        //mIat.setParameter(SpeechConstant.ASR_SOURCE_PATH, "sdcard/XXX/XXX.pcm");
//        ret = mIat.startListening(mRecognizerListener);
//        if (ret != ErrorCode.SUCCESS) {
//            showTip("识别失败,错误码：" + ret);
//        } else {
//            //此处读目标文件转化为二进制文件
//            byte[] audioData = FucUtil.readAudioFile(mContext, "iattest.wav");
//
//            if (null != audioData) {
//                // 一次（也可以分多次）写入音频文件数据，数据格式必须是采样率为8KHz或16KHz（本地识别只支持16K采样率，云端都支持），
//                // 位长16bit，单声道的wav或者pcm
//                // 写入8KHz采样的音频时，必须先调用setParameter(SpeechConstant.SAMPLE_RATE, "8000")设置正确的采样率
//                // 注：当音频过长，静音部分时长超过VAD_EOS将导致静音后面部分不能识别。
//                ArrayList<byte[]> bytes = FucUtil.splitBuffer(audioData,audioData.length,audioData.length/3);
//                for(int i=0;i<bytes.size();i++) {
//                    mIat.writeAudio(bytes.get(i), 0, bytes.get(i).length );
//
//                    try {
//                        Thread.sleep(1000);
//                    }catch(Exception e){
//
//                    }
//                }
//                mIat.stopListening();
//				/*mIat.writeAudio(audioData, 0, audioData.length );
//				mIat.stopListening();*/
//            } else {
//                mIat.cancel();
//                showTip("读取音频流失败");
//            }
//        }
//    }
}
