package com.iikun.anichat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iikun.anichat.entity.Conversation;
import com.iikun.anichat.entity.ConversationMember;
import com.iikun.anichat.mapper.ConversationMapper;
import com.iikun.anichat.mapper.ConversationMemberMapper;
import com.iikun.anichat.service.ConversationService;
import com.iikun.common.base.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 会话服务实现类
 *
 * @author iikun
 */
@Service
@RequiredArgsConstructor
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements ConversationService {

    private final ConversationMemberMapper memberMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Conversation> createOrGetPrivateConversation(String userId1, String userId2) {
        // 查找两个用户之间是否已经存在私聊会话
        // 简单逻辑：找到包含这两个人的所有私聊会话，检查是否只有这两个人
        // 为了性能，通常会有一个专门的索引或关系表。这里我们先查找 userId1 的所有私聊会话，再过滤。
        
        List<String> convIds1 = memberMapper.selectList(new LambdaQueryWrapper<ConversationMember>()
                .eq(ConversationMember::getUserId, userId1))
                .stream().map(ConversationMember::getConversationId).collect(Collectors.toList());

        List<String> convIds2 = memberMapper.selectList(new LambdaQueryWrapper<ConversationMember>()
                .eq(ConversationMember::getUserId, userId2))
                .stream().map(ConversationMember::getConversationId).collect(Collectors.toList());

        // 交集
        convIds1.retainAll(convIds2);

        for (String convId : convIds1) {
            Conversation conv = this.getOne(new LambdaQueryWrapper<Conversation>()
                    .eq(Conversation::getConversationId, convId)
                    .eq(Conversation::getType, 1)); // 1=私聊
            if (conv != null) {
                return Result.success(conv);
            }
        }

        // 不存在，创建新会话
        String convId = UUID.randomUUID().toString().replace("-", "");
        Conversation conversation = new Conversation();
        conversation.setConversationId(convId);
        conversation.setType(1); // 私聊
        this.save(conversation);

        // 添加成员
        ConversationMember m1 = new ConversationMember();
        m1.setConversationId(convId);
        m1.setUserId(userId1);
        m1.setRole(1);
        memberMapper.insert(m1);

        ConversationMember m2 = new ConversationMember();
        m2.setConversationId(convId);
        m2.setUserId(userId2);
        m2.setRole(1);
        memberMapper.insert(m2);

        return Result.success(conversation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Conversation> createGroupConversation(String ownerId, String title, List<String> memberIds) {
        String convId = UUID.randomUUID().toString().replace("-", "");
        Conversation conversation = new Conversation();
        conversation.setConversationId(convId);
        conversation.setType(2); // 群聊
        conversation.setTitle(title);
        conversation.setOwnerId(ownerId);
        this.save(conversation);

        // 添加群主
        ConversationMember owner = new ConversationMember();
        owner.setConversationId(convId);
        owner.setUserId(ownerId);
        owner.setRole(3); // 群主
        memberMapper.insert(owner);

        // 添加其他成员
        if (memberIds != null) {
            for (String memberId : memberIds) {
                if (memberId.equals(ownerId)) continue;
                ConversationMember member = new ConversationMember();
                member.setConversationId(convId);
                member.setUserId(memberId);
                member.setRole(1); // 普通成员
                memberMapper.insert(member);
            }
        }

        return Result.success(conversation);
    }

    @Override
    public Result<List<Conversation>> getUserConversations(String userId) {
        List<String> convIds = memberMapper.selectList(new LambdaQueryWrapper<ConversationMember>()
                .eq(ConversationMember::getUserId, userId))
                .stream().map(ConversationMember::getConversationId).collect(Collectors.toList());

        if (convIds.isEmpty()) {
            return Result.success(List.of());
        }

        List<Conversation> conversations = this.list(new LambdaQueryWrapper<Conversation>()
                .in(Conversation::getConversationId, convIds)
                .orderByDesc(Conversation::getUpdateTime));

        return Result.success(conversations);
    }
}
