import React, { memo } from 'react';
import { Handle } from '@xyflow/react';

const LiferaySearchNode = ({ data }) => {
  const { label, parameters } = data;
  return (
    <div className="liferay-search-node">
      <Handle type="target" position="left" />
      <div className={'node-header-container'}>
        <h3>{label}</h3>
        <span className="label label-warning">
          <span className="label-item label-item-expand">Liferay Search</span>
        </span>
      </div>
      <div className="node-details-container">
        <div className="d-flex flex-row justify-content-between align-items-center">
          <span className={'detail-label'}>Blueprint</span>
          <span className={'detail-value'}>{parameters.sxpBlueprintExternalReferenceCode}</span>
        </div>
        <div className="d-flex flex-row justify-content-between align-items-center">
          <span className={'detail-label'}>Output</span>
          <span className={'detail-value'}>{parameters.taskContextOutputParameterName}</span>
        </div>
      </div>
      <Handle type="source" position="right" />
    </div>
  );
};

export default memo(LiferaySearchNode);
