package com.nepalese.toollibs.Activity.ComponentThird;

import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;

import java.lang.reflect.Method;

public class HdiPlayer {
    static final String TAG                 = "stbserver";
    static final String STB_SERVICE_NAME    = "stbserver";
    static final int    STB_TRANSACT_CODE_OPEN  = 0x2001;
    static final int    STB_TRANSACT_CODE_CLOSE = 0x2002;
    static final int    STB_TRANSACT_CODE_START = 0x2003;
    static final int    STB_TRANSACT_CODE_STOP  = 0x2004;
    static IBinder m_stbsp = null;

    /*
    called before entering HDI play case, to do ready for coming HDI playing.
    **/
    public static int initPlay(){
        Log.d(TAG, "initPlay>>>to do ");
        Class localClass;
        try {
            localClass = Class.forName("android.os.ServiceManager");
            Method getService = localClass.getMethod("getService", new Class[] {String.class});
            if(getService != null) {
                Object result = getService.invoke(localClass, new Object[]{STB_SERVICE_NAME});
                m_stbsp = (IBinder) result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(null == m_stbsp) {
            Log.e(TAG, "Can't connect to service: "+STB_SERVICE_NAME);
        }
        try {
            if(null != m_stbsp) {
                Parcel data  = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInt(0);
                if(m_stbsp.transact(STB_TRANSACT_CODE_OPEN,data,reply,0)) {
                    Log.d(TAG, "open success: ");
                } else {
                    Log.e(TAG, "open Failed ");
                }
                data.recycle();
                reply.recycle();
            }
        }catch(Throwable t) {
            Log.e(TAG, "Exception", t);
        }
        return 0;
    }

    /*
    called after stop all  HDI play case and exit HDI play case.
    NOTE: Must stop all HDI playing before call termPlay.
    **/
    public static int termPlay(){
        Log.d(TAG, "termPlay>>>to do ");

        try {
            if(null != m_stbsp) {
                Parcel data  = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInt(0);
                if(m_stbsp.transact(STB_TRANSACT_CODE_CLOSE,data,reply,0)) {
                    Log.d(TAG, "close success: ");
                } else {
                    Log.e(TAG, "close Failed ");
                }
                data.recycle();
                reply.recycle();
            }
        }catch(Throwable t) {
            Log.e(TAG, "Exception", t);
        }

        return 0;
    }

    /*
    to play appointed resource by HDI .
    @param tunerId tuner id such as 1,2,3,4...
    @param strfreq frequency resource, such as "435000.6875.64",  frequency(KHZ)+Synbols(KHZ)+QMode
    @param audPid
    @param vidpid
    @param cprpid
    @param vidType video stream type, such as VIDEO_STREAM_TYPE_MPEG2(2), VIDEO_STREAM_TYPE_H264(5), refering HID AV define
    @param audType audio stream type such as AUDIO_STREAM_TYPE_AAC(4), AUDIO_STREAM_TYPE_AC3(6), refering HDI AV define
    @param posX video position left
    @param posY video position top
    @param posW video width
    @param posH video height
    @return <= 0 for failed to doplay, > 0 for successfully play ID
    **/
    public static int startPlay(int tunerId, String strfreq,
                                int audPid, int vidPid, int pcrPid,
                                int vidType, int audType,
                                int posX, int posY, int posW, int posH){
        Log.d(TAG, "startPlay>>>to do tunerId="+tunerId+" strfreq="+strfreq+
                " audPid="+audPid+" vidPid="+vidPid+" audType="+audType
                +" vidType="+vidType
                +" pcrPid="+pcrPid);
        Log.d(TAG,"  x="+posX+" y="+posY+ "  height="+posH+" width="+posW);

        Parcel data   = Parcel.obtain();
        Parcel reply  = Parcel.obtain();

        data.writeInt(tunerId);
        data.writeString(strfreq);
        data.writeInt(audPid);
        data.writeInt(vidPid);
        data.writeInt(pcrPid);
        data.writeInt(vidType);
        data.writeInt(audType);
        data.writeInt(posX);
        data.writeInt(posY);
        data.writeInt(posW);
        data.writeInt(posH);

        if(m_stbsp == null) {
            return 0;
        }

        try {
            if(m_stbsp.transact(STB_TRANSACT_CODE_START,data,reply,0)) {
                Log.d(TAG, "read success");
            } else {
                Log.e(TAG, "read Failed");
            }
        }catch(Throwable t) {
            Log.e(TAG, "Exception", t);
        }

        reply.recycle();
        data.recycle();

        return 0;
    }

    /*
    to do stop appointed play by playId .
    @param playId return by startPlay
    @return == 0 successfully stop play, != 0 failed to stoip
    **/
    public static int stopPlay(int playId){
        Log.d(TAG, "stopPlay>>>to do playId="+playId);

        Parcel reply   = Parcel.obtain();
        Parcel request = Parcel.obtain();

        if(m_stbsp == null) {
            return 0;
        }

        try {
            request.writeInt(playId);
            if(m_stbsp.transact(STB_TRANSACT_CODE_STOP,request, reply,0)) {
                Log.d(TAG, "stop success");
            } else {
                Log.e(TAG, "stop Failed");
            }
        }catch(Throwable t) {
            Log.e(TAG, "Exception", t);
        }
        return 0;
    }

    public HdiPlayer(Context context){
        //super(context);
    }
}
