package Server;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by mr.cheng on 2016/10/17.
 */

public class MyTextLineFactory implements ProtocolCodecFactory {
    private MyTextLineDecoder mTextLineDecoder;
    private MyTextLineEncoder mTextLineEncoder;

    public MyTextLineFactory() {
        mTextLineDecoder=new MyTextLineDecoder();
        mTextLineEncoder=new MyTextLineEncoder();
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return mTextLineEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return mTextLineDecoder;
    }
}
