/**
 * @author Louis-Guillaume Durand
 * @author Petteri Karttunen
 */
import React from 'react';
import { useTranslation } from 'react-i18next';

import {
  getImageGenerationNodes,
  getTextGenerationNodes,
  getTriggerNodes,
  getUtilityNodes,
} from '../../constants/AITasksNodeTypesConstants';
import { useDnD } from '../../contexts/DnDContext';
import Icon from '../ui/Icon';

const AITaskFlowNodesPane = ({ isOpen, setIsOpen }) => {
  const [_, setType] = useDnD();

  const onDragStart = (event, nodeType) => {
    setType(nodeType);
    event.dataTransfer.effectAllowed = 'move';
  };

  const { t } = useTranslation();

  return (
    <div
      className={
        'flow-nodes-pane contextual-sidebar sidebar-light sidebar-sm contextual-sidebar-visible ' +
        (isOpen ? 'flow-nodes-pane-open' : 'flow-nodes-pane-close')
      }
    >
      <div className="sidebar-header">
        <span className="d-flex flex-row justify-content-between text-truncate-inline">
          Drag & Drop nodes into the canvas
        </span>
      </div>
      <div className="container">
        <div className="flow-nodes-pane-area d-flex flex-column">
          <h3 className={'node-category-title'}>Trigger</h3>
          {getTriggerNodes.map((node, index) => {
            return (
              <div
                key={index}
                className={'new-node'}
                onDragStart={(event) => onDragStart(event, node)}
                draggable
              >
                <div
                  className={
                    'node-header-container d-flex flex-row align-items-center justify-content-start'
                  }
                >
                  <h4>{t(node)}</h4>
                </div>
              </div>
            );
          })}

          <h3 className={'node-category-title'}>Text Generation</h3>
          {getTextGenerationNodes.map((node, index) => {
            return (
              <div
                key={index}
                className={'new-node'}
                onDragStart={(event) => onDragStart(event, node)}
                draggable
              >
                <div
                  className={
                    'node-header-container d-flex flex-row align-items-center justify-content-start'
                  }
                >
                  <h4>{t(node)}</h4>
                </div>
              </div>
            );
          })}
          <h3 className={'node-category-title'}>Image Generation</h3>
          {getImageGenerationNodes.map((node, index) => {
            return (
              <div
                key={index}
                className={'new-node'}
                onDragStart={(event) => onDragStart(event, node)}
                draggable
              >
                <div
                  className={
                    'node-header-container d-flex flex-row align-items-center justify-content-start'
                  }
                >
                  <h4>{t(node)}</h4>
                </div>
              </div>
            );
          })}
          <h3 className={'node-category-title'}>Utility</h3>
          {getUtilityNodes.map((node, index) => {
            return (
              <div
                key={index}
                className={'new-node'}
                onDragStart={(event) => onDragStart(event, node)}
                draggable
              >
                <div
                  className={
                    'node-header-container d-flex flex-row align-items-center justify-content-start'
                  }
                >
                  <h4>{t(node)}</h4>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default AITaskFlowNodesPane;
