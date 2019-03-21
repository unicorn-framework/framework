package org.unicorn.framework.core.utils;

import com.google.common.base.Preconditions;
import lombok.SneakyThrows;

import java.util.Calendar;

/**
 * @author xiebin
 */
public class IdGeneratorSingleton {
    public static final long EPOCH;
    /**
     * 自增序列占12位
     */
    private static final long SEQUENCE_BITS = 12L;
    /**
     * 节点  共10位
     */
    private static final long WORKER_ID_BITS = 10L;
    /**
     * 自增序列最大值
     */
    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;
    /**
     * 自增序列左移12位
     */
    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;
    /**
     * 时间戳左移 12+10=22位
     */
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;
    /**
     * 工作节点最大值
     */
    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;


    private static long workerId;
    /**
     * 最大回拨时间 毫秒 默认10毫秒
     */
    private static int maxTolerateTimeDifferenceMilliseconds = 10;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2019, Calendar.JANUARY, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        EPOCH = calendar.getTimeInMillis();
    }

    private byte sequenceOffset;

    private long sequence;

    private long lastMilliseconds;


    public static void setWorkerId(final long workerId) {
        Preconditions.checkArgument(workerId >= 0L && workerId < WORKER_ID_MAX_VALUE);
        IdGeneratorSingleton.workerId = workerId;
    }


    public static void setMaxTolerateTimeDifferenceMilliseconds(final int maxTolerateTimeDifferenceMilliseconds) {
        IdGeneratorSingleton.maxTolerateTimeDifferenceMilliseconds = maxTolerateTimeDifferenceMilliseconds;
    }


    private IdGeneratorSingleton() {
        if (SingletonHolder.instance != null) {
            throw new IllegalStateException();
        }
    }

    private static class SingletonHolder {
        private static IdGeneratorSingleton instance = new IdGeneratorSingleton();
    }

    public static IdGeneratorSingleton getInstance() {
        return SingletonHolder.instance;
    }


    public synchronized Number generateKey() {
        long currentMilliseconds = System.currentTimeMillis();
        if (waitTolerateTimeDifferenceIfNeed(currentMilliseconds)) {
            currentMilliseconds = System.currentTimeMillis();
        }
        if (lastMilliseconds == currentMilliseconds) {
            if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)) {
                currentMilliseconds = waitUntilNextTime(currentMilliseconds);
            }
        } else {
            vibrateSequenceOffset();
            sequence = sequenceOffset;
        }
        lastMilliseconds = currentMilliseconds;
        return ((currentMilliseconds - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (workerId << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

    @SneakyThrows
    private boolean waitTolerateTimeDifferenceIfNeed(final long currentMilliseconds) {
        if (lastMilliseconds <= currentMilliseconds) {
            return false;
        }
        long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;
        Preconditions.checkState(timeDifferenceMilliseconds < maxTolerateTimeDifferenceMilliseconds,
                "时钟回拨超过设置值, 最后时间： %d milliseconds, 当前时间 %d milliseconds", lastMilliseconds, currentMilliseconds);
        Thread.sleep(timeDifferenceMilliseconds);
        return true;
    }

    private long waitUntilNextTime(final long lastTime) {
        long result = System.currentTimeMillis();
        while (result <= lastTime) {
            result = System.currentTimeMillis();
        }
        return result;
    }

    private void vibrateSequenceOffset() {
        sequenceOffset = (byte) (~sequenceOffset & 1);
    }

    /**
     * 测试
     * @param args
     */
    public  static void main(String [] args){
      for(int i=0;i<1000;i++){
         System.out.println(IdGeneratorSingleton.getInstance().generateKey());
      }
    }
}
