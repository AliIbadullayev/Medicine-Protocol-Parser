package org.testorg.client.servlet;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.PipeParser;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.testorg.client.pojo.Message;
import org.testorg.client.pojo.Observation;
import org.testorg.client.pojo.Patient;
import org.testorg.client.util.HL7Utils;
import org.testorg.client.websocket.WebSocket;

import javax.websocket.EncodeException;
import java.io.IOException;

public class ClientChannelHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final HapiContext context;

    public ClientChannelHandler(HapiContext context) {
        this.context = context;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        context.setModelClassFactory(new DefaultModelClassFactory());
        PipeParser parser = context.getPipeParser();

        try {
            ORU_R01 mm = (ORU_R01) parser.parse(msg.toString(CharsetUtil.UTF_8));

            Patient patient = HL7Utils.getPatientFromPID(mm.getPATIENT_RESULT().getPATIENT().getPID());
            Observation observation = HL7Utils.getObservationFrom(mm.getPATIENT_RESULT().getORDER_OBSERVATIONAll());

            HL7Utils.insertPatient(patient);
            HL7Utils.insertObservation(patient.getSnils(), observation);

            Message message = new Message();
            message.setPatient(patient);
            message.setObservation(observation);

            WebSocket.broadcast(message);
        } catch (HL7Exception | IOException | EncodeException e) {
            throw new RuntimeException(e);
        }
    }
}
