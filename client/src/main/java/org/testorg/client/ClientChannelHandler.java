package org.testorg.client;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.testorg.client.util.HL7Utils;

public class ClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        HapiContext context = new DefaultHapiContext();
        context.setModelClassFactory(new DefaultModelClassFactory());
        PipeParser parser = context.getPipeParser();

        ORU_R01 mm = (ORU_R01) parser.parse(msg.toString(CharsetUtil.UTF_8));
        System.out.println(HL7Utils.getPatientFromPID(mm.getPATIENT_RESULT().getPATIENT().getPID()));
        System.out.println(mm.printStructure());
        System.out.println(HL7Utils.getObservationFrom(mm.getPATIENT_RESULT().getORDER_OBSERVATIONAll()));
        System.out.println(mm.getPATIENT_RESULT().getPATIENT().getPID());
        System.out.println(mm.getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATION(2).getOBX().getObx4_ObservationSubID().getValue());
        System.out.println("IT IS ME!");
    }
}
