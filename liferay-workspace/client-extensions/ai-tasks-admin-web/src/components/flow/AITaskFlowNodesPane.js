/**
 * @author Louis-Guillaume Durand
 */

import React from 'react';
import Icon from '../ui/Icon';
import { useDnD } from '../../contexts/DnDContext';
import {
GEMINI_CHAT_MODEL,
GOOGLE_IMAGEN,
HUGGING_FACE_CHAT_MODEL,
LIFERAY_SEARCH,
OLLAMA_CHAT_MODEL,
OPENAI_CHAT_MODEL,
OPENAI_IMAGE_MODEL,
WEBHOOK,
} from '../../constants/AITasksNodeTypesConstants';

const AITaskFlowNodesPane = ({ isOpen, setIsOpen }) => {
  const [type, setType] = useDnD();

  const onDragStart = (event, nodeType) => {
    setType(nodeType);
    event.dataTransfer.effectAllowed = 'move';
  };

  return (
    <div
      className={
        'flow-nodes-pane contextual-sidebar sidebar-light sidebar-sm contextual-sidebar-visible ' +
        (isOpen ? 'flow-nodes-pane-open' : 'flow-nodes-pane-close')
      }
    >
      <div className="sidebar-header">
        <span className="d-flex flex-row justify-content-between text-truncate-inline">
          <span className="component-title text-truncate">Add Nodes</span>
          <button
            className={'btn btn-default'}
            onClick={(e) => {
              e.preventDefault();
              setIsOpen(false);
            }}
          >
            <Icon name={'times'} />
          </button>
        </span>
      </div>
      <div className="container">
        <div className="flow-nodes-pane-area d-flex flex-column">
          <div
            className={'gemini-chat-model-node'}
            onDragStart={(event) => onDragStart(event, GEMINI_CHAT_MODEL)}
            draggable
          >
            <div
              className={
                'node-header-container d-flex flex-row align-items-center justify-content-center'
              }
            >
              <h4>Gemini Chat Model</h4>
            </div>
          </div>
          <div
            className={'google-imagen-node'}
            onDragStart={(event) => onDragStart(event, GOOGLE_IMAGEN)}
            draggable
          >
            <div
              className={
                'node-header-container d-flex flex-row align-items-center justify-content-center'
              }
            >
              <h4>Google Imagen</h4>
            </div>
          </div>
          <div
            className={'hugging-face-chat-model-node'}
            onDragStart={(event) => onDragStart(event, HUGGING_FACE_CHAT_MODEL)}
            draggable
          >
            <div
              className={
                'node-header-container d-flex flex-row align-items-center justify-content-center'
              }
            >
              <h4>HuggingFace Chat Model</h4>
            </div>
          </div>
          <div
            className={'liferay-search-node'}
            onDragStart={(event) => onDragStart(event, LIFERAY_SEARCH)}
            draggable
          >
            <div
              className={
                'node-header-container d-flex flex-row align-items-center justify-content-center'
              }
            >
              <h4>Liferay Search</h4>
            </div>
          </div>
          <div
            className={'ollama-chat-model-node'}
            onDragStart={(event) => onDragStart(event, OLLAMA_CHAT_MODEL)}
            draggable
          >
            <div
              className={
                'node-header-container d-flex flex-row align-items-center justify-content-center'
              }
            >
              <h4>Ollama Chat Model</h4>
            </div>
          </div>
          <div
            className={'openai-chat-model-node'}
            onDragStart={(event) => onDragStart(event, OPENAI_CHAT_MODEL)}
            draggable
          >
            <div
              className={
                'node-header-container d-flex flex-row align-items-center justify-content-center'
              }
            >
              <h4>OpenAI Chat Model</h4>
            </div>
          </div>
          <div
            className={'openai-image-model-node'}
            onDragStart={(event) => onDragStart(event, OPENAI_IMAGE_MODEL)}
            draggable
          >
            <div
              className={
                'node-header-container d-flex flex-row align-items-center justify-content-center'
              }
            >
              <h4>OpenAI Image Model</h4>
            </div>
          </div>
          <div
            className={'webhook-node'}
            onDragStart={(event) => onDragStart(event, WEBHOOK)}
            draggable
          >
            <div
              className={
                'node-header-container d-flex flex-row align-items-center justify-content-center'
              }
            >
              <h4>Webhook</h4>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AITaskFlowNodesPane;
