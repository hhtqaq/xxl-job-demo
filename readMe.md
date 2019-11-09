#XXL-Job
因为公司觉得有些业务需要通过定时框架来解决。恰好上家公司时做过quartz框架相关的工作，所以接下了这个活。

本来先入为主心里一直觉得quartz天下无敌，后面在网上找了很多资料，发现了quartz确实存在一些不友好的地方，经过对比
决定使用xxl-job。
##Quartz定时任务框架存在的问题
Quartz作为开源作业调度中的佼佼者，是作业调度的首选。但是集群环境中Quartz采用API的方式对任务进行管理，从而可以避免上述问题，但是同样存在以下问题：

- 问题一：调用API的的方式操作任务，不人性化；
- 问题二：需要持久化业务QuartzJobBean到底层数据表中，系统侵入性相当严重。

- 问题三：调度逻辑和QuartzJobBean耦合在同一个项目中，这将导致一个问题，在调度任务数量逐渐增多，同时调度任务逻辑逐渐加重的情况加，此时调度系统的性能将大大受限于业务；
- 问题四：quartz底层以“抢占式”获取DB锁并由抢占成功节点负责运行任务，会导致节点负载悬殊非常大；而XXL-JOB通过执行器实现“协同分配式”运行任务，充分发挥集群优势，负载各节点均衡。

quartz的分布式调度策略是以数据库为边界资源的一种异步策略。各个调度器都遵守一个基于数据库锁的操作规则从而保证了操作的唯一性。同时多个节点的异步运行保证了服务的可靠。但这种策略有自己的局限性：集群特性对于高CPU使用率的任务效果很好，但是对于大量的短任务，各个节点都会抢占数据库锁，这样就出现大量的线程等待资源。这种情况随着节点的增加会越来越严重。
quartz的分布式只是解决了高可用的问题，并没有解决任务分片的问题，还是会有单机处理的极限。