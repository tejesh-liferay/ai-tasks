import React, { memo } from 'react';
import { Handle } from '@xyflow/react';

const GeminiChatModelNode = ({ data }) => {
  const { label, parameters } = data;

  return (
    <div className="gemini-chat-model-node">
      <Handle type="target" position="left" />
      <div className={'node-header-container'}>
        <h3>{label}</h3>
        <span className="label label-info">
          <span className="label-item label-item-expand">Gemini Chat Model</span>
        </span>
      </div>
      <div className="node-details-container">
        <div className="d-flex flex-row justify-content-between align-items-center">
          <span className={'detail-label'}>Model</span>
          <span className={'detail-value'}>{parameters.modelName}</span>
        </div>
        <div className="d-flex flex-row justify-content-between align-items-center">
          <span className={'detail-label'}>Temperature</span>
          <span className={'detail-value'}>{parameters.temperature}</span>
        </div>
      </div>
      <Handle type="source" position="right" />
    </div>
  );
};

export default memo(GeminiChatModelNode);
