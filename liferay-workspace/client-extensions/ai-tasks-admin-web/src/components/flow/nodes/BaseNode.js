/**
 * @author Petteri Karttunen
 */
import React, { memo } from 'react';
import { useTranslation } from 'react-i18next';

import { Handle } from '@xyflow/react';

import { useNodeMenu } from '../../../contexts/NodeMenuContext';
import Icon from '../../ui/Icon';

const BaseNode = memo(
  ({
    className,
    id,
    details,
    hasTargetHandle = true,
    hasSourceHandle = true,
    label,
    type,
    typeLabelVariant = 'info',
  }) => {
    const { t } = useTranslation();

    const { toggleDropdown } = useNodeMenu();

    return (
      <div className={className}>
        {hasTargetHandle && <Handle type="target" position="left" />}
        <div className={'node-header-container'}>
          <div className={'d-flex flex-row justify-content-between'}>
            <h3 className={'node-header-label'}>{label}</h3>
            <button
              className={'btn btn-monospaced btn-secondary btn-xs ml-4'}
              onClick={(event) => {
                toggleDropdown(event, id);
              }}
            >
              <Icon name={'ellipsis-v'} />
            </button>
          </div>
          <span className={`label label-${typeLabelVariant}`}>
            <span className="label-item label-item-expand">{t(type)}</span>
          </span>
        </div>
        {details && (
          <div className="node-details-container">
            {details.map(([key, value], index) => (
              <div
                key={index}
                className="d-flex flex-row justify-content-between align-items-center"
              >
                <span className={'detail-label'}>{t(key)}</span>
                <span className={'detail-value'}>{value}</span>
              </div>
            ))}
          </div>
        )}
        {hasSourceHandle && <Handle type="source" position="right" />}
      </div>
    );
  },
);

export default BaseNode;
