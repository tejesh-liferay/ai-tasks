/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React, { useState } from 'react';

import { useTooltip } from '../../contexts/TooltipContext';
import useClickOutside from '../../hooks/useClickOutside';
import Icon from './Icon';

const ChatMessage = ({ role, executionTrace, children }) => {
  const { isTooltipOpen, setIsTooltipOpen, setTooltipPosition, setTooltipContent } = useTooltip();

  const handleClickOutside = () => {
    setIsTooltipOpen(false);
  };

  const prettyPrintJson = (json) => {
    try {
      return JSON.stringify(json, null, 2); // The '2' adds 2 spaces for indentation
    } catch (error) {
      console.error('Error pretty-printing JSON:', error);
      return 'Invalid JSON';
    }
  };

  const buttonRef = useClickOutside(handleClickOutside);

  const handleExecutionTraceDisplay = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setTooltipPosition({
      top: e.target.getBoundingClientRect().top - 100,
      left: e.target.getBoundingClientRect().left,
      right: e.target.getBoundingClientRect().right,
      bottom: e.target.getBoundingClientRect().bottom,
    });
    setTooltipContent(
      <div className={'d-flex flex-column'}>
        <div>Execution: {executionTrace.executionTime}</div>
        <div>Input Tokens: {executionTrace.tokenUsage.inputTokenCount}</div>
        <div>Output Tokens: {executionTrace.tokenUsage.outputTokenCount}</div>
        <div>Total Tokens: {executionTrace.tokenUsage.totalTokenCount}</div>
      </div>,
    );
    setIsTooltipOpen(!isTooltipOpen);
  };

  const [isTraceVisible, setIsTraceVisible] = useState(false);
  const handleTraceVisibilityToggle = () => {
    setIsTraceVisible(!isTraceVisible);
  };

  return (
    <>
      <div
        className={`chat-preview-message d-flex mb-2 ${
          role === 'USER' ? 'justify-content-end' : 'justify-content-start'
        }`}
      >
        <div className={`message ${role === 'USER' ? 'user-message' : 'system-message'}`}>
          {children}
        </div>
      </div>
      {executionTrace && (
        <div className="d-flex flex-column">
          <button
            className={'btn btn-xs btn-default w-100 text-left'}
            onClick={handleTraceVisibilityToggle}
          >
            <Icon name={isTraceVisible ? 'angle-up' : 'angle-down'} className={'mr-2'} />
            Execution Details
          </button>
          <div className={'px-2 py-2'} style={{ display: isTraceVisible ? 'block' : 'none' }}>
            <div className={'trace-details d-flex flex-column'}>
              <pre>{prettyPrintJson(executionTrace)}</pre>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default ChatMessage;
