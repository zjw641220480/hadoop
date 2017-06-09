package cn.itcast.tom.storm.ackfail;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Created by maoxiangyi on 2016/4/25.
 */
public class MySpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private Random rand;
    private Map<String,Values> buffer = new HashMap<>();


    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence"));
        rand = new Random();
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        String[] sentences = new String[]{"the cow jumped over the moon",
                "the cow jumped over the moon",
                "the cow jumped over the moon",
                "the cow jumped over the moon", "the cow jumped over the moon"};
        String sentence = sentences[rand.nextInt(sentences.length)];
        String messageId = UUID.randomUUID().toString().replace("-", "");
        Values tuple = new Values(sentence);
        collector.emit(tuple, messageId);
        buffer.put(messageId,tuple);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ack(Object msgId) {
        System.out.println("消息处理成功，id= " + msgId);
        buffer.remove(msgId);
    }

    @Override
    public void fail(Object msgId) {
        System.out.println("消息处理失败，id= " + msgId);
        Values tuple = buffer.get(msgId);
        collector.emit(tuple,msgId);
    }
}
