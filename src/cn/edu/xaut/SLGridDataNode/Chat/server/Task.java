/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.xaut.SLGridDataNode.Chat.server;

/**
 * @author greensubmarine
 */
/*
 * 任务接口类
 * 其它任务必须继承访问该类
 */

import java.util.Date;


public abstract class Task implements Runnable {
/*产生时间*/
private Date generateTime = null;
/*提交执行时间*/
private Date submitTime = null;
/*开始执行时间*/
private Date beginExecuteTime = null;
/*执行完成时间*/
private Date finishTime = null;

private long taskId;

public Task() {
    this.generateTime = new Date();
}

/*任务执行接口*/
public void run() {
    /*
     * 执行相关代码
     */
}

/*所有任务的核心*/
public abstract Task[] taskCore() throws Exception;

/*判断是否要用到数据库*/
protected abstract boolean useDb();

/*是否需要立即执行*/
protected abstract boolean needExecuteImmediate();

/*任务信息*/
public abstract String info();

public Date getGenerateTime() {
    return generateTime;
}

public Date getBeginExceuteTime() {
    return beginExecuteTime;
}

public void setBeginExceuteTime(Date beginExecuteTime) {
    this.beginExecuteTime = beginExecuteTime;
}

public Date getFinishTime() {
    return finishTime;
}

public void setFinishTime(Date finishTime) {
    this.finishTime = finishTime;
}

public Date getSubmitTime() {
    return submitTime;
}

public void setSubmitTime(Date submitTime) {
    this.submitTime = submitTime;
}

public long getTaskId() {
    return taskId;
}

public void setTaskId(long taskId) {
    this.taskId = taskId;
}


}

