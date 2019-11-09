package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分片广播任务
 *
 * @author xuxueli 2017-07-25 20:56:50
 */
@JobHandler(value = "shardingJobHandler")
@Component
public class ShardingJobHandler extends IJobHandler {


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        //模拟查询数据库查询到了5000条数据
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            list.add(i);
        }
        // 分片参数
        ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
        XxlJobLogger.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardingVO.getIndex(), shardingVO.getTotal());
        long startTime=System.currentTimeMillis();
        int size = list.size();
        for (int j = 0; j < size; j++) {
            //对数据下标以当前分片取模选取处理
            if (j % shardingVO.getTotal() == shardingVO.getIndex()) {
                //模拟给用户发送系统消息
                XxlJobLogger.log("模拟给{}发送系统消息", j );
                //模拟执行业务需要100ms时间
                Thread.sleep(100);
            }
        }
        long endTime=System.currentTimeMillis();
        XxlJobLogger.log("耗费时间{}",endTime-startTime);
        /*// 业务逻辑
        for (int i = 0; i < shardingVO.getTotal(); i++) {
            if (i == shardingVO.getIndex()) {
                XxlJobLogger.log("第 {} 片, 命中分片开始处理", i);
            } else {
                XxlJobLogger.log("第 {} 片, 忽略", i);
            }
        }*/

        return SUCCESS;
    }

}
