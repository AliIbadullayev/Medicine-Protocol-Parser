import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerChannelHandler extends SimpleChannelInboundHandler<String> {
    private static final AtomicInteger connections = new AtomicInteger(0);

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // не делаем ничего, так как сервер не получает данные от клиента
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Connected: "+ctx.channel().remoteAddress() + " (Active clients: "+ connections.incrementAndGet()+")");
        scheduler.scheduleAtFixedRate(() -> {
            ctx.writeAndFlush(Unpooled.copiedBuffer("MSH|^~\\&|VSP^080019FFFE1443EC^EUI-64|GE Healthcare|||20230309143900||ORU^R01^ORU_R01|0040971443EC|P|2.6|||NE|AL||UNICODE UTF-8|||PCD_DEC_001^IHE PCD^1.3.6.1.4.1.19376.1.6.1.1.1^ISO\r"
                    + "PID|||3499999^^^PID^MR||КОКОНОВА^ТАМАРА^^^^^L||19680309000000|\r"
                    + "PV1||E|X^^443EC\r"
                    + "OBR|1|080019FFFE1443EC20230309143900^VSP^080019FFFE1443EC^EUI-64|080019FFFE1443EC20230309143900^VSP^080019FFFE1443EC^EUI-64|182777000^monitoring of patient^SCT|||20230309143900\r"
                    + "OBX|1||69965^MDC_DEV_MON_PHYSIO_MULTI_PARAM_MDS^MDC|1.0.0.0|||||||X\r" +
                    "OBX|2||69798^MDC_DEV_ECG_VMD^MDC|1.5.0.0|||||||X\r" +
                    "OBX|3|NM|147842^MDC_ECG_HEART_RATE^MDC|1.5.1.1|68|264864^MDC_DIM_BEAT_PER_MIN^MDC|||||R|||||||080019FFFE1443EC^B40_GE\r" +
                    "OBX|4||69766^MDC_DEV_GEN_CONC_AWAY_VMD^MDC|1.11.0.0|||||||X\r" +
                    "OBX|5|NM|151712^MDC_CONC_AWAY_CO2_EXP^MDC|1.11.1.1|1|266016^MDC_DIM_MMHG^MDC|||||R|||||||080019FFFE1443EC^B40_GE\r" +
                    "OBX|6|NM|151716^MDC_CONC_AWAY_CO2_INSP^MDC|1.11.1.2|1|266016^MDC_DIM_MMHG^MDC|||||R|||||||080019FFFE1443EC^B40_GE\r" +
                    "OBX|7||69642^MDC_DEV_ANALY_SAT_O2_VMD^MDC|1.22.0.0|||||||X\r" +
                    "OBX|8|NM|149530^MDC_PULS_OXIM_PULS_RATE^MDC|1.22.1.1|67|264864^MDC_DIM_BEAT_PER_MIN^MDC|||||R|||||||080019FFFE1443EC^B40_GE\r" +
                    "OBX|9|NM|150456^MDC_PULS_OXIM_SAT_O2^MDC|1.22.1.2|97|262688^MDC_DIM_PERCENT^MDC|||||R|||||||080019FFFE1443EC^B40_GE\r", CharsetUtil.UTF_8));
        }, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Disconnected: "+ctx.channel().remoteAddress()+ " (Active clients: "+ connections.decrementAndGet()+")");
        scheduler.shutdown();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println(cause.getMessage() + ": " + ctx.channel().remoteAddress());
    }
}
