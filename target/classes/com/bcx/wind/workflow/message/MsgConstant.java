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

    /**对不起，流程实例号【${0}】没有对应的额流程数据，请确认流程实例是否存在，或者错误手动操作数据库**/
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

}
