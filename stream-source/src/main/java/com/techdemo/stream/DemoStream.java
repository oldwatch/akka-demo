package com.techdemo.stream;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techdemo.entrys.StreamEntry;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class DemoStream {

    private static AtomicLong seq = new AtomicLong(System.currentTimeMillis());

    private static Logger log = LoggerFactory.getLogger(DemoStream.class);

    final Random rnd = new Random();

    public DemoStream() {

    }

    private final ObjectMapper mapper = new ObjectMapper();

    public Source<String, NotUsed> getRandom() {
        return Source.fromIterator(() ->
                Stream.generate(rnd::nextInt).iterator()
        ).map((i) -> String.valueOf(i) + "\n");
    }

    public Source<String, NotUsed> getSource2() {

        return Source.fromIterator(() ->
                Stream.generate(() -> {
                            StreamEntry entry = new StreamEntry();
                            long pk = seq.incrementAndGet();
                            entry.setPk("pk" + pk);
                            entry.setOperate(StreamEntry.OperateType.values()[(int) (pk % 3)]);
                            return entry;
                        }

                ).iterator()
        ).map((entry) -> {
            entry.setUuid(UUID.nameUUIDFromBytes(entry.getPk().getBytes()).toString());

            log.info(" new entry {} ", entry.getPk());

            return entry;
        })
                .map(mapper::writeValueAsString)
                .map(str -> str + "\n");

    }

    public Source<String, NotUsed> getSource() {

        return Source.fromPublisher(new Publisher<StreamEntry>() {
            @Override
            public void subscribe(Subscriber<? super StreamEntry> s) {

                for (int i = 0; i < 100; i++) {
                    StreamEntry entry = new StreamEntry();
                    long pk = seq.incrementAndGet();
                    entry.setPk("pk" + pk);
                    entry.setOperate(StreamEntry.OperateType.values()[(int) (pk % 3)]);
                    s.onNext(entry);

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                s.onComplete();
            }
        }).map((entry) -> {
            entry.setUuid(UUID.nameUUIDFromBytes(entry.getPk().getBytes()).toString());

            log.info(" new entry {} ", entry.getPk());

            return entry;
        })
                .map(mapper::writeValueAsString)
                .map(str -> str + "\n");

    }
}
