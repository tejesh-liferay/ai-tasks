/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { useEffect, useRef, useState } from 'react';
import { Remark } from 'react-remark';

import Toast from '../components/ui/Toast';
import { useAITasksContext } from '../contexts/AITasksContext';
import useChatHistory from '../hooks/useChatHistory';
import LiferayService from '../services/LiferayService';
import ChatMessage from './ui/ChatMessage';
import Icon from './ui/Icon';

const AITaskChatPreview = ({ hasStreamingNode, isOpen, setIsOpen }) => {
  const {
    clearMemory,
    executeTask,
    executeStreamingTask,
    memoryClearing,
    selectedTask,
    taskExecuting,
  } = useAITasksContext();
  const [userInput, setUserInput] = useState('');
  const { history, addMessage, clearHistory } = useChatHistory(selectedTask.id);
  const [visibleThoughts, setVisibleThoughts] = useState([]);
  const chatPreviewEndRef = useRef(null);
  const scrollTimeoutRef = useRef(null);
  const [streamingResponse, setStreamingResponse] = useState('');
  const [isStreaming, setIsStreaming] = useState(false);

  const handleSubmit = async (e) => {
    if (hasStreamingNode) {
      handleStreamingSubmit(e);
    } else {
      handleDefaultSubmit(e);
    }
  };

  const handleDefaultSubmit = async (e) => {
    e.preventDefault();
    addMessage(userInput, 'USER');
    setUserInput('');
    const response = await executeTask(selectedTask.externalReferenceCode, userInput);
    addMessage(
      response.output.text || response.output.error,
      'AI',
      response.executionTrace || {},
      response.output.think || '',
    );
  };

  const handleStreamingSubmit = async (e) => {
    e.preventDefault();
    addMessage(userInput, 'USER');
    setUserInput('');
    setIsStreaming(true);
    setStreamingResponse('');
    let currentResponse = '';

    const response = await executeStreamingTask(selectedTask.externalReferenceCode, userInput);

    const reader = response.body.getReader();

    const decoder = new TextDecoder();

    let executionTrace = {};

    while (true) {
      const { done, value } = await reader.read();
      if (done) {
        break;
      }

      const chunk = decoder.decode(value);

      if (chunk.startsWith('executionTrace:')) {
        executionTrace = JSON.parse(chunk.replace('executionTrace:', ''));
      } else {
        currentResponse += chunk.replaceAll('\n##', '');
        setStreamingResponse(currentResponse);
      }
    }
    addMessage(currentResponse, 'AI', executionTrace);
    setIsStreaming(false);
  };

  useEffect(() => {
    if (scrollTimeoutRef.current !== null) {
      clearTimeout(scrollTimeoutRef.current);
    }
    scrollTimeoutRef.current = setTimeout(() => {
      chatPreviewEndRef.current?.scrollIntoView();
    }, 200);

    setVisibleThoughts([]);
  }, [history, isOpen]);

  return (
    <div
      className={
        'chat-preview contextual-sidebar sidebar-light sidebar-lg contextual-sidebar-visible' +
        (isOpen ? ' chat-preview-open' : ' chat-preview-close')
      }
    >
      <div className="sidebar-header">
        <button
          className={'btn btn-secondary btn-sm mr-2 my-2'}
          onClick={(e) => {
            e.preventDefault();
            clearHistory();
          }}
        >
          <Icon name={'trash'} /> Clear History
        </button>
        <button
          className={'btn btn-secondary btn-sm'}
          onClick={(e) => {
            e.preventDefault();
            clearMemory(selectedTask.externalReferenceCode);
          }}
          disabled={memoryClearing}
        >
          <Icon name={'trash'} /> Clear Memory
        </button>
      </div>
      <div className="messages container">
        <div className="chat-preview-area d-flex flex-column">
          {history.map((message, index) => (
            <ChatMessage key={index} role={message.role} executionTrace={message.executionTrace}>
              <Remark>{message.text}</Remark>
              {message.think && (
                <>
                  <hr />
                  <button
                    className={'btn btn-sm btn-default w-100 text-left'}
                    onClick={() => {
                      if (visibleThoughts.includes(index)) {
                        setVisibleThoughts(visibleThoughts.filter((i) => i !== index));
                      } else {
                        setVisibleThoughts([...visibleThoughts, index]);
                      }
                    }}
                  >
                    <Icon
                      name={visibleThoughts.includes(index) ? 'angle-up' : 'angle-down'}
                      className={'mr-2'}
                    />
                    Thoughts
                  </button>
                  <i
                    className={'px-2 py-2'}
                    style={{ display: visibleThoughts.includes(index) ? 'block' : 'none' }}
                  >
                    <Remark>{message.think}</Remark>
                  </i>
                </>
              )}
            </ChatMessage>
          ))}
          {taskExecuting && !isStreaming && (
            <ChatMessage>
              <span className="loading-dots">
                <span style={{ animationDelay: '0s' }}></span>
                <span style={{ animationDelay: '0.3s' }}></span>
                <span style={{ animationDelay: '0.6s' }}></span>
              </span>
            </ChatMessage>
          )}
          {isStreaming && (
            <ChatMessage role="AI">
              <Remark>{streamingResponse}</Remark>
            </ChatMessage>
          )}
          <div ref={chatPreviewEndRef} className={'chat-preview-end mt-4'}></div>
        </div>
      </div>
      <div className="prompt px-4 py-3">
        <form onSubmit={handleSubmit}>
          <div className="chat-input-wrapper form-group d-flex flex-row">
            <textarea
              className="form-control"
              id="userMessage"
              name="userMessage"
              placeholder="Your message here..."
              value={userInput}
              onChange={(e) => {
                e.preventDefault();
                setUserInput(e.target.value);
              }}
              onKeyDown={(e) => {
                if (e.key === 'Enter') {
                  handleSubmit(e);
                }
              }}
            ></textarea>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AITaskChatPreview;
