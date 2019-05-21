-- spring batch
-- BATCH JOB 实例表 包含与aJobInstance相关的所有信息
-- JOB ID由batch_job_seq分配
-- JOB 名称,与spring配置一致
-- JOB KEY 对job参数的MD5编码,正因为有这个字段的存在，同一个job如果第一次运行成功，第二次再运行会抛出JobInstanceAlreadyCompleteException异常。
CREATE TABLE BATCH_JOB_INSTANCE  (
                                   JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
                                   VERSION BIGINT ,
                                   JOB_NAME VARCHAR(100) NOT NULL,
                                   JOB_KEY VARCHAR(32) NOT NULL,
                                   constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

-- 该BATCH_JOB_EXECUTION表包含与该JobExecution对象相关的所有信息
CREATE TABLE BATCH_JOB_EXECUTION  (
                                    JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                    VERSION BIGINT  ,
                                    JOB_INSTANCE_ID BIGINT NOT NULL,
                                    CREATE_TIME DATETIME NOT NULL,
                                    START_TIME DATETIME DEFAULT NULL ,
                                    END_TIME DATETIME DEFAULT NULL ,
                                    STATUS VARCHAR(10) ,
                                    EXIT_CODE VARCHAR(2500) ,
                                    EXIT_MESSAGE VARCHAR(2500) ,
                                    LAST_UPDATED DATETIME,
                                    JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
                                    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
                                      references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ENGINE=InnoDB;

-- 该表包含与该JobParameters对象相关的所有信息
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
                                           JOB_EXECUTION_ID BIGINT NOT NULL ,
                                           TYPE_CD VARCHAR(6) NOT NULL ,
                                           KEY_NAME VARCHAR(100) NOT NULL ,
                                           STRING_VAL VARCHAR(250) ,
                                           DATE_VAL DATETIME DEFAULT NULL ,
                                           LONG_VAL BIGINT ,
                                           DOUBLE_VAL DOUBLE PRECISION ,
                                           IDENTIFYING CHAR(1) NOT NULL ,
                                           constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
                                             references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

-- 该表包含与该StepExecution 对象相关的所有信息
CREATE TABLE BATCH_STEP_EXECUTION  (
                                     STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                     VERSION BIGINT NOT NULL,
                                     STEP_NAME VARCHAR(100) NOT NULL,
                                     JOB_EXECUTION_ID BIGINT NOT NULL,
                                     START_TIME DATETIME NOT NULL ,
                                     END_TIME DATETIME DEFAULT NULL ,
                                     STATUS VARCHAR(10) ,
                                     COMMIT_COUNT BIGINT ,
                                     READ_COUNT BIGINT ,
                                     FILTER_COUNT BIGINT ,
                                     WRITE_COUNT BIGINT ,
                                     READ_SKIP_COUNT BIGINT ,
                                     WRITE_SKIP_COUNT BIGINT ,
                                     PROCESS_SKIP_COUNT BIGINT ,
                                     ROLLBACK_COUNT BIGINT ,
                                     EXIT_CODE VARCHAR(2500) ,
                                     EXIT_MESSAGE VARCHAR(2500) ,
                                     LAST_UPDATED DATETIME,
                                     constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
                                       references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

-- 该BATCH_STEP_EXECUTION_CONTEXT表包含ExecutionContext与Step相关的所有信息
CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
                                             STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                             SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                             SERIALIZED_CONTEXT TEXT ,
                                             constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
                                               references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ENGINE=InnoDB;

-- 该表包含ExecutionContext与Job相关的所有信息
CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
                                            JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                            SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                            SERIALIZED_CONTEXT TEXT ,
                                            constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
                                              references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_SEQ (
                                        ID BIGINT NOT NULL,
                                        UNIQUE_KEY CHAR(1) NOT NULL,
                                        constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;
INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_STEP_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_EXECUTION_SEQ (
                                       ID BIGINT NOT NULL,
                                       UNIQUE_KEY CHAR(1) NOT NULL,
                                       constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;
INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_SEQ (
                             ID BIGINT NOT NULL,
                             UNIQUE_KEY CHAR(1) NOT NULL,
                             constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;
INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_SEQ);

CREATE TABLE `student` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `name` varchar(20) NOT NULL,
                         `age` int(11) NOT NULL,
                         `sex` varchar(20) NOT NULL,
                         `address` varchar(100) NOT NULL,
                         `cid` int(11) NOT NULL,
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;