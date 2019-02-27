package com.bcx.wind.workflow.message;

/**
 * @author zhanglei
 */
public class MsgConstant {


    /**过滤条件为空，查询失败**/
    public static final String w001 = "w001";

    /**id为空，操作失败**/
    public static final String w002 = "w002";

    /**数据不完整，重新输入**/
    public static final String w003 = "w003";

    /**流程定义【${0}】不存在节点【${1}】 **/
    public static final String w004 = "w004";

    /**流程定义ID【${0}】不存在**/
    public static final String w005 = "w005";

    /**缺少流程数据，无法构建模型**/
    public static final String w006 = "woo6";

    /**流程定义【${0}】状态不为回收，无法删除**/
    public static final String w007 = "w007";

    /**w008 = 流程定义【${0}】状态不为编辑，无法发布**/
    public static final String w008 = "w008";

    /**w009 = 流程定义【${0}】状态不为发布，无法回收**/
    public static final String w009 = "w009";

    /**对不起，该数据正在被使用，请稍后**/
    public static final String w010 = "w010";

    /**对不起，没有审批人！**/
    public static final String w011 = "w011";

    /**流程实例下缺少任务，请联系系统管理员**/
    public static final String w012 = "w012";

    /**对不起【${0}】,流程【${1}】暂时没有您的任务！**/
    public static final String w013 = "w013";

    /**对不起，流程实例号【${0}】没有对应的流程数据，请确认流程实例是否存在，或者错误手动操作数据库**/
    public static final String w014 = "w014";

    /**对不起，流程实例号【${0}】没有任务实例，请确认是否手动操作数据库**/
    public static final String w015 = "w015";

    /**对不起【${0}}】,流程【${0}】中暂时没有您的任务**/
    public static final String w016 = "w016";

    /**对不起，节点【${1}】不存在！**/
    public static final String w017 = "w017";

    /**对不起，节点【${0}】无法确定下一步路线的走向，请输入或配置提交路线**/
    public static final String w018 = "w018";

    /**对不起，您输入的参数submitNode->【${0}】不存在**/
    public static final String w019 = "w019";

    /**对不起，您输入的参数submitNode->【${0}】不可被提交，请选择其他任务节点**/
    public static final String w020 = "w020";

    /**对不起，当前节点【${0}】为【${1}】,禁止退回**/
    public static final String w021 = "w021";

    /**对不起，驳回节点【${0}】为【${1}】,禁止退回**/
    public static final String w022 = "w022";

    /**对不起，驳回节点【${0}】不存在，无法驳回**/
    public static final String w023 = "w023";

    /**w024 = 对不起，驳回节点不可为自身！**/
    public static final String w024 = "w024";

    /**w025 = 对不起，驳回任务不可为后续任务！**/
    public static final String w025 = "w025";

    /**对不起，该业务数据尚未启动流程，请确认是否已经启动！**/
    public static final String w026 = "wo26";

    /**对不起，该流程正处于暂停状态，请恢复流程后在进行操作！**/
    public static final String w027 = "w027";

    /**转办失败！缺少旧审批人或者新审批人信息**/
    public static final String w028 = "w028";

    /**转办失败！旧审批人不可和新审批人相同！**/
    public static final String w029 = "w029";
}
