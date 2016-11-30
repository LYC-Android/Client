package Server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * Created by mr.cheng on 2016/10/17.
 */

public class MyTextLineDecoder implements ProtocolDecoder {

    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput Output) throws Exception {
         int startPostion=ioBuffer.position();
        while (ioBuffer.hasRemaining()){
            byte b=ioBuffer.get();
            if (b=='\n'){
                int currentPosition=ioBuffer.position();
                int limit=ioBuffer.limit();
                ioBuffer.position(startPostion);
                ioBuffer.limit(limit);
                IoBuffer buf=ioBuffer.flip();
                byte[] dest=new byte[buf.limit()];
                buf.get(dest);
                String str=new String(dest);
                Output.write(str);
                ioBuffer.position(currentPosition);
                ioBuffer.limit(limit);
            }
        }
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
