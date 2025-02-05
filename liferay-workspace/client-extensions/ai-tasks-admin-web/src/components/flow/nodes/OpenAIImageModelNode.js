import React, { memo } from 'react';
import { Handle } from '@xyflow/react';

const OpenAIImageModelNode = ({ data }) => {
  const { label, parameters } = data;

  return (
    <div className="openai-image-model-node">
      <Handle type="target" position="left" />
      <div className={'node-header-container'}>
        <h3>{label}</h3>
        <span className="label label-info">
          <span className="label-item label-item-expand">OpenAI Image Model</span>
        </span>
      </div>
      <div className="node-details-container">
        <div className="d-flex flex-row justify-content-between align-items-center">
          <span className={'detail-label'}>Model</span>
          <span className={'detail-value'}>{parameters.modelName}</span>
        </div>
        <div className="d-flex flex-row justify-content-between align-items-center">
          <span className={'detail-label'}>Response Format</span>
          <span className={'detail-value'}>{parameters.responseFormat}</span>
        </div>
      </div>
      <Handle type="source" position="right" />
    </div>
  );
};

export default memo(OpenAIImageModelNode);
