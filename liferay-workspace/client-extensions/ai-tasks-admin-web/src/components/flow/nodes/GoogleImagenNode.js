import React, { memo } from 'react';
import { Handle } from '@xyflow/react';

const GoogleImagenNode = ({ data }) => {
  const { label, parameters } = data;

  return (
    <div className="google-imagen-node">
      <Handle type="target" position="left" />
      <div className={'node-header-container'}>
        <h3>{label}</h3>
        <span className="label label-info">
          <span className="label-item label-item-expand">Google Imagen</span>
        </span>
      </div>
      <div className="node-details-container">
        <div className="d-flex flex-row justify-content-between align-items-center">
          <span className={'detail-label'}>Model</span>
          <span className={'detail-value'}>{parameters.modelName}</span>
        </div>
      </div>
      <Handle type="source" position="right" />
    </div>
  );
};

export default memo(GoogleImagenNode);
