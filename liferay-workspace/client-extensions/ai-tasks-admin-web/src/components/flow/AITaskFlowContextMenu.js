/**
 * @author Louis-Guillaume Durand
 */

import React, { useCallback } from 'react';
import { useReactFlow } from '@xyflow/react';
import Icon from '../ui/Icon';
import { useAITasksContext } from '../../contexts/AITasksContext';

const AITaskFlowContextMenu = ({
  id,
  top,
  left,
  right,
  bottom,
  handleGraphUpdate,
  onRename,
  onConfigure,
  onSetCondition,
  ...props
}) => {
  const { setNodes, setEdges, getNodes, getEdges } = useReactFlow();
  const { selectedTask } = useAITasksContext();

  const deleteNode = useCallback(
    (e) => {
      e.preventDefault();
      const shouldDelete = confirm(`Are you sure you want to delete this node?`);
      if (shouldDelete) {
        const newNodes = getNodes().filter((node) => node.id !== id);
        const newEdges = getEdges().filter((edge) => edge.source !== id);
        setNodes(newNodes);
        setEdges(newEdges);
        handleGraphUpdate(newNodes, newEdges);
      }
    },
    [id, setNodes, setEdges],
  );

  if (id === 'entryPoint') {
    return null;
  }

  return (
    <div style={{ top, left, right, bottom }} className="dropdown-menu show" {...props}>
      <ul className="list-unstyled">
        <li>
          <a className="dropdown-item" onClick={onRename}>
            <Icon name="pencil" />
            <span className={'ml-2'}>Rename</span>
          </a>
        </li>
        <li>
          <a className="dropdown-item" onClick={onConfigure}>
            <Icon name="control-panel" />
            <span className={'ml-2'}>Configure</span>
          </a>
        </li>
        {id !== selectedTask.configuration.startNodeId && (
          <li>
            <a className="dropdown-item" onClick={onSetCondition}>
              <Icon name="merge" />
              <span className={'ml-2'}>Set Condition</span>
            </a>
          </li>
        )}
        <li>
          <a className="dropdown-item" href="#" onClick={deleteNode}>
            <Icon name="trash" />
            <span className={'ml-2'}>Delete</span>
          </a>
        </li>
      </ul>
    </div>
  );
};

export default AITaskFlowContextMenu;
