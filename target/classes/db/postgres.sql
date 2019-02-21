/*
Navicat PGSQL Data Transfer

Source Server         : localhost
Source Server Version : 100100
Source Host           : localhost:5432
Source Database       : wind_workflow
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 100100
File Encoding         : 65001

Date: 2019-02-21 13:38:07
*/


-- ----------------------------
-- Table structure for wind_active_history
-- ----------------------------
DROP TABLE IF EXISTS "public"."wind_active_history";
CREATE TABLE "public"."wind_active_history" (
"id" varchar(64) COLLATE "default" NOT NULL,
"task_id" varchar(64) COLLATE "default",
"task_name" varchar(64) COLLATE "default",
"task_display_name" varchar(64) COLLATE "default",
"process_id" varchar(64) COLLATE "default",
"order_id" varchar(64) COLLATE "default",
"process_name" varchar(64) COLLATE "default",
"process_display_name" varchar(64) COLLATE "default",
"operate" varchar(32) COLLATE "default",
"suggest" varchar(512) COLLATE "default",
"approve_time" varchar(64) COLLATE "default",
"actor_id" varchar(64) COLLATE "default",
"actor_name" varchar(64) COLLATE "default",
"approve_id" varchar(64) COLLATE "default",
"approve_name" varchar(64) COLLATE "default",
"create_time" varchar(32) COLLATE "default",
"approve_user_variable" text COLLATE "default",
"task_type" varchar(16) COLLATE "default",
"system" varchar(64) COLLATE "default",
"submit_user_variable" text COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."wind_active_history"."approve_user_variable" IS '所有审批人';
COMMENT ON COLUMN "public"."wind_active_history"."submit_user_variable" IS '实际提交人';

-- ----------------------------
-- Table structure for wind_business_id
-- ----------------------------
DROP TABLE IF EXISTS "public"."wind_business_id";
CREATE TABLE "public"."wind_business_id" (
"order_id" varchar(64) COLLATE "default" NOT NULL,
"business_id" varchar(64) COLLATE "default" NOT NULL,
"system" varchar(64) COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."wind_business_id"."system" IS '系统';

-- ----------------------------
-- Table structure for wind_complete_history
-- ----------------------------
DROP TABLE IF EXISTS "public"."wind_complete_history";
CREATE TABLE "public"."wind_complete_history" (
"id" varchar(64) COLLATE "default" NOT NULL,
"task_id" varchar(64) COLLATE "default",
"task_name" varchar(64) COLLATE "default",
"task_display_name" varchar(64) COLLATE "default",
"process_id" varchar(64) COLLATE "default",
"order_id" varchar(64) COLLATE "default",
"process_name" varchar(64) COLLATE "default",
"process_display_name" varchar(64) COLLATE "default",
"operate" varchar(32) COLLATE "default",
"suggest" varchar(512) COLLATE "default",
"approve_time" varchar(64) COLLATE "default",
"actor_id" varchar(64) COLLATE "default",
"actor_name" varchar(64) COLLATE "default",
"approve_id" varchar(64) COLLATE "default",
"approve_name" varchar(64) COLLATE "default",
"create_time" varchar(32) COLLATE "default",
"approve_user_variable" text COLLATE "default",
"task_type" varchar(16) COLLATE "default",
"system" varchar(64) COLLATE "default",
"submit_user_variable" text COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for wind_history_order_instance
-- ----------------------------
DROP TABLE IF EXISTS "public"."wind_history_order_instance";
CREATE TABLE "public"."wind_history_order_instance" (
"id" varchar(64) COLLATE "default" NOT NULL,
"process_id" varchar(64) COLLATE "default",
"status" varchar(64) COLLATE "default",
"create_user" varchar(64) COLLATE "default",
"create_time" varchar(64) COLLATE "default",
"expire_time" varchar(64) COLLATE "default",
"parent_id" varchar(64) COLLATE "default",
"version" int2,
"variable" text COLLATE "default",
"data" text COLLATE "default",
"system" varchar(64) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for wind_order_instance
-- ----------------------------
DROP TABLE IF EXISTS "public"."wind_order_instance";
CREATE TABLE "public"."wind_order_instance" (
"id" varchar(64) COLLATE "default" NOT NULL,
"process_id" varchar(64) COLLATE "default",
"status" varchar(64) COLLATE "default",
"create_user" varchar(64) COLLATE "default",
"create_time" varchar(64) COLLATE "default",
"expire_time" varchar(64) COLLATE "default",
"parent_id" varchar(64) COLLATE "default",
"version" int2,
"variable" text COLLATE "default",
"data" text COLLATE "default",
"system" varchar(64) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for wind_process_config
-- ----------------------------
DROP TABLE IF EXISTS "public"."wind_process_config";
CREATE TABLE "public"."wind_process_config" (
"id" varchar(64) COLLATE "default" NOT NULL,
"process_id" varchar(64) COLLATE "default",
"config_name" varchar(64) COLLATE "default",
"process_name" varchar(64) COLLATE "default",
"node_id" varchar(64) COLLATE "default",
"condition" text COLLATE "default",
"node_config" text COLLATE "default",
"approve_user" text COLLATE "default",
"sort" int2,
"level" int2,
"create_time" varchar(64) COLLATE "default",
"logic" varchar(16) COLLATE "default",
"business_config" text COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for wind_process_definition
-- ----------------------------
DROP TABLE IF EXISTS "public"."wind_process_definition";
CREATE TABLE "public"."wind_process_definition" (
"id" varchar(64) COLLATE "default" NOT NULL,
"process_name" varchar(64) COLLATE "default",
"display_name" varchar(64) COLLATE "default",
"status" varchar(64) COLLATE "default",
"version" int2,
"parent_id" varchar(64) COLLATE "default",
"create_time" varchar(64) COLLATE "default",
"content" bytea,
"system" varchar(64) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for wind_task_actor
-- ----------------------------
DROP TABLE IF EXISTS "public"."wind_task_actor";
CREATE TABLE "public"."wind_task_actor" (
"task_id" varchar(64) COLLATE "default",
"task_actor_id" varchar(64) COLLATE "default"
)
WITH (OIDS=FALSE)

;

-- ----------------------------
-- Table structure for wind_task_instance
-- ----------------------------
DROP TABLE IF EXISTS "public"."wind_task_instance";
CREATE TABLE "public"."wind_task_instance" (
"id" varchar(64) COLLATE "default" NOT NULL,
"task_name" varchar(64) COLLATE "default",
"display_name" varchar(64) COLLATE "default",
"task_type" varchar(16) COLLATE "default",
"approve_user" text COLLATE "default",
"create_time" varchar(64) COLLATE "default",
"approve_count" int2,
"status" varchar(64) COLLATE "default",
"order_id" varchar(64) COLLATE "default",
"process_id" varchar(64) COLLATE "default",
"variable" text COLLATE "default",
"parent_id" varchar(64) COLLATE "default",
"version" int2,
"position" varchar(512) COLLATE "default"
)
WITH (OIDS=FALSE)

;
COMMENT ON COLUMN "public"."wind_task_instance"."approve_count" IS '审批人数量  动态改变';
COMMENT ON COLUMN "public"."wind_task_instance"."status" IS '状态  关闭  流程结束所属任务全部删除';

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table wind_active_history
-- ----------------------------
ALTER TABLE "public"."wind_active_history" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wind_complete_history
-- ----------------------------
ALTER TABLE "public"."wind_complete_history" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wind_history_order_instance
-- ----------------------------
ALTER TABLE "public"."wind_history_order_instance" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wind_order_instance
-- ----------------------------
ALTER TABLE "public"."wind_order_instance" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wind_process_config
-- ----------------------------
ALTER TABLE "public"."wind_process_config" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wind_process_definition
-- ----------------------------
ALTER TABLE "public"."wind_process_definition" ADD PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table wind_task_instance
-- ----------------------------
ALTER TABLE "public"."wind_task_instance" ADD PRIMARY KEY ("id");
