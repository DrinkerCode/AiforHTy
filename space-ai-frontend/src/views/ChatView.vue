<template>
  <div class="chat-view fade-in">
    <el-card class="chat-card">
      <template #header>
        <div class="card-header">
          <el-icon :size="24"><Rocket /></el-icon>
          <span>航天知识 AI 问答</span>
        </div>
      </template>
      
      <!-- 聊天消息区域 -->
      <div class="chat-messages" ref="messagesRef">
        <div v-if="messages.length === 0" class="welcome-message">
          <el-icon :size="64" color="#4fc3f7"><ChatDotRound /></el-icon>
          <h2>欢迎使用航天知识 AI 助手</h2>
          <p>我可以回答您关于航天的各种问题，例如：</p>
          <div class="sample-questions">
            <el-tag 
              v-for="(question, index) in sampleQuestions" 
              :key="index"
              class="sample-tag"
              @click="sendSampleQuestion(question)"
            >
              {{ question }}
            </el-tag>
          </div>
        </div>
        
        <div v-else class="message-list">
          <div 
            v-for="(message, index) in messages" 
            :key="index"
            :class="['message', message.role]"
          >
            <div class="message-avatar">
              <el-icon v-if="message.role === 'user'" :size="24"><User /></el-icon>
              <el-icon v-else :size="24"><Cpu /></el-icon>
            </div>
            <div class="message-content">
              <div class="message-bubble">
                <div v-if="message.loading" class="loading-dots">
                  <span></span><span></span><span></span>
                </div>
                <div v-else v-html="formatContent(message.content)"></div>
              </div>
              <div class="message-time">{{ message.time }}</div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 输入区域 -->
      <div class="chat-input">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="请输入您的航天问题..."
          @keydown.enter.exact.prevent="sendMessage"
          :disabled="isLoading"
        />
        <el-button 
          type="primary" 
          :loading="isLoading"
          @click="sendMessage"
          :disabled="!inputMessage.trim()"
        >
          <el-icon><Promotion /></el-icon>
          发送
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { chatApi } from '@/api'

const messagesRef = ref(null)
const inputMessage = ref('')
const isLoading = ref(false)
const messages = ref([])

const sampleQuestions = [
  '中国空间站是如何建设的？',
  '长征五号火箭有什么特点？',
  '嫦娥六号任务取得了什么成果？',
  '北斗导航系统有多少颗卫星？',
  'SpaceX 的星舰进展如何？'
]

const formatTime = () => {
  const now = new Date()
  return now.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
}

const formatContent = (content) => {
  // 简单的 Markdown 格式处理
  return content
    .replace(/\n/g, '<br/>')
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
}

const scrollToBottom = async () => {
  await nextTick()
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}

const sendSampleQuestion = (question) => {
  inputMessage.value = question
  sendMessage()
}

const sendMessage = async () => {
  if (!inputMessage.value.trim() || isLoading.value) return
  
  const userMessage = {
    role: 'user',
    content: inputMessage.value.trim(),
    time: formatTime()
  }
  
  messages.value.push(userMessage)
  const userQuestion = inputMessage.value
  inputMessage.value = ''
  
  // 添加加载中的 AI 消息
  const loadingMessage = {
    role: 'assistant',
    content: '',
    time: formatTime(),
    loading: true
  }
  messages.value.push(loadingMessage)
  isLoading.value = true
  
  await scrollToBottom()
  
  try {
    const response = await chatApi.sendQuestion({
      question: userQuestion
    })
    
    // 移除加载消息
    messages.value.pop()
    
    if (response.data.success) {
      messages.value.push({
        role: 'assistant',
        content: response.data.answer,
        time: formatTime()
      })
    } else {
      ElMessage.error(response.data.errorMessage || 'AI 回答失败')
      messages.value.push({
        role: 'assistant',
        content: '抱歉，暂时无法回答您的问题，请稍后再试。',
        time: formatTime()
      })
    }
  } catch (error) {
    messages.value.pop()
    ElMessage.error('网络错误，请检查连接')
    messages.value.push({
      role: 'assistant',
      content: '网络连接出现问题，请检查后端服务是否正常运行。',
      time: formatTime()
    })
  } finally {
    isLoading.value = false
    await scrollToBottom()
  }
}
</script>

<style lang="scss" scoped>
.chat-view {
  height: calc(100vh - 40px);
  display: flex;
  flex-direction: column;
}

.chat-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: rgba(26, 35, 50, 0.8);
  
  :deep(.el-card__header) {
    padding: 16px 20px;
    background: rgba(42, 53, 68, 0.5);
  }
  
  :deep(.el-card__body) {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 0;
    overflow: hidden;
  }
}

.card-header {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #4fc3f7;
  font-size: 18px;
  font-weight: 600;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.welcome-message {
  text-align: center;
  padding: 60px 20px;
  color: #b8c5d6;
  
  h2 {
    margin: 20px 0 10px;
    color: #4fc3f7;
  }
  
  p {
    margin-bottom: 20px;
  }
}

.sample-questions {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

.sample-tag {
  cursor: pointer;
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-2px);
    background-color: rgba(79, 195, 247, 0.2);
  }
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.message {
  display: flex;
  gap: 12px;
  
  &.user {
    flex-direction: row-reverse;
    
    .message-bubble {
      background: linear-gradient(135deg, #4fc3f7 0%, #29b6f6 100%);
      color: #0d1821;
    }
    
    .message-time {
      text-align: right;
    }
  }
  
  &.assistant {
    .message-bubble {
      background: #2a3544;
      color: #e0e6ed;
    }
  }
}

.message-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #2a3544;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: #4fc3f7;
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.message-bubble {
  padding: 12px 16px;
  border-radius: 12px;
  line-height: 1.6;
  word-break: break-word;
}

.message-time {
  font-size: 12px;
  color: #78909c;
}

.loading-dots {
  display: flex;
  gap: 4px;
  padding: 8px 0;
  
  span {
    width: 8px;
    height: 8px;
    background: #4fc3f7;
    border-radius: 50%;
    animation: bounce 1.4s infinite ease-in-out both;
    
    &:nth-child(1) { animation-delay: -0.32s; }
    &:nth-child(2) { animation-delay: -0.16s; }
  }
}

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

.chat-input {
  padding: 20px;
  border-top: 1px solid #2a3544;
  display: flex;
  gap: 12px;
  background: rgba(26, 35, 50, 0.9);
  
  :deep(.el-textarea__inner) {
    resize: none;
    background: #2a3544;
    border: 1px solid #3d4c5f;
    color: #e0e6ed;
    
    &::placeholder {
      color: #78909c;
    }
    
    &:focus {
      border-color: #4fc3f7;
    }
  }
  
  .el-button {
    min-width: 100px;
  }
}
</style>
