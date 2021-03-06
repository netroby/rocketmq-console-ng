/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.rocketmq.console.controller;

import com.google.common.base.Preconditions;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.rocketmq.console.model.request.ConsumerConfigInfo;
import org.apache.rocketmq.console.model.request.DeleteSubGroupRequest;
import org.apache.rocketmq.console.model.request.ResetOffsetRequest;
import org.apache.rocketmq.console.service.ConsumerService;
import org.apache.rocketmq.console.support.annotation.JsonBody;
import org.apache.rocketmq.console.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/consumer")
public class ConsumerController {
    private Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    @Resource
    private ConsumerService consumerService;

    @RequestMapping(value = "/groupList.query")
    @JsonBody
    public Object list() {
        return consumerService.queryGroupList();
    }

    @RequestMapping(value = "/resetOffset.do", method = {RequestMethod.POST})
    @JsonBody
    public Object resetOffset(@RequestBody ResetOffsetRequest resetOffsetRequest) {
        logger.info("op=look resetOffsetRequest={}", JsonUtil.obj2String(resetOffsetRequest));
        return consumerService.resetOffset(resetOffsetRequest);
    }

    @RequestMapping(value = "/examineSubscriptionGroupConfig.query")
    @JsonBody
    public Object examineSubscriptionGroupConfig(@RequestParam String consumerGroup) {
        return consumerService.examineSubscriptionGroupConfig(consumerGroup);
    }

    @RequestMapping(value = "/deleteSubGroup.do", method = {RequestMethod.POST})
    @JsonBody
    public Object deleteSubGroup(@RequestBody DeleteSubGroupRequest deleteSubGroupRequest) {
        return consumerService.deleteSubGroup(deleteSubGroupRequest);
    }

    @RequestMapping(value = "/createOrUpdate.do", method = {RequestMethod.POST})
    @JsonBody
    public Object consumerCreateOrUpdateRequest(@RequestBody ConsumerConfigInfo consumerConfigInfo) {
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(consumerConfigInfo.getBrokerNameList()) || CollectionUtils.isNotEmpty(consumerConfigInfo.getClusterNameList()),
            "clusterName or brokerName can not be all blank");
        return consumerService.createAndUpdateSubscriptionGroupConfig(consumerConfigInfo);
    }

    @RequestMapping(value = "/fetchBrokerNameList.query", method = {RequestMethod.GET})
    @JsonBody
    public Object fetchBrokerNameList(@RequestParam String consumerGroup) {
        return consumerService.fetchBrokerNameSetBySubscriptionGroup(consumerGroup);
    }

    @RequestMapping(value = "/queryTopicByConsumer.query")
    @JsonBody
    public Object queryConsumerByTopic(@RequestParam String consumerGroup) {
        return consumerService.queryConsumeStatsListByGroupName(consumerGroup);
    }

    @RequestMapping(value = "/consumerConnection.query")
    @JsonBody
    public Object consumerConnection(@RequestParam(required = false) String consumerGroup) {
        return consumerService.getConsumerConnection(consumerGroup);
    }

    @RequestMapping(value = "/consumerRunningInfo.query")
    @JsonBody
    public Object getConsumerRunningInfo(@RequestParam String consumerGroup, @RequestParam String clientId,
        @RequestParam boolean jstack) {
        return consumerService.getConsumerRunningInfo(consumerGroup, clientId, jstack);
    }
}
