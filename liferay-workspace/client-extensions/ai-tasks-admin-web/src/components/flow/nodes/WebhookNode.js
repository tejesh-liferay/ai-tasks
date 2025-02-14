/**
 * @author Petteri Karttunen
 */

import React, { memo } from 'react';
import { Handle } from '@xyflow/react';

const WebhookNode = ({ data }) => {
  const { label, parameters } = data;

  return (
    <div className="webhook-node">
      <Handle type="target" position="left" />
      <div className={'node-header-container'}>
        <h3>{label}</h3>
        <span className="label label-success">
          <span className="label-item label-item-expand">Webhook</span>
        </span>
      </div>
      <div className="node-details-container">
        <div className="d-flex flex-row justify-content-between align-items-center">
          <span className={'detail-label'}>URL</span>
          <span className={'detail-value'}>{parameters.url}</span>
        </div>
      </div>
      <Handle type="source" position="right" />
    </div>
  );
};

export default memo(WebhookNode);
