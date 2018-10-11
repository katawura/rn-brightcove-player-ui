package jp.manse;

import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.brightcove.player.edge.Catalog;
import com.brightcove.player.edge.VideoListener;
import com.brightcove.player.event.Event;
import com.brightcove.player.event.EventEmitter;
import com.brightcove.player.event.EventListener;
import com.brightcove.player.event.EventType;
import com.brightcove.player.mediacontroller.BrightcoveMediaController;
import com.brightcove.player.model.Video;
import com.brightcove.player.view.BrightcoveTextureVideoView;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.HashMap;
import java.util.Map;


public class BrightcovePlayerView extends RelativeLayout {
    private ThemedReactContext context;
    private BrightcoveTextureVideoView playerVideoView;
    private BrightcoveMediaController mediaController;
    private String policyKey;
    private String accountId;
    private String videoId;
    private String referenceId;
    private Catalog catalog;
    private boolean autoPlay = true;
    private boolean playing = false;
    private boolean fullscreen = false;

    public BrightcovePlayerView(ThemedReactContext context) {
        this(context, null);
    }

    public BrightcovePlayerView(ThemedReactContext context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setBackgroundColor(Color.BLACK);

        this.playerVideoView = new BrightcoveTextureVideoView(this.context);
        
        this.addView(this.playerVideoView);
        LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        this.playerVideoView.setLayoutParams(lp);
        this.playerVideoView.finishInitialization();
        this.mediaController = new BrightcoveMediaController(this.playerVideoView);
        this.playerVideoView.setMediaController(this.mediaController);
        this.requestLayout();
        ViewCompat.setTranslationZ(this, 0);

        EventEmitter eventEmitter = this.playerVideoView.getEventEmitter();
        eventEmitter.on(EventType.VIDEO_SIZE_KNOWN, new EventListener() {
            @Override
            public void processEvent(Event e) {
                fixVideoLayout();
            }
        });
        eventEmitter.on(EventType.READY_TO_PLAY, new EventListener() {
            @Override
            public void processEvent(Event e) {
                WritableMap event = Arguments.createMap();
                ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_READY, event);
            }
        });
        eventEmitter.on(EventType.DID_PLAY, new EventListener() {
            @Override
            public void processEvent(Event e) {
                BrightcovePlayerView.this.playing = true;
                WritableMap event = Arguments.createMap();
                ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_PLAY, event);
            }
        });
        eventEmitter.on(EventType.DID_PAUSE, new EventListener() {
            @Override
            public void processEvent(Event e) {
                BrightcovePlayerView.this.playing = false;
                WritableMap event = Arguments.createMap();
                ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_PAUSE, event);
            }
        });
        eventEmitter.on(EventType.COMPLETED, new EventListener() {
            @Override
            public void processEvent(Event e) {
                WritableMap event = Arguments.createMap();
                ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_END, event);
            }
        });
        eventEmitter.on(EventType.PROGRESS, new EventListener() {
            @Override
            public void processEvent(Event e) {
                WritableMap event = Arguments.createMap();
                Long playhead = (Long)e.properties.get(Event.PLAYHEAD_POSITION);
                event.putDouble("currentTime", playhead / 1000d);
                ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_PROGRESS, event);
            }
        });
        eventEmitter.on(EventType.ENTER_FULL_SCREEN, new EventListener() {
            @Override
            public void processEvent(Event e) {
                mediaController.show();
                WritableMap event = Arguments.createMap();
                ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_TOGGLE_ANDROID_FULLSCREEN, event);
            }
        });
        eventEmitter.on(EventType.EXIT_FULL_SCREEN, new EventListener() {
            @Override
            public void processEvent(Event e) {
                mediaController.show();
                WritableMap event = Arguments.createMap();
                ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_TOGGLE_ANDROID_FULLSCREEN, event);
            }
        });
        eventEmitter.on(EventType.VIDEO_DURATION_CHANGED, new EventListener() {
            @Override
            public void processEvent(Event e) {
                Integer duration = (Integer)e.properties.get(Event.VIDEO_DURATION);
                WritableMap event = Arguments.createMap();
                event.putDouble("duration", duration / 1000d);
                ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_CHANGE_DURATION, event);
            }
        });
        eventEmitter.on(EventType.BUFFERED_UPDATE, new EventListener() {
            @Override
            public void processEvent(Event e) {
                Integer percentComplete = (Integer)e.properties.get(Event.PERCENT_COMPLETE);
                WritableMap event = Arguments.createMap();
                event.putDouble("bufferProgress", percentComplete / 100d);
                ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_UPDATE_BUFFER_PROGRESS, event);
            }
        });
    }

    public void setPolicyKey(String policyKey) {
        this.policyKey = policyKey;
        this.setupCatalog();
        this.loadMovie();
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
        this.setupCatalog();
        this.loadMovie();
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
        this.referenceId = null;
        this.setupCatalog();
        this.loadMovie();
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
        this.videoId = null;
        this.setupCatalog();
        this.loadMovie();
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void setPlay(boolean play) {
        if (this.playing == play) return;
        if (play) {
            this.playerVideoView.start();
        } else {
            this.playerVideoView.pause();
        }
    }

    public void setDefaultControlDisabled(boolean disabled) {
        this.mediaController.hide();
        this.mediaController.setShowHideTimeout(disabled ? 1 : 4000);
    }

    public void setFullscreen(boolean fullscreen) {
        this.mediaController.show();

        //BrightcovePlayerView.this.setRotation(90);
        WritableMap event = Arguments.createMap();
        event.putBoolean("fullscreen", fullscreen);
        ReactContext reactContext = (ReactContext) BrightcovePlayerView.this.getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(BrightcovePlayerView.this.getId(), BrightcovePlayerManager.EVENT_TOGGLE_ANDROID_FULLSCREEN, event);
    }

    public void setVolume(float volume) {
        Map<String, Object> details = new HashMap<>();
        details.put(Event.VOLUME, volume);
        this.playerVideoView.getEventEmitter().emit(EventType.SET_VOLUME, details);
    }

    public void seekTo(int time) {
        this.playerVideoView.seekTo(time);
    }

    private void setupCatalog() {
        if (this.catalog != null || this.policyKey == null || this.accountId == null) return;
        this.catalog = new Catalog(this.playerVideoView.getEventEmitter(), this.accountId, this.policyKey);
    }

    private void loadMovie() {
        if (this.catalog == null) return;
        VideoListener listener = new VideoListener() {

            @Override
            public void onVideo(Video video) {
                BrightcovePlayerView.this.playerVideoView.clear();
                BrightcovePlayerView.this.playerVideoView.add(video);
                if (BrightcovePlayerView.this.autoPlay) {
                    BrightcovePlayerView.this.playerVideoView.start();
                }
            }
        };
        if (this.videoId != null) {
            this.catalog.findVideoByID(this.videoId, listener);
        } else if (this.referenceId != null) {
            this.catalog.findVideoByReferenceID(this.referenceId, listener);
        }
    }

    private void fixVideoLayout() {

    }

    public int dpToPx(int dp) {
        float density = this.context.getResources()
                            .getDisplayMetrics()
                            .density;
        return Math.round((float) dp * density);
    }

    private void printKeys(Map<String, Object> map) {
        Log.d("debug", "-----------");
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            Log.d("debug", entry.getKey());
        }
    }
}
