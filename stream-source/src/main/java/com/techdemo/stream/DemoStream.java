package com.techdemo.stream;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.techdemo.entrys.StreamEntry;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DemoStream {

    private static AtomicLong seq = new AtomicLong(System.currentTimeMillis());

    private static Logger log = LoggerFactory.getLogger(DemoStream.class);

    public DemoStream() {

    }

    public static Source<StreamEntry, NotUsed> getSource() {
        Source<StreamEntry, NotUsed> source = Source.fromPublisher(new Publisher<StreamEntry>() {
            @Override
            public void subscribe(Subscriber<? super StreamEntry> s) {

                for (int i = 0; i < 100; i++) {
                    StreamEntry entry = new StreamEntry();
                    long pk = seq.incrementAndGet();
                    entry.setPk("pk" + pk);
                    entry.setOperate(StreamEntry.OperateType.values()[(int) (pk % 3)]);
                    s.onNext(entry);

                    log.info(" new entry {} ", s);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                s.onComplete();
            }
        });

        source.map((entry) -> {
            entry.setUuid(UUID.fromString(entry.getPk()).toString());
            return entry;
        });

        return source;
    }
}
